package com.baselib.phase;

import java.net.URL;

public class PhaseContext {

	private URL[] urlsToScan;

//TODO handle this make this parametrik..
	
	private String ignoredPackages;

	public PhaseContext(URL[] urlsToScan) {
		
		this.urlsToScan = urlsToScan;
	}
	/**
	 * @return the urlsToScan
	 */
	public URL[] getUrlsToScan() {
		
		return urlsToScan;
	}

	/**
	 * @param urlsToScan the urlsToScan to set
	 */
	public void setUrlsToScan(URL[] urlsToScan) {
		
		this.urlsToScan = urlsToScan;
	}
}
