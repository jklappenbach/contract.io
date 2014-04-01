package org.cajeta.contract.idl.field;

import org.cajeta.contract.idl.ContractModule;
import org.cajeta.contract.type.Type;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

public class CollectionField extends Field {	
	private int maxElements;
	private int minElements;
	private Type keyType;
	
	// TODO: Make the field store the type information for collections, preventing the need for
	// specific types
	public CollectionField(XMLReader reader, ContractModule module, String uri,
			String localName, String qName, Attributes attributes)
			throws Exception {
		super(reader, module, uri, localName, qName, attributes);
	}

	/**
	 * @return the maxElements
	 */
	public int getMaxElements() {
		return maxElements;
	}

	/**
	 * @param maxElements the maxElements to set
	 */
	public void setMaxElements(int maxElements) {
		this.maxElements = maxElements;
	}

	/**
	 * @return the minElements
	 */
	public int getMinElements() {
		return minElements;
	}

	/**
	 * @param minElements the minElements to set
	 */
	public void setMinElements(int minElements) {
		this.minElements = minElements;
	}
}
