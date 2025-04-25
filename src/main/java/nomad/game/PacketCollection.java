package nomad.game;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.util.List;

public class PacketCollection {
	private final List<ByteBuf> buffers;

	public PacketCollection(List<ByteBuf> buffers) {
		this.buffers = buffers;
	}

	public String encode() {
		var sb = new StringBuilder();
		sb.append("[");
		for (var i = 0; i < buffers.size(); i++) {
			var buffer = buffers.get(i);
			sb.append("\n\t'").append(ByteBufUtil.hexDump(buffer)).append("'");
			if (i < buffers.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("\n]");
		return sb.toString();
	}
}
