
package com.jfixby.oxygen.target;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.jfixby.oxygen.Percentage;
import com.jfixby.oxygen.call.market.Ticker;
import com.jfixby.oxygen.call.pool.PoolPerformance;
import com.jfixby.oxygen.coin.BTC;
import com.jfixby.oxygen.coin.CoinSign;
import com.jfixby.oxygen.coin.ShitCoin;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.log.L;

public class TargetPrice {

	private final Ticker btcdcr;
	private final PoolPerformance pool;

	public TargetPrice (final Ticker btcdcr, final PoolPerformance pool) {
		this.btcdcr = btcdcr;
		this.pool = pool;
		final CoinSign coin = pool.perFlowUnitX24H().coinsX24H().sign;
		final CoinSign ticker = btcdcr.symbol;
		Debug.checkTrue(coin + " = " + ticker, coin == ticker);
	}

	public BTC buildThsx24HBTCPrice (final Percentage percentOffsetPenalty) {
		final ShitCoin dcr24H = this.pool.perFlowUnitX24H().coinsX24H();
		final BTC btc24H = this.btcdcr.toBTC(dcr24H);
		return new BTC(btc24H.v * (1d - percentOffsetPenalty.share()));
	}

	public void print () {
		final Percentage percent = new Percentage(12f);
		for (int i = 1; percent.v < 23; i++) {
			percent.v = 5 + i * 0.25f;
			final BTC ajusted = this.buildThsx24HBTCPrice(percent);
			final NumberFormat THformat = new DecimalFormat("0.000000");
			L.d(THformat.format(ajusted.v), "=> " + percent);
		}
	}

	public Percentage getPercantageFromPrice (final BTC price) {
		final ShitCoin dcr24H = this.pool.perFlowUnitX24H().coinsX24H();
		final BTC btc24H = this.btcdcr.toBTC(dcr24H);
		final double profit = 1d - price.v / btc24H.v;
		final double percent = profit * 100d;
		final float P = (float)percent;
		return new Percentage(P);
	}

}
