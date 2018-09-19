
package com.jfixby.oxygen.usd;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.jfixby.scarabei.api.debug.Debug;

public class USD {

	public USD (final Double USD) {
		Debug.checkNull(USD);
		this.v = USD;
	}

	@Override
	public String toString () {
		final NumberFormat format = new DecimalFormat("#.0000");
		return format.format(this.v).replaceAll("\\G0", " ") + "$";
// return this.v + " USD";
	}

	public final double v;

	public static USD linearPrice (final USD x, final double aX, final USD y, final double aY) {
		return new USD((x.v * aX + y.v * aY) / (aX + aY));
	}

	public USD copy () {
		return new USD(this.v);
	}

	public boolean isEpsilonEqual (final USD other, final double e) {
		return Math.abs(other.v - this.v) < e;
	}
}
