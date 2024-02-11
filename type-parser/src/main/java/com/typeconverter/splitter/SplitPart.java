package com.typeconverter;

import java.util.List;

public class SplitPart {

	private String stringPart;
	private Splitter splitter;
	private String type;

	private SplitPart(String stringPart, Splitter splitter, String type) {
		this.stringPart = stringPart;
		this.splitter = splitter;
		this.type = type;
	}

	public static SplitPart capturePart(String splitted, Splitter splitter) {
		String type = "element";
		if (splitter.isArray(splitted)) {
			type = "array";
			splitted = splitted.substring(1, splitted.length() - 1);
		} else if (splitter.isObject(splitted)) {
			splitted = splitted.substring(1, splitted.length() - 1);
			type = "object";
		} else if (splitter.isMapEntry(splitted)) {
			type = "mapentry";

		}

		return new SplitPart(splitted, splitter, type);
	}

	public List<SplitPart> split() {
		return splitter.split(stringPart);
	}

	public String get() {
		return stringPart;
	}

	public String getKey() {
		if (type.equals("mapentry")) {
			String val = stringPart.split(splitter.getDefaultMapKeyValueSeparator())[0];
			if (splitter.isArray(val) || splitter.isObject(val))
				val = val.substring(1, val.length() - 1);
			return val;
		}

		return stringPart;
	}

	public String getValue() {
		if (type.equals("mapentry")) {
			String val = stringPart.split(splitter.getDefaultMapKeyValueSeparator())[1];
			if (splitter.isArray(val) || splitter.isObject(val))
				val = val.substring(1, val.length() - 1);
			return val;
		}

		return stringPart;
	}

	public String getElement(int i) {
		if (type.equals("mapentry") || type.equals("element")) {
			return stringPart;
		}

		return stringPart.split(splitter.getDefaultSeparator())[i];
	}

	@Override
	public String toString() {
		return get();
	}
}
