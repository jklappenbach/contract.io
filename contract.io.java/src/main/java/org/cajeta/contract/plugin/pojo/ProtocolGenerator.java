/**
 * 
 */
package org.cajeta.contract.plugin.pojo;

import java.util.Set;

import org.cajeta.contract.plugin.Encoding;
import org.cajeta.contract.type.Interface;

/**
 * @author julian
 *
 */
public abstract class ProtocolGenerator {
	protected String name = null;
	protected String pluginTemplate = null;
	protected String classTemplate = null;
	protected StringBuffer portBody = new StringBuffer();
	protected StringBuffer initializerBody = new StringBuffer();
	protected JavaTypeConverter typeConverter = new JavaTypeConverter();


	/**
	 * Requires a name to use for map population.
	 * @param name The name of the protocl (tcp, http, etc)
	 */
	public ProtocolGenerator(String name) {
		this.name = name;
		pluginTemplate = JavaGenerator.loadResource("Plugin.template");
		classTemplate = JavaGenerator.loadResource("JavaClass.template");
	}
	
	/**
	 * The main entry method for generating protocol bindings for a set of interfaces.
	 * @param interfaces The interfaces to wrap with the protocol marshalling.  
	 * @param ports The designated ports for the protocol.
	 */
	abstract void execute(Set<Interface> interfaces, Set<Integer> ports, Encoding encoding);

	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}



	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}	
}
