
package com.jfixby.oxygen;

import com.jfixby.scarabei.api.err.Err;

public enum SpeedMagnitude {
	GSols("GSol/s", 1_000_000_000), Sols("Sol/s", 1), THpS("Th/s", 1_000_000_000), GHpS("Gh/s", 1_000_000), MHpS("MHh/s", 1_000);

	public final String sign;
	public final long order;

	SpeedMagnitude (final String sign, final long order) {
		this.sign = sign;
		this.order = order;
	}

	public static SpeedMagnitude parse (final String string) {
		for (final SpeedMagnitude o : SpeedMagnitude.values()) {
			if (o.sign.equalsIgnoreCase(string)) {
				return o;
			}
		}
		Err.reportError("Unknown sign: " + string);
		return null;
	}

}
