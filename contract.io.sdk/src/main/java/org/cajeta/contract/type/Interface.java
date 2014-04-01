/**
 * 
 */
package org.cajeta.contract.type;

import org.cajeta.contract.idl.ContractModule;
import org.cajeta.contract.idl.TypeUnknownException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @author julian
 *
 */
public class Interface extends Type {
	private static final String METHOD = "method";
	private static final String INTERFACE_URI = "uri";
	private static final String IMPLEMENTABLE = "implementable";
	private String relativeUri = null;
	private boolean implementable = false;
	
	public Interface(XMLReader reader, ContractModule module, String uri, String localName,
			String qName, Attributes attributes) throws Exception {
		super(reader, module, uri, localName, qName, attributes);
		setRelativeUri(attributes.getValue(INTERFACE_URI));
		setImplementable(attributes.getValue(IMPLEMENTABLE));
		this.structureType = "interface";
		
	}

	private void setImplementable(String value) {
		implementable = (value != null && value.equals("true"));
	}
	
	public boolean isImplementable() {
		return implementable;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException, TypeUnknownException {
		if (qName.equals(METHOD)) {
			Method method = new Method(reader, module, uri, localName, qName, attributes);
			methods.add(method);
		}
		super.startElement(uri, localName, qName, attributes);
	}

	/* (non-Javadoc)
	 * @see org.cajeta.contract.type.Type#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, "type");
	}

	/**
	 * @return the relativeUri
	 */
	public String getRelativeUri() {
		return relativeUri;
	}

	/**
	 * @param relativeUri the relativeUri to set
	 */
	public void setRelativeUri(String relativeUri) {
		this.relativeUri = relativeUri;
	}
}
