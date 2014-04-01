/**
 * 
 */
package org.cajeta.contract.plugin;

import java.util.Set;

import org.cajeta.contract.type.Type;
/**
 * @author julian
 *
 */
public interface TypeConverter {
	String toField(Type type);
	Set<String> toImport(Type type);
}
