package com.booking.wechat.persistence.service.base;

/**
 * @author 张炎民 2011-11-14
 */

public class EasyHibernateException extends RuntimeException {

    private static final long serialVersionUID = -3359208020354601498L;

    public EasyHibernateException(String message) {
        super(message);
    }

    public EasyHibernateException(String message, Throwable cause) {
        super(message, cause);
    }

    public EasyHibernateException(Throwable cause) {
        super(cause);
    }

}
