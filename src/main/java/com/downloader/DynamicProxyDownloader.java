package com.downloader;

import com.google.common.base.Joiner;
import org.apache.http.HttpHost;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.selector.Json;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DynamicProxyDownloader extends HttpClientDownloader {

    private static Logger logger = LoggerFactory.getLogger(DynamicProxyDownloader.class);

    //定义申请获得的appKey和appSecret
    private static final String appkey = "175626467";
    private static final String secret = "8de6bc69f697bf7a28f94de361188ea2";


    protected static final HttpHost httpHost = new HttpHost("s2.proxy.mayidaili.com", 8123);//蚂蚁动态代理ip)
    protected static final RequestConfig requestConfig = RequestConfig.custom().setProxy(httpHost).build();
    public static String getAuthHeader() {
        // 创建参数表
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("app_key", appkey);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));//使用中国时间，以免时区不同导致认证错误
        paramMap.put("timestamp", format.format(new Date()));

        // 对参数名进行排序
        String[] keyArray = paramMap.keySet().toArray(new String[0]);
        Arrays.sort(keyArray);

        // 拼接有序的参数名-值串
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(secret);
        for (String key : keyArray) {
            stringBuilder.append(key).append(paramMap.get(key));
        }

        stringBuilder.append(secret);
        String codes = stringBuilder.toString();

        // MD5编码并转为大写， 这里使用的是Apache codec
        String sign = org.apache.commons.codec.digest.DigestUtils.md5Hex(codes).toUpperCase();

        paramMap.put("sign", sign);

        // 拼装请求头Proxy-Authorization的值，这里使用 guava 进行map的拼接
        String authHeader = "MYH-AUTH-MD5 " + Joiner.on('&').withKeyValueSeparator("=").join(paramMap);

        logger.info("authHeader:" + authHeader);
        return authHeader;
    }



    /*
     * 忽略认证
     */
    public CloseableHttpClient ignoreValidationHttpClient() {
//		SSLContext sslcontext = null;
//		try {
//			sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
//
//				@Override
//				public boolean isTrusted(X509Certificate[] chain, String authType)
//						throws CertificateException {
//					return true;
//				}
//			}).build();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

        HttpClientBuilder builder = ignoreValidating();

        builder.setProxy(httpHost);
        CloseableHttpClient httpclient = builder.build();
        return httpclient;

    }

    public void sleep(int num) {
        try {
            Thread.sleep(num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public HttpClientBuilder ignoreValidating() {
        HttpClientBuilder builder = HttpClients.custom();

        try {
            SSLContext ignoreVerifySSL = createIgnoreVerifySSL();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ignoreVerifySSL, NoopHostnameVerifier.INSTANCE);
            builder.setSSLSocketFactory(sslsf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder;
    }

    protected SSLContext createIgnoreVerifySSL() throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(new KeyManager[0], new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
//                    return null;
            }
        }}, new SecureRandom());
        return sslContext;
    }

    @Override
    public Page download(Request request, Task task) {
        Site site = task.getSite();
        Page page = this.myDownloader(request, task);
        if (!site.getAcceptStatCode().contains(page.getStatusCode())) {
            logger.info("status code error. {}, url : {}", page.getStatusCode(), page.getUrl());
            page.setDownloadSuccess(false);
        }
        return page;
    }

    private Page myDownloader(Request request, Task task) {
        Page page = Page.fail();
        page.setUrl(new Json(request.getUrl()));
        try (CloseableHttpClient httpClient = ignoreValidating()
//                .setProxy(new HttpHost("s2.proxy.mayidaili.com", 8123))
                .build()) {
            RequestConfig config = RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                    .setConnectionRequestTimeout(task.getSite().getTimeOut())
                    .setSocketTimeout(task.getSite().getTimeOut())
                    .setConnectTimeout(task.getSite().getTimeOut())
                    .build();
            HttpGet get = new HttpGet(request.getUrl());
            get.setConfig(config);
            task.getSite().getHeaders().forEach(get::addHeader);

            try (CloseableHttpResponse response = httpClient.execute(get)) {
                String content = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
                int statusCode = response.getStatusLine().getStatusCode();
                page.setRequest(request);
                page.setRawText(content);
                page.setStatusCode(statusCode);
                page.setDownloadSuccess(true);
                return page;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                TimeUnit.MILLISECONDS.sleep(task.getSite().getSleepTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return page;
    }

	public static void main (String[] args) throws Exception {
		DynamicProxyDownloader dynamicProxyDownloader = new DynamicProxyDownloader();
		String url = "https://detail.tmall.com/item.htm?spm=a220m.1000858.1000725.1.28c53598LSZv3m&id=576309495894&areaId=110100&user_id=2457652587&cat_id=2&is_b=1&rn=a26af552c9a8bdb50564b03199906acf";
		HttpHost httpHost = new HttpHost("s2.proxy.mayidaili.com", 8123);//蚂蚁动态代理ip)
		RequestConfig requestConfig = RequestConfig.custom().setProxy(httpHost).build();
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0");
		httpGet.addHeader("cookie", "cna=6KZmFH+sOWkCAXjAWe1mcgAH; otherx=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0; x=__ll%3D-1%26_ato%3D0; lid=question0001; enc=BDgS2%2Be0BxJxe6UwrzvMyc%2ByCANaqitOdopxOQhqhWPGfFTfvO46sQT4TKJBZ%2FrEcjYhAe7MPjqOIxntKmrTPg%3D%3D; hng=CN%7Czh-CN%7CCNY%7C156; t=960894f2a131c5c60fd8e542b104ae32; _tb_token_=5be3e4e5edbe3; cookie2=12f70ea79bab65536768245228aaa2ac; uc1=cookie16=UIHiLt3xCS3yM2h4eKHS9lpEOw%3D%3D&cookie21=U%2BGCWk%2F7p4mBoUyS4E9C&cookie15=Vq8l%2BKCLz3%2F65A%3D%3D&existShop=false&pas=0&cookie14=UoTYM86OAOm6Ug%3D%3D&tag=8&lng=zh_CN; uc3=vt3=F8dByRMHgbSz1pWlzWw%3D&id2=UU6nRR5oExz1%2FA%3D%3D&nk2=EuC2vKAXDBsbHsZN&lg2=WqG3DMC9VAQiUQ%3D%3D; tracknick=question0001; _l_g_=Ug%3D%3D; ck1=\"\"; unb=2664822273; lgc=question0001; cookie1=BxBC6YironU4cwHoxYtETnLnKGTwedxq6kLi49rlsI8%3D; login=true; cookie17=UU6nRR5oExz1%2FA%3D%3D; _nk_=question0001; uss=\"\"; csg=e01dbade; skt=72c9de3304fceaf5; mbk=4aef25468ec707a2; whl=-1%260%260%260; l=aBWaJLpxysC8isEmQManfSskD707g9fPVER91MakgTEhNP0IInGtLjno-VwWj_qC51Gy_K-5F; isg=BAYG695VpjU1KXJWClDt0hTIV_xIz0umfQN9ZPAv8ikA86YNWPeaMewFz2-a20I5");
		httpGet.setConfig(requestConfig);
		httpGet.addHeader("Proxy-Authorization", dynamicProxyDownloader.getAuthHeader());
        String header = dynamicProxyDownloader.getAuthHeader();

        CloseableHttpResponse response = dynamicProxyDownloader.ignoreValidationHttpClient().execute(httpGet);
		String content = EntityUtils.toString(response.getEntity());
		System.out.println(content);



    }
}
