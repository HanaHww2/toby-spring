package me.study.user.dao;

/*
 * 오브젝트 팩토리 클래스를 이용한 제어의 역전 적용
 * dao와 connectionMaker 구현 클래스 간
 * 런타임 오브젝트 의존 관계를 설정하는 책임을 갖는다.
 * 즉, dao 클래스 객체의 생성 방법을 결정하고 객체를 반환한다.
 * */
public class DaoFactory {
    public UserDao userDao() {
        UserDao userDao = new UserDao(connectionMaker());// 생성자 파라미터로 오브젝트 전달, 런타임 의존 관계 주입
        return userDao;
    }
    public SampleDao sampleDao() {
        SampleDao sampleDao = new SampleDao(connectionMaker());
        return sampleDao;
    }
    /*
    * Dao가 많아지는 경우 발생할 중복 문제 해결을 위해 메소드 분리
    * */
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
