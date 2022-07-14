package me.study.user.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoContext {
    /*
     * 자바에서 제공하는 DataSource 인터페이스를 활용하다
     * */
    private DataSource dataSource;

    /*
     * 수정자 메소드 DI 방식 사용을 위한 수정자 메소드
     * */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /*
    * 컨텍스트(변하지 않는 부분)를 갖는 메소드
    * 전략 인터페이스를 메소드의 파라미터로 지정한다.
    * 파라미터로 전달될 실제 구현체는 이 메소드를 호출하는 클라이언트가 결정한다.
    * */
    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = dataSource.getConnection();
            ps = stmt.makePreparedStatement(c);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e; // 다시 메소드 밖으로 예외를 던진다.
        } finally {
            if (ps!=null) {
                try {
                    ps.close(); // close() 메소드에서도 예외 발생이 가능하므로 주의
                } catch (SQLException e) {
                }
            }
            if (c!=null) {
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void deleteAll() throws SQLException {
        StatementStrategy st = new DeleteAllStatement(); // 클라이언트 역할의 메소드가 실제 사용할 전략 클래스의 구현체(객체)를 생성
        jdbcContextWithStatementStrategy(st); // 컨텍스트 메소드를 호출하여 전략 오브젝트 전달
    }
}
