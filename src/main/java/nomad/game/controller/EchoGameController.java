package nomad.game.controller;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import nomad.game.IGameController;

import java.util.Map;
import java.util.function.BiConsumer;

public class EchoGameController implements IGameController {
	@Override
	public void register(Map<Integer, BiConsumer<ChannelHandlerContext, ByteBuf>> handlers) {
		handlers.put(0x0001, this::echo);
	}

	private void echo(ChannelHandlerContext ctx, ByteBuf in) {
		ctx.write(in);
	}
}
