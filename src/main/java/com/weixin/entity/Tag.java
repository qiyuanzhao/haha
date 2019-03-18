package com.weixin.entity;

import java.util.Date;


public class Tag {

    public Tag() {
    }

    public String name;
    public TagOrigin origin;
    public Float weight;
    public boolean newTag = false;

    public Date tC;
    public Date tU;

    public enum TagOrigin {
        Source, // source site tags
        Manual, // manual tag
        Machine, // ML/Analytics result
        Topics, // based on site topics
        Brand,  // based on brand name
        Account, // based on account id.
        Search, // site search result
        Match, // text match in title and content
        Filter, // text found for project filters
        DataProcess,
        Other,
        Title, // title of the web page
        Category, // category of good
        Hashtag, // hash tags of a page
        Mark,
        Project, // project of the projectId
        Exclude // 项目排除词
    }
}
