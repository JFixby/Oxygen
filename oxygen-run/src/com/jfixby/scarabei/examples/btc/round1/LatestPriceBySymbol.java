
package com.jfixby.scarabei.examples.btc.round1;

import java.io.IOException;

import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.collections.Map;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.strings.Strings;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;
import com.jfixby.scarabei.examples.btc.coinmarket.Coinmarketcap;
import com.jfixby.scarabei.examples.btc.coinmarket.Entry;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class LatestPriceBySymbol {
// final static String bt = "BTC " + "ETH " + "BCH " + "XRP " + "LTC " + "XEM " + "DASH " + "MIOTA "
// + "XMR " + "ETC " + "NEO " + "OMG";
	final static String bt = "DCR " + "BTC " + "ETH " + "XMR " + "GRS " + "BTCP " + "DASH " + "LTC";
// final static String bt = "BCH " + "BTC " + "BTG " + "DASH " + "DCR " + "ETC " + "ETH " + "GRS " + "LTC " + "MIOTA " + "NEO "
// + "OMG " + "VRM " + "VTC " + "XEM " + "XMR " + "XRP " + "ZEC";

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		SystemSettings.setExecutionMode(ExecutionMode.DEMO);
		L.d("bt", bt);
		final List<String> tags = Strings.split(bt, " ");
// tags.sort();

		final List<Entry> top = Coinmarketcap.getRanking();
		final Map<String, Entry> btx = Collections.newMap();
		for (final Entry e : top) {
			btx.put(e.symbol, e);
		}
// L.d("btx", btx);
		for (final String t : tags) {
			final Entry val = btx.get(t);
			if (val == null) {
// L.d("btx", btx);
// Err.reportError("Symbil not found " + t);

// L.d(t + " " + "$$$" + " " + "$$$");
				L.d("$$$");
				continue;
			}
// L.d(t + " " + val.price_usd + " " + val.market_cap_usd);
			L.d(val.price_usd);
		}

	}

}
