package iec104.example.iec104.handler;

import iec104.example.iec104.Iec104Constant;
import iec104.example.iec104.util.ByteUtil;
import iec104.example.iec104.util.Iec104Util;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
* @ClassName: SysSframeInboundHandler  
* @Description: 处理S帧的问题 
* @author YDL 
* @date 2020年5月13日
 */
public class SysSframeHandler extends ChannelInboundHandlerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelInboundHandlerAdapter.class);
	
	/**
	 * 拦截系统消息 
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf result = (ByteBuf) msg;
		byte[] bytes = new byte[result.readableBytes()];
		result.readBytes(bytes);
		if (isSysInstruction(bytes)) {
			LOGGER.info("收到S帧" + Iec104Util.getAccept(ByteUtil.getByte(bytes, 2, 4)));
			ReferenceCountUtil.release(result);
			return;
		} 
		result.writeBytes(bytes);
//		LOGGER.info("普通指令");
		ctx.fireChannelRead(result);
	}

	/**
	 * 
	* @Title: isSysInstruction  
	* @Description: TODO  判断是否是 系统报文
	* @param @param bytes
	* @param @return 
	* @return boolean   
	* @throws
	 */
	private boolean isSysInstruction(byte[] bytes) {
		if (bytes.length != Iec104Constant.APCI_LENGTH) {
			return false;
		}
		if (bytes[Iec104Constant.ACCEPT_LOW_INDEX] == 1 && bytes[Iec104Constant.ACCEPT_HIGH_INDEX] == 0) {
			// 判断S帧的方法
			return true;
		}
		// U帧只有6字节
		return false;
	}
}
