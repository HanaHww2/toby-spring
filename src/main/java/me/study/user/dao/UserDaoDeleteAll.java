package me.study.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
* 추상 클래스를 상속받고 추상 메소드를 구현함으로써 기능을 확장한다.
* 그러나 매 로직마다 서브 클래스를 생성해야 하며,
* 확장 구조가 클래스 설계 시점에 고정이 되어버린다.
* 또한 컴파일 시점에 클레스 간의 관계가 결정되어 버리므로, 유연성이 떨어진다.
* */

public class UserDaoDeleteAll extends AbstractUserDao {
    @Override
    protected PreparedStatement makeStatement(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("delete from users");
        return ps;
    }
}
