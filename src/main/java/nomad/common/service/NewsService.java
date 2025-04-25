package nomad.common.service;

import nomad.common.message.GetNewsItemsMessage;
import nomad.common.record.News;
import org.jdbi.v3.core.Jdbi;

public class NewsService {
	private final Jdbi jdbi;

	public NewsService(Jdbi jdbi) {
		this.jdbi = jdbi;
	}

	public GetNewsItemsMessage getNewsItems() {
		var message = new GetNewsItemsMessage();

		try (var handle = jdbi.open()) {
			var newsItems = handle.createQuery("select * from news").mapTo(News.class).list();
			message.setNewsItems(newsItems);
		}

		return message;
	}

	public void addNewsItem(News news) {
		try (var handle = jdbi.open()) {
			handle.createUpdate("insert into news (title, body) VALUES (:title, :body)")
				.bind("title", news.getTitle()).bind("body", news.getBody()).execute();
		}
	}
}
