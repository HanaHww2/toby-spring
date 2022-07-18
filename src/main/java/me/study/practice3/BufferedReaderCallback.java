package me.study.practice3;

import java.io.BufferedReader;
import java.io.IOException;

/*
* 변하는 계산 로직을 갖는 콜백 인터페이스
* */
public interface BufferedReaderCallback {
    Integer doSomethingWithReader(BufferedReader br) throws IOException;
}
