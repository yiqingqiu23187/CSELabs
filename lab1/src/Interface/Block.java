package Interface;

public interface Block {
    Id getIndexId();
    Blockmanager getBlockManager();
    byte[] read();
    int blockSize();
}
