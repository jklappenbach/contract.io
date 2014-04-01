/**
 * 
 */
package org.cajeta.contract.idl;

import org.cajeta.contract.ContractParseThread;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @author julian
 *
 */
public class Include extends DelegatingHandler {
	protected static final String NAMESPACE = "namespace";	
	protected String include = "";
	
	public Include(XMLReader reader, ContractModule module, String uri, String localName,
			String qName, Attributes attributes) throws SAXException {
		super(reader, uri, localName, qName, attributes);
		include = attributes.getValue(NAMESPACE);
		ContractParseThread thread = new ContractParseThread(include);
	}

	/**
	 * @return the include
	 */
	public String getInclude() {
		return include;
	}

	/**
	 * @param include the include to set
	 */
	public void setInclude(String include) {
		this.include = include;
	}
}
