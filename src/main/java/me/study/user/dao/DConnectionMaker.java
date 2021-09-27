package me.study.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DConnectionMaker implements ConnectionMaker {

    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver"); // ClassNotFoundException
        Connection c = DriverManager.getConnection( // SQLException
                "jdbc:mysql://localhost/toby", "root", "1234");
        return c;
    }
}