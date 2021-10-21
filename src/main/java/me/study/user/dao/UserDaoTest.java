package me.study.user.dao;

import me.study.user.domain.User;

import java.sql.SQLException;

/*
* dao의 클라이언트 클래스, 테스트 클래스
* dao와 connectionMaker 구현 클래스 간
* 런타임 오브젝트 의존 관계를 설정하는 책임을 갖는다.
*/
public class UserDaoTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ConnectionMaker connectionMaker = new DConnectionMaker();
        UserDao dao = new UserDao(connectionMaker);// 생성자 파라미터로 오브젝트 전달, 런타임 의존 관계 주입

        User user = new User();
        user.setId("test1");
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
