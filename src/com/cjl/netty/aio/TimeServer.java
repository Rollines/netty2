package com.cjl.netty.aio;

import java.io.IOException;

/**
 * @author chenjunlin
 * @date 2018-12-25
 */
public class TimeServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
        new Thread((Runnable) timeServer, "AIO-AsyncTimeServerHandler-001").start();
    }
}