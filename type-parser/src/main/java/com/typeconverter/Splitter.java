package com.typeconverter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Splitter {

	private String collectionSplitter;
	private String mapSplitter;

	private Splitter(String colS, String mapS) {
		this.collectionSplitter = colS;
		this.mapSplitter = mapS;
	}

	public static Splitter CUSTOM(String colSplitter, String mapSplitter) {
		return new Splitter(colSplitter, mapSplitter);
	}

	public static Splitter DEFAULT() {
		return new Splitter(",", "-");
	}

	public List<String> splitCol(String vals) {
		return Arrays.asList(vals.split(collectionSplitter));
	}
	
	public Map<String , String> splitMap(String vals) {
		
	}

	
	
	
}
