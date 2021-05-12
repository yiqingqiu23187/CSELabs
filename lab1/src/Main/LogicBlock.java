package Main;

import Expection.ErrorCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

public class LogicBlock implements Serializable {
    public static final int CAPACITY = 4;
    ArrayList<Block> blocks = new ArrayList<>();

    byte[] read() {
        for (Block block : blocks) {
            byte[] b = block.read();
            if (b != null)return b;
        }
        Logger.getGlobal().warning(ErrorCode.getErrorText(ErrorCode.LOGICBLOCK_BROKEN));
        return null;
    }

    void write(byte[] b) {
        Blockmanager[] blockmanagers = Main.blockmanagers;
        int random = new Random().nextInt(blockmanagers.length);
        blocks.add(blockmanagers[random].newBlock(b));
        blocks.add(blockmanagers[(random+1)%3].newBlock(b));

    }
}
