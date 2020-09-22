package com.hedong.hedongwx.utils;

import java.security.Provider;

public class BouncyCastleProvider extends Provider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected BouncyCastleProvider(String name, double version, String info) {
		super(name, version, info);
	}

}
