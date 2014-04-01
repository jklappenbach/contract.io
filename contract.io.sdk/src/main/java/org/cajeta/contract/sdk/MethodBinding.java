/**
 * 
 */
package org.cajeta.contract.sdk;

import io.netty.buffer.ByteBuf;

/**
 * @author julian
 *
 */
public abstract class MethodBinding {
	protected String key;
	
	/**
	 * 
	 */
	public MethodBinding(String key) {
		this.key = key;
	}
	
	public abstract ByteBuf execute(ByteBuf buffer);
	
	public String key() {
		return key;
	}
}
