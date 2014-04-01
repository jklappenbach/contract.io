/**
 * 
 */
package org.cajeta.contract.idl.field;

import java.util.HashSet;
import java.util.Set;
import org.cajeta.contract.idl.TypeUnknownException;
import org.cajeta.contract.idl.ContractElementHandler;
import org.cajeta.contract.idl.ContractModule;
import org.cajeta.contract.type.IndexType;
import org.cajeta.contract.type.MapCollection;
import org.cajeta.contract.type.Type;
import org.cajeta.contract.type.TypeCollection;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

/**
 * @author julian
 *
 */
public class Field extends ContractElementHandler {
	protected static final String FIELD_NAME = "name";
	protected static final String FIELD_TYPE = "type";
	protected static final String FIELD_INDEX = "index";
	protected static final String FIELD_OPTIONAL = "opt";
	protected static final String FIELD_ORDINAL = "ord";
	protected static final String FIELD_MIN_LENGTH = "minLength";
	protected static final String FIELD_MAX_LENGTH = "maxLength";
	protected static final String FIELD_REGEX = "regex";
	protected static final String FIELD_DEFAULT_VALUE = "defValue";
	protected static final String FIELD_MAX_VALUE = "maxValue";
	protected static final String FIELD_MIN_VALUE = "minValue";
	
	protected int ordinal;
	protected boolean optional;
	protected Type valueType;
	protected String name;
	protected IndexType indexType = IndexType.none;
	protected long size = 0;
	protected Set<Type> dependencies = new HashSet<Type>();
	
	public Field(String name, Type type, int ordinal) {
		this.name = name;
		this.valueType = type;
		this.size = type.getSize();
		this.ordinal = ordinal;
	}
	
	public Field(XMLReader reader, ContractModule module, String uri, String localName, 
			String qName, Attributes attributes) throws Exception {
		super(reader, module, uri, localName, qName, attributes);
		name = attributes.getValue(FIELD_NAME);
		String typeName = attributes.getValue(FIELD_TYPE);
		String opt = attributes.getValue(FIELD_OPTIONAL); 
		optional = opt != null && opt.equals("true");
		valueType = Type.resolve(typeName);
		if (valueType == null) {
			int i = 0;
		}
		
		if (attributes.getValue(FIELD_INDEX) != null) {
			indexType = IndexType.valueOf(attributes.getValue(FIELD_INDEX));
		}
		
		if (!valueType.isNativeType()) {
			dependencies.add(valueType);
			if (valueType instanceof MapCollection) {
				MapCollection mapCollection = (MapCollection) valueType;
				if (!mapCollection.getKeyType().isNativeType()) {
					dependencies.add(mapCollection.getKeyType());
				}
			}
			if (valueType instanceof TypeCollection) {
				TypeCollection typeCollection = (TypeCollection) valueType;
				if (!typeCollection.getValueType().isNativeType()) {
					dependencies.add(typeCollection.getValueType());
				}
			}
		}
	}
		
	/**
	 * @return the ordinal
	 */
	public int getOrdinal() {
		return ordinal;
	}
	
	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return The field type
	 */
	public Type getType() { 
		return valueType;
	}

	/**
	 * @return the optional
	 */
	public boolean isOptional() {
		return optional;
	}

	public Set<Type> getDependencies() {
		return dependencies;
	}
	
	public long getSize() {
		return valueType.getSize();
	}
	
	public void setSize(long size) {
		this.size = size;
	}

	public IndexType getIndexType() {
		return indexType;
	}
	
	public void setIndexType(IndexType indexType) {
		this.indexType = indexType;
	}
	
}
