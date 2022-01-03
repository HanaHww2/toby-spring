package me.study.user.dao;

import me.study.user.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

/*
* dao의 클라이언트 클래스, 테스트 클래스
*/
public class UserDaoTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao2", UserDao.class); // 두번째 매개변수로 제네릭 타입을 이용한 반환 타입 지정

        User user = new User();
        user.setId("test12");
        user.setName("하나");
        user.setPassword("password");

        dao.add(user);
        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());

        // 테스트 결과의 검증을 자동화하는 코드로 수정한다.
        if (!user.getName().equals(user2.getName())) {
            System.out.println("테스트 실패 (name)");
        }
        else if (!user.getPassword().equals(user2.getPassword())) {
            System.out.println("테스트 실패 (password)");
        }
        else {
            System.out.println("조회 테스트 성공");
        }

    }
}
