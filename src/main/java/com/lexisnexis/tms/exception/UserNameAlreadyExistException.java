package com.lexisnexis.tms.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@SuppressWarnings("serial")
public class UserNameAlreadyExistException extends RuntimeException{
   
	final private static Logger LOGGER = LogManager.getLogger(UserNameAlreadyExistException.class);

    public UserNameAlreadyExistException() {


        super();
        LOGGER.debug("tms UserNameAlreadyExistException() ");
    }
}
