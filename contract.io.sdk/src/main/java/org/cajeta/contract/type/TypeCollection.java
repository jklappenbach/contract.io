/**
 * 
 */
package org.cajeta.contract.type;

import java.sql.Types;

import org.cajeta.contract.idl.ContractModule;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

/**
 * @author julian
 *
 */
public class TypeCollection extends Type {
	private Type valueType;
	
	public TypeCollection(String name, ContractModule module) {
		super(name, module);
	}

	public TypeCollection(XMLReader reader, ContractModule module, String uri,
			String localName, String qName, Attributes attributes)
			throws Exception {
		super(reader, module, uri, localName, qName, attributes);
		valueType = Type.resolve(attributes.getValue("valueType"));
	}

	public Type getValueType() {
		return valueType;
	}
}
