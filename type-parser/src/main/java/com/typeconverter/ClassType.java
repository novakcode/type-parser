package com.typeconverter;

import com.typeconverter.Parsers.Parser.ArrayParser;
import com.typeconverter.Parsers.Parser.CollectionParser;
import com.typeconverter.Parsers.Parser.EnumSetParser;
import com.typeconverter.Parsers.Parser.MapParser;
import com.typeconverter.Parsers.SingleParser.EnumParser;
import com.typeconverter.Parsers.SingleParser.PrimitiveParser;
import com.typeconverter.Parsers.SingleParser.WrapperParser;

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
