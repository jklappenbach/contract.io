/**
 * 
 */
package org.cajeta.contract.plugin.pojo;

import org.cajeta.contract.idl.field.Field;
import org.cajeta.contract.plugin.FieldEncodingGenerator;
import org.cajeta.contract.type.MapCollection;
import org.cajeta.contract.type.Type;
import org.cajeta.contract.type.TypeCollection;

/**
 * @author julian
 *
 */
public class ContractEncodingGenerator extends FieldEncodingGenerator {

	public ContractEncodingGenerator() {
		encoding = "contract";
	}

	/* (non-Javadoc)
	 * @see org.cajeta.contract.plugin.EncodingTypeAdaptor#onStartMethod(java.lang.StringBuffer, java.lang.StringBuffer)
	 */
	public void onStartMethod(StringBuffer decode, StringBuffer encode, Type superType) {
		decode.append("\tpublic void " + getDecodeMethodName() + "(ContractBuf in) throws Exception, " +
				"ClassNotFoundException {\r\n");
		if (superType != null && !superType.isNativeType())
			decode.append("\t\tsuper." + getDecodeMethodName() + "(in);\r\n");
		encode.append("\tpublic void " + getEncodeMethodName() + "(ContractBuf out) throws Exception " +
				"{\r\n");
		if (superType != null && !superType.isNativeType())
			encode.append("\t\tsuper." + getEncodeMethodName() + "(out);\r\n");
	}

