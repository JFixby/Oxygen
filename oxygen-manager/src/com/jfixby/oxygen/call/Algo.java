
package com.jfixby.oxygen.call;

import com.jfixby.scarabei.api.err.Err;

public enum Algo {
	DECRED(21), LYRA(14), X11(3), EQUIHASH(24), SHA256(-1), USD(-1);

	public final int code;

	Algo (final int code) {
		this.code = code;
	}

	public static Algo resolve (final int code) {
		for (final Algo algo : Algo.values()) {
			if (algo.code == code) {
				return algo;
			}
		}

		Err.reportError("Unknown code " + code);
		return null;
	}

}
