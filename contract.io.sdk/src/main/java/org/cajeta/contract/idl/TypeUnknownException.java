/**
 * 
 */
package org.cajeta.contract.idl;

import org.xml.sax.SAXException;

/**
 * @author julian
 *
 */
public class TypeUnknownException extends SAXException {
	private static final long serialVersionUID = 1L;
	String typeName = "";
	
	public TypeUnknownException(String typeName) {
		super();
		this.typeName = typeName;
		// TODO Auto-generated constructor stub
	}
}
