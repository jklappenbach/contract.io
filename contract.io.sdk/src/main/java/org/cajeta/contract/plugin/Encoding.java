package org.cajeta.contract.plugin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Encoding {
	public static final Map<String, Encoding> dictionary = new HashMap<String, Encoding>();
	private String name;
	private boolean isBinary;

	static {
		dictionary.put("xml", new Encoding("xml", false));
		dictionary.put("json", new Encoding("json", false));
		dictionary.put("thrift", new Encoding("thrift", true));
		dictionary.put("protobuf", new Encoding("protobuf", true));
		dictionary.put("contract", new Encoding("contract", true));
	}
	
	public Encoding() { }
	
	public Encoding(String name, boolean isBinary) {
		this.name = name;
		this.isBinary = isBinary;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the isBinary
	 */
	public boolean isBinary() {
		return isBinary;
	}

	/**
	 * @param isBinary the isBinary to set
	 */
	public void setBinary(boolean isBinary) {
		this.isBinary = isBinary;
	}
	
	public static List<Encoding> fromString(String str) {
		List<Encoding> encodings = new LinkedList<Encoding>();
		String[] terms = str.split(",");
		for (String term : terms) {
			Encoding encoding = dictionary.get(term.trim());
			if (encoding != null) {
				encodings.add(encoding);
			}
		}
		
		return encodings;
	}
}
