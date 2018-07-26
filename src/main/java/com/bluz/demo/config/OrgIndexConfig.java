package com.bluz.demo.config;

import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

public class OrgIndexConfig implements IndexConfig {

    @Value("${org.index}")
    private String orgIndex;
    @Value("${org.type}")
    private String orgIndexType;
    @Value("${org.query.sql}")
    private String orgQuerySql;
    @Value("${org.index.mapping}")
    private String orgIndexMapping;

    @Override
    public String getIndex() {
        return orgIndex;
    }

    @Override
    public String getIndexType() {
        return orgIndexType;
    }

    @Override
    public String getQuerySql() {
        return orgQuerySql;
    }

    @Override
    public String getIndexMapping() {
        return orgIndexMapping;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrgIndexConfig that = (OrgIndexConfig) o;
        return Objects.equals(orgIndex, that.orgIndex) &&
                Objects.equals(orgIndexType, that.orgIndexType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(orgIndex, orgIndexType);
    }
}
