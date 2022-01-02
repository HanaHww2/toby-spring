package me.study.user.dao;

import me.study.user.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

public class UserDaoXmlConfigWithDSTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        ApplicationContext context = new GenericXmlApplicationContext("applicationContextWithDS.xml");
        UserDaoWithDS dao = context.getBean("userDaoWithDS", UserDaoWithDS.class); // 두번째 매개변수로 제네릭 타입을 이용한 반환 타입 지정

        User user = new User();
        user.setId("test9");
        user.setName("하나");
        user.setPassword("password");

        dao.add(user);
        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + " 조회 성공");

    }
}
