package me.study.user.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
* 결과적으로 해당 클래스 내에
* 클라이언트, 템플릿, 콜백이 모두 공존하면서 함께 동작하는 구조가 되었다.
* 이와 같이 하나의 목적을 위해 서로 긴밀하게 연관되어 동작하는 응집력 강한 코드는
* 한군데 모아서 캡슐화하는 것이 유리하다.
* 외부에는 꼭 필요한 기능을 제공하는 단순한 메소드만을 노출한다.
* */
public class JdbcContext {

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
    public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
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

    /*
    * 모든 DAO에서 템플릿에 콜백 메소드를 전달하는
    * 클라이언트 메소드를 활용할 수 있도록 템플릿 메소드를 가진 클래스로 이동
    * */
    public void executeSql(final String query) throws SQLException {
        this.workWithStatementStrategy(
                new StatementStrategy() {
                    @Override
                    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                        return c.prepareStatement(query);
                    }
                }
        );
    }

}