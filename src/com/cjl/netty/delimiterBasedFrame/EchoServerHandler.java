package com.cjl.netty.delimiterBasedFrame;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author chenjunlin  junlin.chen@msn.cn
 * @Date 2019-06-10 14:49
 * @Description:
 */
public class EchoServerHandler extends ChannelHandlerAdapter {
    int counter = 0;
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg){
        String body = (String) msg;
        System.out.println("this is "+ ++counter + "times receive client:["+body+"]");
        body += "$_";
        ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
        ctx.writeAndFlush(echo);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        cause.printStackTrace();
        ctx.close();//发生异常，关闭链路
    }
}
