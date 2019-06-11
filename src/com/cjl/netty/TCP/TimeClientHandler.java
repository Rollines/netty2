package com.cjl.netty.TCP;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;


/**
 * @author chenjunlin  junlin.chen@msn.cn
 * @Date 2019-06-10 13:16
 * @Description:
 */
public class TimeClientHandler extends ChannelHandlerAdapter {
    private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());

    private int counter;
    private byte[] req;

    public TimeClientHandler(){
        req = ("query time order" + System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        ByteBuf message = null;
        for (int i=0;i<100;i++){
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg){
        String body = (String) msg;
        System.out.println("Now is :"+body+";the counter is :"+ ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        logger.warning("unexpected exception from downstream"+cause.getMessage());
        ctx.close();
    }
}
