package org.s2n.ddt.util.threads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;

import org.s2n.ddt.bean.UtlConf;
import org.s2n.ddt.util.LangUtils;
public class DdtPools {
	private static final Logger logger = LogManager.getLogger(DdtPools.class);
	
	public static final String DEFAULT_POOL = "_d";
	private static Map<String, ThreadPoolExecutor> pools = new HashMap<String, ThreadPoolExecutor>(1);
	//private static java.util.concurrent.ThreadPoolExecutor threadExe;
	static {
		try {
			PoolOptions opt = new PoolOptions();
			DdtThreadFactory threadFac = new DdtThreadFactory(DEFAULT_POOL);
			opt.setFactory(threadFac);
			reinitDefault(opt);
		} catch (Throwable e) {
			logger.warn("init a:" + e, e);
		}
	}
	private static void reinitDefault(PoolOptions opt) {
		
		initPool(DEFAULT_POOL, opt);
	}
	
	public static void initPool(String poolName, PoolOptions options) {
		ThreadPoolExecutor pool = pools.get(poolName);
		if(pool != null) {
			logger.info("Reinit " + poolName + ", " + options);
		}
		ThreadFactory threadFactory = new DdtThreadFactory(poolName);//TODO
		long keepAliveTime = 5;
		TimeUnit unit = TimeUnit.MINUTES;
		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
		RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
		pool = new ThreadPoolExecutor(options.getCoreThreads(), options.getMaxThreads(), keepAliveTime, unit, workQueue, threadFactory, handler);
		pools.put(poolName, pool);
	}
	
	public static void offer(String poolName, Runnable job) {
		ThreadPoolExecutor pool = pools.get(poolName);
		if(pool == null) {
			logger.warn("Pool " + poolName + ", not found using default pool ");
			pool = pools.get(DEFAULT_POOL);
		}
		pool.execute(job);
	}
	
	public static ThreadPoolExecutor getPool(String poolName) {
		ThreadPoolExecutor pool = pools.get(poolName);
		if(pool == null) {
			logger.warn("Pool " + poolName + ", not found giving default pool ");
			pool = pools.get(DEFAULT_POOL);
		}
		return pool;
	}
	
	public static List <String> getPoolNames() {
		ArrayList <String> a = new ArrayList<String>();
		a.addAll(pools.keySet());
		return a;
	}
	
	public static String remove(String pool){
		if(pool == null){
			return "Error : null pool name";
		}
		if(DEFAULT_POOL.equals(pool)){
			return "Error : cannot remove default";
		}
		Object s = pools.remove(pool);
		if(s == null){
			return "Pool not found :" + pool;
		}
		return "Removed :" + pool;
	}
	
	public static String stop(String poolName, boolean hardStop){
		StringBuilder msg = new StringBuilder();
		try {
			ThreadPoolExecutor tpe = DdtPools.getPool(poolName);
			if(tpe == null){
				msg.append(" Not found " + poolName + "<br>");
				return msg.toString();
			}
			tpe.shutdown();
			if (hardStop) {
				int tim = 2000;
				String sTim = UtlConf.getProperty("POOL_SHUTDOWN_TIMEOUT", tim + "");
				tim = LangUtils.getInt(sTim, tim, "timeout for shutdown of master pool : " + poolName);
				// TODO put this in a task with time out
				if (!tpe.awaitTermination(tim, TimeUnit.MILLISECONDS)) {
					msg.append("Shutdown now<br>");
					tpe.shutdownNow();
				}
				List<String> rt = org.s2n.ddt.util.threads.ThrdsStop.stopThatMatch(poolName, tpe);
				for (String ss : rt) {
					msg.append(ss + "<br>");
				}
			}
		} catch (Throwable e) {
			logger.warn("shutdown pool : " + poolName + ", " + e, e);
			msg.append("Error :" + e + "<br>");
		}
		return msg.toString();
	}
	
}
