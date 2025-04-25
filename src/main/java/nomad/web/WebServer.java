package nomad.web;

import io.jooby.Jooby;
import io.jooby.exception.NotAcceptableException;
import nomad.common.Database;
import nomad.common.NomadAllocator;
import nomad.common.service.AccountService;
import nomad.common.service.NewsService;
import nomad.game.PacketCollection;
import nomad.web.controller.AccountWebController;
import nomad.web.controller.NewsWebController;

import java.nio.charset.StandardCharsets;

public class WebServer extends Jooby {

	public NomadAllocator allocator;

	{
		var jdbi = Database.getJdbi();

		var accountService = new AccountService(jdbi);
		var newsService = new NewsService(jdbi);

		allocator = new NomadAllocator();

		before(ctx -> {
			allocator.start();
		});

		after((ctx, result, failure) -> {
			if (failure != null) {
				allocator.finish();
			}
		});

		encoder((ctx, value) -> {
			String string;
			if (value instanceof PacketCollection packetCollection) {
				string = packetCollection.encode();
			} else {
				string = value.toString();
			}
			allocator.finish();
			if (ctx.accept(ctx.getResponseType())) {
				return ctx.getBufferFactory().wrap(string.getBytes(StandardCharsets.UTF_8));
			}
			throw new NotAcceptableException(ctx.header("Accept").valueOrNull());
		});

		get("/", ctx -> "Hello world!");

		var accountController = new AccountWebController(accountService);
		accountController.use(this);

		var newsController = new NewsWebController(newsService);
		newsController.use(this);
	}

	public static void main(String[] args) {
		runApp(args, WebServer::new);
	}
}
