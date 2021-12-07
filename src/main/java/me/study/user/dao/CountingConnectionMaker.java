package me.study.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

/*
* 부가기능(연결횟수 카운팅)을 추가한 ConnectionMaker
* 내부에서 직접 DB 커넥션을 만들지 않고, DI를 통해서 주입받은 객체를 활용한다
* DAO가 DB 커넥션을 가져올 때마다 호출하는 makeConnection()에서 DB 연결횟수 카운터를 증가시키는 역할만 수행한다.
* */
public class CountingConnectionMaker implements ConnectionMaker{

    int counter = 0;
    private ConnectionMaker realConnectionMaker;

    public CountingConnectionMaker(ConnectionMaker realConnectionMaker) {
        this.realConnectionMaker = realConnectionMaker;
    }

    public int getCounter() {
        return this.counter;
    }

    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        this.counter++;
        return realConnectionMaker.makeConnection();
    }
}
