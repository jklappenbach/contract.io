/**
 * 
 */
package org.cajeta.contract.type;

/**
 * @author julian
 *
 */
public class EnumElement {
	private String name;
	private int	ordinal;

	public EnumElement(String name, int ordinal) {
		this.setName(name);
		this.setOrdinal(ordinal);
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
	 * @return the ordinal
	 */
	public int getOrdinal() {
		return ordinal;
	}

	/**
	 * @param ordinal the ordinal to set
	 */
	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}
}
