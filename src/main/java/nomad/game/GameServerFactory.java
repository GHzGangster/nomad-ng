package nomad.game;

import nomad.common.Database;
import nomad.common.NomadAllocator;
import nomad.common.service.NewsService;
import nomad.game.controller.EchoGameController;
import nomad.game.controller.NewsGameController;
import org.jdbi.v3.core.Jdbi;

import java.util.ArrayList;

public class GameServerFactory {
	public static GameServer createGameServer() {
		var jdbi = Database.getJdbi();
		return createGameServer(jdbi);
	}

	public static GameServer createGameServer(Jdbi jdbi) {
		var allocator = new NomadAllocator();

		var controllers = new ArrayList<IGameController>();

		controllers.add(new EchoGameController());

		var newsService = new NewsService(jdbi);
		controllers.add(new NewsGameController(allocator, newsService));

		return new GameServer(controllers);
	}
}
