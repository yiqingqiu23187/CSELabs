package Main;
import Interface.Id;

import java.util.ArrayList;

public class Blockmanager implements Interface.Blockmanager {
    ArrayList<Block> blocks = new ArrayList<>();
    int blockNumber = -1;
    int blockManagerIndex;


    @Override
    public Block getBlock(Id indexId) {
        BlockId blockId = (BlockId)indexId;
        return blocks.get(blockId.getIndex());
    }

    @Override
    public Block newBlock(byte[] b) {
        Block block = new Block();
        blockNumber++;
        BlockId id = new BlockId(blockNumber,blockManagerIndex);
        block.setIndexId(id);
        block.write(b);
        blocks.add(block);

        block.writeMeta();
        return block;
    }

    public Blockmanager(int blockManagerIndex) {
        this.blockManagerIndex = blockManagerIndex;
    }

}
