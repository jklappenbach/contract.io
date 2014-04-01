/**
 * 
 */
package org.cajeta.contract.idl.field;

import org.cajeta.contract.idl.ContractModule;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

/**
 * @author julian
 * 
 */
public class DateField extends Field {
	private String minDate;
	private String maxDate;
	private String defaultValue;
	
	public DateField(XMLReader reader, ContractModule module, String uri,
			String localName, String qName, Attributes attributes)
			throws Exception {
		super(reader, module, uri, localName, qName, attributes);
		minDate = attributes.getValue(FIELD_MIN_VALUE);
		maxDate = attributes.getValue(FIELD_MAX_VALUE);
		defaultValue = attributes.getValue(FIELD_DEFAULT_VALUE);
		
	}

	/**
	 * @return the minDate
	 */
	public String getMinDate() {
		return minDate;
	}

	/**
	 * @param minDate the minDate to set
	 */
	public void setMinDate(String minDate) {
		this.minDate = minDate;
	}

	/**
	 * @return the maxDate
	 */
	public String getMaxDate() {
		return maxDate;
	}

	/**
	 * @param maxDate the maxDate to set
	 */
	public void setMaxDate(String maxDate) {
		this.maxDate = maxDate;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
}
