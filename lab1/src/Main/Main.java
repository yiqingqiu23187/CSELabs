package Main;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    public static final int FMNumber = 3;
    public static final int BMNumber = 3;
    public static FileManager[] fileManagers = new FileManager[FMNumber];
    public static Blockmanager[] blockmanagers = new Blockmanager[BMNumber];
    public static String BMPath = "src/Storage/BlockManager/";
    public static String FMPath = "src/Storage/FileManager/";

    public static void main(String[] args) {
        for (int i = 0; i < FMNumber; i++) {
            fileManagers[i] = new FileManager(i);
        }
        for (int i = 0; i < BMNumber; i++) {
            blockmanagers[i] = new Blockmanager(i);
        }
        // 复原文件系统
        recoverBlockMeta(blockmanagers);
        recoverFileMeta(fileManagers);

//        File file = fileManagers[0].getFile(new FileId("f0",0));
//        smart_cat(0,"f0",8);
        //smart_hex(2,1);
        HashMap<String, Object> hashMap = (HashMap<String, Object>) IOHandler.readObject(FMPath+"0/f0.meta");
        System.out.println(hashMap.get("fileName"));
        ArrayList<LogicBlock> logicBlocks =(ArrayList<LogicBlock>) hashMap.get("blocks");
        for (LogicBlock logicBlock:logicBlocks){
            ArrayList<Block> blocks = logicBlock.blocks;
            for (Block block:blocks){
                System.out.println(block.blockId.getBlockmanager()+" /"+block.blockId.getIndex());
            }
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to my SmartFileSystem!");
        System.out.println();
        System.out.println("new [filemanager] [filename]     to create a new file");
        System.out.println("write [filemanager] [filename] [index]    to write a file");
        System.out.println("read [filemanager] [filename] [length]     to read a file");
        System.out.println("save [filemanager] [filename]     to save a file and close a file");
        System.out.println("move [filemanager] [filename] [offset] [where]     to move the cursor of a file");
        System.out.println("all files      to see all the files' distribution");
        System.out.println("detail [filemanager] [filename]     to see the detail of a file");
        System.out.println("copy [source filemanager] [source filename] [dst filemanager] [dst filemanager]   to copy a file");
        System.out.println("quit     to quit to System, remember to save all your files or they will get lost");

        String command;
        String[] paras;
        while (true) {
            System.out.print(System.getProperty("user.dir") + "$");
            command = scanner.nextLine();
            paras = command.split("\\s+");
            switch (paras[0].charAt(0)) {
                case 'n':
                    fileManagers[Integer.parseInt(paras[1])].newFile(new FileId(paras[2], Integer.parseInt(paras[1])));
                    break;
                case 'w':
                    smart_write(Integer.parseInt(paras[1]), paras[2], Integer.parseInt(paras[3]));
                    break;
                case 'r':
                    smart_cat(Integer.parseInt(paras[1]), paras[2], Integer.parseInt(paras[3]));
                    break;
                case 's':
                    File file = getFile(Integer.parseInt(paras[1]), paras[2]);
                    file.close();
                    break;
                case 'm':
                    File file1 = getFile(Integer.parseInt(paras[1]), paras[2]);
                    file1.move(Integer.parseInt(paras[3]), Integer.parseInt(paras[4]));
                    break;
                case 'a':
                    for (int i = 0; i < FMNumber; i++) {
                        System.out.print("filemanager" + i + ": ");
                        for (File file2 : fileManagers[i].files) {
                            System.out.print(((FileId) file2.getFileId()).getFileName() + "  " + file2.size());
                        }
                        System.out.println();
                    }
                    break;
                case 'd':
                    File file3 = getFile(Integer.parseInt(paras[1]), paras[2]);
                    file3.move(0, 1);
                    System.out.println("size" + file3.size);
                    smart_cat(Integer.parseInt(paras[1]), paras[2], file3.size);
                    file3.move(0, 1);
                    System.out.println(new String(file3.read(file3.size)));
                    break;
                case 'c':
                    smart_copy(Integer.parseInt(paras[1]),paras[2],Integer.parseInt(paras[3]),paras[4]);
                    break;
                case 'q':
                    System.exit(0);
            }
        }

    }

    private static File getFile(int filemanager, String filename) {
        return fileManagers[filemanager].getFile(new FileId(filename, filemanager));
    }

    private static void recoverBlockMeta(Blockmanager[] blockmanagers) {
        for (int i = 0; i < BMNumber; i++) {
            java.io.File path = new java.io.File(BMPath + i);
            java.io.File[] files = path.listFiles();
            for (java.io.File file : files) {
                String fileName = file.getName();
                String postfix = fileName.substring(fileName.lastIndexOf(".") + 1);
                //还原block对象
                if (postfix.equals("meta")) {
                    Block block = (Block) IOHandler.readObject(BMPath + i + "/" + fileName);
                    blockmanagers[i].blocks.add(block);
                    blockmanagers[i].blockNumber++;
                }
            }
        }
    }

    private static void recoverFileMeta(FileManager[] fileManagers) {
        for (int i = 0; i < FMNumber; i++) {
            java.io.File path = new java.io.File(FMPath + i);
            java.io.File[] files = path.listFiles();
            for (java.io.File file : files) {
                String fileName = file.getName();
                //还原file对象
                HashMap<String, Object> hashMap = (HashMap) IOHandler.readObject(FMPath + i + "/" + fileName);
                File temp = new File();
                temp.setFileId(new FileId((String) hashMap.get("fileName"), (int) hashMap.get("fileManager")));
                temp.size = (int) hashMap.get("size");
                temp.setLogicBlocks((ArrayList) (hashMap.get("blocks")));
                fileManagers[i].files.add(temp);
            }
        }
    }

    public static void smart_cat(int fileManager, String fileName, int length) {
        File file = fileManagers[fileManager].getFile(new FileId(fileName, fileManager));
        if (file == null) return;
        byte[] bytes = file.read(length);
        //System.out.println(new String(bytes));
        System.out.println(Arrays.toString(bytes));
    }

    public static void smart_write(int fileManager, String fileName, int index) {
        System.out.println("请输入文件内容：      按Enter结束输入");
        File file = fileManagers[fileManager].getFile(new FileId(fileName, fileManager));
        if (file == null) {
            file = fileManagers[fileManager].newFile(new FileId(fileName, fileManager));
            Logger.getGlobal().info("已为您创建新文件");
        }
        Scanner scanner = new Scanner(System.in);
        byte[] bytes = scanner.nextLine().getBytes();
        file.move(index, 1);
        file.write(bytes);

    }

    public static void smart_copy(int srcfm,String src,  int dstfm, String dst) {
        File file = fileManagers[srcfm].getFile(new FileId(src, srcfm));
        byte[] bytes = file.readAll();
        file = fileManagers[dstfm].newFile(new FileId(dst, dstfm));
        file.write(bytes);
        file.close();
    }

    public static void smart_hex(int blockManager, int blockIndex) {
        BlockId blockId = new BlockId(blockIndex, blockManager);
        Block block = blockmanagers[blockManager].getBlock(blockId);
        byte[] b = block.read();
        String result = DatatypeConverter.printHexBinary(b);
        System.out.println(result);
      //  System.out.println(new String(b));
    }

}
