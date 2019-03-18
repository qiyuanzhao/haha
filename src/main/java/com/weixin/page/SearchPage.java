package com.weixin.page;

import com.utils.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.util.List;

@Component
public class SearchPage implements BasePage{



    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch("http://weixin.sogou.com/weixin.*",url);
    }

    @Override
    public void process(Page page) {
        String keyword = page.getRequest().getExtra("KEY_KEYWORD").toString();
        //10篇文章 url -> request
        List<String> texturl = page.getHtml().xpath("//div[@class='txt-box']/h3/a/@href").all();
        texturl.forEach(nexturl -> {
            if (StringUtils.isNotBlank(nexturl)) {
//                    logger.info("文章的url："+nexturl);
                page.addTargetRequest(new Request(nexturl).putExtra("KEY_KEYWORD", keyword));
            }
        });

        //下一页
        String nexturl = page.getHtml().css("#sogou_next", "href").toString();
        if (StringUtils.isNotBlank(nexturl)) {
            if (!nexturl.contains("weixin.sogou.com")) {
                nexturl = "http://weixin.sogou.com/weixin" + nexturl;
            }
            Request request = generateListRequest(nexturl, keyword);
            page.addTargetRequest(request);
        }

    }

    public static Request generateListRequest(String url, String keyword) {
        return new Request(url)
                .putExtra("KEY_KEYWORD", keyword);

    }
}
