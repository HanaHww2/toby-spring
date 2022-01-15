package me.study.user.dao;

import me.study.user.dao.UserDaoWithDS;
import me.study.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.assertThat;

public class UserDaoTest {
    @Test
    void addAndGet() throws SQLException {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContextWithDS.xml");
        UserDaoWithDS dao = context.getBean("userDaoWithDS", UserDaoWithDS.class);

        User user = new User();
        user.setId("test101");
        user.setName("하나");
        user.setPassword("password");

        dao.add(user);
        User user2 = dao.get(user.getId());

        // 테스트 결과의 검증을 Junit이 제공하는 방법을 통해 수행한다.
        assertThat(user2.getName()).isEqualTo(user.getName());
        assertThat(user2.getPassword()).isEqualTo(user.getPassword());
    }
}
