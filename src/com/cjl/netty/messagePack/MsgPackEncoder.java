package com.cjl.netty.messagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * @author chenjunlin  junlin.chen@msn.cn
 * @Date 2019-06-12 08:15
 * @Description: 编码代码
 */
public class MsgPackEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        MessagePack messagePack = new MessagePack();
        //编码，然后转为butybuf传递
        byte[] bytes = messagePack.write(msg);
        out.writeBytes(bytes);
    }
}
