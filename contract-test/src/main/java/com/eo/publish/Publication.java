/**
 * File: Publication.java
 * Date: 11/9/12 3:10 PM
 * Version: 1.0
 * 
 * This file was autogenerated by contract.io.  Any changes will be overwritten if code generation is
 * invoked on this file.  In addition, other modules may have dependencies on definitions in this file.
 * 
 * EDIT AT YOUR OWN RISK!
 */

package com.eo.publish;

import com.eo.publish.AbstractPost;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;


public class Publication extends AbstractPost {
	protected String svg;

	public void setSvg(String svg) {
		this.svg = svg;
	}
	public String getSvg() {
		return svg;
	}
	public void encodeContract(DataOutputStream out) throws IOException {
		super.encodeContract(out);
		out.writeUTF(svg);
	}

	public void decodeContract(DataInputStream in) throws IOException, ClassNotFoundException {
		super.decodeContract(in);
		svg = in.readUTF();
	}

}
