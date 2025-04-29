package nomad.game;

import nomad.common.Database;
import nomad.common.NomadAllocator;
import nomad.common.Services;
import nomad.common.ServicesFactory;
import nomad.common.record.News;
import nomad.common.service.AccountService;
import nomad.common.service.NewsService;
import nomad.game.controller.EchoGameController;
import nomad.game.controller.NewsGameController;
import org.jdbi.v3.core.Jdbi;

import java.util.ArrayList;

public class GameServerFactory {
	public static GameServer createGameServer() {
		var jdbi = Database.getJdbi();
		var services = ServicesFactory.createServices(jdbi);
		return createGameServer(services);
	}

	public static GameServer createGameServer(Services services) {
		var allocator = new NomadAllocator();

		var controllers = new ArrayList<IGameController>();

		controllers.add(new EchoGameController());

		controllers.add(new NewsGameController(allocator, services.getNewsService()));

		return new GameServer(controllers);
	}
}
