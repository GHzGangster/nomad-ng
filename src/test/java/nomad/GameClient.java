package nomad;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class GameClient {
	public record ChannelActiveResult(ChannelHandlerContext ctx) {
	}

	public record ChannelReadResult(ChannelHandlerContext ctx, Object msg) {
	}

	private static final Logger logger = LogManager.getLogger();

	private MultiThreadIoEventLoopGroup workerGroup;

	private CompletableFuture<ChannelActiveResult> channelActiveFuture = new CompletableFuture<>();

	private CompletableFuture<ChannelReadResult> channelReadFuture = new CompletableFuture<>();

	private final Object channelEventsLock = new Object();

	public CompletableFuture<Void> connect() {
		workerGroup = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());

		var bootstrap = new Bootstrap();

		bootstrap.group(workerGroup);

		bootstrap.channel(NioSocketChannel.class);

		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

		bootstrap.handler(new ChannelInboundHandlerAdapter() {
			@Override
			public void channelActive(ChannelHandlerContext ctx) {
				synchronized (channelEventsLock) {
					channelActiveFuture.complete(new ChannelActiveResult(ctx));
				}
			}

			@Override
			public void channelRead(ChannelHandlerContext ctx, Object msg) {
				synchronized (channelEventsLock) {
					channelReadFuture.complete(new ChannelReadResult(ctx, msg));
				}
			}
		});

		var future = new CompletableFuture<Void>();
		bootstrap.connect("localhost", 5730).addListener(e -> future.complete(null));
		return future;
	}

	public CompletableFuture<Void> disconnect() {
		return disconnect(2L, 15L, TimeUnit.SECONDS);
	}

	public CompletableFuture<Void> disconnect(long quietPeriod, long timeout, TimeUnit unit) {
		var future = new CompletableFuture<Void>();
		workerGroup.shutdownGracefully(quietPeriod, timeout, unit).addListener(e -> future.complete(null));
		return future;
	}

	public CompletableFuture<ChannelActiveResult> onChannelActive() {
		synchronized (channelEventsLock) {
			var result = channelActiveFuture;
			if (result.isDone()) {
				channelActiveFuture = new CompletableFuture<>();
			}
			return result;
		}
	}

	public CompletableFuture<ChannelReadResult> onChannelRead() {
		synchronized (channelEventsLock) {
			var result = channelReadFuture;
			if (result.isDone()) {
				channelReadFuture = new CompletableFuture<>();
			}
			return result;
		}
	}
}
