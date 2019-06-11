package com.cjl.netty.TCP;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

/**
 * @author chenjunlin  junlin.chen@msn.cn
 * @Date 2019-06-10 11:26
 * @Description:
 */
public class TimeServerHandler extends ChannelHandlerAdapter {
    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg){
        String body = (String) msg;
        System.out.println("time server receive order："+body+";the counter is:"+ ++counter);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"BAD ORDER";
        currentTime = currentTime + System.getProperty("line.separator");
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        ctx.close();
    }
}
