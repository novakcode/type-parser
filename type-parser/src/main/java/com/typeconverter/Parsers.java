package com.typeconverter;

public abstract class Parsers implements Parse {

	public static Parse getParser(ClassType type) {
		return type.getParser();
	}

}
