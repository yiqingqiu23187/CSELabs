package Main;

import Expection.ErrorCode;
import Interface.Id;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

public class File implements Interface.File, Serializable {
    final int MOVE_CURR = 0;
    final int MOVE_HEAD = 1;
    final int MOVE_TAIL = 2;
    long cursor = 0;//光标位置，插入和读取都从光标开始（包括光标位）
    FileId fileId;
    int size;
    byte[] buffer;//实现读文件和写文件的buffer
    boolean bufferChanged = false;

    ArrayList<LogicBlock> logicBlocks = new ArrayList<>();

    public ArrayList<LogicBlock> getLogicBlocks() {
        return logicBlocks;
    }

    public void setLogicBlocks(ArrayList<LogicBlock> logicBlocks) {
        this.logicBlocks = logicBlocks;
    }

    @Override
    public Id getFileId() {
        return fileId;
    }

    public void setFileId(Id fileId) {
        this.fileId = (FileId) fileId;
    }

    @Override
    public FileManager getFileManager() {
        return Main.fileManagers[fileId.getFileManager()];
    }

    @Override
    public void write(byte[] b) {
        if (b.length == 0) return;
        bufferChanged = true;
        if (buffer == null) {
            if (size == 0) {
                buffer = b;
                size = b.length;
                cursor = size;
            } else {
                cursor = 0;
                buffer = read(size);
                writeBuffer(b);
            }
        } else {
            writeBuffer(b);
        }
    }

    private void writeBuffer(byte[] b) {
        byte[] newBuffer = new byte[buffer.length + b.length];
        System.arraycopy(buffer, 0, newBuffer, 0, (int) cursor);
        System.arraycopy(b, 0, newBuffer, (int) cursor, b.length);
        System.arraycopy(buffer, (int) cursor, newBuffer, (int) cursor + b.length, (int) (buffer.length - cursor));

        buffer = newBuffer;
        size = buffer.length;
        cursor += b.length;
    }

    void flush() {
        if (!bufferChanged) return;
        move(0, 1);
        logicBlocks = new ArrayList<>();
        int capacity = LogicBlock.CAPACITY;
        int blockNum = buffer.length / capacity;
        for (int i = 0; i < blockNum; i++) {
            LogicBlock logicBlock = new LogicBlock();
            byte[] temp = new byte[capacity];
            System.arraycopy(buffer, capacity * i, temp, 0, capacity);
            logicBlock.write(temp);
            logicBlocks.add(logicBlock);
        }
        if (buffer.length % capacity != 0) {
            LogicBlock logicBlock = new LogicBlock();
            byte[] temp = new byte[buffer.length - capacity * blockNum];
            System.arraycopy(buffer, capacity * blockNum, temp, 0, buffer.length - capacity * blockNum);
            logicBlock.write(temp);

            logicBlocks.add(logicBlock);
        }
        writeMeta();
    }

    void writeMeta() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("fileManager", fileId.getFileManager());
        hashMap.put("fileName", fileId.getFileName());
        hashMap.put("size", size);
        hashMap.put("blocks", logicBlocks);
        IOHandler.writeObject(hashMap, Main.FMPath + fileId.getFileManager() + "/" + fileId.getFileName() + ".meta");
    }


    //从当前光标读
    @Override
    public byte[] read(int length) {
        if (cursor + length >= size){

        }
            if (buffer == null) {
                buffer = new byte[0];
                for (LogicBlock logicBlock : logicBlocks) {
                    byte[] bytes = logicBlock.read();
                    if (bytes != null) {
                        buffer = ByteBuffer.allocate(buffer.length + bytes.length)
                                .put(buffer)
                                .put(bytes)
                                .array();
                    }
                }
            }
        byte[] result = new byte[length];
        System.arraycopy(buffer, (int) cursor, result, 0, length);
        return result;
    }

    byte[] readAll() {
        long temp = cursor;
        move(0, 1);
        byte[] result = read(size);
        cursor = temp;
        return result;
    }


    @Override
    public long move(long offset, int where) {
        if (where != 0 && where != 1 && where != 2) {
            Logger.getGlobal().warning(ErrorCode.getErrorText(ErrorCode.WRONG_PARAMETER));
            return cursor;
        }
        long oldcusor = cursor;
        switch (where) {
            case MOVE_CURR:
                cursor += offset;
                break;
            case MOVE_HEAD:
                cursor = offset;
                break;
            case MOVE_TAIL:
                cursor = size  - offset;
                break;
        }
        try {
            if (cursor > size){
                setSize(cursor);
            }
            if ( cursor < 0) {
                throw new ErrorCode(ErrorCode.CURSOR_OUT_OF_BOUND);
            }
        } catch (ErrorCode errorCode) {
            Logger.getGlobal().warning(ErrorCode.getErrorText(errorCode.getErrorCode()));
            cursor = oldcusor;
        }
        return cursor;
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public void setSize(long newSize) {
        if (size == 0) {
            buffer = new byte[(int) newSize];
            size = (int) newSize;
            bufferChanged = true;
            return;
        }
        if (buffer == null) {
            buffer = read(size);
        }
        buffer = Arrays.copyOf(buffer, (int) newSize);
        size = (int) newSize;
        cursor = size;
        bufferChanged = true;
    }


    @Override
    public void close() {
        flush();
        buffer = null;
    }
}
