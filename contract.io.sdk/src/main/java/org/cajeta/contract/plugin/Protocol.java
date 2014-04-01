/**
 * 
 */
package org.cajeta.contract.plugin;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author julian
 *
 */
public class Protocol {
	private String name;
	private Set<Integer> ports = new HashSet<Integer>();
	
	public Protocol() { }
	
	public Protocol(String parameter) {
		String[] terms = parameter.split(":");
		name = terms[0];
		if (terms.length > 1) {
			String[] temp = terms[1].split(",");
			for (String port : temp) {
				ports.add(Integer.parseInt(port.trim()));
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
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the ports
	 */
	public Set<Integer> getPorts() {
		return ports;
	}

	/**
	 * @param ports the ports to set
	 */
	public void setPorts(Set<Integer> ports) {
		this.ports = ports;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("Protocol(" + name + ":");
		Iterator<Integer> itr = ports.iterator();
		while (itr.hasNext()) {
			sb.append(itr.next());
			if (itr.hasNext())
				sb.append(",");
		}
		sb.append(")#" + hashCode());
		return sb.toString();
	}
	
	/**
	 * Parses a ";" deliniated string for protocol declarations.
	 * @param str A string containing protocol declarations
	 * @return A list of protocol objects
	 */
	public static List<Protocol> fromString(String str) {
		List<Protocol> protocols = new LinkedList<Protocol>();
		String[] entries = str.split(";");
		for (String entry : entries) {
			Protocol protocol = new Protocol(entry);
			protocols.add(protocol);
		}
		return protocols;
	}
}
