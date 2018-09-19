
package com.jfixby.oxygen.coin;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.jfixby.scarabei.api.debug.Debug;

public class ShitCoin {
	public final CoinSign sign;

	ShitCoin (final CoinSign sign, final double shitValue) {
		Debug.checkNull(shitValue);
		Debug.checkNull(sign);
		this.sign = sign;
		this.v = shitValue;
	}

	@Override
	public String toString () {
		final NumberFormat format = new DecimalFormat("#.00000");
		return format.format(this.v).replaceAll("\\G0", " ") + " " + this.sign.symbol;
// return this.v + " BTC";

// return "" + this.v + " DCR";
	}

	public double v;

	public ShitCoin newCoin (final double d) {
		return new ShitCoin(this.sign, d);
	}

	public static ShitCoin newCoin (final double v, final CoinSign s) {
		return new ShitCoin(s, v);
	}

	public ShitCoin copy () {
		return new ShitCoin(this.sign, this.v);
	}

}
