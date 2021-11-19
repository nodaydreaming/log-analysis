package com.analysis.logman.communication;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class MyThread extends Thread{
    Socket socket;
    public MyThread(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        BufferedReader bufferedReader = null;
        String line = null;
        try {
            //获得客户端IP地址和客户端名称
            String ClientIP = socket.getInetAddress().getHostAddress();
            String name = socket.getInetAddress().getHostName();
            System.out.println("接受来自名称为：" + name + "和IP为：" + ClientIP + "的连接");
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            line = bufferedReader.readLine();
            System.out.println(line);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

public class ServersSocket {

    public static void main(String[] argw) throws IOException {
        System.out.println("服务器端启动了。。。");
        //通信端口
        final int port = 1223;
        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = null;
        BufferedReader bufferedReader = null;
        while(true) {
            //获取客户端对象
            socket = serverSocket.accept();
            new MyThread(socket).start();
        }
    }
}
