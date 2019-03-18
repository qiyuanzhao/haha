package com.weixin.page;

import com.utils.RegexUtil;
import com.weixin.entity.Message;
import com.weixin.entity.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.selector.Selectable;

import java.util.Date;
import java.util.List;

@Component
public class DetailPage implements BasePage {

    private Logger logger = LoggerFactory.getLogger(DetailPage.class);


    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://mp.weixin.qq.com/s.*", url) || RegexUtil.isMatch("http://mp.weixin.qq.com/s.*", url);
    }


    @Override
    public void process(Page page) {
        //系统出错文章
        Selectable selectable = page.getHtml().xpath("//div[@class='global_error_msg warn']");
        if (StringUtils.isNotBlank(selectable.toString())) {
            return;
        }
        Message message = messageHandler(page);
        String keyword = page.getRequest().getExtra("KEY_KEYWORD").toString();
        if (message != null) {
            //微信是不牵扯到Quoted  暂用quotedMessage属性来封装keyword
            message.setQuotedMessage(keyword);
            String url = "http://service2:8090/api/v1/elasticsearch/bulk/weixin";
            logger.info("********************** IMPORT WEIXIN URL = {} ********************", url);
//            restTemplate.postForEntity(url, message, String.class);
        }
        logger.info("get message counts : " + 1);
    }

    private Message messageHandler(Page page) {
        Message message = new Message();
        UserInfo postingUser = new UserInfo();

        Selectable titleSelecable = page.getHtml().xpath("//h2[@id='activity-name']/text()");
        if (titleSelecable.toString() == null) {
            return null;
        }
        String title = titleSelecable.toString().trim();
        String content = page.getHtml().xpath("//div[@id='js_content']/allText()").toString();//公众号文章内容
        if (StringUtils.isBlank(content)) {
            return null;
        }
//        String publish_time = page.getHtml().xpath("//em[@id='post-date']/text()").toString();//文章发布时间
        String displayName = page.getHtml().xpath("//span[@class='rich_media_meta rich_media_meta_text rich_media_meta_nickname']/text()").toString();//用户名
        String biz = page.getHtml().xpath("//span[@class='profile_meta_value']/text()").toString();//公众号id
        String timeCreated = "";
        List<String> scripts = page.getHtml().xpath("//script").all();
        for (String script : scripts) {
            if (script.contains("var nickname")) {
                String[] split = script.split("var");
                for (String aSplit : split) {
                    if (aSplit.contains("appuin")) {
                        biz = new PlainText(aSplit).regex(".*\"(.*)\";").get();
                    }

                    if (aSplit.contains("ct ")) {
                        timeCreated = new PlainText(aSplit).regex("= \"(\\d+)\"").get();
                    }

                    if (aSplit.contains("nickname")) {
                        displayName = new PlainText(aSplit).regex("\"(.*)\";").get();
                    }

                    if (aSplit.contains("round_head_img")) {
                        String avatar = new PlainText(aSplit).regex("round_head_img.*(http://.*)\"").toString();
                        if (StringUtils.isNotBlank(avatar)) {
                            postingUser.setAvatar(avatar);
                        }
                    }
                    if (aSplit.contains("msg_link")) {
                        String msg_link = new PlainText(aSplit).regex("msg_link.*(http://mp.weixin.qq.com/.*)\"").toString();
                        if (StringUtils.isNotBlank(msg_link)) {
                            String replace = msg_link.replace("\\x26amp;", "&");
                            message.setUrl(replace);
                        }
                    }
                    if (aSplit.contains("appmsgid")) {
                        String comment_id = aSplit.trim();
                        String messageId = new PlainText(comment_id).regex(".*\"(\\d+)\".*").toString();
                        message.setId(messageId);//文章id
                        break;
                    }
                }
                break;
            }
        }


        if (StringUtils.isNotBlank(message.getId())) {
            int messageId = title.hashCode() + displayName.hashCode();
            message.setId(Math.abs(messageId) + "");//文章id
        }


        message.setTitle(title);//公众号文章标题
        message.setContent(content);//正文
        if (biz != null) {
            postingUser.setId(biz);

        }
        if (displayName != null) {
            postingUser.setDisplayName(displayName);
        }

        if (StringUtils.isBlank(message.getUrl())) {
            message.setUrl("http://weixin.sogou.com/weixin?type=2&query=" + title);
        }
        message.setUser(postingUser);

        //通用属性
        if (StringUtils.isNotBlank(timeCreated)) {
            message.settC(Long.parseLong(timeCreated) * 1000L);//文章发布时间
        }
        message.settF(new Date());//文章抓取时间

        return message;
    }
}
