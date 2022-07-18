package me.study.practice3;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/*
* 연습용으로 만든 숫자합 계산기 코드 테스트
* */
public class CalculatorTest {
    Calculator calculator;
    String numFilepath;
    /*
    * 픽스처 생성
    * 중복해서 사용되는 객체 및 파일 path를 인스턴스 변수로 생성하여 공유
    * */
    @BeforeEach
    void setUp() {
        this.calculator = new Calculator();
        this.numFilepath = getClass().getResource("/numbers.txt").getPath();
    }
    @Test
    void checkGetPathFromWhere(){
        // TODO getClass(), getClassLoader() 공부하기
        System.out.println(getClass().getClassLoader().getResource("numbers.txt").getPath());
        System.out.println(getClass().getResource("/numbers.txt").getPath()); // 이상의 두 경우는 리소스의 루트로 설정된 resource 폴더
        System.out.println(getClass().getResource("numbers.txt").getPath()); //  빌드된 classpath에서 해당 클래스가 위치한 장소에서 리소스 조회
    }

    @Test
    void sumOfNumbers() throws IOException {
//        Calculator calc = new Calculator();
//        int sum = calc.clacSum(getClass().getResource("/numbers.txt").getPath());
//        Assertions.assertThat(sum).isEqualTo(10);

        //Assertions.assertThat(calculator.clacSum(numFilepath)).isEqualTo(10);
        Assertions.assertThat(calculator.clacSumWithLine(this.numFilepath)).isEqualTo(10);
    }

    @Test
    void multiplyOfNumbers() throws IOException {
        //Assertions.assertThat(calculator.clacMultiply(numFilepath)).isEqualTo(24);
        Assertions.assertThat(calculator.clacMultiplyWithLine(this.numFilepath)).isEqualTo(24);
    }
    @Test
    void concatenateStrings() throws IOException {
        Assertions.assertThat(calculator.concatenate(this.numFilepath)).isEqualTo("1234");
    }
}

