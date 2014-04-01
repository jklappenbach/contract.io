/**
 * 
 */
package org.cajeta.contract.plugin.pojo;

import java.util.Iterator;
import java.util.Set;

import org.cajeta.contract.StringUtilities;
import org.cajeta.contract.idl.field.Field;
import org.cajeta.contract.plugin.Encoding;
import org.cajeta.contract.type.Interface;
import org.cajeta.contract.type.Method;

/**
 * @author julian
 *
 */
public class TcpProtocolGenerator extends ProtocolGenerator {
	
	public TcpProtocolGenerator() {
		super("tcp");
	}
	public void execute(Set<Interface> interfaces, Set<Integer> ports, Encoding encoding) {
		String packageName;
		String className;
		StringBuffer includes = new StringBuffer();
		
		packageName = ""; // What do we use for the main plugin package?
		className = "TcpChannelHandler";
		
		
		// Populate port body
		portBody.append("\t\tSet<Integer> ports = new HashSet<Integer>();\r\n");
		for (Integer port : ports) {
			portBody.append("\t\tports.add(" + port + ");\r\n");
		}
		portBody.append("return ports;\r\n");
		
		// Populate channel initializer
		initializerBody.append("\t\treturn new ChannelInitializer<SocketChannel>() {\r\n");
		initializerBody.append("\t\t\t@Override\r\n\t\t\tpublic void initChannel(SocketChannel ch) throws Exception {\r\n");
		initializerBody.append("\t\t\t\tch.pipeline().addLast(new ContractBinaryHandler() {\r\n");
		initializerBody.append("\t\t\t\t\tpublic void populateMethodBindings() {\r\n");
		for (Interface iface : interfaces) {
			String implName = StringUtilities.camelCase(false, new String[] { iface.getName(), "Impl" } );
			initializerBody.append("\t\t\t\t\t" + typeConverter.toField(iface) + " " + implName);
			for (Method method : iface.getMethods()) {
				initializerBody.append("\t\t\t\t\t\tmethodBindings.put(" + iface.getRelativeUri() + ":" + method.getAction() + ", new " +
						iface.getName() + "_" + method.getName() + "_Binding(" + implName +
								"));\r\n");
				generateMethodBinding(packageName + "", className, method, encoding);
			}
		}
		initializerBody.append("});\r\n\t\t\t};\r\n");
		String structureType = "class";
		StringBuffer typeFields = new StringBuffer();
		StringBuffer typeMethods = new StringBuffer();
		
		JavaClassGenerator.execute(packageName, includes, structureType, className, typeFields, typeMethods, null, null);
	}

	private String namespaceToPackage(String namespace) {
		return null;
	}
	
	private void generateMethodBinding(String packageName, String className, Method method, Encoding encoding) {
		StringBuffer includes = new StringBuffer();
		StringBuffer typeMethods = new StringBuffer();
		StringBuffer arguments = new StringBuffer();
		StringBuffer typeFields = new StringBuffer();
		Iterator<Field> itr = method.getFields().iterator();
		while (itr.hasNext()) {
			Field field = itr.next();
			typeMethods.append("\t\t" + typeConverter.toField(field.getType()) + " " + field.getName() + " = new " + typeConverter.toField(field.getType()) + "()");
			typeMethods.append("\t\t" + field.getName() + "." + StringUtilities.camelCase(false, 
					new String[] { "encode", encoding.getName() }));
			typeMethods.append("(in);\r\n");
			typeMethods.append("\t\t" + method.getName() + "(");
			arguments.append(field.getName());
			if (itr.hasNext())
				arguments.append(", ");
		}
		JavaClassGenerator.execute(packageName, includes, "class", className, typeFields, typeMethods, null, null);
	}
}

