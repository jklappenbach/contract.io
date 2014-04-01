/**
 * 
 */
package org.cajeta.contract.idl.field;

import org.cajeta.contract.StringUtilities;
import org.cajeta.contract.idl.ContractModule;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

/**
 * @author julian
 *
 */
public class StringField extends Field {
	Long maxLength = null;
	Long minLength = null;
	String defaultValue = null;
	String regex = null;
	
	public StringField(XMLReader reader, ContractModule module, String uri,
			String localName, String qName, Attributes attributes)
			throws Exception {
		super(reader, module, uri, localName, qName, attributes);
		String value = attributes.getValue(FIELD_MAX_LENGTH);
		if (value != null) {
			maxLength = StringUtilities.toLong(value);
		}
		value = attributes.getValue(FIELD_MIN_LENGTH);
		if (value != null) {
			minLength = StringUtilities.toLong(value);
		}
		
		defaultValue = attributes.getValue(FIELD_DEFAULT_VALUE);
		
		regex = attributes.getValue(FIELD_REGEX);
	}

	/**
	 * @return the maxLength
	 */
	public Long getMaxLength() {
		return maxLength;
	}

	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(Long maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * @return the minLength
	 */
	public Long getMinLength() {
		return minLength;
	}

	/**
	 * @param minLength the minLength to set
	 */
	public void setMinLength(Long minLength) {
		this.minLength = minLength;
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

	/**
	 * @return the regex
	 */
	public String getRegex() {
		return regex;
	}

	/**
	 * @param regex the regex to set
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}
	
	
}
