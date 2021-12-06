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

    // 의존관계 검색(DL)을 이용하는 생성자
    public UserDao() {

        /*
        * 외부 컨테이너를 이용해 다이내믹하게 런타임 의존관계를 맺는다, 하지만 외부에서 주입되는 형태가 아닌 직접 요청하는 방식이다.
        DaoFactory daoFactory = new DaoFactory();
        this.connectionMaker = daoFactory.connectionMaker();
        */

        /*
        * 위와 달리, 스프링 컨테이너를 이용하면 의존관계 검색을 통해 의존관계를 주입받게 된다.
        * */
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        this.connectionMaker = context.getBean("connectionMaker", ConnectionMaker.class);
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
