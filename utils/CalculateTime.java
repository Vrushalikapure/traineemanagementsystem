package com.lexisnexis.tms.utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class CalculateTime {

	public static void main(String[] args) throws ParseException {
		Calendar date = Calendar.getInstance();
		System.out.println("Current Date and TIme : " + date.getTime());
		long timeInSecs = date.getTimeInMillis();
		Date afterAdding10Mins = new Date(timeInSecs + (15 * 60 * 1000));
		System.out.println("After adding 15 mins : " + afterAdding10Mins);
	}
	
}
