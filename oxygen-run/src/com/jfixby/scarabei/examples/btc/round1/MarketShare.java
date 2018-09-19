
package com.jfixby.scarabei.examples.btc.round1;

import java.io.IOException;

import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;
import com.jfixby.scarabei.examples.btc.coinmarket.Coinmarketcap;
import com.jfixby.scarabei.examples.btc.coinmarket.Entry;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class MarketShare {
// final static String bt = "BTC\r\n" + "ETH\r\n" + "BCH\r\n" + "XRP\r\n" + "LTC\r\n" + "XEM\r\n" + "DASH\r\n" + "MIOTA\r\n"
// + "XMR\r\n" + "ETC\r\n" + "NEO\r\n" + "OMG";

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		SystemSettings.setExecutionMode(ExecutionMode.DEMO);
// L.d("bt", bt);

		final List<Entry> top = Coinmarketcap.getRanking();
// L.d("btx", btx);
		int i = 0;
		final int N = 40;
		for (final Entry val : top) {
			L.d(i + "	" + val.symbol + "	" + val.price_usd + "	" + val.market_cap_usd);
			i++;
			if (i >= N) {
				break;
			}
			;
		}

	}

}
