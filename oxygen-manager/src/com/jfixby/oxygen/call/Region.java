
package com.jfixby.oxygen.call;

import com.jfixby.scarabei.api.err.Err;

public enum Region {
	EU(0), US(1);

	public final String code;

	Region (final int code) {
		this.code = code + "";
	}

	public static Region resolve (final String region) {
		if (region.toLowerCase().equals("eu")) {
			return EU;
		}
		if (region.toLowerCase().equals("us")) {
			return US;
		}
		Err.reportError("Unknown region " + region);
		return null;
	}

}
