package com.weixin.entity;

import com.constant.Status;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tao
 */
@Entity
public class Project {

    @Id
    private Long id;

    private String name;

    private String keywordsConcat;

    private String excludeKeywordsConcat;

    private Date timeCreated;

    private String sitesConcat;

    @Transient
    private String[] sites;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Project() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeywordsConcat() {
        return keywordsConcat;
    }

    public void setKeywordsConcat(String keywordsConcat) {
        this.keywordsConcat = keywordsConcat;
    }

    public String getExcludeKeywordsConcat() {
        return excludeKeywordsConcat;
    }

    public void setExcludeKeywordsConcat(String excludeKeywordsConcat) {
        this.excludeKeywordsConcat = excludeKeywordsConcat;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Set<String> getKeywords() {
        if (StringUtils.isEmpty(this.keywordsConcat)) {
            return Collections.emptySet();
        }
        return Stream.of(this.keywordsConcat.split(","))
                    .filter(k -> !StringUtils.isEmpty(k))
                    .collect(Collectors.toSet());
    }

    public Set<String> getExcludedKeywords() {
        if (StringUtils.isEmpty(this.excludeKeywordsConcat)) {
            return Collections.emptySet();
        }
        return Stream.of(this.excludeKeywordsConcat.split(","))
                .filter(k -> !StringUtils.isEmpty(k))
                .collect(Collectors.toSet());
    }

    public String getSitesConcat() {
        return sitesConcat;
    }

    public void setSitesConcat(String sitesConcat) {
        this.sitesConcat = sitesConcat;
    }

    public String[] getSites() {
        if (StringUtils.isEmpty(this.sitesConcat)){
            throw new RuntimeException("sitesConcat is null");
        }
        return sites = this.sitesConcat.split(",");
    }
}
