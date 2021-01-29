package iec104.example.iec104.message;

import lombok.Data;

/**
 * @title 遥测数据：远程测量。采集并传送运行参数，包括各种电气量（线路上的电压、电流、功率等量值） 和负荷潮流等。
 * @author: wangtingsong
 * @Date: 2021/1/20 14:36
 * @since 版本号
 */
@Data
public class TelemetryInfoEntity {

    /**
     * 遥测数据类型
     * 9：归一化值 计算遥测的数据时需要协定出标准：比如2000 就归一，最终遥测数据就是传输过来的值除以2000
     * 11：标度化值 计算出来是多少就是多少 十进制
     * 13： 短浮点数值
     */
    private int type;

    /**
     * 对应的值
     */
    private float floatValue;

    /**
     * 对应的信息对象地址
     */
    private int infoAddress;

    /**
     * 品质描述词
     */
    private int qds;


}
