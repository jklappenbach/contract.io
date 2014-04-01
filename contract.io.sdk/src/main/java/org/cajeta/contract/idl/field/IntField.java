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
public class IntField extends Field {
	private Long maxValue = null;
	private Long minValue = null;
	private Long defaultValue = null;
	
	public IntField(XMLReader reader, ContractModule module, String uri,
			String localName, String qName, Attributes attributes)
			throws Exception {
		super(reader, module, uri, localName, qName, attributes);
		String value = attributes.getValue(FIELD_MAX_VALUE);
		if (value != null) {
			maxValue = StringUtilities.toLong(value);
		}
		value = attributes.getValue(FIELD_MIN_VALUE);
		if (value != null) {
			minValue = StringUtilities.toLong(value);
		}
		value = attributes.getValue(FIELD_DEFAULT_VALUE);
		if (value != null) {
			setDefaultValue(Long.parseLong(value));
		}
	}

	/**
	 * @return the maxValue
	 */
	public Long getMaxValue() {
		return maxValue;
	}

	/**
	 * @param maxValue the maxValue to set
	 */
	public void setMaxValue(Long maxValue) {
		this.maxValue = maxValue;
	}

	/**
	 * @return the minValue
	 */
	public Long getMinValue() {
		return minValue;
	}

	/**
	 * @param minValue the minValue to set
	 */
	public void setMinValue(Long minValue) {
		this.minValue = minValue;
	}

	/**
	 * @return the defaultValue
	 */
	public Long getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(Long defaultValue) {
		this.defaultValue = defaultValue;
	}
}
