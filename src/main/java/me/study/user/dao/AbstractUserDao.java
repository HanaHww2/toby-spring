package me.study.user.dao;

import me.study.user.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
* 변화가 발생하는 부분을 상속을 통해 자유롭게 확장할 수 있도록
* 추상 클래스 생성
* */
public abstract class AbstractUserDao {
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

    public void jdbcContextWithStatement() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = dataSource.getConnection();
            ps = this.makeStatement(c);
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
    abstract protected PreparedStatement makeStatement(Connection c) throws SQLException;
}
