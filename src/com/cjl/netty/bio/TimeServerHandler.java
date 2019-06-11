package com.cjl.netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * @author chenjunlin  junlin.chen@msn.cn
 * @Date 2019-06-05 17:30
 * @Description:
 */
public class TimeServerHandler implements Runnable {
    private Socket socket;
    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try{
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(), true);
            String currentTime = null;
            String body = null;
            while (true){
                body = in.readLine();
                if (body == null)
                    break;
                System.out.println("server order"+body);
                currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(
                        System.currentTimeMillis()).toString():"BAD order";
                out.println(currentTime);
            }
        }catch (Exception e){
            if (in != null){
                try {
                    in.close();
                }catch (IOException el){
                    el.printStackTrace();
                }
            }
            if (out != null){
                out.close();
                out = null;
            }
            if (this.socket != null){
                try {
                    this.socket.close();
                }catch (IOException e1){
                    e1.printStackTrace();
                }
                this.socket = null;
            }
        }


    }
}
