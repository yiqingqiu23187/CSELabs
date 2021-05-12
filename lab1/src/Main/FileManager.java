package Main;

import Expection.ErrorCode;
import Interface.Id;

import java.util.*;
import java.util.logging.Logger;

public class FileManager implements Interface.FileManager {
    ArrayList<File> files = new ArrayList<>();
    int fileNumber = -1;
    int fileManagerIndex;

    @Override
    public File getFile(Id fileId) {
        FileId fileId1 = (FileId) fileId;
        for (File file : files) {
            FileId fileId2 = (FileId) file.getFileId();
            if (fileId1.getFileName().equals(fileId2.getFileName()))
                return file;
        }
        Logger.getGlobal().warning(ErrorCode.getErrorText(ErrorCode.FILE_NOT_EXIST));
        return null;
    }

    public FileManager(int fileManagerIndex) {
        this.fileManagerIndex = fileManagerIndex;
    }

    @Override
    public File newFile(Id fileId) {
        //创建新file对象
        File dupli = null;
        for (File file : files) {
            if (((FileId) (file.getFileId())).getFileName().equals(((FileId) fileId).getFileName()))
                dupli = file;
        }
        if (null != dupli) files.remove(dupli);
        File file = new File();
        fileNumber++;
        file.setFileId(fileId);
        files.add(file);

        file.writeMeta();

        return file;
    }


}
