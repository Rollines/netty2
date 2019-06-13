package com.cjl.netty.messagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * @author chenjunlin  junlin.chen@msn.cn
 * @Date 2019-06-12 08:19
 * @Description: 解码代码
 *
 * 要记好netty接收和传递信息都是经过ByteBuf进行的
 */
public class MsgPackDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        //获取要解码的数组
        final byte[] bytes;
        final int length = byteBuf.readableBytes();
        bytes = new byte[length];
        byteBuf.getBytes(byteBuf.readerIndex(),bytes,0,length);
        //调用messagepack的read方法将其反系列化为object对象
        MessagePack msgPack = new MessagePack();
        list.add(msgPack.read(bytes));
    }
}
