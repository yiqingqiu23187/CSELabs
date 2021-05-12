package Main;

import Expection.ErrorCode;
import Interface.Id;
import sun.rmi.runtime.Log;

import java.io.*;
import java.io.File;
import java.util.logging.Logger;

public class Block implements Interface.Block, Serializable {
    public static final int CAPACITY = 4;
    BlockId blockId;
    private int blockSize = 0;
    private String checkSum;

    void write(byte[] b) {
        //声明文件输出字节流
        try {
            if (b.length > CAPACITY)throw new ErrorCode(ErrorCode.BUFFER_OVERFLOW);
        }catch (ErrorCode errorCode){
            Logger.getGlobal().warning(ErrorCode.getErrorText(errorCode.getErrorCode()));
            return;
        }
        File file = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            file = new File(Main.BMPath + blockId.getBlockmanager() + "/" + blockId.getIndex() + ".data");
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            //清空缓冲区
            bos.write(b);
            blockSize = b.length;
            bos.flush();
            checkSum = MD5Utils.byteEncode(b);
        } catch (IOException e){
            Logger.getGlobal().warning(ErrorCode.getErrorText(ErrorCode.IO_EXCEPTION));
            return;
        }
        writeMeta();
    }

    @Override
    public byte[] read() {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        byte[] bytes = null;
        try {
            fis = new FileInputStream(Main.BMPath + blockId.getBlockmanager() + "/" + blockId.getIndex() + ".data");
            bis = new BufferedInputStream(fis);
            bytes = new byte[bis.available()];
            bis.read(bytes);
        } catch (IOException e) {
            Logger.getGlobal().warning(ErrorCode.getErrorText(ErrorCode.IO_EXCEPTION));
            return null;
        }

        try {
            if (checkSum.equals(MD5Utils.byteEncode(bytes))) return bytes;
            else throw new ErrorCode(ErrorCode.CHECKSUM_CHECK_FAILED);
        } catch (ErrorCode errorCode) {
            Logger.getGlobal().warning(blockId.getBlockmanager()+"Blockmanager"+blockId.getIndex()+ErrorCode.getErrorText(errorCode.getErrorCode()));
            return null;
        }

    }

    void writeMeta() {
        IOHandler.writeObject(this, Main.BMPath + blockId.getBlockmanager() + "/" + blockId.getIndex() + ".meta");
    }

    @Override
    public Id getIndexId() {
        return blockId;
    }


    @Override
    public int blockSize() {
        return blockSize;
    }

    @Override
    public Blockmanager getBlockManager() {
        return Main.blockmanagers[blockId.getBlockmanager()];
    }

    public void setIndexId(Id indexId) {
        this.blockId = (BlockId) indexId;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }
}
