package iec104.example.iec104;

import iec104.example.iec104.core.ASDU;
import iec104.example.iec104.exception.IllegalFormatException;
import iec104.example.iec104.exception.LengthException;
import iec104.example.iec104.exception.UnknownTransferReasonException;
import iec104.example.iec104.exception.UnknownTypeIdentifierException;
import iec104.example.iec104.message.ASDUEntity;
import iec104.example.iec104.message.ControlEntity;
import iec104.example.iec104.message.Iec104MessageEntity;
import iec104.example.iec104.util.Iec104Util;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @title 一句话说明功能
 * @author: wangtingsong
 * @Date: 2021/1/19 12:21
 * @since 版本号
 */
@Slf4j
public class Iec104ConnectorHandler extends SimpleChannelInboundHandler<String> {




    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("收到数据包:{}", msg);

        //解析104数据包
        try {
            Iec104MessageEntity iec104MessageEntity = getMessage(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("解析监测包出错,包内容:{}错误:{}", msg, ex.getMessage());
        }
    }


    /**
     * 解析104数据为 实体
     * @param msg
     * @return
     */
    public Iec104MessageEntity getMessage(String msg) throws IllegalFormatException, LengthException, UnknownTransferReasonException, UnknownTypeIdentifierException {
        Iec104MessageEntity iec104MessageEntity = new Iec104MessageEntity();
        StringBuilder contentbuilder = new StringBuilder();
        String mes = msg.replaceAll(" ", "");
        if ((mes.length() == 0) || (mes.length() % 2) == 1) {
            //104 报文没有结束字符，所以最好判断一下报文串的长度是否是偶数
            throw new IllegalFormatException();
        }
        // 将报文转化成int数组
        int msgArray[] = Iec104Util.hexStringToIntArray(mes);
        int length = msgArray.length;// 记录报文的总长度
        if (msgArray[0] == 0x68 && length >= 2) {
            iec104MessageEntity.setStart("68");
            contentbuilder.append("*APCI应用规约控制信息*").append("\n");
            contentbuilder.append("启动字符[1 byte]: 0x68 ").append("\n");
        } else {
            throw new IllegalFormatException();
        }

        // 验证数据单元长度 第二个字节就是长度
        int messageLength = msgArray[1];
        if (length-2 != messageLength) {
            throw new LengthException(messageLength+ 2, length);
        }
        iec104MessageEntity.setMessageLength(messageLength);

        //解析控制域数据
        ControlEntity controlEntity;
        contentbuilder.append("应用规约数据单元(APDU)长度[2 byte]:").append(messageLength).append("字节").append("\n");
        contentbuilder.append("控制域[3 byte - 6 byte]：").append("\n");
        controlEntity = Control(new int[]{msgArray[2],msgArray[3],msgArray[4], msgArray[5]}, contentbuilder);
        iec104MessageEntity.setControlEntity(controlEntity);

        //解析ASDU 只有I格式才有 ASDU 数据传输
        if ((msgArray[2] & 0x03) != 3 && (msgArray[2] & 0x03) != 1) {
            //解析ASDU
            contentbuilder.append("*ASDU应用服务数据单元*\n");
            //获取到剩余的字节，除出起始符（1字节）、长度值（1字节）、四个字节的控制域
            int asdu[] = new int[length - 6];
            for (int j = 0; j < length - 6; j++) {
                asdu[j] = msgArray[6 + j];
            }
            ASDUEntity asduEntity =  ASDU.ASDU_analysis(asdu, contentbuilder);
            iec104MessageEntity.setAsduEntity(asduEntity);
        }
        log.info(contentbuilder.toString());

        return iec104MessageEntity;
    }

    /**
     * 解析104规约的控制域
     *
     * @param con
     * @return
     */
    private static ControlEntity Control(int[] con, StringBuilder conBuilder) {
        ControlEntity controlEntity = new ControlEntity();
        // 把第一控制域的值和 16进制的03进行与运行 是1代表S格式， 3代表U格式，其余就是I格式
        switch (con[0] & 0x03) {
            case 1:
                controlEntity.setControlType("S");
                int acceptNumber = ((con[3] << 8) + con[2]) >> 1;
                controlEntity.setAcceptNumber(acceptNumber);
                conBuilder.append("\t(S格式控制域标志)\n");
                conBuilder.append(acceptNumber);
                break;
            case 3:
                controlEntity.setControlType("U");
                conBuilder.append("\t(U格式控制域标志)\n");
                if ((con[0] & 0xC0) == 128) {
                    controlEntity.setTestFr("confirm");
                    conBuilder.append("\t链路测试TESTFR:确认\n");
                } else if ((con[0] & 0xC0) == 64) {
                    controlEntity.setTestFr("command");
                    conBuilder.append("\t链路测试TESTFR：命令\n");
                }
                if ((con[0] & 0x30) == 32) {
                    controlEntity.setStopDt("confirm");
                    conBuilder.append("\t断开数据传输STOPDT:确认\n");
                } else if ((con[0] & 0x30) == 16) {
                    controlEntity.setStopDt("command");
                    conBuilder.append("\t断开数据传输STOPDT：命令\n");
                }
                if ((con[0] & 0x0C) == 8) {
                    controlEntity.setStartDt("confirm");
                    conBuilder.append("\t启动数据传输STARTDT:确认\n");
                } else if ((con[0] & 0x0C) == 4) {
                    controlEntity.setStartDt("command");
                    conBuilder.append("\t启动数据传输STARTDT：命令\n");
                }
                break;
            default:
                controlEntity.setControlType("I");
                int sendNumber = ((con[1] << 8) + con[0]) >> 1;
                controlEntity.setSendNumber(sendNumber);
                acceptNumber = ((con[3] << 8) + con[2]) >> 1;
                controlEntity.setAcceptNumber(acceptNumber);
                conBuilder.append("\t(I格式控制域标志)\n");
                conBuilder.append("\t发送序列号：").append(sendNumber).append("\n");
                conBuilder.append("\t接受序列号：").append(acceptNumber).append("\n");
                break;
        }
        return controlEntity;
    }


}
