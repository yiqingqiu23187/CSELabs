package Main;

import Interface.Id;

import java.io.Serializable;

public class BlockId implements Id , Serializable {
    private int index;
    private int blockmanager;

    public int getIndex() {
        return index;
    }

    public BlockId(int index, int blockmanager) {
        this.index = index;
        this.blockmanager = blockmanager;
    }

    public int getBlockmanager() {
        return blockmanager;
    }

}
