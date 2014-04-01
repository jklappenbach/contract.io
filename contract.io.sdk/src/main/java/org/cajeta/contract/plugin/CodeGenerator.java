/**
 * 
 */
package org.cajeta.contract.plugin;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.cajeta.contract.idl.ContractModule;
import org.cajeta.contract.type.Interface;

/**
 * @author julian
 *
 */
public interface CodeGenerator {
	public Collection<Protocol> getProtocols();
	public void execute(List<Protocol> protocols, Encoding encoding, Queue<ContractModule> modules, String rootPath) throws Exception;
	public String pluginInfo();
}
