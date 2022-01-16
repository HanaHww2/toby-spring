package me.study.user.dao;

import me.study.user.dao.UserDaoWithDS;
import me.study.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserDaoTest {

    @Test
    void addAndGet() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContextWithDS.xml");
        UserDaoWithDS dao = context.getBean("userDaoWithDS", UserDaoWithDS.class);

        User user1 = new User("test101", "하나", "password1");
        User user2 = new User("test102", "둘", "password2");

        /*
        * 동일한 결과를 보장하는 테스트를 위해
        * UserDaoWithDS 에 deleteAll() 과 getCount() 메소드 추가 후
        * 테스트 코드를 수정하며,
        * deleteAll() 과 getCount() 메소드의 검증 작업을 추가
        * */
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        dao.add(user1);
        dao.add(user2);
        // getCount() 메소드 검증 작업 추가
        assertThat(dao.getCount()).isEqualTo(2);

        User userGet1 = dao.get(user1.getId());
        // 테스트 결과의 검증을 Junit이 제공하는 방법을 통해 수행한다.
        assertThat(userGet1.getName()).isEqualTo(user1.getName());
        assertThat(userGet1.getPassword()).isEqualTo(user1.getPassword());

        // 두 개의 User를 이용하여 get() 메소드에 대한 검증 강화
        User userGet2 = dao.get(user2.getId());
        assertThat(userGet2.getName()).isEqualTo(user2.getName());
        assertThat(userGet2.getPassword()).isEqualTo(user2.getPassword());

    }

    /*
    * getCount()를 위한 테스트
    * */
    @Test
    void count() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContextWithDS.xml");
        UserDaoWithDS dao = context.getBean("userDaoWithDS", UserDaoWithDS.class);

        User user1 = new User("test101", "하나", "password1");
        User user2 = new User("test102", "둘", "password2");
        User user3 = new User("test103", "셋", "password3");

        dao.deleteAll();

        assertThat(dao.getCount()).isEqualTo(0);
        dao.add(user1);
        assertThat(dao.getCount()).isEqualTo(1);
        dao.add(user2);
        assertThat(dao.getCount()).isEqualTo(2);
        dao.add(user3);
        assertThat(dao.getCount()).isEqualTo(3);
    }

    /*
    * get() 메소드의 예외상황에 대한 테스트
    * 예외가 발생하면 성공, 발생하지 않으면 실패하는 테스트
    * */
    @Test
    void getUserFailure() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContextWithDS.xml");

        UserDaoWithDS dao = context.getBean("userDaoWithDS", UserDaoWithDS.class);
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);
        // 메소드 실행 중 예외가 제대로 발생하는지 확인
        assertThrows(EmptyResultDataAccessException.class, () -> {dao.get("unkown_id");});
    }
}
