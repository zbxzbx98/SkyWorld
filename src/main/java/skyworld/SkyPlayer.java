package skyworld;

import java.util.Random;

public class SkyPlayer {
    public int permanent_light_wing;
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
        this.wings_of_light = r.nextInt(this.permanent_light_wing + 1) + r.nextInt(107);
        this.id = r.nextInt(500000000);
        this.name = String.valueOf(r.nextInt(100));
        getMaxEnergy();
        start();
    }

//    public SkyPlayer(boolean b){}

    public SkyPlayer(int permanent_light_wing, int wings_of_light, int id, String name) {
        this.permanent_light_wing = permanent_light_wing;
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
        if (wings_of_light == 0) {
            maxEnergy = 0;
        } else if (wings_of_light == 1) {
            maxEnergy = 1;
        } else if (wings_of_light < 5) {
            maxEnergy = 2;
        } else if (wings_of_light < 10) {
            maxEnergy = 3;
        } else if (wings_of_light < 20) {
            maxEnergy = 4;
        } else if (wings_of_light < 35) {
            maxEnergy = 5;
        } else if (wings_of_light < 55) {
            maxEnergy = 6;
        } else if (wings_of_light < 75) {
            maxEnergy = 7;
        } else if (wings_of_light < 100) {
            maxEnergy = 8;
        } else if (wings_of_light < 120) {
            maxEnergy = 9;
        } else if (wings_of_light < 150) {
            maxEnergy = 10;
        } else {
            maxEnergy = 11;
        }
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
                ", 当前光翼数=" + wings_of_light +
                ", id=" + id +
                '}';
    }
}
