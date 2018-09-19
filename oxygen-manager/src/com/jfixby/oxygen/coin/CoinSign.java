
package com.jfixby.oxygen.coin;

import com.jfixby.oxygen.call.Algo;
import com.jfixby.scarabei.api.err.Err;

public enum CoinSign {
	BITCOIN("BTC", Algo.SHA256), DECRED("DCR", Algo.DECRED), TETHER("USDT", Algo.USD), DASH("DASH", Algo.X11), VERTCOIN("VTC",
		Algo.LYRA), ZCASH("ZEC", Algo.EQUIHASH), MONA("MONA", Algo.LYRA);

	public final String symbol;
	public final Algo algo;

	CoinSign (final String symbol, final Algo algo) {
		this.symbol = symbol;
		this.algo = algo;
	}

	public static CoinSign resolve (final String s) {
		for (final CoinSign sign : CoinSign.values()) {
			if (s.toLowerCase().equals(sign.symbol.toLowerCase())) {
				return sign;
			}
		}
		Err.reportError("Unknown Symbol " + s);
		return null;
	}

}
