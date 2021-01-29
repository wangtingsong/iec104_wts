package iec104.example.iec104.message;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * @title ASDU应用服务数据单元 实体
 * @author: wangtingsong
 * @Date: 2021/1/20 11:55
 * @since 版本号
 */
@Data
public class ASDUEntity implements Serializable {

    /**
     * 类属性标识符 参考 类 TypeIdentifier
     */
    private int typeIdentifierValue;

    /**
     * SQ = 1 信息元素地址顺序
     * SQ=0  信息元素地址非顺序
     */
    private int sq;

    /**
     * 信息总数
     */
    private int infoTotal;

    /**
     * 传送原因 参考类TransferReason
     */
    private int reason;

    /**
     * 传送原因中的 测试状态
     * true:以测试
     * false:为测试
     */
    private boolean reasonT;

    /**
     * 传送原因中的 是否确认/认可
     * true:肯定确认
     * false:否定确认
     */
    private boolean reasonPN;

    /**
     * 应用服务数据单元公共地址
     */
    private String messageAddress;


    /**
     * 遥测信息值
     */
    private List<TelemetryInfoEntity> telemetryInfoEntityList;

    /**
     * 遥信信息值
     */
    private List<TeleSignallingInfoEntity> teleSignallingInfoEntities;






}
