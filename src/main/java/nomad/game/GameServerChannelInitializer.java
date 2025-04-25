package nomad.game;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.List;

public class GameServerChannelInitializer extends ChannelInitializer<SocketChannel> {
	private final List<IGameController> controllers;

	public GameServerChannelInitializer(List<IGameController> controllers) {
		this.controllers = controllers;
	}

	@Override
	public void initChannel(SocketChannel ch) {
		ch.pipeline().addLast(new GameServerHandler(controllers));
	}
}
