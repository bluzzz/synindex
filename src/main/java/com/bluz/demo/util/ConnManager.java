package com.bluz.demo.util;

// 数据库操作管理器

import com.bluz.demo.query.ResultSetHandler;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@Component
public class ConnManager {
    @Autowired
    private SpringApplicationContext context;

    public Connection getConn() throws Exception{
        SqlSessionFactory sqlSessionFactory=
                (SqlSessionFactory)context.getBean("sqlSessionFactory");
        SqlSession session = sqlSessionFactory.openSession();
        return session.getConnection();
    }

    public void executeQuery(String sql, ResultSetHandler handler) throws Exception{
        Connection conn = getConn();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();
        handler.handle(resultSet);
        close(conn,ps,resultSet);
    }

    public void close(Connection conn, Statement st,ResultSet rs) throws Exception{
        if(rs!=null){
            rs.close();
        }

        if(st!=null){
            st.close();
        }

        if(conn!=null){
            conn.close();
        }
    }
}
