package iec104.example.iec104.handler;

import iec104.example.iec104.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;


/**
 * 解码器
 * @author Admin
 *
 */
public class DataDecoder extends ByteToMessageDecoder {
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		byte[] data = new byte[in.readableBytes()];
		in.readBytes(data);
		String msg = ByteUtil.byteArrayToHexString(data);
		out.add(msg);
	}
}
