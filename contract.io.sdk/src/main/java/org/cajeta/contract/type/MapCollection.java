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
public class MapCollection extends TypeCollection {
	private Type keyType;

	
	public MapCollection(String name, Type keyType, ContractModule module) {
		super(name, module);
		this.keyType = keyType;
	}


	public MapCollection(XMLReader reader, ContractModule module, String uri,
			String localName, String qName, Attributes attributes)
			throws Exception {
		super(reader, module, uri, localName, qName, attributes);
		keyType = Type.resolve(attributes.getValue("keyValue"));
	}

	/**
	 * @return the keyType
	 */
	public Type getKeyType() {
		return keyType;
	}
}
