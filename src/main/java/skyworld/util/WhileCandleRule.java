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
    private final Map<Integer, Integer> CANDLE = new HashMap<>();

    public WhileCandleRule() {
        ObjectMapper objectMapper = new ObjectMapper();
        URL jsonFile = SkyMap.class.getResource("/whileCandleRule.json");
        MapType mapType = objectMapper.getTypeFactory().constructMapType(Map.class,Integer.class,String.class);
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
    public int calculateCandlelight(int candleNum) {
        Map<Integer, Integer> CANDLE = this.CANDLE;
        if (candleNum <= 0) {
            return 0;
        }

        int size = 0;
        while (candleNum != 0) {
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
        return this.calculateCandlelight(endCandleNum) - this.calculateCandlelight(beginCandleNum);
    }

    /**
     * 通过烛火数量计算蜡烛
     * @param candlelight 烛火数量
     * @return 蜡烛数量
     */
    public int calculateCandle(int candlelight) {
        int base = CANDLE.size();
        while (base >= 0) {
            if (this.calculateCandlelight(base) <= candlelight) {
                return base;
            }
            base--;
        }
        return 0;
    }

    /**
     * 通过烛火数量计算余量
     * @param candlelight 烛火数量
     * @return 合成后多余出来的烛火
     */
    public int calculateSurplus(int candlelight) {
        int candle = this.calculateCandle(candlelight);
        int justNeed = this.calculateCandlelight(candle);
        return candlelight - justNeed;
    }

    /**
     * 计算以当前烛火数量再获取几根后还需要的烛火数量
     * @param currentCandlelight 当前烛火数量
     * @param needCandleNum 再获取几根
     * @return 还需要的烛火数量
     */
    public int calculateNeedCandlelight(int currentCandlelight, int needCandleNum) {
        int candleNum = this.calculateCandle(currentCandlelight);
        return this.calculateTwoCandlelight(candleNum, candleNum + needCandleNum) - (currentCandlelight - this.calculateCandlelight(candleNum));
    }

    /**
     * 计算以当前烛火数量再获取1根后还需要的烛火数量
     * @param currentCandlelight 当前烛火数量
     * @return 还需要的烛火数量
     */
    public int calculateNextNeedCandlelight(int currentCandlelight) {
        return this.calculateNeedCandlelight(currentCandlelight, 1);
    }

    /**
     * 计算以当前烛火数量 获取下一根的百分比进度（不包含小数点后的进度）
     * @param currentCandlelight 当前烛火数量
     * @return 还需要的烛火数量
     */
    public int calculateNextNeedCandlelightProgress(int currentCandlelight) {
        int candleNum = this.calculateCandle(currentCandlelight);
        int need = this.calculateTwoCandlelight(candleNum, candleNum + 1);
        return (int) Math.floor(((double) (need - this.calculateNeedCandlelight(currentCandlelight, 1)) / need) * 100);
    }
}
