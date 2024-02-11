package com.typeconverter;

import com.typeconverter.CollectionParsers.ArrayParser;
import com.typeconverter.CollectionParsers.CollectionParser;
import com.typeconverter.CollectionParsers.EnumSetParser;
import com.typeconverter.CollectionParsers.GenericArrayParser;
import com.typeconverter.CollectionParsers.MapParser;
import com.typeconverter.PrimitiveParsers.EnumParser;
import com.typeconverter.PrimitiveParsers.PrimitiveParser;

public enum ClassType {
	COLLECTION(new CollectionParser()), ARRAY(new ArrayParser()), GENERICARRAY(new GenericArrayParser()),
	MAP(new MapParser()), PRIMITIVE(new PrimitiveParser()), PTWRAPPER(new ParameterizedWrapperParser()),
	WRAPPER(new WrapperParser()), ENUMSET(new EnumSetParser()), ENUM(new EnumParser());

	private Parse parser;

	ClassType(Parse parser) {
		this.parser = parser;
	}

	public Parse getParser() {
		return parser;
	}

}
