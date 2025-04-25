package nomad.common;

import nomad.common.record.Account;
import nomad.common.record.Chara;
import nomad.common.record.Lobby;
import nomad.common.record.News;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;

public class Database {
	public static Jdbi getJdbi() {
		var url = "jdbc:postgresql://localhost:15432/nomad";
		var username = "postgres";
		var password = "password";
		return getJdbi(url, username, password);
	}

	public static Jdbi getJdbi(String url, String username, String password) {
		var jdbi = Jdbi.create(url, username, password);

		jdbi.registerRowMapper(BeanMapper.factory(Account.class));
		jdbi.registerRowMapper(BeanMapper.factory(Chara.class));
		jdbi.registerRowMapper(BeanMapper.factory(Lobby.class));
		jdbi.registerRowMapper(BeanMapper.factory(News.class));

		return jdbi;
	}
}
