/**
 * 
 */
package org.cajeta.contract.type;

import java.util.LinkedList;
import java.util.List;

import org.cajeta.contract.idl.ContractElementHandler;
import org.cajeta.contract.idl.ContractModule;
import org.cajeta.contract.idl.TypeUnknownException;
import org.cajeta.contract.idl.field.Field;
import org.cajeta.contract.idl.field.FieldFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @author julian
 *
 */
public class Method extends ContractElementHandler {
	private static final String METHOD = "method";
	private static final String METHOD_NAME = "name";
	private static final String METHOD_RETURNS = "returns";
	private static final String METHOD_ACTION = "action";
	private static final String FIELD = "field";
	private static final String EXCEPTION = "exception";
	
	private String fieldType;
	private String name;
	private String action;
	private Type returns = null;
	private List<Field> fields = new LinkedList<Field>();
	private List<MethodException> exceptions = new LinkedList<MethodException>();
	public Method() throws TypeUnknownException { 
		returns = Type.resolve(Type.VOID_TYPE);
	}
	
	public Method(XMLReader reader, ContractModule module, String uri,
			String localName, String qName, Attributes attributes) throws SAXException, TypeUnknownException {
		super(reader, module, uri, localName, qName, attributes);
		name = attributes.getValue(METHOD_NAME);
		action = attributes.getValue(METHOD_ACTION);
		if (name == null)
			throw new SAXException("Invalid Method element, requires valid name and action attributes");
		returns = Type.resolve(attributes.getValue(METHOD_RETURNS));
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (qName.equals(FIELD)) {
			try {
				fields.add(FieldFactory.newField(reader, module, uri, localName, qName, attributes));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (qName.equals(EXCEPTION)) {
			try {
				MethodException exception = (MethodException) Type.resolve(attributes.getValue("name"));
				exceptions.add(exception);
			} catch (Exception e) {
				System.out.println("Error: exception caught processing module [" + this.getModule().toString() + "]");
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.cajeta.contract.idl.DelegatingHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equals(METHOD))
			super.endElement(uri, localName, qName);
	}

	/**
	 * @return the fieldType
	 */
	public String getFieldType() {
		return fieldType;
	}

	/**
	 * @param fieldType the fieldType to set
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
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
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the returns
	 */
	public Type getReturns() {
		return returns;
	}

	/**
	 * @param returns the returns to set
	 */
	public void setReturns(Type returns) {
		this.returns = returns;
	}

	/**
	 * @return the fields
	 */
	public List<Field> getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	/**
	 * @return the exceptions
	 */
	public List<MethodException> getExceptions() {
		return exceptions;
	}

	/**
	 * @param exceptions the exceptions to set
	 */
	public void setExceptions(List<MethodException> exceptions) {
		this.exceptions = exceptions;
	}
	
}
