package iec104.example.iec104.message;

import lombok.Data;

/**
 * @title 控制规约实体
 * @author: wangtingsong
 * @Date: 2021/1/20 11:21
 * @since 版本号
 */
@Data
public class ControlEntity {

    /**
     * I ,U, S 三种控制域格式
     * I帧是信息帧，作用是用于传输具体的通信数据的 长度必大于6个字节。
     * S帧是用于确认接收的I帧，即当接收到I帧后需要返回一个S帧 长度等于6个字节。
     * U帧是控制帧，用于控制启动/停止子站进行数据传输，或测试TCP链路连接，长度等于6个字节
     */
    private String controlType;

    /**
     *  接收序列号 S 和 I格式 才会有这个字段
      */
    private int  acceptNumber;

    /**
     * 发送序列号 只有 I格式才会有
     */
    private int sendNumber;

    /**
     * U格式才有值
     * 链路测试TESTFR值
     * confirm 确认
     * command 命令
     */
    private String testFr;


    /**
     * U格式才有值
     * 启动数据传输STARTDT值
     * confirm 确认
     * command 命令
     */
    private String startDt;

    /**
     *U格式才有值
     *断开数据传输STOPDT 值
     * confirm 确认
     * command 命令
     */
    private String stopDt;




}
