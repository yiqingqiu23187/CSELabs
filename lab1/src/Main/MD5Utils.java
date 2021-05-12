package Main;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    public static String byteEncode(byte[] bytes) {
        try {
            // 加密对象，指定加密方式
            MessageDigest md5 = MessageDigest.getInstance("md5");
            // 加密
            byte[] digest = md5.digest(bytes);
            StringBuilder sb = byte2Hex(digest);
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static StringBuilder byte2Hex(byte[] data) {
        char[] chars = new char[]{'0', '1', '2', '3', '4', '5',
                '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuilder sb = new StringBuilder();
        // 处理成十六进制的字符串(通常)
        for (byte bb : data) {
            sb.append(chars[(bb >> 4) & 15]);
            sb.append(chars[bb & 15]);
        }
        return sb;
    }
}


