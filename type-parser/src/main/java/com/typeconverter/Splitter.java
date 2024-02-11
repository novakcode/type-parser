package com.typeconverter;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Splitter {

	private boolean splittingObject;
	private String defaultSeparator;
	private String defaultMapKeyValueSeparator;
	private String[] objectBrackets;
	private String[] arrayBrackets;

	// ,(?=[^\\}|\\]]*(?:\\{|\\[|$))
	private Pattern splitBySeparator;

	private Pattern isMapEntry;

	private Splitter() {
		this.splittingObject = true;
		this.defaultSeparator = ",";
		this.defaultMapKeyValueSeparator = "=";
		this.objectBrackets = new String[] { "[", "]" };
		this.arrayBrackets = new String[] { "{", "}" };

		patternInit();
	}

	private Splitter(String sep, String keyValSep, boolean splittingObject, String[] objectBrackates,
			String[] arrayBrackets) {
		this.defaultSeparator = sep;
		this.defaultMapKeyValueSeparator = keyValSep;
		this.splittingObject = splittingObject;
		this.objectBrackets = objectBrackates;
		this.arrayBrackets = arrayBrackets;

		patternInit();
	}

	private void patternInit() {
		this.splitBySeparator = Pattern.compile(defaultSeparator + "(?=[^\\" + arrayBrackets[1] + "|\\"
				+ objectBrackets[1] + "]*(?:\\" + arrayBrackets[0] + "|\\" + objectBrackets[0] + "|$))");

		this.isMapEntry = Pattern.compile("(.)+" + defaultMapKeyValueSeparator + "(.)+");
	}

	public List<SplitPart> split(String val) {

		return Arrays.asList(splitBySeparator.split(val)).stream().map(e -> SplitPart.capturePart(e, this))
				.collect(Collectors.toList());

	}

	public static Splitter customSplitter(String separator, String keyValSeparator, boolean splitObject,
			String[] objectBrackets, String[] arrayBrackets) {
		return new Splitter();
	}

	public static Splitter defaultSplitter() {
		return new Splitter();
	}

	public boolean isObject(String val) {
		return val.startsWith(objectBrackets[0]) && val.endsWith(objectBrackets[1]);
	}

	public boolean isArray(String val) {
		return val.startsWith(arrayBrackets[0]) && val.endsWith(arrayBrackets[1]);
	}

	public boolean isMapEntry(String val) {
		return isMapEntry.matcher(val).matches();
	}

	public boolean splittingObject() {
		return splittingObject;
	}

	public String getDefaultSeparator() {
		return defaultSeparator;
	}

	public String getDefaultMapKeyValueSeparator() {
		return defaultMapKeyValueSeparator;
	}

	public String[] getObjectBrackets() {
		return objectBrackets;
	}

	public String[] getArrayBrackets() {
		return arrayBrackets;
	}

//	

}
