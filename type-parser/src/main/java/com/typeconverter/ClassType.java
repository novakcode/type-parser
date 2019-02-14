package com.typeconverter;

import com.typeconverter.CollectionParsers.ArrayParser;
import com.typeconverter.CollectionParsers.CollectionParser;
import com.typeconverter.CollectionParsers.EnumSetParser;
import com.typeconverter.CollectionParsers.MapParser;
import com.typeconverter.SingleParsers.EnumParser;
import com.typeconverter.SingleParsers.PrimitiveParser;
import com.typeconverter.SingleParsers.WrapperParser;

public enum ClassType {
	COLLECTION(new CollectionParser()), ARRAY(new ArrayParser()), MAP(new MapParser()), PRIMITIVE(
			new PrimitiveParser()), WRAPPER(new WrapperParser()), ENUMSET(new EnumSetParser()), ENUM(new EnumParser());

	private Parse parser;

	ClassType(Parse parser) {
		this.parser = parser;
	}

	public Parse getParser() {
		return parser;
	}

}
