
package com.jfixby.oxygen.dcr;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.jfixby.scarabei.api.debug.Debug;

public class DCR {

	public DCR (final Double val) {
		Debug.checkNull(val);
		this.v = val;
	}

	@Override
	public String toString () {
		final NumberFormat format = new DecimalFormat("#.0000");
		return format.format(this.v).replaceAll("\\G0", " ") + " DCR";
// return this.v + " USD";
	}

	public final double v;

	public static DCR linearPrice (final DCR x, final double aX, final DCR y, final double aY) {
		return new DCR((x.v * aX + y.v * aY) / (aX + aY));
	}

	public DCR copy () {
		return new DCR(this.v);
	}

	public boolean isEpsilonEqual (final DCR other, final double e) {
		return Math.abs(other.v - this.v) < e;
	}
}
