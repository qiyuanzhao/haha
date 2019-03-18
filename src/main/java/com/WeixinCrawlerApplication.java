package com;

import com.weixin.SougouWeixinCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class WeixinCrawlerApplication implements CommandLineRunner {

    @Autowired
    private SougouWeixinCrawler sougouWeixinCrawler;

    @Override
    public void run(String... args) throws Exception {
        sougouWeixinCrawler.startCrawler();
    }

    public static void main(String[] args) {
        SpringApplication.run(WeixinCrawlerApplication.class,args);
    }



}
