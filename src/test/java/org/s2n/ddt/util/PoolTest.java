package org.s2n.ddt.util;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.junit.Test;

import org.s2n.ddt.util.threads.PoolOptions;
import org.s2n.ddt.util.threads.DdtPools;

public class PoolTest {
	private static final Logger logger = LogManager.getLogger(PoolTest.class);

	@Test
	public void test() {
		PoolOptions options = new PoolOptions();
		options.setCoreThreads(2);
		options.setMaxThreads(33);
		DdtPools.initPool("a", options);
		Do1 p = null;
		for (int i = 0; i < 10; i++) {
			p = new Do1();
			DdtPools.offer("a", p);

		}
		LangUtils.sleep(3 + (int) (Math.random() * 3));
		org.junit.Assert.assertNotNull(p);
		org.junit.Assert.assertEquals(Do1.getLs(), 10);
	}

}

class Do1 implements Runnable {
	volatile static long l = 0;

	public Do1() {
		l++;

	}

	public void run() {

		// LangUtils.sleep(1 + (int) (Math.random() * 3));
		System.out.println("hi " + l);
	}

	public static long getLs() {
		return l;
	}
}
