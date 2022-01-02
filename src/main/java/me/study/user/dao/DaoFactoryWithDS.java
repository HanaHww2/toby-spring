package me.study.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration // 설정 정보
public class DaoFactoryWithDS {

    /*
    * 수정자 메소드 DI를 사용하는 팩토리 메소드
    * */
    @Bean // 오브젝트 생성을 담당하는 IoC용 메소드를 가리키는 어노테이션
    public UserDaoWithDS userDao() {
        UserDaoWithDS userDao = new UserDaoWithDS();
        userDao.setDataSource(dataSource());
        return userDao;
    }

    /*
    * DataSource 인터페이스를 활용
    * */
    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/toby");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");
        return dataSource;
    }
}
