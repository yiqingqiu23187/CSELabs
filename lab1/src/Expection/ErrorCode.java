package Expection;
import java.util.HashMap;
import java.util.Map;

public class ErrorCode extends RuntimeException {
    public static final int IO_EXCEPTION = 1;
    public static final int CHECKSUM_CHECK_FAILED = 2;
    public static final int CURSOR_OUT_OF_BOUND = 3;
    public static final int CLASS_NOT_FOUND = 4;
    public static final int BUFFER_OVERFLOW = 5;
    public static final int LOGICBLOCK_BROKEN = 6;
    public static final int WRONG_PARAMETER =7;
    public static final int FILE_NOT_EXIST = 8;

    // ... and more
    public static final int UNKNOWN = 1000;

    private static final Map<Integer, String> ErrorCodeMap = new HashMap<>();

    static {
        ErrorCodeMap.put(IO_EXCEPTION, "IO exception");
        ErrorCodeMap.put(CHECKSUM_CHECK_FAILED, "Block checksum check failed");
        ErrorCodeMap.put(CURSOR_OUT_OF_BOUND,"File cursor out of bound");
        ErrorCodeMap.put(CLASS_NOT_FOUND,"Class not found while readObject");
        ErrorCodeMap.put(BUFFER_OVERFLOW,"Trying to write a block with more data than its' capacity");
        ErrorCodeMap.put(LOGICBLOCK_BROKEN,"All blocks of a logicblock is broken");
        ErrorCodeMap.put(WRONG_PARAMETER,"Wrong parameter");
        ErrorCodeMap.put(FILE_NOT_EXIST,"File not exist");

        ErrorCodeMap.put(UNKNOWN, "unknown");
    }

    public static String getErrorText(int errorCode) {
        return ErrorCodeMap.getOrDefault(errorCode, "invalid");
    }

    private int errorCode;

    public ErrorCode(int errorCode) {
        super(String.format("error code '%d' \"%s\"", errorCode,
                getErrorText(errorCode)));
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    } }