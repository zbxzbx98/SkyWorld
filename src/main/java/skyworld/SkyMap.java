package skyworld;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;

import javax.swing.*;

public class SkyMap {
    public MapEnum mapEnum;
    public YouSelf you;
    public final String[] mapName = {"遇境", "晨岛", "云野", "雨林", "霞谷", "墓土", "禁阁", "暴风眼"};
    public final Map<Integer, Map<Integer, String>> mapInfo;

    public final Map<MapEnum, ImageIcon> mapIcon;
    public int mapID;
    public ArrayList<SkyPlayer> players = new ArrayList<>();

    public SkyMap() {
        ObjectMapper objectMapper = new ObjectMapper();
        URL jsonFile = SkyMap.class.getResource("/locations.json");
        MapType mapType = objectMapper.getTypeFactory().constructMapType(Map.class, Integer.class, Map.class);

        Map<Integer, Map<String, String>> rawMapInfo;
        try {
            rawMapInfo = objectMapper.readValue(jsonFile, mapType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mapInfo = new HashMap<>();
        for (Map.Entry<Integer, Map<String, String>> entry : rawMapInfo.entrySet()) {
            Map<String, String> map = entry.getValue();
            Map<Integer, String> newMap = new HashMap<>();
            for (Map.Entry<String, String> entry1 : map.entrySet()) {
                newMap.put(Integer.parseInt(entry1.getKey()), entry1.getValue());
            }
            mapInfo.put(entry.getKey(), newMap);
        }
        mapIcon = new HashMap<>();
        for (int i = 0; i < MapEnum.values().length; i++) {
            URL url = SkyApp.class.getResource("/Images/" + MapEnum.values()[i].name() + ".jpg");
            if (url != null)
                mapIcon.put(MapEnum.values()[i], new ImageIcon(url));
            else
                mapIcon.put(MapEnum.values()[i], new ImageIcon(SkyApp.class.getResource("/Images/home.jpg")));
        }
    }

    /**
     * 添加玩家
     *
     * @param newPlayer 新玩家
     */
    public void addPlayer(SkyPlayer newPlayer) {
        if (players.size() == 8) {
            System.out.println("地图内玩家已满 !");
        } else {
            for (SkyPlayer player : players) {
                if (player.id == newPlayer.id) {
                    System.out.println("玩家重复!");
                    return;
                }
            }
            players.add(newPlayer);
            System.out.println(newPlayer.name + " 进入了地图");
        }
    }

    public void outPlayer(SkyPlayer thePlayer) {
        if (players.isEmpty()) {
            System.out.println("地图内无玩家！");
        } else {
            if (players.remove(thePlayer)) {
                System.out.println(thePlayer.name + " 离开了地图");
            } else {
                System.out.println(thePlayer.name + " 不在地图内！");
            }
        }
    }

    /**
     * 点亮小黑
     *
     * @param sc 输入
     */
    public void lightUpOther(Scanner sc) {
        System.out.println("点亮小黑操作：");
        System.out.println("随机点亮一个小黑 ---> 输入1");
        System.out.println("点亮全部小黑 ---> 输入2");
        while (true) {
            switch (sc.nextInt()) {
                case 1 -> {
                    ArrayList<SkyPlayer> not = new ArrayList<>();
                    for (SkyPlayer pla : players) {
                        if (!you.lightUpPlayer.contains(pla) && you != pla) {
                            not.add(pla);
                        }
                    }
                    if (not.size() > 1) {
                        Random r = new Random();
                        int i = r.nextInt(not.size());
                        you.lightUpPlayer.add(not.get(i));
                        System.out.println("你点亮了 " + not.get(i).name);
                    } else {
                        you.lightUpPlayer.add(not.get(0));
                        System.out.println("你点亮了 " + not.get(0).name);
                    }
                    return;
                }
                case 2 -> {
                    for (SkyPlayer pla : players) {
                        if (!you.lightUpPlayer.contains(pla) && you != pla) {
                            you.lightUpPlayer.add(pla);
                            System.out.println("你点亮了 " + pla.name);
                        }
                    }
                    return;
                }
                default -> System.out.println("输入错误，请重新输入！");
            }
        }

    }

    /**
     * 移动到其他地图
     *
     * @param sc 输入
     */
    public void goToOtherMap(Scanner sc) {
        System.out.println("请选择你要去的地图：");
        switch (mapEnum) {
            case home -> {
                if (mapID == 0) {
                    System.out.println("云巢 ---> 输入0");
                } else if (mapID == 1) {
                    System.out.println("遇境 ---> 输入0");
                }
                System.out.println("晨岛 ---> 输入1");
                System.out.println("云野 ---> 输入2");
                System.out.println("雨林 ---> 输入3");
                System.out.println("霞谷 ---> 输入4");
                System.out.println("墓土 ---> 输入5");
                System.out.println("禁阁 ---> 输入6");
                System.out.println("暴风眼 ---> 输入7");
                if (mapID == 1) {
                    System.out.println("风行网道 ---> 输入8");
                }
                while (true) {
                    switch (sc.nextInt()) {
                        case 0 -> {
                            if (mapID == 0) {
                                mapID = 1;
                                System.out.println("你已进入云巢");
                            } else if (mapID == 1) {
                                mapID = 0;
                                System.out.println("你已进入遇境");
                            }
                            return;
                        }
                        case 1 -> {
                            mapEnum = MapEnum.island;
                            System.out.println("你已进入" + mapName[mapEnum.ordinal()]);
                            return;
                        }
                        case 2 -> {
                            mapEnum = MapEnum.plain;
                            System.out.println("你已进入" + mapName[mapEnum.ordinal()]);
                            return;
                        }
                        case 3 -> {
                            mapEnum = MapEnum.forests;
                            System.out.println("你已进入" + mapName[mapEnum.ordinal()]);
                            return;
                        }
                        case 4 -> {
                            mapEnum = MapEnum.race;
                            System.out.println("你已进入" + mapName[mapEnum.ordinal()]);
                            return;
                        }
                        case 5 -> {
                            mapEnum = MapEnum.wasteland;
                            System.out.println("你已进入" + mapName[mapEnum.ordinal()]);
                            return;
                        }
                        case 6 -> {
                            mapEnum = MapEnum.court;
                            System.out.println("你已进入" + mapName[mapEnum.ordinal()]);
                            return;
                        }
                        case 7 -> {
                            mapEnum = MapEnum.eden;
                            System.out.println("你已进入" + mapName[mapEnum.ordinal()]);
                            return;
                        }
                        case 8 -> {
                            if (mapID != 1) {
                                System.out.println("输入错误，请重新输入！");
                                continue;
                            }
                            mapEnum = MapEnum.forests;
                            mapID = 8;
                            System.out.println("你已进入风行网道");
                            return;
                        }
                        default -> System.out.println("输入错误，请重新输入！");
                    }
                }
            }
            case island -> {
                switch (mapID) {
                    case 0 -> {
                        System.out.println("终点神庙 ---> 输入1");
                        System.out.println("预言山谷 ---> 输入2");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入晨岛神庙");
                                    mapID = 9;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入预言山谷");
                                    mapID = 1;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 1 -> {
                        System.out.println("水试炼 ---> 输入1");
                        System.out.println("土试炼 ---> 输入2");
                        System.out.println("风试炼 ---> 输入3");
                        System.out.println("火试炼 ---> 输入4");
                        System.out.println("风行网道 ---> 输入5");
                        System.out.println("晨岛荒漠 ---> 输入6");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入水试炼");
                                    mapID = 2;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入土试炼");
                                    mapID = 3;
                                    return;
                                }
                                case 3 -> {
                                    System.out.println("你已进入风试炼");
                                    mapID = 4;
                                    return;
                                }
                                case 4 -> {
                                    System.out.println("你已进入火试炼");
                                    mapID = 5;
                                    return;
                                }
                                case 5 -> {
                                    System.out.println("你已进入风行网道");
                                    mapEnum = MapEnum.forests;
                                    mapID = 8;
                                    return;
                                }
                                case 6 -> {
                                    System.out.println("你已进入晨岛荒漠");
                                    mapID = 0;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 2, 3, 4, 5 -> {
                        System.out.println("预言山谷 ---> 输入1");
                        while (true) {
                            if (sc.nextInt() == 1) {
                                System.out.println("你已进入预言山谷");
                                mapID = 1;
                                return;
                            } else {
                                System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 9 -> {
                        System.out.println("云野 ---> 输入1");
                        System.out.println("晨岛荒漠 ---> 输入2");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入云野");
                                    mapEnum = MapEnum.plain;
                                    mapID = 0;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入晨岛荒漠");
                                    mapID = 0;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                }
            }
            case plain -> {
                switch (mapID) {
                    case 0 -> {
                        System.out.println("蝴蝶平原 ---> 输入1");
                        System.out.println("晨岛神庙 ---> 输入2");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入蝴蝶平原");
                                    mapID = 1;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入晨岛神庙");
                                    mapEnum = MapEnum.island;
                                    mapID = 9;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 1 -> {
                        System.out.println("三塔图 ---> 输入1");
                        System.out.println("幽光山洞 ---> 输入2");
                        System.out.println("浮岛图 ---> 输入3");
                        System.out.println("云野大厅 ---> 输入4");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入三塔图");
                                    mapID = 2;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入幽光山洞");
                                    mapID = 3;
                                    return;
                                }
                                case 3 -> {
                                    System.out.println("你已进入浮岛图");
                                    mapID = 4;
                                    return;
                                }
                                case 4 -> {
                                    System.out.println("你已进入云野大厅");
                                    mapID = 0;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 2 -> {
                        System.out.println("终点神庙 ---> 输入1");
                        System.out.println("幽光山洞 ---> 输入2");
                        System.out.println("浮岛图 ---> 输入3");
                        System.out.println("八人门 ---> 输入4");
                        System.out.println("蝴蝶平原 ---> 输入5");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入云野神庙");
                                    mapID = 9;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入幽光山洞");
                                    mapID = 3;
                                    return;
                                }
                                case 3 -> {
                                    System.out.println("你已进入浮岛图");
                                    mapID = 4;
                                    return;
                                }
                                case 4 -> {
                                    System.out.println("你已进入八人门");
                                    mapID = 5;
                                    return;
                                }
                                case 5 -> {
                                    System.out.println("你已进入蝴蝶平原");
                                    mapID = 1;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 3 -> {
                        System.out.println("三塔图 ---> 输入1");
                        System.out.println("蝴蝶平原 ---> 输入2");
                        System.out.println("云峰 ---> 输入3");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入三塔图");
                                    mapID = 2;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入蝴蝶平原");
                                    mapID = 1;
                                    return;
                                }
                                case 3 -> {
                                    System.out.println("你已进入云峰");
                                    mapID = 7;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 4 -> {
                        System.out.println("三塔图 ---> 输入1");
                        System.out.println("圣岛 ---> 输入2");
                        System.out.println("蝴蝶平原 ---> 输入3");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入三塔图");
                                    mapID = 2;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入圣岛");
                                    mapID = 6;
                                    return;
                                }
                                case 3 -> {
                                    System.out.println("你已进入蝴蝶平原");
                                    mapID = 1;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 5 -> {
                        System.out.println("三塔图 ---> 输入1");
                        while (true) {
                            if (sc.nextInt() == 1) {
                                System.out.println("你已进入三塔图");
                                mapID = 2;
                                return;
                            } else {
                                System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 6 -> {
                        System.out.println("浮岛图 ---> 输入1");
                        System.out.println("风行网道 ---> 输入2");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入浮岛图");
                                    mapID = 4;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入风行网道");
                                    mapEnum = MapEnum.forests;
                                    mapID = 8;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 7 -> {
                        System.out.println("幽光山洞 ---> 输入1");
                        while (true) {
                            if (sc.nextInt() == 1) {
                                System.out.println("你已进入幽光山洞");
                                mapID = 3;
                                return;
                            } else {
                                System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 9 -> {
                        System.out.println("雨林 ---> 输入1");
                        System.out.println("三塔图 ---> 输入2");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入雨林");
                                    mapEnum = MapEnum.forests;
                                    mapID = 0;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入三塔图");
                                    mapID = 2;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }

                }
            }
            case forests -> {
                switch (mapID) {
                    case 0 -> {
                        System.out.println("荧光森林 ---> 输入1");
                        System.out.println("大树屋 ---> 输入2");
                        System.out.println("风行网道 ---> 输入3");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入荧光森林");
                                    mapID = 1;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入大树屋");
                                    mapID = 7;
                                    return;
                                }
                                case 3 -> {
                                    System.out.println("你已进入风行网道");
                                    mapID = 8;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 1 -> {
                        System.out.println("水母图 ---> 输入1");
                        System.out.println("秘密花园 ---> 输入2");
                        System.out.println("静谧亭院 ---> 输入3");
                        System.out.println("大树屋 ---> 输入4");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入水母图");
                                    mapID = 2;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入秘密花园");
                                    mapID = 3;
                                    return;
                                }
                                case 3 -> {
                                    System.out.println("你已进入静谧亭院");
                                    mapID = 0;
                                    return;
                                }
                                case 4 -> {
                                    System.out.println("你已进入大树屋");
                                    mapID = 7;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 2 -> {
                        System.out.println("雨林神庙 ---> 输入1");
                        System.out.println("秘密花园 ---> 输入2");
                        System.out.println("大树屋 ---> 输入3");
                        System.out.println("荧光森林 ---> 输入4");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入雨林神庙");
                                    mapID = 9;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入秘密花园");
                                    mapID = 3;
                                    return;
                                }
                                case 3 -> {
                                    System.out.println("你已进入大树屋");
                                    mapID = 7;
                                    return;
                                }
                                case 4 -> {
                                    System.out.println("你已进入荧光森林");
                                    mapID = 1;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 3 -> {
                        System.out.println("雨林隐藏图 ---> 输入1");
                        System.out.println("荧光森林 ---> 输入2");
                        System.out.println("水母图 ---> 输入3");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入雨林隐藏图");
                                    mapID = 4;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入荧光森林");
                                    mapID = 1;
                                    return;
                                }
                                case 3 -> {
                                    System.out.println("你已进入水母图");
                                    mapID = 3;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 4 -> {
                        System.out.println("秘密花园 ---> 输入1");
                        System.out.println("风行网道 ---> 输入2");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入秘密花园");
                                    mapID = 3;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入风行网道");
                                    mapID = 8;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 7 -> {
                        System.out.println("静谧亭院 ---> 输入1");
                        System.out.println("荧光森林 ---> 输入2");
                        System.out.println("水母图 ---> 输入3");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入静谧亭院");
                                    mapID = 0;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入荧光森林");
                                    mapID = 1;
                                    return;
                                }
                                case 3 -> {
                                    System.out.println("你已进入水母图");
                                    mapID = 2;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 8 -> {
                        System.out.println("预言山谷 ---> 输入1");
                        System.out.println("圣岛 ---> 输入2");
                        System.out.println("星光沙漠 ---> 输入3");
                        System.out.println("失落方舟 ---> 输入4");
                        System.out.println("雪隐峰 ---> 输入5");
                        System.out.println("雨林隐藏图 ---> 输入6");
                        System.out.println("静谧亭院 ---> 输入7");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入预言山谷");
                                    mapEnum = MapEnum.island;
                                    mapID = 1;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入圣岛");
                                    mapEnum = MapEnum.plain;
                                    mapID = 6;
                                    return;
                                }
                                case 3 -> {
                                    System.out.println("你已进入星光沙漠");
                                    mapEnum = MapEnum.court;
                                    mapID = 7;
                                    return;
                                }
                                case 4 -> {
                                    System.out.println("你已进入失落方舟");
                                    mapEnum = MapEnum.wasteland;
                                    mapID = 5;
                                    return;
                                }
                                case 5 -> {
                                    System.out.println("你已进入雪隐峰");
                                    mapEnum = MapEnum.race;
                                    mapID = 6;
                                    return;
                                }
                                case 6 -> {
                                    System.out.println("你已进入雨林隐藏图");
                                    mapID = 4;
                                    return;
                                }
                                case 7 -> {
                                    System.out.println("你已进入静谧亭院");
                                    mapID = 0;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 9 -> {
                        System.out.println("霞谷 ---> 输入1");
                        while (true) {
                            if (sc.nextInt() == 1) {
                                System.out.println("你已进入霞谷");
                                mapEnum = MapEnum.race;
                                mapID = 0;
                                return;
                            } else {
                                System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                }
            }
            case race -> {
                switch (mapID) {
                    case 0 -> {
                        System.out.println("滑行赛道 ---> 输入1");
                        System.out.println("霞光城 ---> 输入2");
                        System.out.println("圆梦村 ---> 输入3");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入滑行赛道");
                                    mapID = 1;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入霞光城");
                                    mapID = 2;
                                    return;
                                }
                                case 3 -> {
                                    System.out.println("你已进入圆梦村");
                                    mapID = 5;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 1, 3 -> {
                        System.out.println("竞技场 ---> 输入1");
                        while (true) {
                            if (sc.nextInt() == 1) {
                                System.out.println("你已进入竞技场");
                                mapID = 4;
                                return;
                            } else {
                                System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 2 -> {
                        System.out.println("飞行赛道 ---> 输入1");
                        System.out.println("霞谷大厅溜冰场 ---> 输入2");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入飞行赛道");
                                    mapID = 3;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入霞谷大厅溜冰场");
                                    mapID = 0;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 4 -> {
                        System.out.println("终点神庙 ---> 输入1");
                        System.out.println("圆梦村 ---> 输入2");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入霞谷神庙");
                                    mapID = 9;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入圆梦村");
                                    mapID = 5;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 5 -> {
                        System.out.println("雪隐峰 ---> 输入1");
                        System.out.println("竞技场 ---> 输入2");
                        System.out.println("剧场 ---> 输入3");
                        System.out.println("音乐商店 ---> 输入4");
                        System.out.println("溜冰场 ---> 输入5");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入雪隐峰");
                                    mapID = 6;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入竞技场");
                                    mapID = 4;
                                    return;
                                }
                                case 3 -> {
                                    System.out.println("你已进入剧场");
                                    mapID = 7;
                                    return;
                                }
                                case 4 -> {
                                    System.out.println("你已进入音乐商店");
                                    mapID = 8;
                                    return;
                                }
                                case 5 -> {
                                    System.out.println("你已进入溜冰场");
                                    mapID = 0;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 6 -> {
                        System.out.println("风行网道 ---> 输入1");
                        System.out.println("圆梦村 ---> 输入2");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入风行网道");
                                    mapEnum = MapEnum.forests;
                                    mapID = 8;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入圆梦村");
                                    mapID = 5;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 7 -> {
                        System.out.println("音乐商店 ---> 输入1");
                        System.out.println("圆梦村 ---> 输入2");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入音乐商店");
                                    mapID = 8;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入圆梦村");
                                    mapID = 5;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 8 -> {
                        System.out.println("剧场 ---> 输入1");
                        System.out.println("圆梦村 ---> 输入2");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入剧场");
                                    mapID = 7;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入圆梦村");
                                    mapID = 5;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 9 -> {
                        System.out.println("墓土 ---> 输入1");
                        System.out.println("竞技场 ---> 输入2");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入墓土");
                                    mapEnum = MapEnum.wasteland;
                                    mapID = 0;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入竞技场");
                                    mapID = 4;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                }
            }
            case wasteland -> {
                switch (mapID) {
                    case 0 -> {
                        System.out.println("边陲荒漠 ---> 输入1");
                        System.out.println("藏宝岛礁 ---> 输入2");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入边陲荒漠");
                                    mapID = 1;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入藏宝岛礁");
                                    mapID = 6;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 1 -> {
                        System.out.println("巨兽荒原 ---> 输入1");
                        System.out.println("失落方舟 ---> 输入2");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入巨兽荒原");
                                    mapID = 2;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入失落方舟");
                                    mapID = 5;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 2 -> {
                        System.out.println("远古战场 ---> 输入1");
                        System.out.println("沉船图 ---> 输入2");
                        System.out.println("边陲荒漠 ---> 输入3");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入远古战场");
                                    mapID = 3;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入沉船图");
                                    mapID = 4;
                                    return;
                                }
                                case 3 -> {
                                    System.out.println("你已进入边陲荒漠");
                                    mapID = 1;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 3 -> {
                        System.out.println("终点神庙 ---> 输入1");
                        System.out.println("沉船图 ---> 输入2");
                        System.out.println("巨兽荒原 ---> 输入3");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入墓土神庙");
                                    mapID = 9;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入沉船图");
                                    mapID = 4;
                                    return;
                                }
                                case 3 -> {
                                    System.out.println("你已进入巨兽荒原");
                                    mapID = 2;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 4 -> {
                        System.out.println("远古战场 ---> 输入1");
                        System.out.println("巨兽荒原 ---> 输入2");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入远古战场");
                                    mapID = 3;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入巨兽荒原");
                                    mapID = 2;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 5 -> {
                        System.out.println("风行网道 ---> 输入1");
                        System.out.println("边陲荒漠 ---> 输入2");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入风行网道");
                                    mapEnum = MapEnum.forests;
                                    mapID = 8;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入边陲荒漠");
                                    mapID = 1;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 6 -> {
                        System.out.println("墓土大厅 ---> 输入1");
                        while (true) {
                            if (sc.nextInt() == 1) {
                                System.out.println("你已进入墓土大厅");
                                mapID = 0;
                                return;
                            } else {
                                System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 9 -> {
                        System.out.println("禁阁 ---> 输入1");
                        while (true) {
                            if (sc.nextInt() == 1) {
                                System.out.println("你已进入禁阁");
                                mapEnum = MapEnum.court;
                                mapID = 0;
                                return;
                            } else {
                                System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                }
            }
            case court -> {
                switch (mapID) {
                    case 0 -> {
                        System.out.println("禁阁二层 ---> 输入1");
                        System.out.println("禁阁隐藏图 ---> 输入2");
                        System.out.println("联动大厅 ---> 输入3");
                        System.out.println("庇护所 ---> 输入4");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入禁阁二层");
                                    mapID = 1;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入禁阁隐藏图");
                                    mapID = 6;
                                    return;
                                }
                                case 3 -> {
                                    System.out.println("你已进入联动大厅");
                                    mapID = 10;
                                    return;
                                }
                                case 4 -> {
                                    System.out.println("你已进入庇护所");
                                    mapID = 8;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 1 -> {
                        System.out.println("禁阁三层 ---> 输入1");
                        while (true) {
                            if (sc.nextInt() == 1) {
                                System.out.println("你已进入禁阁三层");
                                mapID = 2;
                                return;
                            } else {
                                System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 2 -> {
                        System.out.println("禁阁四层 ---> 输入1");
                        while (true) {
                            if (sc.nextInt() == 1) {
                                System.out.println("你已进入禁阁四层");
                                mapID = 3;
                                return;
                            } else {
                                System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 3 -> {
                        System.out.println("禁阁五层 ---> 输入1");
                        while (true) {
                            if (sc.nextInt() == 1) {
                                System.out.println("你已进入禁阁五层");
                                mapID = 4;
                                return;
                            } else {
                                System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 4 -> {
                        System.out.println("禁阁顶层 ---> 输入1");
                        while (true) {
                            if (sc.nextInt() == 1) {
                                System.out.println("你已进入禁阁顶层");
                                mapID = 5;
                                return;
                            } else {
                                System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 5 -> {
                        System.out.println("禁阁终点 ---> 输入1");
                        while (true) {
                            if (sc.nextInt() == 1) {
                                System.out.println("你已进入禁阁终点");
                                mapID = 9;
                                return;
                            } else {
                                System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 6, 8 -> {
                        System.out.println("禁阁一层 ---> 输入1");
                        while (true) {
                            if (sc.nextInt() == 1) {
                                System.out.println("你已进入禁阁一层");
                                mapID = 0;
                                return;
                            } else {
                                System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 7 -> {
                        System.out.println("风行网道 ---> 输入1");
                        System.out.println("联动大厅 ---> 输入2");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入风行网道");
                                    mapEnum = MapEnum.forests;
                                    mapID = 8;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入禁阁一层");
                                    mapID = 10;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 10 -> {
                        System.out.println("星光沙漠 ---> 输入1");
                        System.out.println("竞技场 ---> 输入2");
                        System.out.println("月牙绿洲 ---> 输入3");
                        System.out.println("禁阁一层 ---> 输入9");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入星光沙漠");
                                    mapID = 7;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入竞技场");
                                    mapEnum = MapEnum.race;
                                    mapID = 4;
                                    return;
                                }
                                case 3 -> {
                                    System.out.println("你已进入月牙绿洲");
                                    mapID = 11;
                                    return;
                                }
                                case 9 -> {
                                    System.out.println("你已进入禁阁一层");
                                    mapID = 0;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 11 -> {
                        System.out.println("联动大厅 ---> 输入1");
                        while (true) {
                            if (sc.nextInt() == 1) {
                                System.out.println("你已进入联动大厅");
                                mapID = 10;
                                return;
                            } else {
                                System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 9 -> {
                        System.out.println("暴风眼 ---> 输入1");
                        while (true) {
                            if (sc.nextInt() == 1) {
                                System.out.println("你已进入暴风眼");
                                mapEnum = MapEnum.eden;
                                mapID = 0;
                                return;
                            } else {
                                System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                }
            }
            case eden -> {
                switch (mapID) {
                    case 0 -> {
                        System.out.println("暴风眼二阶段 ---> 输入1");
                        while (true) {
                            if (sc.nextInt() == 1) {
                                System.out.println("你已进入暴风眼二阶段");
                                mapID = 1;
                                return;
                            } else {
                                System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 1 -> {
                        System.out.println("伊甸之眼 ---> 输入1");
                        System.out.println("暴风眼一阶段 ---> 输入2");
                        while (true) {
                            switch (sc.nextInt()) {
                                case 1 -> {
                                    System.out.println("你已进入伊甸之眼");
                                    mapID = 2;
                                    return;
                                }
                                case 2 -> {
                                    System.out.println("你已进入暴风眼一阶段");
                                    mapID = 0;
                                    return;
                                }
                                default -> System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 2 -> {
                        System.out.println("献祭 ---> 输入1");
                        while (true) {
                            if (sc.nextInt() == 1) {
                                mapID = 3;
                                you.xianJi();
                                return;
                            } else {
                                System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 3 -> {
                        System.out.println("继续献祭 ---> 输入1");
                        while (true) {
                            if (sc.nextInt() == 1) {
                                you.xianJi();
                                return;
                            } else {
                                System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                    case 4 -> {
                        System.out.println("重生 ---> 输入1");
                        while (true) {
                            if (sc.nextInt() == 1) {
                                you.congSeng();
                                return;
                            } else {
                                System.out.println("输入错误，请重新输入！");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 刷新地图
     */
    public void refreshMap() {
        if (you.getLastMap() == MapEnum.eden && you.getLastMapID() >= 2 && you.getLastMapID() <= 4)
            return;
        players.clear();
        players.add(you);
        System.out.println("你串线啦！");
        Random r = new Random();
        int num = r.nextInt(7) + 1;
        while (num > 0) {
            SkyPlayer newPlayer = new SkyPlayer();
            System.out.println(newPlayer.name + " 进入了地图");
            players.add(newPlayer);
            num--;
        }
        allPlayer();
    }

    /**
     * 显示地图及所有玩家信息
     */
    public void allPlayer() {
        System.out.println("当前地图为：" + mapInfo.get(mapEnum.ordinal()).get(mapID));
        System.out.print("当前地图内所有玩家为： ");
        for (SkyPlayer player : players) {
            System.out.print(player.name + " ");
        }
        if (!you.lightUpPlayer.isEmpty()) {
            System.out.print("\n你点亮的所有玩家为： ");
            for (SkyPlayer player : you.lightUpPlayer) {
                System.out.print(player.name + " ");
            }
        }
    }
}
