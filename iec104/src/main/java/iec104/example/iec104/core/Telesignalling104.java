package iec104.example.iec104.core;


import iec104.example.iec104.message.ASDUEntity;
import iec104.example.iec104.message.TelemetryInfoEntity;
import iec104.example.iec104.util.Iec104Util;
import java.util.ArrayList;
import java.util.List;

/**
 * 遥测信息解析
 *
 * @author wangtingsong
 *
 */
public class Telesignalling104 {
	/**
	 * 测量值，归一化值
	 *
	 * @param infoElement
	 * @param num
	 * @param tI
	 * @param sQ
	 * @return
	 */
	public String normalization(int[] infoElement, int num, int tI, int sQ, ASDUEntity asduEntity) {
		List<TelemetryInfoEntity> telemetryInfoEntitys = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		if (sQ == 1) {
			int infoAddress = (infoElement[2] << 16) + (infoElement[1] << 8) + infoElement[0];
			builder.append("遥测信息对象地址");
			builder.append(new ASDU().InfoAddress(infoElement[0], infoElement[1],
					infoElement[2]));
			builder.append("\n");
			for (int i = 0; i < num; i++) {
				TelemetryInfoEntity telemetryInfoEntity = new TelemetryInfoEntity();
				telemetryInfoEntity.setInfoAddress(infoAddress);
				//低位
				int low = infoElement[i * 3 + 3];
				//高位
				int height = infoElement[i * 3 + 4];
				//TODO:计算归一化基础值，后续需要和客服端定义归一化值
				telemetryInfoEntity.setFloatValue(Bytes2Float_NVA(low, height));
				//和类型标识符一样的值
				telemetryInfoEntity.setType(9);
				int qds = infoElement[i * 3 + 5];
				telemetryInfoEntity.setQds(qds);
				telemetryInfoEntitys.add(telemetryInfoEntity);
				builder.append("信息对象");
				builder.append(i + 1);
				builder.append("归一化值NVA:");
				builder.append(Iec104Util.toHexString(low));
				builder.append("\t");
				builder.append(Iec104Util.toHexString(height));
				builder.append("\n");
				builder.append("品质描述词QDS:");
				builder.append(Iec104Util.toHexString(qds));
				builder.append("\n");
			}
		} else {
			for (int i = 0; i < num; i++) {
				TelemetryInfoEntity telemetryInfoEntity = new TelemetryInfoEntity();
				int infoAddress = (infoElement[i * 6 + 2] << 16) + (infoElement[i * 6 + 1] << 8) + infoElement[i *6];
				telemetryInfoEntity.setInfoAddress(infoAddress);
				//低位
				int low = infoElement[i * 6 + 3];
				//高位
				int height = infoElement[i * 6 + 4];
				//TODO: 计算归一化基础值，后续需要和客服端定义归一化值
				telemetryInfoEntity.setFloatValue(Bytes2Float_NVA(low, height));
				int qds = infoElement[i * 6 + 5];
				telemetryInfoEntity.setQds( qds);
				//和类型标识符一样的值
				telemetryInfoEntity.setType(9);
				telemetryInfoEntitys.add(telemetryInfoEntity);
				builder.append("信息对象");
				builder.append((i + 1));
				builder.append("的地址：");
				builder.append(new ASDU().InfoAddress(infoElement[i * 6],
						infoElement[i * 6 + 1], infoElement[i * 6 + 2]));
				builder.append("\n");
				builder.append("归一化值NVA:");
				builder.append(Iec104Util.toHexString(low));
				builder.append("\t");
				builder.append(Iec104Util.toHexString(height));
				builder.append("\n");
				builder.append("品质描述词QDS:");
				builder.append(Iec104Util.toHexString(qds));
				builder.append("\n");
			}
		}
		asduEntity.setTelemetryInfoEntityList(telemetryInfoEntitys);
		return builder.toString();
	}

