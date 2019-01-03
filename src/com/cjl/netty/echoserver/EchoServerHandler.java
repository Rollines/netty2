package com.cjl.netty.echoserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author chenjunlin
 * @date 2018-12-26
 */
public class EchoServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    //当被通知channel是活跃的时候，发送一条信息
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }
     /*
     * 记录已接收消息的转绪
      */
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        System.out.println(
                "Client received: "+in.toString(CharsetUtil.UTF_8)
        );
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
