package com.analysis.logman.read;

import java.io.*;
import java.util.Scanner;

public class ReadHelper {
//    日志读取帮助，模拟随时产生日志的样式

//    从现有的日志当中读取日志内容

//    写入新的文件当中
    public void readHelper() throws IOException,InterruptedException{
        File readFile = new File("E:\\log\\log1.txt");
        File writeFile = new File("E:\\log\\log_1.txt");
        InputStreamReader read = new InputStreamReader(new FileInputStream(readFile), "gbk");

        BufferedReader br;
        br = new BufferedReader(read);

        FileWriter fw = new FileWriter(writeFile);

        String line;
        int i = 0;
        while ((line = br.readLine()) != null){
            i ++;
            if (i % 100 == 0)
                Thread.sleep(100);//线程休眠1S
            fw.write(line + "\n");
            fw.flush();
        }

        Scanner scanner = new Scanner(System.in);
        String s = scanner.next();
        new FileOutputStream(writeFile,false);
        fw.write("");
        fw.close();
    }

    public static void main(String[] args) throws IOException,InterruptedException {
        ReadHelper readHelper = new ReadHelper();
        readHelper.readHelper();
        System.out.println("finish");
    }
}
