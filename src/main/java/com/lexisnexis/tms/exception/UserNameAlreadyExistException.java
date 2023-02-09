package com.lexisnexis.tms.exception;

import com.lexisnexis.tms.advice.UserAppHandlerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("serial")
public class UserNameAlreadyExistException extends RuntimeException {
    private static final Logger LOGGER = LogManager.getLogger(UserAppHandlerException.class);

    public UserNameAlreadyExistException() {
        super();
        LOGGER.debug("tms UserNameAlreadyExistException() ");
    }
}
