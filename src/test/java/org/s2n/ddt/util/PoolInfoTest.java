package org.s2n.ddt.util;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.junit.Test;

import org.s2n.ddt.util.threads.PoolOptions;
import org.s2n.ddt.util.threads.DdtPools;

public class PoolInfoTest {
	private static final Logger logger = LogManager.getLogger(PoolInfoTest.class);

	@Test
	public void test() {
		PoolOptions options = new PoolOptions();
		options.setCoreThreads(2);
		options.setMaxThreads(33);
		DdtPools.initPool("a", options);
		DdtPools.initPool("b", options);
		Do1 p = null;
		String poolName = "a";
		int i = 1;
		for (; i < 31; i++) {
			p = new Do1();
			if(i > 10){
				poolName = "b";
			}
			DdtPools.offer(poolName, p);

		}
		//i--;
		printIno();
		LangUtils.sleep(3 + (int) (Math.random() * 3));
		org.junit.Assert.assertNotNull(p);
		org.junit.Assert.assertEquals( (i-1), Do1.getLs());
	}

	private void printIno() {
		try {
			List<String> pools = DdtPools.getPoolNames();
			for(String pool : pools){
				ThreadPoolExecutor exe = DdtPools.getPool(pool);
				int activeCnt = exe.getActiveCount();
				long taskCnt = exe.getCompletedTaskCount();
				int maxPoolSize = exe.getMaximumPoolSize();
				int largestPoolSize = exe.getLargestPoolSize();
				StringBuilder sb = new StringBuilder();
				sb.append("\n<br>Active Cnt :").append(activeCnt);
				sb.append("Completed task Cnt :").append(taskCnt);
				sb.append("max Pool Size :").append(maxPoolSize);
				sb.append("Reached largestPoolSize :").append(largestPoolSize);
				sb.append("Current pool size :").append(exe.getPoolSize());
				
			}
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		
	}

	static class Do1 implements Runnable {
		volatile static long l = 0;

		public Do1() {
			l++;

		}

		public void run() {

			LangUtils.sleep(5000 + (int) (Math.random() * 30));
			System.out.println("hi " + l);
		}

		public static long getLs() {
			return l;
		}
	}
}

