/**
 * 
 */
package org.cajeta.contract.idl.field;

import org.cajeta.contract.idl.ContractModule;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

/**
 * @author julian
 *
 */
public class FieldFactory {
	private static final String TYPE = "type";
	private static final String DATE_FIELD = "date";
	private static final String FLOAT_FIELD = "float";
	private static final String DOUBLE_FIELD = "double";
	private static final String INT8_FIELD = "i8";
	private static final String INT16_FIELD = "i16";
	private static final String INT32_FIELD = "i32";
	private static final String INT64_FIELD = "i64";
	private static final String STRING_FIELD = "string";
	private static final String LIST_FIELD = "list";
	private static final String SET_FIELD = "set";
	private static final String MAP_FIELD = "map";
	private static final String ARRAY_FIELD = "array";
	
	public static Field newField(XMLReader reader, ContractModule module, String uri, String localName, String qName,
			Attributes attributes) throws Exception {
		Field field = null;
		String type = attributes.getValue(TYPE);
		if (type.equals(DATE_FIELD)) {
			field = new DateField(reader, module, uri, localName, qName, attributes);
		} else if (type.equals(FLOAT_FIELD)) {
			field = new FloatField(reader, module, uri, localName, qName, attributes);
		} else if (type.equals(DOUBLE_FIELD)) {
			field = new FloatField(reader, module, uri, localName, qName, attributes);
		} else if (type.equals(INT8_FIELD)) {
			field = new IntField(reader, module, uri, localName, qName, attributes);
		} else if (type.equals(INT16_FIELD)) {
			field = new IntField(reader, module, uri, localName, qName, attributes);
		} else if (type.equals(INT32_FIELD)) {
			field = new IntField(reader, module, uri, localName, qName, attributes);
		} else if (type.equals(INT64_FIELD)) {
			field = new IntField(reader, module, uri, localName, qName, attributes);
		} else if (type.equals(STRING_FIELD)) {
			field = new StringField(reader, module, uri, localName, qName, attributes);
		} else if (type.startsWith(LIST_FIELD)) {
			field = new CollectionField(reader, module, uri, localName, qName, attributes);
		} else if (type.startsWith(SET_FIELD)) {
			field = new CollectionField(reader, module, uri, localName, qName, attributes);
		} else if (type.startsWith(MAP_FIELD)) {
			field = new CollectionField(reader, module, uri, localName, qName, attributes);
		} else if (type.startsWith(ARRAY_FIELD)) {
			field = new CollectionField(reader, module, uri, localName, qName, attributes);
		} else {
			field = new Field(reader, module, uri, localName, qName, attributes);
		}
		
		return field;
	}
}
