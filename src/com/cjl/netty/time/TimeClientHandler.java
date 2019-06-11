package com.cjl.netty.time;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;


/**
 * @author chenjunlin  junlin.chen@msn.cn
 * @Date 2019-06-10 08:38
 * @Description:
 */
public class TimeClientHandler extends ChannelHandlerAdapter {
    private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());
    private final ByteBuf firstMessage;

    public TimeClientHandler(){
        byte[] req = "query time order".getBytes();
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req,"UTF-8");
        System.out.println("Now is :"+body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        logger.warning("from downstream : "+cause.getMessage());
        ctx.close();
    }

}
