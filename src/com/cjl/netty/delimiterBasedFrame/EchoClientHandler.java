package com.cjl.netty.delimiterBasedFrame;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandlerInvoker;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @author chenjunlin  junlin.chen@msn.cn
 * @Date 2019-06-11 08:13
 * @Description:
 * 1、将DelimiterBasedFrameDecoder和StringDecoder添加到客户端
 * ChannelPipeline中，最后添加客户端I/O事件处理类EchoClientHandler
 */
public class EchoClientHandler extends ChannelHandlerAdapter {
    private int counter;

    static final String ECHO_REQ = "Hi,doctor.Welcome to Netty.$_";

    /**
     * Creates a client-side handle
     */
    public EchoClientHandler(){

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        for (int i = 0;i<10;i++){
        ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg){
        System.out.println("this is"+ ++counter + "time receive server:["+msg+"]");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
