package me.study.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
* 추상체인 인터페이스의 구현체인 실제 전략을 갖는 클래스 생성
* */
public class DeleteAllStatement implements StatementStrategy{
    @Override
    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("delete from users");
        return ps;
    }
}
