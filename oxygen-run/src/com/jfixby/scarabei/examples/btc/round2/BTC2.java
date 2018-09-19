
package com.jfixby.scarabei.examples.btc.round2;

import java.io.IOException;
import java.util.Comparator;

import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.EditableCollection;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.collections.Set;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.strings.Strings;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;
import com.jfixby.scarabei.examples.btc.coinmarket.Coinmarketcap;
import com.jfixby.scarabei.examples.btc.coinmarket.Entry;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

class BTC2 {

	private static Comparator<Entry> by24HGrowth = new Comparator<Entry>() {

		@Override
		public int compare (final Entry o1, final Entry o2) {
			return o1.percent_change_24h.compareTo(o2.percent_change_24h) * 0;
		}
	};

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		SystemSettings.setExecutionMode(ExecutionMode.DEMO);

		final List<Entry> top = Coinmarketcap.getRanking();
		final EditableCollection<Entry> tail = top.splitAt(10);
		L.d(" top", top);
		L.d("tail", tail);
		tail.sort(by24HGrowth);
		int index = 0;
		final int topRank = 40;
		Set<String> exchange = karaken();
		exchange = bitfinex();
		final int limit = 10;
		for (final Entry c : tail) {
			if (index <= topRank) {
				if (pass(c, exchange) && index <= limit) {
					L.d(c.toCSV());
				} else {
// L.d(c.toCSV(0));
				}
			} else {
				break;
			}
			index++;
		}
// L.d("by24HGrowth", btx);

	}

	private static boolean pass (final Entry c, final Set<String> karaken) {
		return true;
// return c.market_cap_eur
// if (!karaken.contains(c.symbol)) {
// return false;
// }
// return c.percent_change_24h.doubleValue() > 0 || true;
	}

	public static final Set<String> bitfinex () {
		final String bt = "BTC\r\n" + "BCH\r\n" + "DASH\r\n" + "ETH\r\n" + "ZEC\r\n" + "XMR\r\n" + "LTC\r\n" + "ETC\r\n" + "OMG\r\n"
			+ "EOS\r\n" + "MIOTA\r\n" + "XRP";
		final List<String> tags = Strings.split(bt, "\r\n");
		final Set<String> bitfinex = Collections.newSet(tags);
		return bitfinex;
	}

	static public Set<String> karaken () {
		final String kr = "Yes. You can trade Bitcoin(XBT), Ethereum (ETH), Monero (XMR), Dash (DASH), Litecoin (LTC), Ripple (XRP), Stellar/Lumens (XLM), Ethereum Classic (ETC), Augur REP tokens (REP), ICONOMI (ICN), Melon (MLN), Zcash (ZEC), Dogecoin (XDG), Tether (USDT), Gnosis (GNO), and EOS (EOS)";
		final List<String> lefts = Strings.split(kr, "\\)");
		final Set<String> karaken = Collections.newSet();
		for (final String l : lefts) {
			final List<String> sm = Strings.split(l, "\\(");
// L.d("", );
			final String s = sm.getLast();
			karaken.add(s);
		}
		return karaken;

	}

}
