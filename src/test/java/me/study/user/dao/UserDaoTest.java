package me.study.user.dao;

import me.study.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserDaoTest {

    /*
    * 픽스처 fixture : 테스트를 수행하는데 필요한 정보나 오브젝트
    * 여러 테스트에서 반복적으로 사용하므로 로컬 변수가 아닌
    * 인스턴스 변수로 두고, @BeforeEach 메소드를 이용해 생성해두면 편리하다.
    * */
    private UserDaoWithDS dao;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContextWithDS.xml");
        this.dao = context.getBean("userDaoWithDS", UserDaoWithDS.class);

        this.user1 = new User("test101", "하나", "password1");
        this.user2 = new User("test102", "둘", "password2");
        this.user3 = new User("test103", "셋", "password3");
    }

    @Test
    void addAndGet() throws SQLException {

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

        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);
        // 메소드 실행 중 예외가 제대로 발생하는지 확인
        assertThrows(EmptyResultDataAccessException.class, () -> {dao.get("unkown_id");});
    }
}
