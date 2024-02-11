package com.typeconverter.parsers;

public abstract class Parsers implements Parse {

	public static Parse getParser(ClassType type) {
		return type.getParser();
	}
	
	

}
