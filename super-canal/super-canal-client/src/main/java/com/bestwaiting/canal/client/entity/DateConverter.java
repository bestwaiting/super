package com.bestwaiting.canal.client.entity;

import org.springframework.core.convert.converter.Converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DateConverter implements Converter<String, Date> {
	
	
	private static final List<String> formarts = new ArrayList<String>(4);
	static{
		formarts.add("yyyy-MM");
		formarts.add("yyyy-MM-dd");
		formarts.add("yyyy-MM-dd HH:mm");
		formarts.add("yyyy-MM-dd HH:mm:ss");
	}
	public Date convert(String source) {
		String value = source.trim();
		if ("".equals(value)) {
			return null;
		}
		if(source.matches("^\\d{4}-\\d{1,2}$")){ 
			return parseDate(source, formarts.get(0));
		}else if(source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")){
			return parseDate(source, formarts.get(1));
		}else if(source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")){
			return parseDate(source, formarts.get(2));
		}else if(source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")){
			return parseDate(source, formarts.get(3));
		}else {
			throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
		}
	}

	/**
	 * ������������ʽ������
	 *
	 * @param dateStr
	 *            String �ַ�������
	 * @param format
	 *            String ��ʽ
	 * @return Date ����
	 */
	public  Date parseDate(String dateStr, String format) {
		Date date=null;
		try {
			DateFormat dateFormat = new SimpleDateFormat(format);
			date = (Date) dateFormat.parse(dateStr);
		} catch (Exception e) {
		}
		return date;
	}
	public static void main(String[] args) {
		System.err.println(new DateConverter().convert("2014-04-23 21:33"));
	}
}