/**
 * 
 */
package org.cajeta.contract.plugin.pojo;

import org.cajeta.contract.plugin.TypeConverter;
import org.cajeta.contract.type.MapCollection;
import org.cajeta.contract.type.Type;
import org.cajeta.contract.type.TypeCollection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author julian
 *
 */
public class JavaTypeConverter implements TypeConverter {

	/**
	 * 
	 */
	public JavaTypeConverter() {
		// TODO Auto-generated constructor stub
	}

	public Set<String> toImport(Type type) {
		TypeCollection typeCollection;
		MapCollection mapCollection;
		Set<String> result = new HashSet<String>();
		if (!type.isNativeType()) {
			switch (type.getTypeId()) {
			case ListType:
				result.add("java.util.List");
				result.add("java.util.LinkedList");
				typeCollection = (TypeCollection) type;
				if (!typeCollection.getValueType().isNativeType())
					result.addAll(toImport(typeCollection.getValueType()));
				break;
			case SetType:
				result.add("java.util.Set");
				result.add("java.util.HashSet");
				typeCollection = (TypeCollection) type;
				if (!typeCollection.getValueType().isNativeType())
					result.addAll(toImport(typeCollection.getValueType()));
				break;
			case MapType:
				result.add("java.util.Map");
				result.add("java.util.HashMap");
				mapCollection = (MapCollection) type;
				if (!mapCollection.getValueType().isNativeType()) 
					result.addAll(toImport(mapCollection.getValueType()));
				if (!mapCollection.getKeyType().isNativeType())
					result.addAll(toImport(mapCollection.getKeyType()));
				break;
			case ArrayType:
				result.add(((TypeCollection) type).getValueType().getCannonicalName());
				typeCollection = (TypeCollection) type;
				if (!typeCollection.getValueType().isNativeType())
					result.addAll(toImport(typeCollection.getValueType()));
				break;
			default:
				result.add(type.getCannonicalName());
				break;
			}
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.cajeta.contract.plugin.FieldWriter#write(org.cajeta.contract.idl.field.Field)
	 */
	public String toField(Type type) {
		String result = null;
		switch (type.getTypeId()) {
		case BooleanType:
			result = "Boolean";
			break;
		case EnumType:
			result = type.getName();
			break;
		case FloatType:
			result = "Float";
			break;
		case DoubleType:
			result = "Double";
			break;
		case i8Type:
			result = "Byte";
			break;
		case i16Type:
			result = "Short";
			break;
		case i32Type:
			result = "Integer";
			break;
		case i64Type:
			result = "Long";
			break;
		case StringType:
			result = "String";
			break;
		case ListType:
			TypeCollection typeCollection = (TypeCollection) type;
			result = "List<" + toField(typeCollection.getValueType()) + ">"; 
			break;
		case SetType:
			typeCollection = (TypeCollection) type;
			result = "Set<" + toField(typeCollection.getValueType()) + ">";
			break;
		case MapType:
			// Handle encoding (out)
			MapCollection mapCollection = (MapCollection) type;
			result = "Map<" + toField(mapCollection.getKeyType()) + ", " + toField(mapCollection.getValueType()) + ">";
			break;
		case UnionType:
			
			break;
		case ArrayType:
			typeCollection = (TypeCollection) type;
			result = toField(typeCollection.getValueType()) + "[]";
			break;
		case ExceptionType:
			result = "Exception";
			break;
		default:
			result = type.getName();
			break;
		}
		return result;
	}

}
