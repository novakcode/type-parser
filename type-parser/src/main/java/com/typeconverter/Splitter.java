package com.typeconverter;

import java.util.Arrays;
import java.util.List;

public class Splitter {

	public static List<String> split(String vals, String splitter) {

		return Arrays.asList(vals.split(splitter));
	}
	
	

}
