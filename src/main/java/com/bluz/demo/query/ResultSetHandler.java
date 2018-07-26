package com.bluz.demo.query;

import java.sql.ResultSet;

public interface ResultSetHandler {
    void handle(ResultSet resultSet) throws Exception;
}
