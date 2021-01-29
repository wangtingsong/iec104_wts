package iec104.example.iec104.handler;

import iec104.example.iec104.Iec104Constant;
import iec104.example.iec104.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
* @ClassName: Check104Handler  
* @Description: 检查104报文 
* @author YDL 
* @date 2020年5月13日
 */
public class Check104Handler extends ChannelInboundHandlerAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelInboundHandlerAdapter.class);
	
	/**
	 * 拦截系统消息
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf result = (ByteBuf) msg;
		byte[] bytes = new byte[result.readableBytes()];
		result.readBytes(bytes);
		LOGGER.info("接收到的报文: " + ByteUtil.byteArrayToHexString(bytes));
		if (bytes.length < Iec104Constant.APCI_LENGTH || bytes[0] != Iec104Constant.HEAD_DATA) {
			LOGGER.error("报文无效");
			ReferenceCountUtil.release(result);
		} else {
			result.writeBytes(bytes);
			ctx.fireChannelRead(msg);
		}
	}
}
