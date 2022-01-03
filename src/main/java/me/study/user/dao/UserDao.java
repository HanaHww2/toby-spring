package me.study.user.dao;

import me.study.user.domain.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.*;

/*
* Dao 클래스는 사용자 데이터 액세스 작업을 위한 SQL을 생성하고, 이를 실행한다.
* */
public class UserDao {
    private ConnectionMaker connectionMaker;

    // 생성자 파라미터를 이용해 외부에서 오브젝트를 주입받는다.(의존관계 생성)
    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    // setter 메소드를 이용한 주입 방식 적용을 위해 기본 생성자 수정
    public UserDao() {}

    /*
    * 수정자 메소드 DI 방식 사용을 위한 수정자 메소드
    * */
    public void setConnectionMaker(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = connectionMaker.makeConnection();
        PreparedStatement ps = c.prepareStatement(
                "insert into users(id,name, password) values (?, ?, ?)");

        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());
        ps.executeUpdate();
        ps.close();
        c.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = connectionMaker.makeConnection();
        PreparedStatement ps = c.prepareStatement(
                "select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();
        return user;
    }
}
