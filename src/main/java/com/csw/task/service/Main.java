package com.csw.task.service;

public class Main {
    public static void main(String[] args) {
        JsonParser jp = new JsonParser();
        jp.parse(args[0],args[1]);
    }
}
