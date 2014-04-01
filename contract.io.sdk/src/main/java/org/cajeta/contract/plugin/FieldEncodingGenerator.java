/**
 * 
 */
package org.cajeta.contract.plugin;

import org.cajeta.contract.StringUtilities;
import org.cajeta.contract.idl.field.Field;
import org.cajeta.contract.type.Type;

/**
 * @author julian
 *
 */
public abstract class FieldEncodingGenerator {
	protected String encoding;
	
	public String getEncoding() {
		return encoding;
	}
	
	public abstract String getDependencies();
	
	protected String getEncodeMethodName() {
		return StringUtilities.camelCase(false,
				new String[] { "encode", encoding });
	}
	
	protected String getDecodeMethodName() {
		return StringUtilities.camelCase(false,
				new String[] { "decode", encoding });
	}
	
	public abstract void onStartMethod(StringBuffer in, StringBuffer out, Type superType);
	public abstract void onField(int indent, Field field, StringBuffer in, StringBuffer out);
	public abstract void onEndMethod(StringBuffer in, StringBuffer out);
}
