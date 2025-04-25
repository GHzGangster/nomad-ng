package nomad.common;

import io.netty.channel.ChannelHandlerContext;
import nomad.common.service.AccountService;
import nomad.common.service.NewsService;
import org.jdbi.v3.core.Jdbi;

public class NomadContext {
	public Jdbi jdbi;
	public ChannelHandlerContext channelHandlerContext;

	public AccountService accountService;
	public NewsService newsService;
}
