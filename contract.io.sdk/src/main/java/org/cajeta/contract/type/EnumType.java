package org.cajeta.contract.type;

import java.util.LinkedList;
import java.util.List;

import org.cajeta.contract.idl.ContractModule;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


public class EnumType extends Type {
	private static final String ELEMENT = "element";
	private static final String ENUM = "enum";
	private static final String ELEMENT_ORDINAL = "ordinal";
	private static final String ELEMENT_NAME = "name";
	private List<EnumElement> elements = new LinkedList<EnumElement>();
	private int lastOrdinal = -1;
	
	public EnumType(XMLReader reader, ContractModule module, String uri,
			String localName, String qName, Attributes attributes)
			throws Exception {
		super(reader, module, uri, localName, qName, attributes);
		structureType = "enum";
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (qName.equals(ELEMENT)) {
			String strOrdinal = attributes.getValue(ELEMENT_ORDINAL);
			lastOrdinal =  (strOrdinal != null) ? Integer.parseInt(strOrdinal) : lastOrdinal + 1; 
			elements.add(new EnumElement(attributes.getValue(ELEMENT_NAME), lastOrdinal));
		}
	}
	
	

	/* (non-Javadoc)
	 * @see org.cajeta.contract.type.Type#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equals(ENUM))
			super.endElement(uri, localName, qName);
	}

	/**
	 * @return the elements
	 */
	public List<EnumElement> getElements() {
		return elements;
	}
	/**
	 * @param elements the elements to set
	 */
	public void setElements(List<EnumElement> elements) {
		this.elements = elements;
	}

}
