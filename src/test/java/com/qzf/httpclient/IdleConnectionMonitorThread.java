package com.qzf.httpclient;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.HttpClientConnectionManager;

public class IdleConnectionMonitorThread extends Thread {

	private final HttpClientConnectionManager connMgr;
	private volatile boolean shutdown;
	public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
		super();
		this.connMgr = connMgr;
	}
	
	public void run() {
		try {
			while(!shutdown) {
				synchronized (this) {
					wait(5000);
					//关闭过期的连接
					connMgr.closeExpiredConnections();
					//可选的，关闭闲置时间超过30秒的连接
					connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
				}
			}
		}catch(InterruptedException ex) {
			
		}
	}
	
	public void shutdown() {
		shutdown = true;
		synchronized (this) {
			notifyAll();
		}
	}
}
