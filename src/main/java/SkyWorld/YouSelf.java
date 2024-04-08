package SkyWorld;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class YouSelf extends SkyPlayer
{
    public ArrayList<SkyPlayer> lightUpPlayer = new ArrayList<>();
    public SkyMap nowMap;
    private MapEnum lastMap;
    private int lastMapID;
    private final String password;
    public boolean black;

    public YouSelf(int permanent_light_wing, int wings_of_light, int id, String name,String password)
    {
        super(permanent_light_wing, wings_of_light, id, name);
        this.password=password;
        lastMap= MapEnum.home;
        lastMapID=0;
    }

    public YouSelf(String name,String password)
    {
        permanent_light_wing=0;
        wings_of_light=0;
        id=1;
        this.name=name;
        this.password=password;
        lastMap= MapEnum.home;
        lastMapID=0;
    }

    public MapEnum getLastMap()
    {
        return lastMap;
    }

    public void setLastMap(MapEnum lastMap)
    {
        this.lastMap = lastMap;
    }

    public int getLastMapID()
    {
        return lastMapID;
    }

    public void setLastMapID(int lastMapID)
    {
        this.lastMapID = lastMapID;
    }

    public void choose()
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n你现在在" + nowMap.mapInfo.get(nowMap.mapEnum.ordinal()).get(nowMap.mapID) + "！你可以：");
        if (lightUpPlayer.size() < nowMap.players.size() - 1)
        {
            System.out.println("点亮小黑 ---> 输入1");
        }
        if(!(lastMap == MapEnum.eden && lastMapID>=2 && lastMapID<=4))
            System.out.println("去其他地图 ---> 输入2");
        if (nowMap.mapEnum != MapEnum.home && (nowMap.mapEnum != MapEnum.eden || nowMap.mapID < 2))
        {
            System.out.println("回遇境 ---> 输入3");
        }
        if (nowMap.mapEnum == MapEnum.home && lastMap != null && lastMap != MapEnum.home)
        {
            System.out.println("坐神坛 ---> 输入4");
        }
        System.out.println("观察地图及玩家 ---> 输入8");
        System.out.println("查看个人信息 ---> 输入9");
        System.out.println("退出游戏 ---> 输入0");
        if(lastMap == MapEnum.eden && lastMapID>=2 && lastMapID<=4)
            System.out.println("提示：光明在呼唤，请返回伊甸字眼继续你的升华之旅");
        while (true)
        {
            switch (sc.nextInt())
            {
                case 1:
                    if (lightUpPlayer.size() == nowMap.players.size() - 1)
                    {
                        System.out.println("输入错误，请重新输入！");
                        break;
                    }
                    else
                    {
                        nowMap.lightUpOther(sc);
                        return;
                    }
                case 2:
                    if(lastMap == MapEnum.eden && lastMapID>=2 && lastMapID<=4){
                        System.out.println("提示：光明在呼唤，请返回伊甸字眼继续你的升华之旅");
                        continue;
                    }
                    nowMap.goToOtherMap(sc);
                    lastMap = nowMap.mapEnum;
                    lastMapID = nowMap.mapID;
                    save();
                    lightUpPlayer.clear();
                    if (nowMap.mapEnum != MapEnum.eden || nowMap.mapID < 2)
                        nowMap.refreshMap();
                    return;
                case 3:
                    if (nowMap.mapEnum == MapEnum.home || (nowMap.mapEnum == MapEnum.eden && nowMap.mapID > 1))
                    {
                        System.out.println("输入错误，请重新输入！");
                        break;
                    }
                    else
                    {
                        nowMap.mapEnum = MapEnum.home;
                        nowMap.mapID = 0;
                        System.out.println("你已返回遇境");
                        save();
                        nowMap.refreshMap();
                        return;
                    }
                case 4:
                    if (nowMap.mapEnum != MapEnum.home || lastMap == null || lastMap == MapEnum.home)
                    {
                        System.out.println("输入错误，请重新输入！");
                        break;
                    }
                    else
                    {
                        nowMap.mapEnum = lastMap;
                        nowMap.mapID = lastMapID;
                        System.out.println("你已回归" + nowMap.mapName[nowMap.mapEnum.ordinal()] + "的旅途");
                        save();
                        nowMap.refreshMap();
                        return;
                    }
                case 8:
                    nowMap.allPlayer();
                    return;
                case 9:
                    showInfo();
                    return;
                case 0:
                    System.out.println("你退出了光遇！");
                    save();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.exit(0);
                default:
                    System.out.println("输入错误，请重新输入！");
            }
        }
    }

    public void xianJi()
    {
        Sacrifice sa =new Sacrifice();
        YouEnergy ye =new YouEnergy(sa,this);
        Thread th1 =new Thread(sa,"Sacrifice");
        Thread th2 =new Thread(ye,"YouEnergy");
        System.out.println("你开始了献祭！");
        sa.inSafePlace=true;
        Scanner sc = new Scanner(System.in);
        th1.start();
        th2.start();
        while (wings_of_light>0)
        {
            System.out.println("你现在还剩"+wings_of_light+"个光翼，"+String.format("%.2f",nowEnergy)+"点能量,你现在可以：");
            if(sa.stage==1)
            {
                System.out.println("冲入红石雨点石像（1s） ---> 输入1");
                System.out.println("留在掩体等候（1s） ---> 输入2");
            }
            else
            {
                System.out.println("在红石雨中点石像（1s） ---> 输入1");
            }
            try
            {
                switch (sc.nextInt())
                {
                case 1 ->
                {
                    sa.inSafePlace = false;
                    if (sa.stage == 1)
                        System.out.println("你离开了掩体...");
                    if (sa.get == 33)
                    {
                        sa.stage = 2;
                        System.out.println("你进入了二阶段！");
                    }
                    Thread.sleep(1000);
                    if (sa.nextStatue())
                    {
                        wings_of_light--;
                        System.out.println("你点到了一个石像！现在还剩"+wings_of_light+"个光翼");
                        if(wings_of_light<0)
                        {
                            System.out.println("你没有光翼可以献祭了！");
                        }
                    }
                    else
                        System.out.println("已经没有石像可以点了！");
                }
                case 2 ->
                {
                    if (sa.stage == 2)
                    {
                        System.out.println("二阶段没有掩体可以躲避！");
                        break;
                    }
                    sa.inSafePlace = true;
                    System.out.println("你进入了掩体等待...");
                    Thread.sleep(1000);
                }
                default -> System.out.println("输入错误！什么都没发生！");
            }
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }
        ye.type=-1;
        try
        {
            Thread.sleep(100);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
//        th1.interrupt();
//        th2.interrupt();
        ye.type=-1;
        sa.stage=-1;
        double get = (double) sa.get / 4;
        System.out.println("你献祭了！获得了" + get + "根红蜡烛！");
        redCandle += get;
        wings_of_light = permanent_light_wing;
        System.out.println("拿完永久光翼后，你现在只剩下" + wings_of_light + "个光翼了");
        getMaxEnergy();
        start();
        nowMap.mapID = 4;
        save();
    }

    public void congSeng()
    {
        wings_of_light++;
        getMaxEnergy();
        start();
        System.out.println("你重生了！当前光翼数:" + wings_of_light + "光翼," + maxEnergy + "翼");
        nowMap.mapEnum = MapEnum.home;
        nowMap.mapID = 0;
        lastMap= MapEnum.home;
        lastMapID=0;
        save();
    }

    public void showInfo()
    {
        System.out.println("您当前账号的信息为：账号名称："+name);
        System.out.println("光翼数量："+wings_of_light+"，能量上限："+maxEnergy+"，当前能量："+nowEnergy);
        System.out.println("蜡烛数量："+candle+"爱心数量："+heart+"红蜡烛数量："+redCandle);
    }

    public String getInfo()
    {
        StringBuilder sb =new StringBuilder();
        sb.append(name).append(" ").append(permanent_light_wing).append(" ").append(wings_of_light).append(" ")
                .append(candle).append(" ").append(heart).append(" ").append(redCandle)
                .append(" ").append(lastMap.name()).append(" ").append(lastMapID).append(" ").append(password);
        return sb.toString();
    }

    public void save()
    {
        File directory = new File("SkyWorld\\");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try (PrintWriter osw = new PrintWriter(new File(directory, name+"data.txt")))
        {
            osw.print(getInfo());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public int lostLightWing()
    {
        if (wings_of_light < 50)
        {
            wings_of_light-=1;
            return 1;
        }
        else if (wings_of_light < 80)
        {
            wings_of_light-=2;
            return 2;
        }
        else if (wings_of_light < 100)
        {
            wings_of_light-=3;
            return 3;
        }
        else if (wings_of_light < 120)
        {
            wings_of_light-=4;
            return 4;
        }
        else
        {
            wings_of_light-=5;
            return 5;
        }
    }
}
