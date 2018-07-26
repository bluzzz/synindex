package com.bluz.demo.exception;


public class IndexBuildException extends Exception {
    public IndexBuildException() {
    }

    public IndexBuildException(String message) {
        super(message);
    }

    public IndexBuildException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndexBuildException(Throwable cause) {
        super(cause);
    }
}
