package me.study.user.dao;

import me.study.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserDaoTestWithNoSpring {

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

        /*
        * 매 시행마다 주소값이 다르다.
        * JUnit은 테스트 메소드를 실행할 때마다 새로운 테스트 오브젝트를 만들기 때문이다.
        * */
        System.out.println(this);

        /*
        * 스프링에서 제공하는 컨테이너(applicationContext)에 의존하지 않고,
        * 직접 수동으로 DI를 수행한다.
        * DataSource 객체를 만드는 번거로움은 있지만, 코드는 더 간단해진다.
        * applicationContext가 만들어지는 시간도 절약된다.
        * ""=> UserDao 코드가 애초에 스프링 API에 의존하지 않고 자신의 관심에만 집중한 코드이기에 가능한 방식이다.
        * +) 스프링이 비침투적인 기술이기 때문이다.""
        * */
        this.dao = new UserDaoWithDS();
        // 테스트용 db 활용을 위해 새로운 dataSource 객체 생성
        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mysql://localhost/testdb", "root", "1234", true);
        this.dao.setDataSource(dataSource);

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
