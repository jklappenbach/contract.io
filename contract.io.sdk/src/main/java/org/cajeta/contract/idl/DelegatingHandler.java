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
public class DelegatingHandler extends DefaultHandler {
	protected XMLReader reader = null;
	protected ContentHandler oldHandler = null;
	
	public DelegatingHandler() { }	
	
	public DelegatingHandler(XMLReader reader, String uri, String localName,
			String qName, Attributes attributes) throws SAXException {
		this.reader = reader;
		if (reader != null)
			oldHandler = reader.getContentHandler();
		reader.setContentHandler(this);
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

}
