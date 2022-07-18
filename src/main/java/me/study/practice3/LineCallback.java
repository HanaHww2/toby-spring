package me.study.practice3;
/*
* 제네릭을 활용한 확장
* */
public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}
