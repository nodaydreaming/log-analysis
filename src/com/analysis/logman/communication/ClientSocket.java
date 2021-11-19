package com.analysis.logman.communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

class YRB{
    private String name;
    private int sex;
    YRB(String name,int sex){
        this.name = name;
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "name="+name+",sex="+((sex==1)?"男":"nv");
    }
}

public class ClientSocket {
    public static void main(String[] args){
        try {
            Socket socket = new Socket("localhost",1223);
            Scanner in = new Scanner(System.in);
            String str = null;
            while((str = in.nextLine())!=null){
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.println(str);
                out.flush();
            }
        }catch (UnknownHostException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
