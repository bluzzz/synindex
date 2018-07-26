package com.bluz.demo.config;

import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;


public class ProjectIndexConfig implements IndexConfig {

    @Value("${project.index}")
    private String projectIndex;
    @Value("${project.type}")
    private String projectIndexType;
    @Value("${project.query.sql}")
    private String projectQuerySql;
    @Value("${project.index.mapping}")
    private String projectIndexMapping;

    @Override
    public String getIndex() {
        return projectIndex;
    }

    @Override
    public String getIndexType() {
        return projectIndexType;
    }

    @Override
    public String getQuerySql() {
        return projectQuerySql;
    }

    @Override
    public String getIndexMapping() {
        return projectIndexMapping;
    }

    public void setProjectIndex(String projectIndex) {
        this.projectIndex = projectIndex;
    }

    public void setProjectIndexType(String projectIndexType) {
        this.projectIndexType = projectIndexType;
    }

    public void setProjectQuerySql(String projectQuerySql) {
        this.projectQuerySql = projectQuerySql;
    }

    public void setProjectIndexMapping(String projectIndexMapping) {
        this.projectIndexMapping = projectIndexMapping;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectIndexConfig that = (ProjectIndexConfig) o;
        return Objects.equals(projectIndex, that.projectIndex) &&
                Objects.equals(projectIndexType, that.projectIndexType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(projectIndex, projectIndexType);
    }
}
