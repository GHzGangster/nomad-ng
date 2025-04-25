package nomad.game;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.function.BiConsumer;

public interface IGameController {
	void register(Map<Integer, BiConsumer<ChannelHandlerContext, ByteBuf>> handlers);
}
