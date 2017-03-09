package org.s2n.ddt.util.threads;

import java.util.concurrent.ThreadFactory;

public class PoolOptions {

	private int maxThreads = 5;
	private int coreThreads = 2;
	private ThreadFactory factory;
	
    public PoolOptions() {
        super();

    }
    public PoolOptions(int maxThreads, int coreThreads) {
        super();
        this.maxThreads = maxThreads;
        this.coreThreads = coreThreads;
    }

    public final int getMaxThreads() {
		return maxThreads;
	}
	public final void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}
	public final int getCoreThreads() {
		return coreThreads;
	}
	public final void setCoreThreads(int coreThreads) {
		this.coreThreads = coreThreads;
	}
	public final ThreadFactory getFactory() {
		return factory;
	}
	public final void setFactory(ThreadFactory factory) {
		this.factory = factory;
	}
	
	
}
