package com.karnty.training.backend.exception;

public class FileException extends BaseException {
    public FileException(String code) {
        super("file." + code);
    }
    public static FileException fileNull() {
        return new FileException("null");
    }

    public static FileException fileMaxSize() {
        return new FileException("max.size");
    }
    public static FileException fileTypeNotSupported() {
        return new FileException("type.not.supported");
    }
}
