
package com.jfixby.oxygen.coin;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.jfixby.scarabei.api.debug.Debug;

public class BTC {

	public static final BTC STEP = new BTC(0.0001);

	public BTC (final Double btc) {
		Debug.checkNull(btc);
		this.v = btc;
	}

	@Override
	public String toString () {
		final NumberFormat format = new DecimalFormat("#.000000");
		return format.format(this.v).replaceAll("\\G0", " ") + " BTC";
// return this.v + " BTC";
	}

	public final double v;

	public static BTC linearPrice (final BTC x, final double aX, final BTC y, final double aY) {
		return new BTC((x.v * aX + y.v * aY) / (aX + aY));
	}

	public BTC copy () {
		return new BTC(this.v);
	}

	public boolean isEpsilonEqual (final BTC other, final double e) {
		return Math.abs(other.v - this.v) < e;
	}
}
