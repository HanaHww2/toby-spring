package me.study.context;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class JUnitTest {
    static JUnitTest testObject;
    static Set<JUnitTest> testObjects = new HashSet<>();

    @DisplayName("JUnit을 이용한 테스트에서 매번 테스트 클래스가 새롭게 만들어지는지 확인1")
    @Test void test1() {
        assertThat(this).isNotSameAs(testObject);
        testObject = this;
    }

    @DisplayName("JUnit을 이용한 테스트에서 매번 테스트 클래스가 새롭게 만들어지는지 확인2")
    @Test void test2() {
        assertThat(this).isNotSameAs(testObject);
        testObject = this;
    }

    @DisplayName("JUnit을 이용한 테스트에서 매번 테스트 클래스가 새롭게 만들어지는지 확인3")
    @Test void test3() {
        assertThat(this).isNotSameAs(testObject);
        testObject = this;
    }

    @Test void checkIfJUnitMakesNewObjectWithSet1() {
        assertThat(testObjects).doesNotContain(this);
        testObjects.add(this);
    }

    @Test void checkIfJUnitMakesNewObjectWithSet2() {
        assertThat(testObjects).doesNotContain(this);
        testObjects.add(this);
    }

    @Test void checkIfJUnitMakesNewObjectWithSet3() {
        assertThat(testObjects).doesNotContain(this);
        testObjects.add(this);
    }

    /*
    * 이하 테스트는 실패한다.
    * 하나의 테스트 메소드 내에서 수행이 되기에 동일한 컨텍스트를 유지한다.
    * 즉, this는 매번 같은 객체가 된다.
    * */
//    @Test void testTreeTimes() {
//        System.out.println(testObject); // null
//        System.out.println(this);
//        test1(); // 최초에는 testObject가 null 값이어서 패스
//        test1(); // 오류 발생
//        test1();
//    }
//    @Test void checkIfJUnitMakesNewObjectWithSet() {
//        checkIfJUnitMakesNewObjectWithSet1();
//        checkIfJUnitMakesNewObjectWithSet1(); // 오류 발생
//        checkIfJUnitMakesNewObjectWithSet1();
//    }
}
