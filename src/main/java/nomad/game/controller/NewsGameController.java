package nomad.game.controller;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import nomad.common.BufferUtil;
import nomad.common.NomadAllocator;
import nomad.common.service.NewsService;
import nomad.game.IGameController;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.BiConsumer;

public class NewsGameController implements IGameController {
	private final NomadAllocator allocator;
	private final NewsService newsService;

	public NewsGameController(NomadAllocator allocator, NewsService newsService) {
		this.allocator = allocator;
		this.newsService = newsService;
	}

	@Override
	public void register(Map<Integer, BiConsumer<ChannelHandlerContext, ByteBuf>> handlers) {
		handlers.put(0x2008, this::getNewsItems);
	}

	private void getNewsItems(ChannelHandlerContext ctx, ByteBuf in) {
		var buffers = new ArrayList<ByteBuf>();
		var message = newsService.getNewsItems();
		System.out.println(message);

		for (var newsItem : message.getNewsItems()) {
			var buffer = allocator.buffer(1023);
			buffers.add(buffer);

			buffer.writeInt((int) newsItem.getId()).writeBoolean(newsItem.isImportant())
				.writeInt((int) newsItem.getTime().toEpochSecond());

			BufferUtil.writeString(buffer, newsItem.getTitle(), StandardCharsets.ISO_8859_1, 128);
			BufferUtil.writeString(buffer, newsItem.getBody(), StandardCharsets.ISO_8859_1, 886);
		}

		for (var buffer : buffers) {
			ctx.write(buffer);
		}
	}
}
