package org.s2n.ddt.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;

public class ReflectUtils {
	private static final Logger logger = LogManager.getLogger(ReflectUtils.class);

	public static String fillMethods(Class clz, Class[] argsCLasses, Map<String, Method> methods) {
		String rtn = null;
		synchronized (methods) {
			try {
				Method[] methodsAvail = clz.getMethods();
				boolean match = false;
				for (Method mtd : methodsAvail) {
					Class rtnM = mtd.getReturnType();
					// if (rtnM.isAssignableFrom( rtnType)) {
					// continue;
					// }
					Class[] args = mtd.getParameterTypes();
					if (argsCLasses.length != args.length) {
						continue;
					}
					match = true;
					for (int i = 0; i < argsCLasses.length; i++) {
						if (args[i] != argsCLasses[i]) {
							match = false;
						}
					}
					if (match) {
						methods.put(mtd.getName(), mtd);
					}
				}
			} catch (Throwable e) {
				rtn = "ERROR :" + e;
				logger.error("Err fill methods :" + e, e);

			}

		}
		return rtn;
	}

	public static void main(String[] args) {
		Class[] argsCLasses = new Class[] { String.class, String.class, Integer.class };
		Map<String, Method> methods = new HashMap<String, Method>();
		// Class clz, Class[] argsCLasses, Map<String, Method> methods
		fillMethods(Foo.class, argsCLasses, methods);
		System.out.println(methods);
		System.out.println();
		System.out.println(methods.keySet());
	}
}

class Foo {

	public void methodA(String a, String b, int i) {

	}

	public void methodB(String a2, String b2, int i2) {

	}

	public void methodB(String a2, int i2) {

	}

	public void methodD(String a2, String b2, int i2) {

	}

}
