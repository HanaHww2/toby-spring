package me.study.user.dao;

import me.study.user.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
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
        //StatementStrategy st = new DeleteAllStatement(); // 클라이언트 역할의 메소드가 실제 사용할 전략 클래스의 구현체(객체)를 생성
        // 컨텍스트 메소드를 호출하여 전략 오브젝트 전달
        jdbcContextWithStatementStrategy(
                new StatementStrategy() {
                    @Override
                    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                        PreparedStatement ps = c.prepareStatement("delete from users");
                        return ps;
                    }
                }
        );
    }
    public void add(final User user) throws SQLException {
        // 내부 클래스가 선언된 로컬의 매개변수(user)에 접근 가능하므로,
        // 내부 클래스의 user 멤버 변수와 이를 초기화해주는 생성자를 지울 수 있다.
        // 다만 final로 선언되어야 사용할 수 있으므로, 메소드의 매개변수에 이러한 제한자를 추가한다.

        // 해당 메소드에서만 사용되는 클래스이므로
        // 외부에 생성하기 보다는 메소드의 내부 클래스(로컬 클래스)로 생성해서 사용하는 것이 깔끔하다.
        class InnerAddStatement implements StatementStrategy {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");

                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());

                return ps;
            }
        }
        StatementStrategy st = new InnerAddStatement();

        jdbcContextWithStatementStrategy(
                // 내부 클래스 선언보다도 익명 내부 클래스로 바로 선언과 동시에 구현체를 생성하여
                // 매개변수로 전달하면, 더욱 간결하게 작성할 수 있다.
                new StatementStrategy() {
                    @Override
                    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");

                        ps.setString(1, user.getId());
                        ps.setString(2, user.getName());
                        ps.setString(3, user.getPassword());

                        return ps;
                    }
                }
        );
    }

    public User get(String id) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement(
                "select * from users where id = ?");
            ps.setString(1, id);
            rs = ps.executeQuery();

            if (rs.next()) { // 쿼리의 결과가 있다면,
                user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            throw e; // 다시 메소드 밖으로 예외를 던진다.
        } finally {
            if (rs!=null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
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

        // 쿼리 결과로 가져 온 user가 없다면,
        // 예외를 던진다.
        if (user == null) throw new EmptyResultDataAccessException(1);
        return user;
    }

    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement(
                    "select count(*) from users");
            rs = ps.executeQuery();
            rs.next();
            count = rs.getInt(1);

        } catch (SQLException e) {
            throw e; // 다시 메소드 밖으로 예외를 던진다.
        } finally {
            if (rs!=null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
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
            c.close();
        }
        return count;
    }

    /*
    * 단순 메소드 추출 적용
    * 여전히 반복되는 부분의 재사용은 불가능하다.
    * 그다지 이득이 없어 보인다.
    * -> 템플릿 메소드 패턴 활용 : 추상 클래스 생성
    * */
    private PreparedStatement makeStatement(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("delete from users");
        return ps;
    }
}
