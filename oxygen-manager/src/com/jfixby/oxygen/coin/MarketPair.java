
package com.jfixby.oxygen.coin;

public class MarketPair {

	public final CoinSign top;
	public final CoinSign bottom;

	public MarketPair (final CoinSign top, final CoinSign bottom) {
		this.top = top;
		this.bottom = bottom;
	}

	public static MarketPair newMarketPair (final CoinSign top, final CoinSign bottom) {
		final MarketPair p = new MarketPair(top, bottom);
		return p;
	}

	@Override
	public String toString () {
		return "" + this.top + "/" + this.bottom + "";
	}

	public String toAPIParam () {
		return "" + this.top.symbol + "-" + this.bottom.symbol + "";
	}

}
