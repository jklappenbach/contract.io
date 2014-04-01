/**
 * 
 */
package org.cajeta.contract;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.cajeta.contract.idl.ContractModule;

/**
 * @author julian
 *
 */
public class ContractParseThread implements Runnable {
	private File file;
	private Contract contract;
	
	public ContractParseThread(String namespace) {
		this.contract = Contract.getInstance();
		
		if (contract.addNamespace(namespace)) {
			String path = Contract.getParameters().containsKey(Constants.ARG_INPUT_PATH) ?
					Contract.getParameters().get(Constants.ARG_INPUT_PATH)  : ".";
					
			path += File.separatorChar + namespace + ".xml";
			file = new File(path);
		}
	}
	
	public ContractParseThread(File file) throws Exception {
		this.contract = Contract.getInstance();
		this.file = file;
		String namespace = file.getName().substring(0, file.getName().lastIndexOf("."));
		if (!contract.addNamespace(namespace))
			throw new Exception("Could not add namespace.");
	}

	public void run() {
		System.out.println("Processing " + file.getPath());
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			ContractModule module = new ContractModule(parser.getXMLReader()); 
			parser.parse(file, module);
			if (module.getNamespace() != null)
				Contract.getModules().add(module);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
