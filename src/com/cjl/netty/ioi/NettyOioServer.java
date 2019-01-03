package com.cjl.netty.ioi;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @author chenjunlin
 * @date 2018-12-26
 */
//通过Netty使用OIO和NIO
public class NettyOioServer {
    public void server(int port) throws InterruptedException {
        final ByteBuf buf = Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("HI!\r\n", Charset.forName("UTF-8"))
        );
        EventLoopGroup group = new OioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group) //使用OioEventLoopGroup以允许阻塞模式<旧的i/o
                    .channel(OioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    //添加一个channelInbound...拦截和处理事件
                                    new ChannelInboundHandlerAdapter(){
                                        /*
                                        * 将消息写到客户端，并添加ChannelFutureListener，
                                        * 以便消息被写完就关闭连接
                                        * */
                                        @Override
                                        public void channelActive(
                                                ChannelHandlerContext ctx
                                        ) throws Exception{
                                            ctx.writeAndFlush(buf.duplicate())
                                                    .addListener(ChannelFutureListener.CLOSE);
                                        }
                                    });
                        }
                    });
            //绑定服务器以接受连接
            ChannelFuture future = bootstrap.bind().sync();
            future.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }
}
