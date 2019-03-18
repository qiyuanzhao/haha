package com.repository;


import com.weixin.entity.WeixinTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface WeixinTaskRepository extends JpaRepository<WeixinTask, Long> {

    @Query(value = "select wt.* from weixin_task wt where wt.project_id = ?1 order by wt.end_time DESC limit 1",nativeQuery = true)
    WeixinTask findByOrOrderByEndTimeDesc(Long projectId);

    @Modifying
    @Query(value = "delete from WeixinTask wt where wt.projectId = ?1 and wt.endTime = ?2")
    void deleteByProjectId(Long projectId, Date date);

    @Query(value = "select wt from WeixinTask wt where wt.projectId = ?1")
    List<WeixinTask> findByProjectId(Long projectId);
}
