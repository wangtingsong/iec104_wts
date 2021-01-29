package iec104.example.iec104.core;


/**
 * 104 规约的基本指令封装
* @ClassName: BasicInstruction104  
* @Description: 返回指定的指令 
* @author YDL 
 */

public class BasicInstruction104 {
	// 68040B 00 00 00
	/**
	 * 初始确认指令
	 */
	public static final byte[] STARTDT_YES = new byte[] {0x68, 0x04, 0x0B, 0x00, 0x00, 0x00};
	
	/**
	 * 链路启动指令
	 */
	public static final byte[] STARTDT = new byte[] {0x68, 0x04, 0x07, 0x00, 0x00, 0x00};
	
	
	/**
	 * 测试确认
	 */
	public static final byte[] TESTFR_YES = new byte[] {0x68, 0x04, (byte) 0x83, 0x00, 0x00, 0x00};
	 
	/**
	 * 测试命令指令
	 */
	public static final byte[] TESTFR = new byte[] {0x68, 0x04, (byte) 0x43, 0x00, 0x00, 0x00};
	
	
	/**
	 * 停止确认
	 */
	public static final byte[] STOPDT_YES = new byte[] {0x68, 0x04, 0x23, 0x00, 0x00, 0x00};
}
