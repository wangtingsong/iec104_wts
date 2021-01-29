package iec104.example.iec104.core;


import iec104.example.iec104.message.ASDUEntity;
import iec104.example.iec104.message.TeleSignallingInfoEntity;
import iec104.example.iec104.util.Iec104Util;
import java.util.ArrayList;
import java.util.List;

/**
 * 遥信信息解析
 *
 * @author wangtingsong
 */
public class Telemetry104 {
    /**
     * 不带时标的单双点信息（遥信）
     *
     * @param infoElement
     * @param num
     * @param tI
     * @param sQ
     * @return
     */
    public String NoTime_Point(int[] infoElement, int num, int tI, int sQ, ASDUEntity asduEntity) {
        StringBuilder builder = new StringBuilder();
        List<TeleSignallingInfoEntity> teleSignallingInfoEntities = new ArrayList<>();
        if (sQ == 1) {
            int infoAddress = (infoElement[2] << 16)+(infoElement[1] << 8)+infoElement[0];
            builder.append("信息对象地址：    ");
            builder.append(new ASDU().InfoAddress(infoElement[0], infoElement[1],
                    infoElement[2])).append("\n");
            for (int i = 0; i < num; i++) {
                TeleSignallingInfoEntity teleSignallingInfoEntity = new TeleSignallingInfoEntity();
                teleSignallingInfoEntity.setInfoAddress(infoAddress);
                int value = infoElement[i + 3];
                teleSignallingInfoEntity.setValue(value);
                //设置类型 这里应该是固定的1
                teleSignallingInfoEntity.setType(tI);
                teleSignallingInfoEntities.add(teleSignallingInfoEntity);
                builder.append("信息元素");
                builder.append(i + 1);
                builder.append("的信息元素值：");
                builder.append(Iec104Util.toHexString(value));
                builder.append("\n");
                //TODO: 暂时未解析这个值到实体类中
                builder.append(this.point(value, tI));
                builder.append("\n");
            }

        } else {
            for (int i = 1; i <= num; i++) {

                int infoAddress = (infoElement[((i - 1) * 4 + 2)] << 16)+(infoElement[((i - 1) * 4 + 1)] << 8)+(infoElement[(i - 1) * 4]);
                TeleSignallingInfoEntity teleSignallingInfoEntity = new TeleSignallingInfoEntity();
                teleSignallingInfoEntity.setInfoAddress(infoAddress);
                int value = infoElement[i * 4 - 1];
                teleSignallingInfoEntity.setValue(value);
                //设置类型 这里应该是固定的1
                teleSignallingInfoEntity.setType(tI);
                teleSignallingInfoEntities.add(teleSignallingInfoEntity);
                builder.append("信息元素");
                builder.append(i);
                builder.append("的内容如下：\n");
                builder.append("信息对象地址：    ");
                builder.append(new ASDU().InfoAddress(infoElement[(i - 1) * 4],
                        infoElement[(i - 1) * 4 + 1],
                        infoElement[(i - 1) * 4 + 2]));
                builder.append("\n");
                builder.append("信息元素值：");
                builder.append(Iec104Util.toHexString(value));
                builder.append("\n");
                //TODO: 暂时未解析这个值到实体类中
                builder.append(this.point(value, tI));
                builder.append("\n");
            }
            asduEntity.setTeleSignallingInfoEntities(teleSignallingInfoEntities);
        }
        return builder.toString();
    }

