
package com.jfixby.scarabei.examples.btc.round1;

import java.io.IOException;

import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;
import com.jfixby.scarabei.examples.btc.coinmarket.Coinmarketcap;
import com.jfixby.scarabei.examples.btc.coinmarket.Entry;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

class BTC {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		SystemSettings.setExecutionMode(ExecutionMode.DEMO);
		final List<Entry> top = Coinmarketcap.getRanking();
		int i = 0;
		for (final Entry e : top) {
// L.d(e.rank + " " + e.id + " " + e.symbol + " " + e.market_cap_usd);
			L.d(e.toCSV());
			i++;
			if (i == 40) {
				return;
			}
		}
	}

}
