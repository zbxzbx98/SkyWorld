package skyworld;

import skyworld.util.MD5;

import java.io.*;
import java.util.Scanner;

public class SkyApp {
    public static void main(String[] args) {
        System.out.println("欢迎来到光遇世界！肝硬化~");
        SkyMap theMap = new SkyMap();
        theMap.mapEnum = MapEnum.home;
        theMap.mapID = 0;
        YouSelf you = null;
        boolean flag = true;
        Scanner sc = new Scanner(System.in);
        while (flag) {
            System.out.println("请输入你的角色名字");
            String name = sc.nextLine();
            File f = new File("SkyWorld\\" + name + "data.txt");
            if (f.exists()) {
                String info = load(name);
                String[] infos = info.split(" ");
                System.out.println("请输入密码：");
                if (infos[9].equals(MD5.encode(sc.nextLine()))) {
                    System.out.println("登录成功！");
                    you = new YouSelf(Integer.parseInt(infos[1]), Integer.parseInt(infos[2]),Integer.parseInt(infos[3]), 1, infos[0], infos[9]);
                    you.setLastMap(MapEnum.valueOf(infos[7]));
                    you.setLastMapID(Integer.parseInt(infos[8]));
                    you.candle = Double.parseDouble(infos[4]);
                    you.heart = Double.parseDouble(infos[5]);
                    you.redCandle = Double.parseDouble(infos[6]);
                    break;
                } else {
                    System.out.println("你输入的密码有误！");
                }
            } else {
                System.out.println("账号不存在，将为您新建账号...\n请输入此账号的密码（账号与密码绑定）：");
                while (true) {
                    String psw = sc.nextLine();
                    System.out.println("请确认密码：");
                    if (psw.equals(sc.nextLine())) {
                        System.out.println("成功创建账号！祝您游戏愉快！");
                        you = new YouSelf(name, MD5.encode(psw));
                        you.save();
                        flag = false;
                        break;
                    }
                    System.out.println("两次输入的密码不一致，请重新输入：");
                }
            }
        }
        theMap.addPlayer(you);
        theMap.you = you;
        you.nowMap = theMap;
        theMap.refreshMap();
        while (true) {
            you.choose();
        }
    }

    public static String load(String name) {
        try (BufferedReader isr = new BufferedReader(new FileReader("SkyWorld\\" + name + "data.txt"))) {
            return isr.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