	/* (non-Javadoc)
	 * @see org.cajeta.contract.plugin.EncodingTypeAdaptor#onType(int, java.lang.StringBuffer, java.lang.StringBuffer)
	 */
	public void onField(int indent, Field field, StringBuffer encode, StringBuffer decode) {
		String strIndent = "\t\t";
		for (int i = 0; i < indent; i++) {
			strIndent += "\t";
		}
			
		switch (field.getType().getTypeId()) {
		case BooleanType:
			encode.append(strIndent + "out.writeBoolean(" + field.getName() + ");\r\n");
			decode.append(strIndent + field.getName() + " = in.readBoolean();\r\n");
			break;
		case EnumType:
			encode.append(strIndent + "out.writeInt(" + field.getName() + ");\r\n");
			decode.append(strIndent + field.getName() + " = in.readInt();\r\n");
			break;
		case FloatType:
			encode.append(strIndent + "out.writeFloat(" + field.getName() + ");\r\n");
			decode.append(strIndent + field.getName() + " = in.readFloat();\r\n");
			break;
		case DoubleType:
			encode.append(strIndent + "out.writeDouble(" + field.getName() + ");\r\n");
			decode.append(strIndent + field.getName() + " = in.readDouble();\r\n");
			break;
		case i8Type:
			encode.append(strIndent + "out.writeByte(" + field.getName() + ");\r\n");
			decode.append(strIndent + field.getName() + " = in.readByte();\r\n");
			break;
		case i16Type:
			encode.append(strIndent + "out.writeShort(" + field.getName() + ");\r\n");
			decode.append(strIndent + field.getName() + " = in.readShort();\r\n");
			break;
		case i32Type:
			encode.append(strIndent + "out.writeInt(" + field.getName() + ");\r\n");
			decode.append(strIndent + field.getName() + " = in.readInt();\r\n");
			break;
		case i64Type:
			encode.append(strIndent + "out.writeLong(" + field.getName() + ");\r\n");
			decode.append(strIndent + field.getName() + " = in.readLong();\r\n");
			break;
		case StringType:
			encode.append(strIndent + "out.writeString(" + field.getName() + ");\r\n");
			decode.append(strIndent + field.getName() + " = in.readString();\r\n");
			break;
		case ArrayType:
			// TODO Create a use case test for this.
			TypeCollection typeCollection = (TypeCollection) field.getType();
			encode.append(strIndent + "out.writeInt(" + field.getName() +  ".length);\r\n");
			encode.append(strIndent + "for (" + JavaGenerator.typeConverter.toField(typeCollection.getValueType()) + 
					" element : " + field.getName() + ") {\r\n");
			decode.append(strIndent + "int count = in.readInt();\r\n");
			decode.append(strIndent + field.getName() + " = new " + 
					JavaGenerator.typeConverter.toField(typeCollection.getValueType()) + "[count];\r\n");
			decode.append(strIndent + "for (int i = 0; i < count; i++) {\r\n");
			if (!typeCollection.getValueType().isNativeType()) {
				decode.append(strIndent + "\t" + field.getName() + "[i] = new " + JavaGenerator.typeConverter.toField(typeCollection.getValueType()) + ";\r\n");
				decode.append(strIndent + "\t" + field.getName() + "[i]." + this.getDecodeMethodName() + "(in);\r\n" + strIndent + "}\r\n");
				encode.append(strIndent + "\t" + field.getName() + "[i]." + this.getEncodeMethodName() + "(out);\r\n");
			} else {
				decode.append(strIndent + "\t" + JavaGenerator.typeConverter.toField(typeCollection.getValueType()) + " element;\r\n");
				onField(indent + 1, new Field(field.getName() + "[i]", typeCollection.getValueType(), -1), encode, decode); 
			}
			encode.append(strIndent + "}\r\n");
			decode.append(strIndent + "}\r\n");			
			break;
		case ListType:
			typeCollection = (TypeCollection) field.getType();
			encode.append(strIndent + "out.writeInt(" + field.getName() +  ".size());\r\n");
			encode.append(strIndent + "for (" + JavaGenerator.typeConverter.toField(typeCollection.getValueType()) + 
					" element : " + field.getName() + ") {\r\n");
			decode.append(strIndent + field.getName() + " = new LinkedList<" + 
					JavaGenerator.typeConverter.toField(typeCollection.getValueType()) + ">();\r\n");
			decode.append(strIndent + "int count = in.readInt();\r\n");
			decode.append(strIndent + "for (int i = 0; i < count; i++) {\r\n");
			if (!typeCollection.getValueType().isNativeType()) {
				decode.append(strIndent + "\t" + JavaGenerator.typeConverter.toField(typeCollection.getValueType()) + 
						" element = new " + JavaGenerator.typeConverter.toField(typeCollection.getValueType()) + "();\r\n");
				decode.append(strIndent + "\telement." + this.getDecodeMethodName() + "(in);\r\n");
				encode.append(strIndent + "\telement." + this.getEncodeMethodName() + "(out);\r\n");
			} else {
				decode.append(strIndent + "\t" + JavaGenerator.typeConverter.toField(typeCollection.getValueType()) + " element;\r\n");
				onField(indent + 1, new Field("element", typeCollection.getValueType(), -1), encode, decode); 
			}
			decode.append(strIndent + "\t" + field.getName() + ".add(element);\r\n");
			encode.append(strIndent + "}\r\n");
			decode.append(strIndent + "}\r\n");			
			break;
		case SetType:
			typeCollection = (TypeCollection) field.getType();
			encode.append(strIndent + "out.writeInt(" + field.getName() +  ".size());\r\n");
			encode.append(strIndent + "for (" + JavaGenerator.typeConverter.toField(typeCollection.getValueType()) + 
					" element : " + field.getName() + ") {\r\n");
			decode.append(strIndent + field.getName() + " = new HashSet<" + 
					JavaGenerator.typeConverter.toField(typeCollection.getValueType()) + ">();\r\n");
			decode.append(strIndent + "int count = in.readInt();\r\n");
			decode.append(strIndent + "for (int i = 0; i < count; i++) {\r\n");
			if (!typeCollection.getValueType().isNativeType()) {
				decode.append(strIndent + "\t" + JavaGenerator.typeConverter.toField(typeCollection.getValueType()) + 
						" element = new " + JavaGenerator.typeConverter.toField(typeCollection.getValueType()) + ";\r\n");
				decode.append(strIndent + "\telement.decode(in);\r\n" + strIndent + "}\r\n");
				encode.append(strIndent + "\telement." + this.getEncodeMethodName() + "(out);\r\n");
			} else {
				decode.append(strIndent + "\t" + JavaGenerator.typeConverter.toField(typeCollection.getValueType()) + " element;\r\n");
				onField(indent + 1, new Field("element", typeCollection.getValueType(), -1), encode, decode); 
			}
			decode.append(strIndent + "\t" + field.getName() + ".add(element);\r\n");
			encode.append(strIndent + "}\r\n");
			decode.append(strIndent + "}\r\n");			
			break;
		case MapType:
			// Handle encoding (out)
			MapCollection mapCollection = (MapCollection) field.getType();
			encode.append(strIndent + "out.writeInt(" + field.getName() +  ".size());\r\n");
			encode.append(strIndent + "for (Map.Entry<" + JavaGenerator.typeConverter.toField(mapCollection.getKeyType()) + ", " + 
					JavaGenerator.typeConverter.toField(mapCollection.getValueType()) + "> entry : " + 
					field.getName() + ".entrySet()) {\r\n");
			
			decode.append(strIndent + field.getName() + " = new HashMap<" + JavaGenerator.typeConverter.toField(mapCollection.getKeyType())
					+ ", " + JavaGenerator.typeConverter.toField(mapCollection.getValueType()) + ">();\r\n");
			decode.append(strIndent + "int count = in.readInt();\r\n");
			decode.append(strIndent + "for (int i = 0; i < count; i++) {\r\n");
			if (!mapCollection.getKeyType().isNativeType()) {
				decode.append(strIndent + "\t" + JavaGenerator.typeConverter.toField(mapCollection.getKeyType())
						+ " key = new " + JavaGenerator.typeConverter.toField(mapCollection.getKeyType()) + ";\r\n");
				decode.append(strIndent + "\t" + JavaGenerator.typeConverter.toField(mapCollection.getValueType()) + 
						" value = new " + JavaGenerator.typeConverter.toField(mapCollection.getValueType()) + ";\r\n");
				decode.append(strIndent + "\tkey.decode(in);\r\n");
				decode.append(strIndent + "\tvalue.decode(in);\r\n");
			} else {
				decode.append(strIndent + "\t" + JavaGenerator.typeConverter.toField(mapCollection.getKeyType()) + " key;\r\n");
				decode.append(strIndent + "\t" + JavaGenerator.typeConverter.toField(mapCollection.getValueType()) + " value;\r\n");
				onField(indent + 1, new Field("key", mapCollection.getKeyType(),  -1), encode, decode);
				onField(indent + 1, new Field("value", mapCollection.getValueType(), -1), encode, decode);
			}
			
			encode.append(strIndent + "}\r\n");			
			decode.append(strIndent + "}\r\n");
			break;
		case UnionType:
			break;
		default:
			break;
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.cajeta.contract.plugin.EncodingTypeAdaptor#onEndMethod(java.lang.StringBuffer, java.lang.StringBuffer)
	 */
	public void onEndMethod(StringBuffer decode, StringBuffer encode) {
		decode.append("\t}\r\n\r\n");
		encode.append("\t}\r\n\r\n");

	}

	@Override
	public String getDependencies() {
		return "import java.io.IOException;\r\nimport org.cajeta.contract.sdk.buffer.ContractBuf;\r\njava.io.UnsupportedEncodingException;\r\n";
	}
}
