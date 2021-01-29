package iec104.example.iec104.message;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @title 遥信：远程信号。采集并传送各种保护告警和开关量信息。
 * @author: wangtingsong
 * @Date: 2021/1/20 16:30
 * @since 版本号
 */
@Data
public class TeleSignallingInfoEntity implements Serializable {

    /**
     * 遥信类型
     * 1：不带时标的单点遥信
     * 3：不带师表的双点遥信
     * 30：带CP56Time2a时标的单点遥信
     * 31：CP56Time2a时标的双点遥信
     */
    private int type;

    /**
     * 信息对象地址
     */
    private int infoAddress;

    /**
     * 值
     */
    private int value;

    /**
     * 带时标时的 时间值
     */
    private Date time;


}
