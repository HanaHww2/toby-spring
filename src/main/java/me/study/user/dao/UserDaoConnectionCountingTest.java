package me.study.user.dao;

import me.study.user.domain.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

/*
* 커넥션 카운팅을 위한 실제 실행 코드, 테스트 코드
* */
public class UserDaoConnectionCountingTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
        // DL 방식으로 dao를 가져온다.
        UserDao dao = context.getBean("userDao", UserDao.class);

        // 간단한 테스트 작업 수행
        User user = new User();
        user.setId("test5");
        user.setName("하나");
        user.setPassword("password");

        dao.add(user);
        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + " 조회 성공");

        // CountingConnectionMaker를 DL 방식으로 가져와서, DB 커넥션 요청 횟수를 확인해 볼 수 있다.
        CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
        System.out.println("Connection counter : " + ccm.getCounter());
    }
}
