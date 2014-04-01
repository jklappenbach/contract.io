/**
 * 
 */
package org.cajeta.contract.sdk;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.HashMap;
import java.util.Map;

/**
 * @author julian
 * @deprecated 
 *
 */
public class Services {
	static ThreadLocal<Map<String, CharsetDecoder>> decoders;
	static ThreadLocal<Map<String, CharsetEncoder>> encoders;
	
	/**
	 * 
	 */
	public Services() {
		// TODO Auto-generated constructor stub
	}
	
	public static CharsetDecoder getDecoder(String encoding) {
		Map<String, CharsetDecoder> map = decoders.get();
		if (map == null) {
			map = new HashMap<String, CharsetDecoder>();
		}
		CharsetDecoder decoder = map.get(encoding);
		if (decoder == null) {
			decoder = Charset.forName(encoding).newDecoder();
	        decoder.onMalformedInput(CodingErrorAction.REPORT);  
	        decoder.onUnmappableCharacter(CodingErrorAction.REPORT);
	        map.put(encoding, decoder);
		}

		return decoder;
	}
	
	public static CharsetEncoder getEncoder(String encoding) {
		Map<String, CharsetEncoder> map = encoders.get();
		if (map == null) {
			map = new HashMap<String, CharsetEncoder>();
		}
		CharsetEncoder encoder = map.get(encoding);
		if (encoder == null) {
			encoder = Charset.forName(encoding).newEncoder();
	        encoder.onMalformedInput(CodingErrorAction.REPORT);  
	        encoder.onUnmappableCharacter(CodingErrorAction.REPORT);
	        map.put(encoding, encoder);
		}

		return encoder;
		
	}
}
