package nomad.game;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class GameServerHandler extends ChannelInboundHandlerAdapter {
	private final Map<Integer, BiConsumer<ChannelHandlerContext, ByteBuf>> handlers = new HashMap<>();

	public GameServerHandler(List<IGameController> controllers) {
		for (var controller : controllers) {
			var map = new HashMap<Integer, BiConsumer<ChannelHandlerContext, ByteBuf>>();
			controller.register(map);
			handlers.putAll(map);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof ByteBuf buffer) {
			if (buffer.readableBytes() >= 4) {
				var command = buffer.readInt();
				var handler = handlers.get(command);
				if (handler != null) {
					handler.accept(ctx, buffer);
				}
			}

			ctx.flush();
		} else {
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}
}
