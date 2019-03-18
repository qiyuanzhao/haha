package com;


import com.weixin.page.BasePage;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.processor.PageProcessor;

public abstract class BasePageProcessor implements PageProcessor {

    private BasePage[] basePages;

    private static String[] USER_AGENTS = new String[]{
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36", // chrome
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/604.3.5 (KHTML, like Gecko) Version/11.0.1 Safari/604.3.5", // safari
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36", // 360
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:45.0) Gecko/20100101 Firefox/45.0"
    };

    protected static String randomUserAgent() {
        int user_agent_index = (int) (Math.random() * (USER_AGENTS.length));
        return USER_AGENTS[user_agent_index];
    }

    public BasePageProcessor(BasePage... basePages) {
        if (basePages == null || basePages.length == 0) {
            throw new IllegalArgumentException("page parse size is 0");
        }
        this.basePages = basePages;
    }


    @Override
    public void process(Page page) {
        for (BasePage basePage : basePages) {
            if (basePages == null) {
                throw new RuntimeException("page parse is null");
            }
            if (basePage.handleUrl(page.getUrl().get())) {
                basePage.process(page);
            }
        }
    }

}
