package com.cjl.netty.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author chenjunlin  junlin.chen@msn.cn
 * @Date 2019-06-05 17:21
 * @Description:
 */
public class TimeServer {
    public static void main(String[] args) throws IOException {
        int port = 8787;
        if (args!=null && args.length>0){
            try{
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        ServerSocket serverSocket= null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("time server"+port);
            Socket socket = null;
            while (true){
                socket = serverSocket.accept();
                new Thread(new TimeServerHandler(socket)).start();
            }

        } finally {
            if (serverSocket!=null){
                System.out.println("time close");
                serverSocket.close();
                serverSocket = null;
            }
        }
    }
}
