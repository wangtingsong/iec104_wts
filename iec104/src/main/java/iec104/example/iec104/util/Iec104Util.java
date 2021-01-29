package iec104.example.iec104.util;


import iec104.example.iec104.enums.UControlEnum;
import iec104.example.iec104.message.TeleSignallingInfoEntity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
* @ClassName: Iec104Util
* @Description: 工具类 
* @author YDL 
* @date 2020年5月13日
 */
public class Iec104Util {
	
	private static int controlLength = 4;
	
	/**
	 * I 格式 低位在前
	 * @param accept 接收序列号
	 * @param send 发送序列号
	 * @return
	 */
	public static byte[] getIcontrol(short accept, short send) {
		byte[] control = new byte[4];
		// 向左移动一位 保证低位的D0 是0
		send = (short) (send << 1);
		control[0] =  (byte) ((send));
		control[1]  =  (byte) ((send >> 8));
		accept = (short) (accept << 1);
		control[2] =   (byte) ((accept));
		control[3]  =  (byte) ((accept >> 8));
		return control;
	}
	
	/**
	 * 返回控制域中的接收序号
	 * @param control
	 * @return
	 */
	public  static short getAccept(byte[] control) {
		int accept = 0;
		short  acceptLow =   (short) (control[2] & 0xff);
		short  acceptHigh =   (short) (control[3] & 0xff);
		accept += acceptLow;
		accept += acceptHigh << 8;
		accept = accept >> 1;
		return (short) accept;
		
	}
	
	/**
	 * 返回控制域中的发送序号
	 * @param control
	 * @return
	 */
	public  static short getSend(byte[] control) {
		int send = 0;
		short  acceptLow =  (short) (control[0] & 0xff);
		short  acceptHigh =  (short) (control[1] & 0xff);
		send += acceptLow;
		send += acceptHigh << 8;
		send = send >> 1;
		return (short) send;
	}
	
	/**
	 * S 格式
	 * @param accept
	 * @return
	 */
	public static byte[] getScontrol(short accept) {
		byte[] control = new byte[4];
		// 向左移动一位 保证低位的D0 是0
		short send = 1;
		control[0] =  (byte) ((send));
		control[1]  =  (byte) ((send >> 8));
		accept = (short) (accept << 1);
		control[2] =   (byte) ((accept));
		control[3]  =  (byte) ((accept >> 8));
		return control;
	}

	/**
	 * 
	* @Title: 返回U帧
	* @Description: 判断是否是
	* @param @param control
	* @param @return 
	* @return boolean   
	* @throws
	 */
	public static UControlEnum getUcontrol(byte[] control) {
		if (control.length < controlLength || control[1] != 0 || control[3] != 0 || control[2] != 0) {
			return null;
		}  
		int controlInt = ByteUtil.byteArrayToInt(control);
		for (UControlEnum ucontrolEnum : UControlEnum.values()) {
			if (ucontrolEnum.getValue() == controlInt) {
				return ucontrolEnum;
			}
		}
		return null;
	}
	
	
	
	
	


	/**
	 * 16进制表示的字符串转换为字节数组
	 *
	 * @param s 16进制表示的字符串
	 * @return byte[] 字节数组
	 */
	public static int[] hexStringToIntArray(String s) {
		int len = s.length();
		int[] b = new int[len / 2];
		for (int i = 0; i < len; i += 2) {
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
			b[i / 2] = (int) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return b;
	}

	/**
	 * 解析地址域
	 *
	 * @param low  第一个地址
	 * @param high 第二个地址
	 * @return
	 */
	public static String address(int low, int high) {

		String lowString = String.format("%02X", low);
		String highString = String.format("%02X", high);

		return highString + lowString;
	}

	/**
	 * int转换成16进制字符串
	 *
	 * @param b 需要转换的int值
	 * @return 16进制的String
	 */
	public static String toHexString(int b) {
		String hex = Integer.toHexString(b & 0xFF);
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		return "0x" + hex.toUpperCase();
	}

	/**
	 * int转换成16进制字符串 不需要0x
	 *
	 * @param b 需要转换的int值
	 * @return 16进制的String
	 */
	public static String toHexStringNo0x(int b) {
		String hex = Integer.toHexString(b & 0xFF);
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		return hex.toUpperCase();
	}

	/**
	 * 时标CP56Time2a解析
	 *
	 * @param b 时标CP56Time2a（长度为7 的int数组）
	 * @return 解析结果
	 */
	public static String TimeScale(int b[], TeleSignallingInfoEntity teleSignallingInfoEntity) {

		String str = "";
		int year = (b[6] & 0x7F)+ 2000;
		int month = b[5] & 0x0F;
		int day = b[4] & 0x1F;
		int week = (b[4] & 0xE0) / 32;
		int hour = b[3] & 0x1F;
		int minute = b[2] & 0x3F;
		int second = (b[1] << 8) + b[0];
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, day);
		calendar.set(Calendar.HOUR, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second/1000);
		Date date = calendar.getTime();
		teleSignallingInfoEntity.setTime(date);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(simpleDateFormat.format(date));
		str += "时标CP56Time2a:" + year + "-"
				+ String.format("%02d", month) + "-"
				+ String.format("%02d", day) + "," + hour + ":" + minute + ":"
				+ second / 1000 + "." + second % 1000;
		return str + "\n";
	}

}
