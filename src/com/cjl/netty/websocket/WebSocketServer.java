package com.cjl.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author chenjunlin  junlin.chen@msn.cn
 * @Date 2019-06-13 08:04
 * @Description:
 */
public class WebSocketServer {
    public void run(int port) throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("http-codec",new HttpServerCodec());
                            pipeline.addLast("aggregator",new HttpObjectAggregator(65536));
                            ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                            pipeline.addLast("handler",new WebSocketServerHandler());
                        }
                    });
               //
            Channel ch = bootstrap.bind(port).sync().channel();
            System.out.println("web socket server start at port"+port+'.');
            System.out.println("open browser http://localhost:"+port+'/');
            ch.closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 1234;
        if (args!=null && args.length>0){
            try{
            port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        new WebSocketServer().run(port);
    }
}
