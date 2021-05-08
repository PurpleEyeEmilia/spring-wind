package com.github.springwind.common.exception;

/**
 * @author pengnian
 * @version V1.0
 * @date 2019/9/16 16:13
 * @Desc
 */
public class DistributedLockException extends RuntimeException {

    public DistributedLockException() {
    }

    public DistributedLockException(String message) {
        super(message);
    }

    public DistributedLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public DistributedLockException(Throwable cause) {
        super(cause);
    }

    public DistributedLockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
