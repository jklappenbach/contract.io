package org.cajeta.contract.sdk;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundByteHandlerAdapter;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.util.Map;

import org.cajeta.contract.sdk.MethodBinding;

public abstract class ContractBinaryHandler extends
		ChannelInboundByteHandlerAdapter {
	protected int byteCount = -1;
	protected int MAX_SIZE = 1024 * 1024 * 5; // Max 5MB of data
	protected CharsetDecoder utf8Decoder;
	protected static final int GET_ACTION = 0;
	protected static final int POST_ACTION = 1;
	protected static final int DELETE_ACTION = 2;
	protected static final int PUT_ACTION = 3;
	protected static final int OPTIONS_ACTION = 4;
	protected Map<String, MethodBinding> methodBindings;
	
	
	public ContractBinaryHandler() {
	}

	@Override
	public void inboundBufferUpdated(ChannelHandlerContext ctx,
			ByteBuf in) throws Exception {
		if (byteCount == -1) {
			if (in.array().length > 4) {
				byteCount = in.readInt();
				if (byteCount > MAX_SIZE)
					throw new Exception("Message too long");
			} else { 
				// Haven't read in even enough bytes to get the byte count
				return;
			}
		} else {
			if (in.array().length < byteCount) return;
			// Parse the URI for the request
			int uriLength = in.readInt();
			String uri = null;
	        try {  
	            uri = utf8Decoder.decode(ByteBuffer.wrap(in.readBytes(uriLength).array())).toString();  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }
	        
	        int action = in.readInt();
	        
	        // Now, get the adaptor for the uri/action pair
	        String key = uri + ":" + action;
	        ctx.outboundByteBuffer().writeBytes(methodBindings.get(key).execute(in));
		}
	}
	
	public abstract void populateMethodBindings();
}