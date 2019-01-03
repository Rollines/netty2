package com.cjl.netty;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.apache.log4j.pattern.LogEvent;


import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author chenjunlin
 * @date 2018-12-29
 */
public class LogEventEncoder extends MessageToMessageEncoder {

    private final InetSocketAddress remoteAddress;
    public LogEventEncoder(InetSocketAddress remoteAddress){
        this.remoteAddress = remoteAddress;
    }


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {

    }
}