	/**
     * 测量值，标度化值
	 * @param infoElement 信息地址+信息元素+限定词+（时标）
	 * @param num 信息体个数
	 * @param tI 类型标识
	 * @param sQ sq值
	 * @param asduEntity asdu实体
	 * @return
     */
	public String standardization(int[] infoElement, int num, int tI, int sQ, ASDUEntity asduEntity) {
		List<TelemetryInfoEntity> telemetryInfoEntitys = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		if (sQ == 1) {
			int infoAddress = (infoElement[2] << 16) + (infoElement[1] << 8) + infoElement[0];
			builder.append("遥测信息对象地址");
			builder.append(new ASDU().InfoAddress(infoElement[0], infoElement[1],
					infoElement[2]));
			builder.append("\n");
			for (int i = 0; i < num; i++) {
				TelemetryInfoEntity telemetryInfoEntity = new TelemetryInfoEntity();
				telemetryInfoEntity.setInfoAddress(infoAddress);
				//低位
				int low = infoElement[i * 3 + 3];
				//高位
				int height = infoElement[i * 3 + 4];
				//计算标度化值
				telemetryInfoEntity.setFloatValue(Bytes2Float_SVA(low, height));
				//和类型标识符一样的值
				telemetryInfoEntity.setType(11);
				int qds = infoElement[i * 3 + 5];
				telemetryInfoEntity.setQds(qds);
				telemetryInfoEntitys.add(telemetryInfoEntity);
				builder.append("信息对象");
				builder.append(i + 1);
				builder.append("标度化值SVA:");
				builder.append(Iec104Util.toHexString(low));
				builder.append("\t");
				builder.append(Iec104Util.toHexString(height));
				builder.append("\n");
				builder.append("品质描述词QDS:");
				builder.append(Iec104Util.toHexString(qds));
				builder.append("\n");
			}
		} else {
			for (int i = 0; i < num; i++) {
				TelemetryInfoEntity telemetryInfoEntity = new TelemetryInfoEntity();
				int infoAddress = (infoElement[i * 6 + 2] << 16) + (infoElement[i * 6 + 1] << 8) + infoElement[i *6];
				telemetryInfoEntity.setInfoAddress(infoAddress);
				//低位
				int low = infoElement[i * 6 + 3];
				//高位
				int height = infoElement[i * 6 + 4];
				//计算标度化基础值
				telemetryInfoEntity.setFloatValue(Bytes2Float_SVA(low, height));
				int qds = infoElement[i * 6 + 5];
				telemetryInfoEntity.setQds( qds);
				//和类型标识符一样的值
				telemetryInfoEntity.setType(11);
				telemetryInfoEntitys.add(telemetryInfoEntity);
				builder.append("信息对象");
				builder.append((i + 1));
				builder.append("的地址：");
				builder.append(new ASDU().InfoAddress(infoElement[i * 6],
						infoElement[i * 6 + 1], infoElement[i * 6 + 2]));
				builder.append("\n");
				builder.append("标度化值SVA:");
				builder.append(Iec104Util.toHexString(low));
				builder.append("\t");
				builder.append(Iec104Util.toHexString(height));
				builder.append("\n");
				builder.append("品质描述词QDS:");
				builder.append(Iec104Util.toHexString(qds));
				builder.append("\n");
			}
		}
		asduEntity.setTelemetryInfoEntityList(telemetryInfoEntitys);
		return builder.toString();
	}

