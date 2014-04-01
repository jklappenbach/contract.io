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
public class HttpProtocolGenerator extends ProtocolGenerator {

	/**
	 * 
	 */
	public HttpProtocolGenerator() {
		super("http");
	}

	@Override
	void execute(Set<Interface> interfaces, Set<Integer> ports, Encoding encoding) {
		// TODO Auto-generated method stub
		
	}

}
