package com.weixin.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "weixin_task")
public class WeixinTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date endTime;

    private Long projectId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
