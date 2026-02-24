package com.example.gymcrm.initialize;

public interface StoreInitializer {
    String scopeName();
    void parseLineAndSave(String line);
}