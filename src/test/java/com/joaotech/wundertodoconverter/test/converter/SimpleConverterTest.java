package com.joaotech.wundertodoconverter.test.converter;

import java.io.File;

import org.junit.Test;

import com.joaotech.wundertodoconverter.converter.SimpleConverter;

public class SimpleConverterTest {

	@Test
	public void simpleFileConverterTest() {
		File json = new File("C:\\Users\\Gabriel\\Dropbox\\Wunderlist Backup\\wunderlist-20150418-final-edit.json");
		
		SimpleConverter simpleConverter = new SimpleConverter(json);
		simpleConverter.processJson("C:\\Temp");
	}
	
}
