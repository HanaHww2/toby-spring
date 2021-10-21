package me.study.user.dao;

public class SampleDao {
    private ConnectionMaker connectionMaker;

    public SampleDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }
}
