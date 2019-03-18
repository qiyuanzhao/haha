package com.weixin.entity;

public class UserInfo {

    public String id;

    public String userName;

    public String displayName;

    public String dn;

    public String avatar;

    public String site;

    public int followersCount;

    public int friendsCount;

    @Override
    public String toString() {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", dn='" + dn + '\'' +
                ", avatar='" + avatar + '\'' +
                ", followersCount=" + followersCount +
                ", friendsCount=" + friendsCount +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDn() {
        return dn;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getSite() {
        return site;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }
}
