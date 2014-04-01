/**
 * 
 */
package org.cajeta.contract.idl;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author julian
 *
 */
public class ContractElementHandler extends DelegatingHandler {
	protected ContractModule module = null;
	
	public ContractElementHandler() { }	
	
	public ContractElementHandler(XMLReader reader, ContractModule module, String uri, String localName,
			String qName, Attributes attributes) throws SAXException {
		super(reader, uri, localName, qName, attributes);
		this.module = module;
	}

	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		if (oldHandler != null)
			reader.setContentHandler(oldHandler);
	}
	
	/**
	 * @return the reader
	 */
	public XMLReader getReader() {
		return reader;
	}

	/**
	 * @param reader the reader to set
	 */
	public void setReader(XMLReader reader) {
		this.reader = reader;
	}

	/**
	 * @return the oldHandler
	 */
	public ContentHandler getOldHandler() {
		return oldHandler;
	}

	/**
	 * @param oldHandler the oldHandler to set
	 */
	public void setOldHandler(DefaultHandler oldHandler) {
		this.oldHandler = oldHandler;
	}

	/**
	 * @return the module
	 */
	public ContractModule getModule() {
		return module;
	}

	/**
	 * @param module the module to set
	 */
	public void setModule(ContractModule module) {
		this.module = module;
	}
}