	/**
	 * 测量值，短浮点数
	 *
	 * @param infoElement
	 * @param num
	 * @param tI
	 * @param sQ
	 * @return
	 */
	public String short_float(int[] infoElement, int num, int tI, int sQ, ASDUEntity asduEntity) {
		StringBuilder builder = new StringBuilder();
		List<TelemetryInfoEntity> telemetryInfoEntitys = new ArrayList<>();
		if (sQ == 1) {
			int infoAddress = (infoElement[2] << 16) + (infoElement[1] << 8) + infoElement[0];

			builder.append("遥测信息对象地址");
			builder.append(new ASDU().InfoAddress(infoElement[0], infoElement[1],
					infoElement[2])).append("\n");
			for (int i = 0; i < num; i++) {
				TelemetryInfoEntity telemetryInfoEntity = new TelemetryInfoEntity();
				telemetryInfoEntity.setInfoAddress(infoAddress);
				//浮点数1
				String floatOne = Iec104Util.toHexStringNo0x(infoElement[i * 5 + 3]);
				//浮点数2
				String floatTwo = Iec104Util.toHexStringNo0x(infoElement[i * 5 + 4]);
				//浮点数3
				String floatThree = Iec104Util.toHexStringNo0x(infoElement[i * 5 + 5]);
				//浮点数4
				String floatFour = Iec104Util.toHexStringNo0x(infoElement[i * 5 + 6]);
				//计算最终的短浮点值
				telemetryInfoEntity.setFloatValue(Bytes2Float_IEEE754(floatOne+ floatTwo+ floatThree +floatFour));
				//和类型标识符一样的值
				telemetryInfoEntity.setType(13);
				int qds = infoElement[i * 5 + 7];
				telemetryInfoEntity.setQds(qds);
				telemetryInfoEntitys.add(telemetryInfoEntity);
				builder.append("遥测");
				builder.append((i + 1));
				builder.append("IEEE STD745短浮点数:");
				builder.append(floatOne);
				builder.append("\t");
				builder.append(floatTwo);
				builder.append("\t");
				builder.append(floatThree);
				builder.append("\t");
				builder.append(floatFour);
				builder.append("\n");
				builder.append("品质描述词QDS:");
				builder.append(Iec104Util.toHexString(infoElement[i * 5 + 7]));
				builder.append("\n");
			}
		} else {
			for (int i = 0; i < num; i++) {
				TelemetryInfoEntity telemetryInfoEntity = new TelemetryInfoEntity();
				int infoAddress = (infoElement[i * 8 + 2] << 16) + (infoElement[i * 8 + 1] << 8) + infoElement[i *8];
				telemetryInfoEntity.setInfoAddress(infoAddress);
				//浮点数1
				String floatOne = Iec104Util.toHexStringNo0x(infoElement[i * 8 + 3]);
				//浮点数2
				String floatTwo = Iec104Util.toHexStringNo0x(infoElement[i * 8 + 4]);
				//浮点数3
				String floatThree = Iec104Util.toHexStringNo0x(infoElement[i * 8 + 5]);
				//浮点数4
				String floatFour = Iec104Util.toHexStringNo0x(infoElement[i * 8 + 6]);
				//计算最终的短浮点值
				telemetryInfoEntity.setFloatValue(Bytes2Float_IEEE754(floatOne+ floatTwo+ floatThree +floatFour));
				//和类型标识符一样的值
				telemetryInfoEntity.setType(13);
				int qds = infoElement[i * 8 + 7];
				telemetryInfoEntity.setQds(qds);
				telemetryInfoEntitys.add(telemetryInfoEntity);
				builder.append("信息对象");
				builder.append((i + 1));
				builder.append("的地址：");
				builder.append(new ASDU().InfoAddress(infoElement[i * 8],
						infoElement[i * 8 + 1], infoElement[i * 8 + 2]));
				builder.append("\n");
				builder.append("IEEE STD745短浮点数:");
				builder.append(floatOne);
				builder.append("\t");
				builder.append(floatTwo);
				builder.append("\t");
				builder.append(floatThree);
				builder.append("\t");
				builder.append(floatFour);
				builder.append("\t");
				builder.append("\n");
				builder.append("品质描述词QDS:");
				builder.append(Iec104Util.toHexString(qds)).append("\n");
			}
			asduEntity.setTelemetryInfoEntityList(telemetryInfoEntitys);
		}

		return builder.toString();
	}

	/**
	 * 归一化值
	 */
	public static float Bytes2Float_NVA(int low, int high) {
		float fVal;
		int nva = (high << 8) + low;
		// 符号位1位，0为正数，1为负数，后面的为补码表示，
		// 正数的补码和原码相同不需要转换。对于负数,先取反码再加1得到补码的补码，就是原码了。
		int symbol = (high & 0x80);//符号位：0表示正数，1表示负数
		if (symbol == 0x80)
			fVal = -1 * (((nva ^ 0xffff) + 1) & 0x7fff);//  (nva ^ 0xffff) + 1 :补码的补码  ，取出后面的15位数据乘上-1得到值
		else fVal = nva;
		return fVal;//这里的32767值的是一个量纲，一般这个参数是主站和从站商定的
	}

    /**
	 * 标度化值
	 */
	public static float Bytes2Float_SVA(int low, int high){
		float fVal;
		int nva = (high << 8) + low;
		int symbol = (high & 0x80);//0表示正数，1表示负数
		if (symbol == 0x80)
			fVal = -1 * (((nva ^ 0xffff) + 1) & 0x7fff);
		else fVal = nva;
		return fVal;
	}

	/**
	 * 短浮点数
	 *
	 * @param data 从低位到高位按顺序
	 * @return
	 */
	public static float Bytes2Float_IEEE754(String data) {

		return Float.intBitsToFloat(Integer.valueOf(data, 16));
	}


}
