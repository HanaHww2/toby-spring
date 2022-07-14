package me.study.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
* 템플릿 메소드 패턴보다 유연하고 확장성이 뛰어난 전략 패턴을 적용
* 확장에 해당하는 부분을 별도의 클래스로 만들면서, 추상화 된 인터페이스를 통해 위임하는 방식이다.
* */
public interface StatementStrategy {
    PreparedStatement makePreparedStatement(Connection c) throws SQLException;
}
