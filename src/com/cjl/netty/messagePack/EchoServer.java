package com.cjl.netty.messagePack;

import com.cjl.netty.delimiterBasedFrame.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author chenjunlin  junlin.chen@msn.cn
 * @Date 2019-06-12 08:24
 * @Description:
 */
public class EchoServer {
    public void bind(int port) throws InterruptedException{
        NioEventLoopGroup bossGrop = new NioEventLoopGroup();
        NioEventLoopGroup bossWork = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGrop,bossWork)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("decoder",new MsgPackDecoder());
                            ch.pipeline().addLast("encoder",new MsgPackEncoder());
                            ch.pipeline().addLast(new EchoServerHandler());

                        }
                    });
            //绑定端口,同步的等待
            ChannelFuture future = bootstrap.bind(port).sync();
            //等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        }finally {
            bossGrop.shutdownGracefully();
            bossWork.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 7878;
        if (args!=null && args.length>0){
            port = Integer.parseInt(args[0]);
        }

        new EchoServer().bind(port);
    }
}
