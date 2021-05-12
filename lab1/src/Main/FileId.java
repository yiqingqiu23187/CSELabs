package Main;

import Interface.Id;

public class FileId implements Id {
    String fileName;
    int fileManager;

    public FileId(String fileName, int fileManager) {
        this.fileName = fileName;
        this.fileManager = fileManager;
    }

    public String getFileName() {
        return fileName;
    }

    public int getFileManager() {
        return fileManager;
    }
}
