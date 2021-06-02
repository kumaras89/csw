package com.csw.task.service.exception;

public class XMLBuildingException  extends RuntimeException {
    public XMLBuildingException(String message, Exception e) {
        super(message,e);
    }
    public XMLBuildingException(Exception e) {
        super(e);
    }
}
