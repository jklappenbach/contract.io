/**
 * 
 */
package org.cajeta.contract.type;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.cajeta.contract.Contract;
import org.cajeta.contract.ParseException;
import org.cajeta.contract.idl.ContractElementHandler;
import org.cajeta.contract.idl.ContractModule;
import org.cajeta.contract.idl.TypeUnknownException;
import org.cajeta.contract.idl.field.Field;
import org.cajeta.contract.idl.field.FieldFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @author julian
 *
 */
public class Type extends ContractElementHandler {
	public enum TypeId { VoidType, BooleanType, FloatType, DoubleType, i8Type, 
		i16Type, i32Type, i64Type, StringType, ListType, SetType, MapType, ArrayType, EnumType,
		CustomType, EntityType 
	}
	protected static final String TYPE_NAME = "name";
	protected static final String TYPE_EXTENDS = "extends";
	protected static final String ORDINAL = "ord";
	protected static final String TYPE = "type";
	protected static final String FIELD = "field";
	public static final String VOID_TYPE = "void";
	public static final String BOOL_TYPE = "bool";
	public static final String FLOAT_TYPE = "float";
	public static final String DOUBLE_TYPE = "double";
	public static final String INT8_TYPE = "i8";
	public static final String INT16_TYPE = "i16";
	public static final String INT32_TYPE = "i32";
	public static final String INT64_TYPE = "i64";
	public static final String STRING_TYPE = "string";
	public static final String LIST_TYPE = "list";
	public static final String SET_TYPE = "set";
	public static final String MAP_TYPE = "map";
	public static final String ARRAY_TYPE = "array";
	public static final Map<String, Type> types = new ConcurrentHashMap<String, Type>();
	private int currentFieldOrdinal = 0;
	private long size = 0;
	protected String name = null;
	protected int ordinal = -1;
	protected String namespace = "";
	protected Type parentType = null;
	protected String structureType = "class";
	protected TypeId typeId = TypeId.CustomType;
	protected List<Method> methods = new LinkedList<Method>();
	protected Set<Type> dependencies = new HashSet<Type>();  
	protected LinkedHashMap<String, Field> fields = new LinkedHashMap<String, Field>();

	static {
		// Add native types
		try {
			types.put(VOID_TYPE,  new Type(VOID_TYPE, TypeId.VoidType, 0, -1, null));
			types.put(BOOL_TYPE, new Type(BOOL_TYPE, TypeId.BooleanType, 8, -2, null));
			types.put(FLOAT_TYPE, new Type(FLOAT_TYPE, TypeId.FloatType, 32, -3, null));
			types.put(DOUBLE_TYPE, new Type(DOUBLE_TYPE, TypeId.DoubleType, 64, -4, null));
			types.put(INT8_TYPE, new Type(INT8_TYPE, TypeId.i8Type, 8, -5, null));
			types.put(INT16_TYPE, new Type(INT16_TYPE, TypeId.i16Type, 16, -6, null));
			types.put(INT32_TYPE, new Type(INT32_TYPE, TypeId.i32Type, 32, -7, null));
			types.put(INT64_TYPE, new Type(INT64_TYPE, TypeId.i64Type, 64, -8, null));
			types.put(STRING_TYPE, new Type(STRING_TYPE, TypeId.StringType, 16, -9, null));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	public Type(String name, ContractModule module) {
		this.name = name;
		this.module = module;
		if (module != null)
			ordinal = module.getNextTypeOrdinal();
	}
		
	public Type(String name, TypeId typeId, int ordinal, int size, ContractModule module) throws Exception {
		this.name = name;
		this.typeId = typeId; 
		this.module = module;
		if (module != null)
			this.ordinal = module.setAndAllocateTypeOrdinal(ordinal);
	}

	public Type(XMLReader reader, ContractModule module, String uri, String localName,
			String qName, Attributes attributes) throws Exception {
		super(reader, module, uri, localName, qName, attributes);
		name = attributes.getValue(TYPE_NAME);
		namespace = module.getNamespace();
		Type.resolve(getCannonicalName());
		
		if (types.containsKey(getCannonicalName()))
			throw new ParseException("Duplicate type definition for type: " + namespace + "." + name);
		else {
			types.put(getCannonicalName(), this);
		}
		
		String parentTypeName = attributes.getValue(TYPE_EXTENDS);
		if (parentTypeName != null) {
			parentType = resolve(parentTypeName);
			dependencies.add(parentType);
		}

		String strOrdinal = attributes.getValue(ORDINAL);
		if (strOrdinal != null) {
			ordinal = Integer.parseInt(strOrdinal);
		}
		if (ordinal >= 0)
			ordinal = module.setAndAllocateTypeOrdinal(ordinal);

		Type.types.put(name, this);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		
		try {
			if (qName.equals(FIELD)) {
				Field field = FieldFactory.newField(reader, module, uri, localName, qName, attributes);
				dependencies.addAll(field.getDependencies());
				String strOrdinal = attributes.getValue(ORDINAL);
				if (strOrdinal != null && !strOrdinal.isEmpty()) {
					int fieldOrdinal = Integer.parseInt(strOrdinal);
					if (fieldOrdinal < currentFieldOrdinal) {
						throw new SAXException("Field ordinal already assigned (" + fieldOrdinal + ")") ;
					} else {
						currentFieldOrdinal = fieldOrdinal;
					}
				}
					
				field.setOrdinal(currentFieldOrdinal++);
				fields.put(field.getName(), field);
								
				// Add getters and setters for the field
				Method method = new Method();
				method.setName("set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
				method.getFields().add(field);
				methods.add(method);
				method = new Method();
				method.setName("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
				methods.add(method);
				method.setReturns(field.getType());
			}
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
			throw new SAXException(e);
		}
	}
	
	private long getSize() {
		
		return 0;
	}

	/**
	 * Overridden to provide dependency population
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		
		if (qName.equals(TYPE)) {
			for (Field field : fields.values()) {
				if (!field.getType().isNativeType()) {
					dependencies.add(field.getType());
				}
			}
			for (Method method : methods) {
				for (Field field : method.getFields()) {
					if (!field.getType().isNativeType()) {
						dependencies.add(field.getType());
					}
				}
				dependencies.addAll(method.getExceptions());
				if (!method.getReturns().isNativeType())
					dependencies.add(method.getReturns());
			}			
		}
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * @return the cannonicalName
	 */
	public String getCannonicalName() {
		return namespace + "." + name;
	}

	/**
	 * @return the parentType
	 */
	public Type getParentType() {
		return parentType;
	}
	
	public TypeId getTypeId() {
		return typeId;
	}
	
	public boolean isNativeType() {
		return typeId != TypeId.CustomType && typeId != TypeId.EnumType && typeId != TypeId.ListType &&
				typeId != TypeId.SetType && typeId != TypeId.MapType;
	}

	/**
	 * @return the fields
	 */
	public LinkedHashMap<String, Field> getFields() {
		return fields;
	}

	/**
	 * @return the structureType
	 */
	public String getStructureType() {
		return structureType;
	}

	/**
	 * @return the methods
	 */
	public List<Method> getMethods() {
		return methods;
	}	
	
	public Set<Type> getDependencies() {
		return dependencies;
	}
	
	public static Type resolve(String typeName) throws TypeUnknownException {
		Type type = types.get(typeName.toLowerCase());
		
		if (type == null) {
			if (!typeName.contains(".")) {
				for (String ns : Contract.getInstance().getNamespaces()) {
					type = types.get(ns + "." + typeName);
					if (type != null)
						break;
				}
			}
		}

		return type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return hashCode() == obj.hashCode();
	}
	
}
