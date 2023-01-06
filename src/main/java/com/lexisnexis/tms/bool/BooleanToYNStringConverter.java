package com.lexisnexis.tms.bool;

import jakarta.persistence.AttributeConverter;

public class BooleanToYNStringConverter implements AttributeConverter<Boolean, String> {

	
	@Override
	public String convertToDatabaseColumn(Boolean b) {
		if (b == null) {
			return null;
		}
		if (b.booleanValue()) {
			return "Y";
		}
		return "N";
	}


	@Override
	public Boolean convertToEntityAttribute(String s) {
		if (s == null) {
			return null;
		}
		if (s.equals("Y") || s.equals("y")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

}