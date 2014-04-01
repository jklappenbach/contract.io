/**
 * 
 */
package org.cajeta.contract.sdk.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.HeapByteBuf;

import java.io.UnsupportedEncodingException;

/**
 * @author julian
 *
 */
public class ContractBuf extends HeapByteBuf {
	public class CharsetNames {
		static final String UTF_8 = "UTF-8";
		static final String UTF_16 = "UTF-16";
	}

	public ContractBuf(byte[] initialArray, int maxCapacity) {
		super(initialArray, maxCapacity);
	}

	public ContractBuf(int initialCapacity, int maxCapacity) {
		super(initialCapacity, maxCapacity);
	}
	public void writeString(String str) throws UnsupportedEncodingException {
		writeString(str, CharsetNames.UTF_8);
	}
	public String readString() throws UnsupportedEncodingException {
		return readString(CharsetNames.UTF_8);
	}

	public void writeString(String str, String charset) throws UnsupportedEncodingException {
		byte[] bytes = str.getBytes(charset);
		this.writeInt(bytes.length);
		this.writeBytes(bytes);
	}
	
	public String readString(String charset) throws UnsupportedEncodingException {
		int length = this.readInt();
		ByteBuf bytes = this.readBytes(length);
		return new String(bytes.array(), charset);
	}
}
