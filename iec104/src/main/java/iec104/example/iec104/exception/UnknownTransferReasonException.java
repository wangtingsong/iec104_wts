package iec104.example.iec104.exception;

/**
 * @Author zhangyu
 * @create 2019/5/27 16:53
 */
public class UnknownTransferReasonException extends Exception {

    public UnknownTransferReasonException() {
        super();
    }

    @Override
    public String toString() {
        return "未定义传送原因。";
    }
}
