package com.bluz.demo.config;

import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

public class EventIndexConfig implements IndexConfig {

    @Value("${event.index}")
    private String eventIndex;
    @Value("${event.type}")
    private String eventIndexType;
    @Value("${event.query.sql}")
    private String eventQuerySql;
    @Value("${event.index.mapping}")
    private String eventIndexMapping;



    @Override
    public String getIndex() {
        return eventIndex;
    }

    @Override
    public String getIndexType() {
        return eventIndexType;
    }

    @Override
    public String getQuerySql() {
        return eventQuerySql;
    }

    @Override
    public String getIndexMapping() {
        return eventIndexMapping;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventIndexConfig that = (EventIndexConfig) o;
        return Objects.equals(eventIndex, that.eventIndex) &&
                Objects.equals(eventIndexType, that.eventIndexType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(eventIndex, eventIndexType);
    }
}
