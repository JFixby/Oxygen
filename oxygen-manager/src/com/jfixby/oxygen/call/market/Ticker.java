
package com.jfixby.oxygen.call.market;

import com.jfixby.oxygen.coin.BTC;
import com.jfixby.oxygen.coin.CoinSign;
import com.jfixby.oxygen.coin.MarketPair;
import com.jfixby.oxygen.coin.ShitCoin;
import com.jfixby.scarabei.api.err.Err;
import com.jfixby.scarabei.api.sys.Sys;

public class Ticker {
	public boolean success;
	public String message;
	public TickerResult result;
	public CoinSign symbol;
	public MarketPair pair;

	@Override
	public String toString () {
		if (this.pair == null) {
			return this.result.toString();
		}
		return this.pair.toString() + " BID " //
			+ new BTC(this.result.Bid);
	}

	public BTC toBTC (final ShitCoin coins) {
		if (this.symbol == coins.sign) {
			return new BTC(this.result.Last * coins.v);
		} else {
			Err.reportError("Sign mistamch: ticker=" + this.symbol + " coin=" + coins.sign);
			Sys.exit();
		}
		return null;
	}

	public ShitCoin fromBTC (final BTC btc) {
		final Double v = btc.v / this.result.Last;
		return ShitCoin.newCoin(v, this.symbol);
	}

}
