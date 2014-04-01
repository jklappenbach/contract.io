/**
 * 
 */
package org.cajeta.contract.type;

import org.cajeta.contract.idl.ContractModule;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

/**
 * @author julian
 *
 */
public class MethodException extends Type {
	private static final String NAME = "name";
	private static final String WHY = "why";
	
	private String why = null;
	private String name = null;

	public MethodException(XMLReader reader, ContractModule module, String uri,
			String localName, String qName, Attributes attributes)
			throws Exception {
		super(reader, module, uri, localName, qName, attributes);
		why = attributes.getValue(WHY);
		name = attributes.getValue(NAME);
		this.parentType = Type.resolve("exception");
	}

	/**
	 * @return the why
	 */
	public String getWhy() {
		return why;
	}

	/**
	 * @param why the why to set
	 */
	public void setWhy(String why) {
		this.why = why;
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
}
