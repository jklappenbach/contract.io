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
public class FloatField extends Field {
	private Double maxValue = null;
	private Double minValue = null;
	private Double defaultValue = null;
	
	public FloatField(XMLReader reader, ContractModule module, String uri,
			String localName, String qName, Attributes attributes)
			throws Exception {
		super(reader, module, uri, localName, qName, attributes);
		String value = attributes.getValue(FIELD_MAX_VALUE);
		if (value != null) {
			maxValue = Double.parseDouble(value);
		}
		value = attributes.getValue(FIELD_MIN_VALUE);
		if (value != null) {
			minValue = Double.parseDouble(value);
		}
		
		value = attributes.getValue(FIELD_DEFAULT_VALUE);
		if (value != null) {
			setDefaultValue(Double.parseDouble(value));
		}
	}


	/**
	 * @return the maxValue
	 */
	public Double getMaxValue() {
		return maxValue;
	}


	/**
	 * @param maxValue the maxValue to set
	 */
	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}


	/**
	 * @return the minValue
	 */
	public Double getMinValue() {
		return minValue;
	}


	/**
	 * @param minValue the minValue to set
	 */
	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}


	/**
	 * @return the defaultValue
	 */
	public Double getDefaultValue() {
		return defaultValue;
	}


	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(Double defaultValue) {
		this.defaultValue = defaultValue;
	}
	
}
