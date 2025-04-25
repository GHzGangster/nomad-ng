package nomad;

import io.jooby.Server;
import io.netty.buffer.ByteBuf;
import nomad.web.WebServer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

public class AppTest {
	private static final String HOST = "http://localhost:8080";

	private static final OkHttpClient client = new OkHttpClient();

	private Server server;

	private ByteBuf testExceptionBuffer;

	@BeforeEach
	public void setup() {
		var app = new WebServer();
		app.get("/test-exception", ctx -> {
			testExceptionBuffer = app.allocator.buffer(1024);
			throw new RuntimeException("Test exception");
		});
		server = app.start();
	}

	@AfterEach
	public void teardown() {
		server.stop();
	}

	@Test
	public void whenExceptionIsThrownBufferIsReleased() throws IOException {
		var request = new Request.Builder()
			.url(HOST + "/test-exception")
			.build();

		try (var ignored = client.newCall(request).execute()) {
			assertThat(testExceptionBuffer).isNotNull();
			assertThat(testExceptionBuffer.refCnt()).isEqualTo(0);
		}
	}
}