    private String point(int i, int tI) {
        StringBuilder builder = new StringBuilder();
        if (tI == 1 || tI == 30) {
            if ((i & 0x80) == 128) {
                builder.append("无效/");
            }
            if ((i & 0x80) == 0) {
                builder.append("有效/");
            }
            if ((i & 0x40) == 64) {
                builder.append("非当前值/");
            }
            if ((i & 0x40) == 0) {
                builder.append("当前值/");
            }
            if ((i & 0x20) == 32) {
                builder.append("被取代/");
            }
            if ((i & 0x20) == 0) {
                builder.append("未被取代/");
            }
            if ((i & 0x10) == 16) {
                builder.append("被闭锁/");
            }
            if ((i & 0x10) == 0) {
                builder.append("未被闭锁/");
            }
            if ((i & 0x01) == 1) {
                builder.append("开关合	");
            }
            if ((i & 0x01) == 0) {
                builder.append("开关分	");
            }
        }
        if (tI == 3 || tI == 31) {
            if ((i & 0x80) == 128) {
                builder.append("无效/");
            }
            if ((i & 0x80) == 0) {
                builder.append("有效/");
            }
            if ((i & 0x40) == 64) {
                builder.append("非当前值/");
            }
            if ((i & 0x40) == 0) {
                builder.append("当前值/");
            }
            if ((i & 0x20) == 32) {
                builder.append("被取代/");
            }
            if ((i & 0x20) == 0) {
                builder.append("未被取代/");
            }
            if ((i & 0x10) == 16) {
                builder.append("被闭锁/");
            }
            if ((i & 0x10) == 0) {
                builder.append("未被闭锁/");
            }
            if ((i & 0x03) == 0) {
                builder.append("不确定或中间状态	");
            }
            if ((i & 0x03) == 1) {
                builder.append("确定开关分	");
            }
            if ((i & 0x03) == 2) {
                builder.append("确定开关合	");
            }
            if ((i & 0x03) == 3) {
                builder.append("不确定	");
            }

        }

        return builder.toString();
    }

    /**
     * 带CP56Time2a时标的单双点信息（遥信）
     *
     * @param infoElement
     * @param num
     * @param tI
     * @param sQ
     * @return
     */
    public String Time_Point(int[] infoElement, int num, int tI, int sQ, ASDUEntity asduEntity) {

        StringBuilder builder = new StringBuilder();
        List<TeleSignallingInfoEntity> teleSignallingInfoEntities = new ArrayList<>();
        if (sQ == 0) {
            for (int i = 1; i <= num; i++) {
                int infoAddress = (infoElement[((i - 1) * 11 + 2)] << 16)+(infoElement[((i - 1) * 11 + 1)] << 8)+(infoElement[(i - 1) * 11]);
                TeleSignallingInfoEntity teleSignallingInfoEntity = new TeleSignallingInfoEntity();
                teleSignallingInfoEntity.setInfoAddress(infoAddress);
                int value = infoElement[(i - 1) * 11 + 3];
                teleSignallingInfoEntity.setValue(value);
                //设置类型 这里应该是固定的1
                teleSignallingInfoEntity.setType(tI);

                int time[] = new int[7];
                for (int j = 0; j < 7; j++) {
                    time[j] = infoElement[(i - 1) * 11 + 4 + j];
                }
                String timeStr = Iec104Util.TimeScale(time, teleSignallingInfoEntity);
                teleSignallingInfoEntities.add(teleSignallingInfoEntity);
                builder.append("信息元素");
                builder.append(i);
                builder.append("的内容如下：\n");
                builder.append("信息对象地址：");
                builder.append(new ASDU().InfoAddress(infoElement[(i - 1) * 11],
                        infoElement[(i - 1) * 11 + 1],
                        infoElement[(i - 1) * 11 + 2])).append("\n");
                builder.append("信息元素值：");
                builder.append(Iec104Util.toHexString(value)).append("\n");

                //TODO: 暂时未解析这个值到实体类中
                builder.append(this.point(value, tI)).append("\n");


                builder.append(timeStr);
            }
        } else {
            builder.append("按照DL/T 634.5101-2002规定，带长时标的单/双点信息遥信报文并不存在信息元素序列（SQ=1）的情况。");
        }
        asduEntity.setTeleSignallingInfoEntities(teleSignallingInfoEntities);
        return builder.toString();

    }
}
