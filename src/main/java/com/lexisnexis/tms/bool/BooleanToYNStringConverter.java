package com.lexisnexis.tms.bool;

import javax.persistence.AttributeConverter;

public class BooleanToYNStringConverter implements AttributeConverter<Boolean, String> {
    @Override
    public String convertToDatabaseColumn(Boolean bool) {
        if (bool == null) {
            return null;
        }
        if (bool.booleanValue()) {
            return "Y";
        }
        return "N";
    }

    @Override
    public Boolean convertToEntityAttribute(String str) {
        if (str == null) {
            return null;
        }
        if ("Y".equals(str)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}