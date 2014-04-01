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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.cajeta.contract.Constants;
import org.cajeta.contract.idl.ContractModule;
import org.cajeta.contract.idl.field.Field;
import org.cajeta.contract.plugin.CodeGenerator;
import org.cajeta.contract.plugin.Encoding;
import org.cajeta.contract.plugin.FieldEncodingGenerator;
import org.cajeta.contract.plugin.Protocol;
import org.cajeta.contract.plugin.TypeConverter;
import org.cajeta.contract.type.Interface;
import org.cajeta.contract.type.Method;
import org.cajeta.contract.type.MethodException;
import org.cajeta.contract.type.Type;

/**
 * @author julian
 * 
 */
public class JavaGenerator implements CodeGenerator {
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
	private static final String TCP_PROTOCOL = "tcp";
	private static final String HTTP_PROTOCOL = "http";
	public static final TypeConverter typeConverter = new JavaTypeConverter();
	private static String classTemplate; 
	private static SimpleDateFormat sdf = new SimpleDateFormat();
	protected File sourceRoot = null;

	protected static Map<String, FieldEncodingGenerator> encodingGenerators = new HashMap<String, FieldEncodingGenerator>();
	protected static Map<String, ProtocolGenerator> protocolGenerators = new HashMap<String, ProtocolGenerator>();

	static {
		// Populate the encoding code generators
		encodingGenerators.put(Constants.ENCODING_CONTRACT, new ContractEncodingGenerator());
		protocolGenerators.put(TCP_PROTOCOL, new TcpProtocolGenerator());
		protocolGenerators.put(HTTP_PROTOCOL, new HttpProtocolGenerator());
	}

	/**
	 * Algorithm:
	 * 
	 * 1. Iterate through each module. For each... 2. Create package structure
	 * to support the module's namespace 3. For each type in a module... 4.
	 * Create a Java file (.java) in the package directory 5. Add the package
	 * statement to the file 6. Add the includes for the type 7. Define the type
	 */
	public void execute(List<Protocol> protocols, Encoding encoding, 
			Queue<ContractModule> modules, String rootPath) throws Exception {
		this.sourceRoot = new File(rootPath);

		classTemplate = loadResource("JavaClass.template");
		
		for (ContractModule module : modules) {		
			String namespace = module.getNamespace();
			File packageDir = createPackageForNamespace(sourceRoot, namespace);
			File implPackageDir = createPackageForNamespace(sourceRoot, namespace + ".impl");
			Set<Interface> interfaces = new HashSet<Interface>();
			
			for (Type type : module.getTypes()) {
				JavaClassGenerator.execute(type, packageDir, encoding);
				if (type instanceof Interface) {
					Interface iface = (Interface) type;
					interfaces.add(iface);
					if (iface.isImplementable()) {
						createImplementation((Interface) type, implPackageDir);
					}
				}
			}
			
			// Now, for each protocol binding, create a new namespace / package structure
			// And generate the implementation classes for the module's interfaces
			for (Protocol protocol : protocols) {
				ProtocolGenerator protoGen = protocolGenerators.get(protocol.getName());
				if (protoGen == null)
					throw new Exception("Unsupported protocol: " + protocol.getName());
				protoGen.execute(interfaces, protocol.getPorts(), encoding);
			}
		}
	}
	
	
	private void createImplementation(Interface iface, File packageDir) throws Exception {
		StringBuffer fields = new StringBuffer();
		StringBuffer methods = new StringBuffer();
		StringBuffer imports = new StringBuffer();
		StringBuffer encodingMethods = new StringBuffer();
		StringBuffer decodingMethods = new StringBuffer();

		String typeName = iface.getName() + "Impl implements " + iface.getName();

		// Generate import statements
		for (Type dependency : iface.getDependencies()) {
			if (!dependency.isNativeType()) {
				for (String strType : typeConverter.toImport(dependency)) {
					imports.append("import " + strType + ";\r\n");
				}
			}
		}
		for (String strType : typeConverter.toImport(iface)) {
			imports.append("import " + strType + ";\r\n");
		}

		// Generate field declarations
		for (Field field : iface.getFields().values()) {
			fields.append("\tprotected "
					+ typeConverter.toField(field.getType()) + " "
					+ field.getName() + ";\r\n");
		}

		for (Method method : iface.getMethods()) {
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
			methods.append(" {\r\n");
			Type returns = method.getReturns();
			if (returns != null) {
				methods.append("\t\t return null;\r\n");
			}
			methods.append("\t}\r\n\r\n");
		}
		String strType = classTemplate.replace(PACKAGE, iface.getNamespace() + ".impl");
		strType = strType.replace(INCLUDES, imports.toString());
		strType = strType.replace(FILENAME, iface.getName() + ".java");
		strType = strType.replace(DATE, sdf.format(new Date()));
		strType = strType.replace(VERSION, iface.getModule().getVersion());
		strType = strType.replace(STRUCTURE_TYPE, "class");
		strType = strType.replace(TYPE_NAME, typeName);
		strType = strType.replace(TYPE_FIELDS, fields.toString());
		strType = strType.replace(TYPE_METHODS, methods.toString());
		strType = strType.replace(ENCODING_METHODS, encodingMethods.toString());
		strType = strType.replace(DECODING_METHODS, decodingMethods.toString());
		
		// First, write out types and interfaces directly to the namespace / package
		// Write out the type to a file
		FileWriter writer = new FileWriter(
				packageDir.getAbsolutePath() + File.separatorChar
						+ iface.getName() + "Impl" + ".java");
		writer.write(strType);
		writer.close();
	}
	
	private File createPackageForNamespace(File path, String namespace) {
		String newPath = null;
		File newPackage = null;
		try {
			newPath = path.getCanonicalPath() + File.separatorChar
					+ namespace.replace(".", File.separator);
			newPackage = new File(newPath);
			if (!newPackage.exists()) {
				if (!newPackage.mkdirs())
					newPackage = null;
			}
		} catch (IOException e) {
			System.out.println("Exception caught referencing file path: "
					+ e.getLocalizedMessage());
			e.printStackTrace();
			newPackage = null;
		}

		return newPackage;
	}
	
	public String pluginInfo() {
		StringBuffer sb = new StringBuffer(
				"Java plugin\n\tSupported protocols:");
		return sb.toString();
	}
	
	public Collection<Protocol> getProtocols() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static String loadResource(String resourceName) {
		InputStream in = JavaGenerator.class.getClassLoader().getResourceAsStream(resourceName);
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
}
