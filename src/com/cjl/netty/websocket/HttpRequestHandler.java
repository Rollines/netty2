package com.cjl.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.http.HttpHeaders;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author chenjunlin
 * @date 2018-12-29
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String wsUri;
    private static final File INDEX;
    static {
        URL location = HttpRequestHandler.class
                .getProtectionDomain()
                .getCodeSource().getLocation();
        try{
            String path = location.toURI()+"index.html";
            path =!path.contains("file:")?path:path.substring(5);
            INDEX = new File(path);
        }catch (URISyntaxException e){
            throw new IllegalStateException("unable to locate index.html",e);
        }
    }

    public HttpRequestHandler(String wsUri){
        this.wsUri = wsUri;
    }


    public void channelRead0(ChannelHandlerContext ctx,FullHttpRequest request){
        if (wsUri.equalsIgnoreCase(request.getUri())){
            ctx.fireChannelRead(request.retain());
        }else {
            /*if (HttpHeaders.is100ContinueExpected(request)){
                send100Continue(ctx);
            }*/
        }
    }

    private void send100Continue(ChannelHandlerContext ctx) {
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {

    }
}
