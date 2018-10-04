package simple.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		System.out.println("Hello 客户端活跃了起来");
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		System.out.println("Hello 客户端读取到信息：" + msg);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
