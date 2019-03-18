package com.weixin;

import com.constant.Site;
import com.downloader.SougouWeixinDownloader;
import com.repository.ProjectRepository;
import com.repository.WeixinTaskRepository;
import com.utils.UrlUtils;
import com.weixin.entity.Project;
import com.weixin.entity.WeixinTask;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 搜狗微信
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class SougouWeixinCrawler {

    private Logger logger = LoggerFactory.getLogger(SougouWeixinCrawler.class);

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private WeixinTaskRepository weixinTaskRepository;

    @Autowired
    private SougouWeixinPageProcessor sougouWeixinPageProcessor;

    public void startCrawler() {
        List<Request> requests = getStartRequests();
        Spider spider = new Spider(sougouWeixinPageProcessor);
        for (Request request : requests) {
            spider.addRequest(request);
        }
        Downloader downloader = new SougouWeixinDownloader();
        spider.setDownloader(downloader);
        spider.thread(1);
        spider.start();
    }

    public List<Request> getStartRequests() {
        List<Request> requests = new ArrayList<>();
        Date today = new Date();
        for (Project project : getProject(Site.WEIXIN)) {
            WeixinTask timeDesc = weixinTaskRepository.findByOrOrderByEndTimeDesc(project.getId());
            Date timeIndex = new Date("2019-03-16");
            if (timeDesc != null) {
                timeIndex = timeDesc.getEndTime(); //最后获取爬取的时间
            }
            logger.info("keywords : {}", project.getKeywords());

            for (String keyword : project.getKeywords()) {
                logger.info("start time : {}, today : {}", timeIndex, today);
                int count = 1;
                while (true) {
                    timeIndex = DateUtils.addDays(timeIndex, count);
                    if (DateUtils.isSameDay(timeIndex, today) || timeIndex.compareTo(new Date()) > 0) {
                        break;
                    }
                    String startDateString = simpleDateFormat.format(timeIndex);
                    String url = "http://weixin.sogou.com/weixin?type=2&ie=utf8&query=" + UrlUtils.encodeStr(keyword)
                            + "&tsn=5&ft=" + startDateString + "&et=" + startDateString;
                    logger.info("**************url:{}**********", url);
                    requests.add(new Request(url).putExtra("KEY_KEYWORD", keyword));
                }
            }
            if (!CollectionUtils.isEmpty(requests)) {
                WeixinTask weixinTask = new WeixinTask();
                weixinTask.setProjectId(project.getId());
                weixinTask.setEndTime(DateUtils.addDays(new Date(),-1));
                weixinTaskRepository.save(weixinTask);
            }
        }
        logger.info("requests size : {}", requests.size());
        return requests;
    }


    private List<Project> getProject(Site siteEnum) {
        List<Project> curProjects = projectRepository.findAllActive();
        List<Project> projectList = new LinkedList<>();
        for (Project project : curProjects) {
            if (projectHasSite(project, siteEnum)) {
                projectList.add(project);
            }
        }
        return projectList;

    }

    private static boolean projectHasSite(Project project, Site siteEnum) {
        if (project.getSites() != null && project.getSites().length > 0) {
            for (String site : project.getSites()) {
                if (site.equalsIgnoreCase(siteEnum.getCode())) {
                    return true;
                }
            }
        }
        return false;
    }

}
