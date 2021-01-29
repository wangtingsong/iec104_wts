package iec104.example.iec104.message;

import java.io.Serializable;
import lombok.Data;

/**
 * @title 一句话说明功能
 * @author: wangtingsong
 * @Date: 2021/1/20 11:09
 * @since 版本号
 */
@Data
public class Iec104MessageEntity implements Serializable {

    //开始符 十六进制的
    private String start = "68";

    //数据单元长度（除去 开始符和数据单元长度字符 后面所有的字符长度）
    private int messageLength = 0;

    // 控制域 实体
    private ControlEntity controlEntity;

    //ASDU应用服务数据单元
    private ASDUEntity asduEntity;
}
