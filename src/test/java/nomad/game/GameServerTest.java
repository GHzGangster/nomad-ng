package nomad.game;

import io.netty.buffer.ByteBuf;
import nomad.BaseGameControllerTest;
import nomad.Bytes;
import nomad.BytesAssert;
import nomad.TestUtil;
import org.junit.jupiter.api.Test;

public class GameServerTest extends BaseGameControllerTest {
	@Test
	public void echoTest() {
		var activeResult = client.onChannelActive().join();
		activeResult.ctx().writeAndFlush(TestUtil.toBuffer("00000001" + "aabbccdd"));
		var readResult = client.onChannelRead().join();
		var buffer = (ByteBuf) readResult.msg();

		BytesAssert.assertThat(Bytes.from(buffer)).isEqualTo(Bytes.from("aabbccdd"));
	}
}
