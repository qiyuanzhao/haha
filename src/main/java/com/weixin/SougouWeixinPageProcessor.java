package com.weixin;

import com.BasePageProcessor;
import com.downloader.DynamicProxyDownloader;
import com.weixin.page.DetailPage;
import com.weixin.page.SearchPage;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Site;


@Component
public class SougouWeixinPageProcessor extends BasePageProcessor{

    private Site site = Site.me()
            .setCharset("utf-8")
            .setRetryTimes(6)//重试6次
            .setSleepTime(2000)
            .setTimeOut(30 * 1000)
            .setUserAgent("Mozilla/5.0 (Windows NT 5.1; rv:5.0) Gecko/20100101 Firefox/5.0")
            .addHeader("Proxy-Authorization", DynamicProxyDownloader.getAuthHeader())
            .addHeader("Referer", "http://weixin.sogou.com/weixin?type=2&ie=utf8&query=")
            .addHeader("user-agent", randomUserAgent());

    public SougouWeixinPageProcessor() {
        super(new SearchPage(),new DetailPage());
    }

    @Override
    public Site getSite() {
        return site;
    }

}
