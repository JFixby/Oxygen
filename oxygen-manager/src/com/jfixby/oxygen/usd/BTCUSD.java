
package com.jfixby.oxygen.usd;

import com.jfixby.oxygen.coin.BTC;

public class BTCUSD {

	private final Double btcusd_rate;

	public BTCUSD (final Double btcusd_rate) {
		this.btcusd_rate = btcusd_rate;
	}

	public USD toUSD (final BTC btc) {
		final double usd_val = btc.v * this.btcusd_rate;
		final USD r = new USD(usd_val);
		return r;
	}

	@Override
	public String toString () {
		return "BTC/USD = " + this.btcusd_rate;
	}

}
