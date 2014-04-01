/**
 * 
 */
package org.cajeta.contract.plugin.pojo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.cajeta.contract.Constants;
import org.cajeta.contract.idl.field.Field;
import org.cajeta.contract.plugin.Encoding;
import org.cajeta.contract.plugin.FieldEncodingGenerator;
import org.cajeta.contract.plugin.TypeConverter;
import org.cajeta.contract.type.EnumElement;
import org.cajeta.contract.type.EnumType;
import org.cajeta.contract.type.Interface;
import org.cajeta.contract.type.Method;
import org.cajeta.contract.type.MethodException;
import org.cajeta.contract.type.Type;

/**
 * @author julian
 * 
 */
public class JavaClassGenerator {
	private static final String PACKAGE = "{PACKAGE}";
	private static final String INCLUDES = "{INCLUDES}";
	private static final String FILENAME = "{FILENAME}";
	private static final String DATE = "{DATE}";
	private static final String VERSION = "{VERSION}";
	private static final String STRUCTURE_TYPE = "{STRUCTURE_TYPE}";
	private static final String TYPE_NAME = "{TYPE_NAME}";
	private static final String TYPE_FIELDS = "{TYPE_FIELDS}";
	private static final String TYPE_METHODS = "{TYPE_METHODS}";
	private static final String ENCODING_METHODS = "{ENCODING_METHODS}";
	private static final String DECODING_METHODS = "{DECODING_METHODS}";
	public static final TypeConverter typeConverter = new JavaTypeConverter();
	private static String classTemplate; 
	private static SimpleDateFormat sdf = new SimpleDateFormat();

	protected static Map<String, FieldEncodingGenerator> encodingGenerators = new HashMap<String, FieldEncodingGenerator>();

	static {
		// Populate the encoding code generators
		encodingGenerators.put(Constants.ENCODING_CONTRACT, new ContractEncodingGenerator());
	}

	public static void execute(Type type, File packageDir, Encoding encoding) throws Exception {
		StringBuffer fields = new StringBuffer();
		StringBuffer methods = new StringBuffer();
		StringBuffer imports = new StringBuffer();
		StringBuffer encodingMethods = new StringBuffer();
		StringBuffer decodingMethods = new StringBuffer();
		Set<String> importEntries = new HashSet<String>();

		String typeName = type.getName();
		if (type instanceof EnumType) {
			EnumType enumType = (EnumType) type;
			Iterator<EnumElement> itr = enumType.getElements().iterator();
			fields.append("\t");
			while (itr.hasNext()) {
				EnumElement element = itr.next();
				fields.append(element.getName());
				if (itr.hasNext())
					fields.append(", ");
			}
		} else {
			// Process our dependencies into imports
			for (Type dependency : type.getDependencies()) {
				if (!dependency.isNativeType()) {
					importEntries.addAll(typeConverter.toImport(dependency));
				}
			}
			
			for (String str : importEntries) {
				imports.append("import " + str + ";\r\n");
			}

			// Generate field declarations
			for (Field field : type.getFields().values()) {
				fields.append("\tprotected "
						+ typeConverter.toField(field.getType()) + " "
						+ field.getName() + ";\r\n");
			}
			fields.append("\r\n");

			for (Method method : type.getMethods()) {
				methods.append("\tpublic " + typeConverter.toField(method.getReturns()) + " "
						+ method.getName() + "(");
				Iterator<Field> itrFields = method.getFields().iterator();
				while (itrFields.hasNext()) {
					Field field = itrFields.next();
					methods.append(typeConverter.toField(field.getType()) + " "
							+ field.getName());
					if (itrFields.hasNext())
						methods.append(", ");
				}
				methods.append(")");
				Iterator<MethodException> itrExceptions = method.getExceptions().iterator();
				if (itrExceptions.hasNext())
					methods.append(" throws ");
				while (itrExceptions.hasNext()) {
					MethodException exception = itrExceptions.next();
					methods.append(exception.getName());
					if (itrExceptions.hasNext())
						methods.append(", ");
				}
				if (type instanceof Interface) {
					methods.append(";\r\n");
				} else {
					String fieldName = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
					if (method.getName().contains("get")) {
						methods.append(" {\r\n\t\treturn "
								+ fieldName + ";\r\n\t}\r\n");
					} else if (method.getName().contains("set")) {
						methods.append(" {\r\n\t\tthis."
								+ fieldName + " = "
								+ fieldName + ";\r\n\t}\r\n");
					}
				}
			}
			if (type.getParentType() != null) {
				typeName += " extends " + typeConverter.toField(type.getParentType());
			}

			if (!(type instanceof Interface)) {
				// Generate an externalizable entry per encoding type.		
				int indent = 0;
				FieldEncodingGenerator encodingGenerator = encodingGenerators.get(encoding.getName());
				if (encodingGenerator == null)
					throw new Exception("The encoding " + encoding + " was not associated with a code generator");
				imports.append(encodingGenerator.getDependencies() + "\r\n");
				encodingGenerator.onStartMethod(decodingMethods, encodingMethods, type.getParentType());
				for (Field field : type.getFields().values()) {				
					encodingGenerator.onField(indent, field, encodingMethods, decodingMethods);
				}
				encodingGenerator.onEndMethod(decodingMethods, encodingMethods);
			}
		}

		imports.append("\r\n");

		String strType = classTemplate.replace(PACKAGE, type.getNamespace());
		strType = strType.replace(INCLUDES, imports.toString());
		strType = strType.replace(FILENAME, type.getName()
				+ ".java");
		strType = strType.replace(DATE, sdf.format(new Date()));
		strType = strType.replace(VERSION, type.getModule().getVersion());
		strType = strType.replace(STRUCTURE_TYPE,
				type.getStructureType());
		strType = strType.replace(TYPE_NAME, typeName);
		strType = strType.replace(TYPE_FIELDS, fields.toString());
		strType = strType.replace(TYPE_METHODS, methods.toString());
		strType = strType.replace(ENCODING_METHODS, encodingMethods.toString());
		strType = strType.replace(DECODING_METHODS, decodingMethods.toString());
		
		// First, write out types and interfaces directly to the namespace / package
		// Write out the type to a file
		FileWriter writer = new FileWriter(
				packageDir.getAbsolutePath() + File.separatorChar
						+ type.getName() + ".java");
		writer.write(strType);
		writer.close();
	}
	
	
	
	public static String loadResource(String resourceName) {
		InputStream in = JavaClassGenerator.class.getClassLoader().getResourceAsStream(resourceName);
		InputStreamReader is = new InputStreamReader(in);
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(is);
		try {
			String read = br.readLine();
			while (read != null) {
				sb.append(read + "\r\n");
				read = br.readLine();
			} while (read != null);
		} catch (IOException e) {
			System.out.println("Error: Could not load internal template file '" + resourceName + "'");
			e.printStackTrace();
		}

		return sb.toString();	
	}



	public static void execute(String packageName, StringBuffer includes2,
			String structureType, String className, StringBuffer typeFields,
			StringBuffer typeMethods, StringBuffer encodingMethods, StringBuffer decodingMethods) {
		// TODO Auto-generated method stub
		
	}
}
