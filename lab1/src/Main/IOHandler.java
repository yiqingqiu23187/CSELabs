package Main;

import Expection.ErrorCode;

import java.io.*;
import java.util.logging.Logger;

public class IOHandler {
    public static void writeObject(Object object, String path) {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(path);

            bos = new BufferedOutputStream(fos);
            oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            //清空缓冲区
            oos.flush();
        } catch (IOException e) {
            Logger.getGlobal().warning(ErrorCode.getErrorText(1));
        }
    }

    public static Object readObject(String path) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(path);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (IOException e) {
            Logger.getGlobal().warning(ErrorCode.getErrorText(1));
        } catch (ClassNotFoundException classNotFoundException) {
            Logger.getGlobal().warning(ErrorCode.getErrorText(4));
        }
        return null;
    }
}
