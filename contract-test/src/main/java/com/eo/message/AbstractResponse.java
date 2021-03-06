/**
 * File: AbstractResponse.java
 * Date: 11/9/12 3:10 PM
 * Version: 1.0
 * 
 * This file was autogenerated by contract.io.  Any changes will be overwritten if code generation is
 * invoked on this file.  In addition, other modules may have dependencies on definitions in this file.
 * 
 * EDIT AT YOUR OWN RISK!
 */

package com.eo.message;

import com.eo.message.StatusCode;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;


public class AbstractResponse {
	protected Integer messageId;
	protected StatusCode statusCode;
	protected Long lastUpdate;

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}
	public Integer getMessageId() {
		return messageId;
	}
	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}
	public StatusCode getStatusCode() {
		return statusCode;
	}
	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public Long getLastUpdate() {
		return lastUpdate;
	}
	public void encodeContract(DataOutputStream out) throws IOException {
		out.writeInt(messageId);
		out.writeLong(lastUpdate);
	}

	public void decodeContract(DataInputStream in) throws IOException, ClassNotFoundException {
		messageId = in.readInt();
		lastUpdate = in.readLong();
	}

}
