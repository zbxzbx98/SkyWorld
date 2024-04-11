package skyworld;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import skyworld.thread.Sacrifice;
import skyworld.thread.YouEnergy;
import skyworld.util.AudioPlayer;
import skyworld.util.WhileCandleRule;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class YouSelf extends SkyPlayer {
    public ArrayList<SkyPlayer> lightUpPlayer = new ArrayList<>();
    public SkyMap nowMap;
    public boolean black;
    private final String password;

    private MapEnum lastMap;
    private int lastMapID;

    private LocalDate today;
    private int todayGetCandleCount;
    private int todayGetFilePointCount;
    private double beforeGetCandleCount;
    private final WhileCandleRule whileCandleRule = new WhileCandleRule();

    private HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> mapData;

    public YouSelf(int permanent_light_wing, int now_permanent_light_wing, int wings_of_light, int id, String name, String password) {
        super(permanent_light_wing, now_permanent_light_wing, wings_of_light, id, name);
        this.password = password;
        lastMap = MapEnum.home;
        lastMapID = 0;
    }

    public YouSelf(String name, String password) {
        super(0, 0, 0, 1, name);
        this.password = password;
        lastMap = MapEnum.home;
        lastMapID = 0;
    }

    public MapEnum getLastMap() {
        return lastMap;
    }

    public void setLastMap(MapEnum lastMap) {
        this.lastMap = lastMap;
    }

    public int getLastMapID() {
        return lastMapID;
    }

    public void setLastMapID(int lastMapID) {
        this.lastMapID = lastMapID;
    }

    /**
     * 玩家选择
     */
    public void choose() {
        try {
            System.in.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("------------------------");
        System.out.println("你现在在" + nowMap.mapInfo.get(nowMap.mapEnum.ordinal()).get(nowMap.mapID).get(0) + "！你可以：");
        if (lightUpPlayer.size() < nowMap.players.size() - 1) {
            System.out.println("点亮小黑 ---> 输入1");
        }
        if (!(nowMap.mapEnum == MapEnum.home && lastMap == MapEnum.eden && lastMapID >= 2 && lastMapID <= 4))
            System.out.println("去其他地图 ---> 输入2");
        if (nowMap.mapEnum != MapEnum.home && (nowMap.mapEnum != MapEnum.eden || nowMap.mapID < 2)) {
            System.out.println("回遇境 ---> 输入3");
        }
        if (nowMap.mapEnum == MapEnum.home && lastMap != null && lastMap != MapEnum.home) {
            System.out.println("坐神坛 ---> 输入4");
        }
        if (nowMapFilePointInfo() > 0) {
            System.out.println("收集地图中的烛火 ---> 输入5");
        }
        if (nowMapLightWingInfo() > 0) {
            System.out.println("收集地图中的光翼 ---> 输入6");
        }
        System.out.println("合成蜡烛 ---> 输入7");
        System.out.println("观察地图及玩家 ---> 输入8");
        System.out.println("查看个人信息 ---> 输入9");
        System.out.println("退出游戏 ---> 输入0");
        if (nowMap.mapEnum == MapEnum.home && lastMap == MapEnum.eden && lastMapID >= 2 && lastMapID <= 4)
            System.out.println("提示：光明在呼唤，请返回伊甸之眼继续你的升华之旅");
        System.out.println("------------------------");
        while (true) {
            switch (sc.nextInt()) {
                case 1 -> {
                    if (lightUpPlayer.size() == nowMap.players.size() - 1) {
                        System.out.println("输入错误，请重新输入！");
                    } else {
                        nowMap.lightUpOther(sc);
                        return;
                    }
                }
                case 2 -> {
                    if (nowMap.mapEnum == MapEnum.home && lastMap == MapEnum.eden && lastMapID >= 2 && lastMapID <= 4) {
                        System.out.println("提示：光明在呼唤，请返回伊甸之眼继续你的升华之旅");
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
                }
                case 3 -> {
                    if (nowMap.mapEnum == MapEnum.home || (nowMap.mapEnum == MapEnum.eden && nowMap.mapID > 1)) {
                        System.out.println("输入错误，请重新输入！");
                    } else {
                        nowMap.mapEnum = MapEnum.home;
                        nowMap.mapID = 0;
                        System.out.println("你已返回遇境");
                        save();
                        nowMap.refreshMap();
                        return;
                    }
                }
                case 4 -> {
                    if (nowMap.mapEnum != MapEnum.home || lastMap == null || lastMap == MapEnum.home) {
                        System.out.println("输入错误，请重新输入！");
                    } else {
                        nowMap.mapEnum = lastMap;
                        nowMap.mapID = lastMapID;
                        System.out.println("你已回归" + nowMap.mapName[nowMap.mapEnum.ordinal()] + "的旅途");
                        save();
                        nowMap.refreshMap();
                        return;
                    }
                }
                case 5 -> {
                    getNowMapFilePoint();
                    return;
                }
                case 6 -> {
                    getNowMapLightWing();
                    return;
                }
                case 7 -> {
                    syntheticCandle();
                    return;
                }
                case 8 -> {
                    nowMap.allPlayer();
                    return;
                }
                case 9 -> {
                    showInfo();
                    return;
                }
                case 0 -> {
                    System.out.println("感谢游玩 光遇极简版--by zbxzbx98~");
                    System.out.println("你退出了光遇！");
                    save();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.exit(0);
                }
                default -> System.out.println("输入错误，请重新输入！");
            }
        }
    }

    /**
     * 开始献祭
     */
    public void xianJi() {
        Sacrifice sa = new Sacrifice();
        YouEnergy ye = new YouEnergy(sa, this);
        Thread th1 = new Thread(sa, "Sacrifice");
        Thread th2 = new Thread(ye, "YouEnergy");
        System.out.println("你开始了献祭！");
        sa.inSafePlace = true;
        Scanner sc = new Scanner(System.in);
        th1.start();
        th2.start();
        while (lightWingInfo() > 0) {
            System.out.println("你现在还剩" + lightWingInfo() + "个光翼，" + String.format("%.2f", nowEnergy) + "点能量,你现在可以：");
            if (sa.stage == 1) {
                System.out.println("冲入红石雨点石像（1s） ---> 输入1");
                System.out.println("留在掩体等候（1s） ---> 输入2");
            } else {
                System.out.println("在红石雨中点石像（1s） ---> 输入1");
            }
            try {
                switch (sc.nextInt()) {
                    case 1 -> {
                        sa.inSafePlace = false;
                        if (sa.stage == 1)
                            System.out.println("你离开了掩体...");
                        if (sa.get == 33) {
                            sa.stage = 2;
                            System.out.println("你进入了二阶段！");
                        }
                        Thread.sleep(1000);
                        if (sa.nextStatue()) {
                            useLightWing();
                            System.out.println("你点到了一个石像！现在还剩" + lightWingInfo() + "个光翼");
                            if (lightWingInfo() <= 0) {
                                System.out.println("你没有光翼可以献祭了！");
                            }
                        } else
                            System.out.println("已经没有石像可以点了！");
                    }
                    case 2 -> {
                        if (sa.stage == 2) {
                            System.out.println("二阶段没有掩体可以躲避！");
                            break;
                        }
                        sa.inSafePlace = true;
                        System.out.println("你进入了掩体等待...");
                        Thread.sleep(1000);
                    }
                    default -> System.out.println("输入错误！什么都没发生！");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        ye.type = -1;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ye.type = -1;
        sa.stage = -1;
        double get = (double) sa.get / 4;
        System.out.println("你献祭了！获得了" + get + "根红蜡烛！");
        redCandle += get;
        now_permanent_light_wing = permanent_light_wing;
        System.out.println("拿完永久光翼后，你现在只剩下" + lightWingInfo() + "个光翼了");
        getMaxEnergy();
        start();
        nowMap.mapID = 4;
        save();
    }

    /**
     * 重生
     */
    public void congSeng() {
        mapData.get(7).get(4).set(0, 1);
        nowMap.mapEnum = MapEnum.home;
        nowMap.mapID = 0;
        lastMap = MapEnum.home;
        lastMapID = 0;
        now_permanent_light_wing = permanent_light_wing;
        save();
        getMaxEnergy();
        start();
        System.out.println("你重生了！当前光翼数:" + lightWingInfo() + "光翼," + maxEnergy + "翼");
    }

    /**
     * 显示信息
     */
    public void showInfo() {
        System.out.println("您当前账号的信息为：账号名称：" + name);
        System.out.println("光翼数量：" + wings_of_light + "，能量上限：" + maxEnergy + "，当前能量：" + nowEnergy);
        System.out.println("蜡烛数量：" + candle + "爱心数量：" + heart + "红蜡烛数量：" + redCandle);
    }

    /**
     * 获取信息
     *
     * @return 玩家信息
     */
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" ").append(permanent_light_wing).append(" ").append(now_permanent_light_wing).append(" ")
                .append(wings_of_light).append(" ").append(candle).append(" ").append(heart).append(" ").append(redCandle)
                .append(" ").append(lastMap.name()).append(" ").append(lastMapID).append(" ").append(password);
        return sb.toString();
    }

    /**
     * 读取玩家蜡烛信息
     */
    public void lodeCandleData() {
        File file = new File("SkyWorld\\" + name + "CandleData.txt");
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line = br.readLine();
                String[] data = line.split(" ");
                today = LocalDate.now();
                if (!data[0].equals(today.toString())) {
                    todayGetCandleCount = Integer.parseInt(data[1]);
                    todayGetFilePointCount = Integer.parseInt(data[2]);
                    beforeGetCandleCount = Double.parseDouble(data[3]);
                    int get = whileCandleRule.calculateCandle(todayGetCandleCount, todayGetFilePointCount);
                    int surplus = whileCandleRule.calculateSurplus(todayGetCandleCount, todayGetFilePointCount);
                    double sumGet = (double) get + whileCandleRule.calculateNextNeedCandlelightProgress(todayGetCandleCount + get, surplus);
                    beforeGetCandleCount += sumGet;
                    todayGetFilePointCount = 0;
                    todayGetCandleCount = 0;
                    clearMapCandleData();
                    saveMapData();
                    saveCandleData();
                } else {
                    todayGetCandleCount = Integer.parseInt(data[1]);
                    todayGetFilePointCount = Integer.parseInt(data[2]);
                    beforeGetCandleCount = Double.parseDouble(data[3]);
                }
            } catch (IOException e) {
                System.err.println("读取玩家蜡烛信息失败:" + e.getMessage());
            }
        } else {
            beforeGetCandleCount = 0;
            todayGetFilePointCount = 0;
            todayGetCandleCount = 0;
            today = LocalDate.now();
            saveCandleData();
        }

    }

    /**
     * 获取当前地图烛火数
     */
    public int nowMapFilePointInfo() {
        return Integer.parseInt(nowMap.mapInfo.get(nowMap.mapEnum.ordinal()).get(nowMap.mapID).get(2));
    }

    /**
     * 收集当前地图烛火
     */
    public void getNowMapFilePoint() {
        int nowMapFilePoint = nowMapFilePointInfo();
        if (nowMapFilePoint <= 0) {
            System.out.println("当前地图没有烛火点!");
            return;
        }
        if (mapData.get(nowMap.mapEnum.ordinal()).get(nowMap.mapID).get(1) < nowMapFilePoint) {
            System.out.println("正在收集当前地图烛火...（3s）");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            todayGetFilePointCount += nowMapFilePoint;
            mapData.get(nowMap.mapEnum.ordinal()).get(nowMap.mapID).set(1, nowMapFilePoint);
            saveCandleData();
            saveMapData();
            System.out.println("你收集了" + nowMapFilePoint + "个烛火点！");
        } else {
            System.out.println("你今天已经拿过当前地图的烛火了！");
        }
    }

    /**
     * 合成蜡烛
     */
    public void syntheticCandle() {
        int get = whileCandleRule.calculateCandle(todayGetCandleCount, todayGetFilePointCount);
        int surplus = whileCandleRule.calculateSurplus(todayGetCandleCount, todayGetFilePointCount);
        double addGetProgress = whileCandleRule.calculateNextNeedCandlelightProgress(todayGetCandleCount + get, surplus);
        beforeGetCandleCount += get;
        todayGetFilePointCount = surplus;
        todayGetCandleCount += get;
        if (beforeGetCandleCount + addGetProgress < 1) {
            System.out.println("你拥有的烛火点不足以合成下一个蜡烛!");
        } else {
            get = (int) (beforeGetCandleCount + addGetProgress);
            beforeGetCandleCount = beforeGetCandleCount - get + addGetProgress;
            candle += get;
            System.out.println("你合成了" + get + "根蜡烛!");
            saveCandleData();
        }
    }

    /**
     * 保存玩家蜡烛信息
     */
    public void saveCandleData() {
        File directory = new File("SkyWorld\\");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try (PrintWriter osw = new PrintWriter(new File(directory, name + "CandleData.txt"))) {
            osw.print(today.toString() + " " + todayGetCandleCount + " " + todayGetFilePointCount + " " + beforeGetCandleCount);
        } catch (IOException e) {
            System.err.println("保存玩家蜡烛信息失败:" + e.getMessage());
        }
    }

    /**
     * 读取玩家地图数据
     */
    public void lodeMapData() {
        File file = new File("SkyWorld\\" + name + "MapData.txt");
        if (file.exists()) {
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType intType = objectMapper.getTypeFactory().constructType(Integer.class);
            MapType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, intType,
                    objectMapper.getTypeFactory().constructMapType(HashMap.class, intType,
                            objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Integer.class)));
            try {
                mapData = objectMapper.readValue(file, mapType);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            creatMapData();
        }
    }

    /**
     * 创建地图数据
     */
    public void creatMapData() {
        ObjectMapper objectMapper = new ObjectMapper();
        URL jsonFile = SkyMap.class.getResource("/baseMapData.json");
        JavaType intType = objectMapper.getTypeFactory().constructType(Integer.class);
        MapType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, intType,
                objectMapper.getTypeFactory().constructMapType(HashMap.class, intType,
                        objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Integer.class)));
        try {
            mapData = objectMapper.readValue(jsonFile, mapType);
            saveMapData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取当前地图的光翼信息
     *
     * @return 光翼数
     */
    public int nowMapLightWingInfo() {
        return Integer.parseInt(nowMap.mapInfo.get(nowMap.mapEnum.ordinal()).get(nowMap.mapID).get(1));
    }

    /**
     * 收集当前地图的光翼
     */
    public void getNowMapLightWing() {
        int nowMapLightWing = nowMapLightWingInfo();
        if (nowMapLightWing <= 0) {
            System.out.println("当前地图没有可收集的光翼！");
            return;
        }
        if (mapData.get(nowMap.mapEnum.ordinal()).get(nowMap.mapID).get(0) < nowMapLightWing) {
            System.out.println("正在收集当前地图光翼...");
            for (int i = 0; i < nowMapLightWing; i++) {
                try {
                    AudioPlayer.startPlay("/Audios/get.mp3");
                    Thread.sleep(6000);
                    if (nowMapLightWing - i - 1 > 0)
                        System.out.println("收集了1个光翼，当前地图还剩" + (nowMapLightWing - i - 1) + "个光翼...");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            mapData.get(nowMap.mapEnum.ordinal()).get(nowMap.mapID).set(0, nowMapLightWing);
            save();
            saveMapData();
            System.out.println("收集完成！你一共收集了" + nowMapLightWing + "个光翼！");
        } else {
            System.out.println("你已经拿过当前地图的光翼了！");
        }

    }

    /**
     * 保存地图数据
     */
    public void saveMapData() {
        File directory = new File("SkyWorld\\");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try (PrintWriter osw = new PrintWriter(new File(directory, name + "MapData.txt"))) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(osw, mapData);
        } catch (IOException e) {
            System.err.println("保存地图数据失败:" + e.getMessage());
        }
    }

    /**
     * 获取地图中已收集的光翼总数
     *
     * @return 已收集的光翼总数
     */
    public int sumMapDataLightWing() {
        int sum = 0;
        for (HashMap<Integer, ArrayList<Integer>> innerMap : mapData.values()) {
            for (ArrayList<Integer> list : innerMap.values()) {
                if (!list.isEmpty()) { // 确保列表非空，避免抛出 IndexOutOfBoundsException
                    sum += list.get(0);
                }
            }
        }
        return sum;
    }

    /**
     * 清空地图中已收集的蜡烛数据
     */
    public void clearMapCandleData() {
        for (HashMap<Integer, ArrayList<Integer>> innerMap : mapData.values()) {
            for (ArrayList<Integer> list : innerMap.values()) {
                if (list.size() >= 2) { // 确保列表长度至少为 2，避免抛出 IndexOutOfBoundsException
                    list.set(1, 0); // 设置第二个元素为 0
                }
            }
        }
    }

    /**
     * 保存玩家信息
     */
    public void save() {
        wings_of_light = sumMapDataLightWing();
        File directory = new File("SkyWorld\\");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try (PrintWriter osw = new PrintWriter(new File(directory, name + "data.txt"))) {
            osw.print(getInfo());
        } catch (IOException e) {
            System.err.println("保存玩家信息失败:" + e.getMessage());
        }
    }

    /**
     * 加载玩家信息
     *
     * @param name 玩家名
     * @return 玩家信息
     */
    public static String load(String name) {
        try (BufferedReader isr = new BufferedReader(new FileReader("SkyWorld\\" + name + "data.txt"))) {
            return isr.readLine();
        } catch (IOException e) {
            System.err.println("读取文件失败:" + e.getMessage());
        }
        return "";
    }

    /**
     * 使用光翼
     */
    public synchronized void useLightWing() {
        if (wings_of_light > 0) {
            wings_of_light--;
            lostNomel(1);
        } else if (permanent_light_wing > 0) {
            now_permanent_light_wing--;
        }
    }

    /**
     * 掉光翼
     * @return 掉光翼的数量
     */
    public synchronized int lostLightWing() {
        int lost = 0;
        int sum = lightWingInfo();
        if (wings_of_light > 0) {
            if (sum < 50) {
                wings_of_light -= 1;
                lost = 1;
            } else if (sum < 80) {
                wings_of_light -= 2;
                lost = 2;
            } else if (sum < 100) {
                wings_of_light -= 3;
                lost = 3;
            } else if (sum < 120) {
                wings_of_light -= 4;
                lost = 4;
            } else {
                wings_of_light -= 5;
                lost = 5;
            }
            if (wings_of_light < 0) {
                now_permanent_light_wing += wings_of_light;
                lostNomel(lost + wings_of_light);
                wings_of_light = 0;
            }
            if (now_permanent_light_wing < 0)
                now_permanent_light_wing = 0;
        }
        AudioPlayer.playLost(lost);
        return lost;
    }

    /**
     * 扣除普通翼
     *
     * @param i 扣普通光翼的数量
     */
    private void lostNomel(int i) {
        if (i <= 0)
            return;
        for (HashMap<Integer, ArrayList<Integer>> positionMapEntry : mapData.values()) {
            for (ArrayList<Integer> positionEntry : positionMapEntry.values()) {
                if (positionEntry.get(0) > 0) {
                    if (positionEntry.get(0) - i >= 0) {
                        positionEntry.set(0, positionEntry.get(0) - i);
                        return;
                    }
                    if (positionEntry.get(0) - i < 0) {
                        i -= positionEntry.get(0);
                        positionEntry.set(0, 0);
                    }
                }
            }
        }
        if (i > 0)
            System.err.println("普通翼扣除异常");
    }
}
