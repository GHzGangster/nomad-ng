package nomad.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;

import java.util.ArrayList;
import java.util.List;

public class NomadAllocator {
	private final ThreadLocal<List<ByteBuf>> allocations = ThreadLocal.withInitial(ArrayList::new);

	public ByteBuf buffer(int initialCapacity) {
		var buffer = Unpooled.buffer(initialCapacity);
		allocations.get().add(buffer);
		return buffer;
	}

	public void start() {
		if (!allocations.get().isEmpty()) {
			System.err.println("There are unhandled previous allocations!");
			release();
		}
	}

	public void finish() {
		release();
	}

	private void release() {
		for (var buffer : this.allocations.get()) {
			ReferenceCountUtil.release(buffer);
		}
		this.allocations.get().clear();
	}
}
