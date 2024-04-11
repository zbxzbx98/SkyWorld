package skyworld.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import skyworld.SkyMap;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @Author zbxzbx98
 * @Date 2024/4/10 下午2:57
 */
public class WhileCandleRule {

    // 每根合成烛火数映射表
    private static final Map<Integer, Integer> CANDLE = new HashMap<>();

    public WhileCandleRule() {
        ObjectMapper objectMapper = new ObjectMapper();
        URL jsonFile = SkyMap.class.getResource("/whileCandleRule.json");
        MapType mapType = objectMapper.getTypeFactory().constructMapType(Map.class,Integer.class,Integer.class);
        try {
            CANDLE.putAll(objectMapper.readValue(jsonFile, mapType));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过蜡烛数量计算所需烛火
     * @param candleNum 蜡烛数量
     * @return 烛火数量
     */
    public int calculateCandlelight(int base,int candleNum) {
//        Map<Integer, Integer> CANDLE = this.CANDLE;
        if (candleNum <= 0) {
            return 0;
        }
        candleNum+=base;
        int size = 0;
        while (candleNum > base) {
            if (CANDLE.containsKey(candleNum)) {
                size += CANDLE.get(candleNum);
                candleNum--;
            } else {
                return Integer.MAX_VALUE;
            }
        }
        return size;
    }

    /**
     * 计算从第几根到第几根所需烛火
     * @param beginCandleNum 从第几根(通常是已经拿到的蜡烛数量)
     * @param endCandleNum 到第几根
     * @return 烛火数量
     */
    public int calculateTwoCandlelight(int beginCandleNum, int endCandleNum) {
        return calculateCandlelight(beginCandleNum,endCandleNum-beginCandleNum);
    }

    /**
     * 通过烛火数量计算蜡烛
     * @param candlelight 烛火数量
     * @return 蜡烛数量
     */
    public int calculateCandle(int base,int candlelight) {
        if(candlelight==0)
            return 0;
        int get = 0;
        while (base+get <= CANDLE.size()) {
            if (this.calculateCandlelight(base, get) > candlelight) {
                return get-1;
            }
            get++;
        }
        return 26-base;
    }

    /**
     * 通过烛火数量计算余量
     * @param candlelight 烛火数量
     * @return 合成后多余出来的烛火
     */
    public int calculateSurplus(int base,int candlelight) {
        int candle = this.calculateCandle(base,candlelight);
        int justNeed = this.calculateCandlelight(base,candle);
        return candlelight - justNeed;
    }

    /**
     * 计算以当前烛火数量再获取几根后还需要的烛火数量
     * @param base 烛火基础
     * @param currentCandlelight 当前烛火数量
     * @param needCandleNum 再获取几根
     * @return 还需要的烛火数量
     */
    public int calculateNeedCandlelight(int base, int currentCandlelight, int needCandleNum) {
        return this.calculateTwoCandlelight(base, base + needCandleNum) - currentCandlelight;
    }

    /**
     * 计算以当前烛火数量再获取1根后还需要的烛火数量
     * @param base 烛火基础
     * @param currentCandlelight 当前烛火数量
     * @return 还需要的烛火数量
     */
    public int calculateNextNeedCandlelight(int base, int currentCandlelight) {
        return this.calculateNeedCandlelight(base, currentCandlelight, 1);
    }

    /**
     * 计算以当前烛火数量 获取下一根的百分比进度（大于100%时返回1）
     * @param base 烛火基础
     * @param currentCandlelight 当前烛火数量
     * @return 还需要的烛火数量
     */
    public double calculateNextNeedCandlelightProgress(int base, int currentCandlelight) {
        int need = this.calculateTwoCandlelight(base, base + 1);
        if(need<currentCandlelight)
            return 1;
        return ((double)currentCandlelight) / need;
    }

    /**
     * 计算进度转为烛火数量
     * @param base 烛火基础
     * @param progress 进度
     * @return 烛火数量
     */
    public int calculateProgressToCandlelight(int base,double progress){
        if(progress<=0)
            return 0;
        if(progress>1)
            return calculateTwoCandlelight(base,base+1);
        return (int)(progress*calculateTwoCandlelight(base,base+1));
    }
}
