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

    public void add(User user) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        try{
            c = dataSource.getConnection();
            ps = c.prepareStatement(
                    "insert into users(id,name, password) values (?, ?, ?)");

            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
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

    public void deleteAll() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = dataSource.getConnection();
            //ps = makeStatement(c);
            /*
            * 전략 패턴은 필요에 따라 컨텍스트는 유지하면서 전략을 바꾸어 쓸 수 있어야하지만,
            * 이하의 코드에서는 구현체의 정보가 노출되어 있고, 정적으로 고정되어 있다.
            * */
            StatementStrategy strategy = new DeleteAllStatement();
            ps = strategy.makePreparedStatement(c);
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
