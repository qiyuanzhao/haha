package com.repository;

import com.weixin.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("select p from Project p where p.status <> 'DELETED' and p.id = ?1")
    Project findActiveById(Long projectId);

    @Query("select p from Project p where p.status = 'ACTIVE' order by p.id desc")
    List<Project> findAllActive();
}
