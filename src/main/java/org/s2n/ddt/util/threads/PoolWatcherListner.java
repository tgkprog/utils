package org.s2n.ddt.util.threads;

public interface PoolWatcherListner {

	/** Called when x scans shows that pool is at zero
	 * where x is configured when adding the watcher
	 */
	public void poolAtZero();
	
	public void poolStopedWithinTimeout();
	
	public void poolForceStopped();
}
