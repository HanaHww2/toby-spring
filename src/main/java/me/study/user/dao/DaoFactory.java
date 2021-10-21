package me.study.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 설정 정보
public class DaoFactory {
    @Bean // 오브젝트 생성을 담당하는 IoC용 메소드를 가리키는 어노테이션
    public UserDao userDao() {
        UserDao userDao = new UserDao(connectionMaker());// 생성자 파라미터로 오브젝트 전달, 런타임 의존 관계 설정
        return userDao;
    }
    /*
    * Dao가 많아지는 경우 발생할 중복 문제 해결을 위해 메소드 분리
    * */
    @Bean
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
