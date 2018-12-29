package com.qzf.test;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.springframework.format.annotation.DateTimeFormat;

public class Test1 {

	public static void main(String[] args) throws Exception {
		//System.out.println(reverst("hello"));
		//test2();
		//test3();
		//test4();
		//test5();
		//test6();
		test7();
		System.out.println(test8());
	}
	
	public static String reverst(String str) {
		if(str == null || str.length()<=1) {
			return str;
		}
		return reverst(str.substring(1))+ str.charAt(0);
	}
	public static void test2() {
		Calendar cal = Calendar.getInstance();
		System.out.println(cal.get(Calendar.YEAR));
		System.out.println(cal.get(Calendar.MONTH));
		System.out.println(cal.get(Calendar.DAY_OF_MONTH));
		System.out.println(cal.get(Calendar.HOUR));
		System.out.println(cal.get(Calendar.MINUTE));
		System.out.println(cal.get(Calendar.SECOND));
		
		LocalDateTime datatime = LocalDateTime.now();
		System.out.println(datatime.getYear());
		System.out.println(datatime.getMonthValue());
		System.out.println(datatime.getDayOfMonth());
		System.out.println(datatime.getHour());
		System.out.println(datatime.getMinute());
		System.out.println(datatime.getSecond());
	}
	
	public static void test3() {
		System.out.println(Calendar.getInstance().getTimeInMillis());
		System.out.println(System.currentTimeMillis());
		System.out.println(Clock.systemDefaultZone().millis());
	}
	
	public static void test4() {
		Calendar cal = Calendar.getInstance();
		System.out.println(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
	}
	
	public static void test5() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		System.out.println(sdf.format(date));
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime datatime = LocalDateTime.now();
		System.out.println(datatime.format(dtf));
	}
	
	public static void test6() throws IOException {
		File directory = new File("F:\\test\\kingdoms");
		BufferedInputStream bis = null;FileReader read = null;
		if(directory.isDirectory()) {
			File[] files = directory.listFiles();
			for (File file : files) {
//				System.out.println(IOUtils.toString(new FileInputStream(file),"utf-8"));
//				bis = new BufferedInputStream(new FileInputStream(file));
				read = new FileReader(file);
				char[] cbuf = new char[1024]; 
				int len = 0;
				while((len = read.read(cbuf))!=-1) {
					
					String str = new String(cbuf, 0 ,len);
					System.out.println(str);
				}
//				read.read(cbuf, offset, length)
//				byte[] buf = new byte[2048];
//				while((len = bis.read(buf, 0 ,200))!=-1) {
//					String str = new String(buf, "utf-8");
//					System.out.println(str);
//				}
			}
		}
		if(read != null) {
			read.close();
		}
		//Collections
		//Closeable
//		Serializable
	}
	
	public static void test7() {
		Calendar cal  =Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(cal.getTime());
		System.out.println(date);
		
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime yesterday = now.minusDays(1);
		DateTimeFormatter format= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		System.out.println(yesterday.format(format));
	}
	
	public static String test8() {
		
		try {
			return "5";
			
			
		} finally {

			return "6";
		}
	}
	
}
