package org.teiid.test.server.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class OutputChannelHandler extends ChannelDuplexHandler {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(ctx.channel() + " active");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(ctx.channel() + " inactive");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(ctx.channel() + " read: " + msg);
	}

}
