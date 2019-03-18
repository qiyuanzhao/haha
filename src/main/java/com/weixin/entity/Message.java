package com.weixin.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;


public class Message {


    private String id;

    private String content;

    public String quotedMessage;

    private String title;

//    @JsonProperty("timeCreated")
//    @JsonSerialize(using = DateToLongSerializer.class)
    private Long tC;

    public Date tF;

    //    @JsonProperty("postingUser")
//    @SerializedName("postingUser")
//    @JsonProperty("pU")
    public UserInfo pU;

    public UserInfo user;

    public Integer reposts; //转

    public Integer likes; //赞

    public Integer comments; //评

    public String url;

    public List<Picture> pics;

    public String context;

    public String site;

    public String type;

    public NLP nlp;

    public String client;

    public String qmId;

    public Float sScore;

    public Float pScore;


    public Set<Tag> tags = new HashSet<>();

    @Transient
    public String quotedMessageEntity;

    @Transient
    public String indexName;

    public Message() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long gettC() {
        return tC;
    }

    public void settC(Long tC) {
        this.tC = tC;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String getQuotedMessage() {
        return quotedMessage;
    }

    public void setQuotedMessage(String quotedMessage) {
        this.quotedMessage = quotedMessage;
    }

    public Date gettF() {
        return tF;
    }

    public UserInfo getpU() {
        return pU;
    }

    public Integer getReposts() {
        return reposts;
    }

    public Integer getLikes() {
        return likes;
    }

    public Integer getComments() {
        return comments;
    }

    public List<Picture> getPics() {
        return pics;
    }

    public String getContext() {
        return context;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }

    public NLP getNlp() {
        return nlp;
    }

    public String getClient() {
        return client;
    }

    public String getQmId() {
        return qmId;
    }

    public Float getsScore() {
        return sScore;
    }

    public Float getpScore() {
        return pScore;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public String getQuotedMessageEntity() {
        return quotedMessageEntity;
    }

    public String getIndexName() {
        return indexName;
    }

    public void settF(Date tF) {
        this.tF = tF;
    }

    public void setpU(UserInfo pU) {
        this.pU = pU;
    }

    public void setReposts(Integer reposts) {
        this.reposts = reposts;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public void setPics(List<Picture> pics) {
        this.pics = pics;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNlp(NLP nlp) {
        this.nlp = nlp;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public void setQmId(String qmId) {
        this.qmId = qmId;
    }

    public void setsScore(Float sScore) {
        this.sScore = sScore;
    }

    public void setpScore(Float pScore) {
        this.pScore = pScore;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void setQuotedMessageEntity(String quotedMessageEntity) {
        this.quotedMessageEntity = quotedMessageEntity;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", tC=" + tC +
                ", pU=" + pU +
                ", user=" + user +
                ", reposts=" + reposts +
                ", likes=" + likes +
                ", comments=" + comments +
                '}';
    }

    public static class NLP {

        public Set<BasedTagNLP> competitors = new HashSet<>();
        public Set<BasedTagNLP> complaints = new HashSet<>();
        public Set<BasedTagNLP> advertisements = new HashSet<>();
        public Set<BasedTagNLP> sentiments = new HashSet<>();
        public Set<BasedTagNLP> pIntentions = new HashSet<>();
        public Set<BasedTagNLP> pIntentionsDL = new HashSet<>();
    }

    public static class BasedTagNLP {

        public String tag;
        public Number value;
        public BasedTagNlpType type = BasedTagNlpType.Manual;
        public Date timeUpdated;
        public Date tU = timeUpdated;
    }

    public enum BasedTagNlpType {
        Manual
    }
}
