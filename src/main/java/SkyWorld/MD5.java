package SkyWorld;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @version 1.0
 * @Author zbxzbx98
 * @Date 2024/4/7 下午 5:17
 */
public class MD5 {
    /**
     * 生成MD5
     */
    public static String encode(String str) {
        byte[] result;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes(StandardCharsets.UTF_8));
            result = md.digest();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
        return parseByte2HexStr(result);
    }

    /**
     * 将二进制转换成十六进制
     */
    private static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buf) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
}
