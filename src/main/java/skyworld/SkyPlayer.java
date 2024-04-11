package skyworld;

import java.util.Random;

public class SkyPlayer {
    public int permanent_light_wing;
    public int now_permanent_light_wing;
    public int wings_of_light;
    public int maxEnergy;
    public double nowEnergy;
    public double candle;
    public double heart;
    public double redCandle;
    public int id;
    public String name;

    public SkyPlayer() {
        Random r = new Random();
        this.permanent_light_wing = r.nextInt(91);
        this.now_permanent_light_wing = r.nextInt(permanent_light_wing);
        this.wings_of_light = r.nextInt(now_permanent_light_wing + r.nextInt(118));
        this.id = r.nextInt(500000000);
        this.name = String.valueOf(r.nextInt(100));
        getMaxEnergy();
        start();
    }

//    public SkyPlayer(boolean b){}

    public SkyPlayer(int permanent_light_wing, int now_permanent_light_wing, int wings_of_light, int id, String name) {
        this.permanent_light_wing = permanent_light_wing;
        this.now_permanent_light_wing = now_permanent_light_wing;
        this.wings_of_light = wings_of_light;
        this.id = id;
        this.name = name;
        getMaxEnergy();
        start();
    }

    /**
     * 重置能量
     */
    public void start() {
        nowEnergy = maxEnergy;
    }

    /**
     * 获取最大能量
     */
    public void getMaxEnergy() {
        int lightWingInfo = lightWingInfo();
        if (lightWingInfo == 0) {
            maxEnergy = 0;
        } else if (lightWingInfo == 1) {
            maxEnergy = 1;
        } else if (lightWingInfo < 5) {
            maxEnergy = 2;
        } else if (lightWingInfo < 10) {
            maxEnergy = 3;
        } else if (lightWingInfo < 20) {
            maxEnergy = 4;
        } else if (lightWingInfo < 35) {
            maxEnergy = 5;
        } else if (lightWingInfo < 55) {
            maxEnergy = 6;
        } else if (lightWingInfo < 75) {
            maxEnergy = 7;
        } else if (lightWingInfo < 100) {
            maxEnergy = 8;
        } else if (lightWingInfo < 120) {
            maxEnergy = 9;
        } else if (lightWingInfo < 150) {
            maxEnergy = 10;
        } else {
            maxEnergy = 11;
        }
    }

    /**
     * 获取光翼信息
     * @return 总光翼数
     */
    public synchronized int lightWingInfo(){
        return now_permanent_light_wing + wings_of_light;
    }

    /**
     * 飞行
     */
    public void fly() {
        if (nowEnergy > 0) {
            nowEnergy--;
            System.out.println(name + " 飞了一下！");
        } else {
            System.out.println(name + " 没有能量，无法飞行！");
        }
    }

    @Override
    public String toString() {
        return name + "{" +
                "永久光翼数=" + permanent_light_wing +
                ", 当前光翼数=" + lightWingInfo() +
                ", id=" + id +
                '}';
    }
}
