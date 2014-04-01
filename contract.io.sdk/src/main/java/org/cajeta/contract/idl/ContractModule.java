/**
 * 
 */
package org.cajeta.contract.idl;

import java.util.LinkedList;
import java.util.List;

import org.cajeta.contract.type.Interface;
import org.cajeta.contract.type.MethodException;
import org.cajeta.contract.type.Type;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.cajeta.contract.type.EnumType;

/**
 * @author julian
 *
 */
public class ContractModule extends DelegatingHandler {
	private static final String CONTRACT = "contract";
	private static final String CONTRACT_NAMESPACE = "namespace";
	private static final String CONTRACT_VERSION = "version";
	private static final String TYPE = "type";
	private static final String ENUM = "enum";
	private static final String INCLUDE = "include";
	private static final String INTERFACE = "interface";
	private static final String EXCEPTION = "exception";
	
	private String namespace;
	private static Integer currentOrdinal = 0;
	private Integer currentTypeOrdinal = 0;
	private int ordinal;
	private String version;
	private List<Include> includes = new LinkedList<Include>();
	
	public ContractModule(XMLReader reader) {
		this.oldHandler = reader.getContentHandler();
		this.reader = reader;
		synchronized (currentOrdinal) {
			ordinal = currentOrdinal;
			currentOrdinal++;
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		super.startElement(uri, localName, qName, attributes);
		
		try {
			if (qName.equals(CONTRACT)) {
				setVersion(attributes.getValue(CONTRACT_VERSION));				
				String namespace = attributes.getValue(CONTRACT_NAMESPACE);
				if (namespace == null)
					namespace = "";
				setNamespace(namespace);
			} else if (qName.equals(TYPE)) { 
				new Type(reader, this, uri, localName, qName, attributes);
			} else if (qName.equals(ENUM)) {
				new EnumType(reader, this, uri, localName, qName, attributes);
			} else if (qName.equals(INCLUDE)) {
				getIncludes().add(new Include(reader, this, uri, localName, qName, attributes));
			} else if (qName.equals(INTERFACE)) {
				new Interface(reader, this, uri, localName, qName, attributes);
			} else if (qName.equals(EXCEPTION)) {
				new MethodException(reader, this, uri, localName, qName, attributes);
			}
		} catch (Exception e) {
			System.out.println("Exception encounterd while parsing!");
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}
	/**
	 * @param namespace the namespace to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	/**
	 * @return the includes
	 */
	public List<Include> getIncludes() {
		return includes;
	}
	/**
	 * @param includes the includes to set
	 */
	public void setIncludes(List<Include> includes) {
		this.includes = includes;
	}

	public String getVersion() { return version; }

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the ordinal
	 */
	public int getOrdinal() {
		return ordinal;
	}

	/**
	 * @param ordinal the ordinal to set
	 */
	public static void setOrdinal(int ordinal) throws Exception {
		synchronized(currentOrdinal) {
			if (ordinal < currentOrdinal) 
				throw new Exception("Module ordinal value has already been assigned: " + ordinal);
			else 
				currentOrdinal = ordinal;
		}
	}
	
	public void setTypeOrdinal(int ordinal) throws Exception {
		synchronized(currentTypeOrdinal) {
			if (ordinal < currentTypeOrdinal) 
				throw new Exception("Module ordinal value has already been assigned: " + ordinal);
			else 
				currentTypeOrdinal = ordinal;
		}
	}
	
	public int setAndAllocateTypeOrdinal(int ordinal) throws Exception {
		synchronized(currentTypeOrdinal) {
			if (ordinal < currentTypeOrdinal) 
				throw new Exception("Module ordinal value has already been assigned: " + ordinal);
			else 
				currentTypeOrdinal = ordinal;
			return currentTypeOrdinal++;
		}
		
	}
	
	public int getNextTypeOrdinal() {
		int ordinal;
		synchronized(currentTypeOrdinal) {
			ordinal = currentTypeOrdinal++;
		}
		return ordinal;
	}
	
	public int peekTypeOrdinal() {
		int ordinal;
		synchronized(currentTypeOrdinal) {
			ordinal = currentTypeOrdinal;
		}
		return ordinal;
	}
}
