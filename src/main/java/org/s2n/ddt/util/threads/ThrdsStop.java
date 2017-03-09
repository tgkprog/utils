package org.s2n.ddt.util.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;

public class ThrdsStop {
	private static final Logger logger = LogManager.getLogger(ThrdsStop.class);

	public static List<String> stopThatMatch(String thrdsName, ThreadPoolExecutor tpe){
		
		List<String> result = new ArrayList<String>();
		ThreadFactory tfm = tpe.getThreadFactory();
		//TODO get all groups if not DdtFactory
		if(tfm instanceof DdtThreadFactory){
			DdtThreadFactory tf = (DdtThreadFactory)tfm;
			ThreadGroup tg = tf.getGroup();
			String name = tf.getNamePrefix();
			int cnt = tg.activeCount();
			Thread[] thrds = new Thread[cnt + 50];
			int cnt2 = tg.enumerate(thrds);
			for(int i = 0; i < cnt2; i++){
				String thNm = thrds[i].getName();
				if(thNm.startsWith(name)){
					StringBuilder msg = new StringBuilder().append(thNm);
					boolean excptn = false;
					try {
						thrds[i].interrupt();
						msg.append(" interrupt okay;");
					} catch (Throwable e) {
						msg.append(" interrupt : " + e + ";");
						logger.warn("thrd stop " + name + ", interrupt " + e , e);
					}
					try {
						thrds[i].stop(new RuntimeException ("Pool Stop"));
						msg.append(" stop okay ");
					} catch (Throwable e) {
						msg.append(" stop : " + e + ";");
						logger.warn("thrd stop " + name + ",  stop " + e , e);
					}
					result.add(thNm.toString());
				}
			}
		}
		return result;
	}
}
