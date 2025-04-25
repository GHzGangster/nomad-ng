package nomad;

import nomad.common.NomadAllocator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NomadAllocatorTest {
	@Test
	public void whenCapacityIsValidReturnedBufferIsValid() {
		var allocator = new NomadAllocator();
		allocator.start();

		var buffer = allocator.buffer(1024);
		allocator.finish();

		Assertions.assertNotNull(buffer);
	}

	@Test
	public void whenFinishIsCalledBufferIsReleased() {
		var allocator = new NomadAllocator();
		allocator.start();
		var buffer = allocator.buffer(1024);

		var referenceCountBefore = buffer.refCnt();
		allocator.finish();
		var referenceCountAfter = buffer.refCnt();

		Assertions.assertEquals(1, referenceCountBefore);
		Assertions.assertEquals(0, referenceCountAfter);
	}
}
