
package com.jfixby.oxygen.run;

import java.io.IOException;

import com.jfixby.oxygen.call.GetTicker;
import com.jfixby.oxygen.call.market.Ticker;
import com.jfixby.oxygen.coin.CoinSign;
import com.jfixby.oxygen.coin.MarketPair;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class GetDCRBTCLoop {

	public static void main (final String[] args) throws IOException {

		ScarabeiDesktop.deploy();
		SystemSettings.setExecutionMode(ExecutionMode.DEMO);

		{
			final MarketPair btcdcr_pair = MarketPair.newMarketPair(CoinSign.BITCOIN, CoinSign.DECRED);
			L.d("btcdcr_pair", btcdcr_pair);

			final Ticker btc_dcr = GetTicker.get(btcdcr_pair);

			L.d("btc_dcr", btc_dcr);

			final MarketPair usdtdcr_pair = MarketPair.newMarketPair(CoinSign.TETHER, CoinSign.DECRED);
			L.d("usdtdcr_pair", usdtdcr_pair);

			final Ticker usdt_dcr = GetTicker.get(usdtdcr_pair);

			L.d("usdt_dcr", usdt_dcr);

			final MarketPair usdtbtc_pair = MarketPair.newMarketPair(CoinSign.TETHER, CoinSign.BITCOIN);
			L.d("usdtbtc_pair", usdtbtc_pair);

			final Ticker usdt_btc = GetTicker.get(usdtbtc_pair);

			L.d("usdt_btc", usdt_btc);
			L.d();
			{
				// 1DCR
				final double dcr = 1;
				final double tousdt = usdt_dcr.result.Bid * dcr;

				L.d("DCR to USDT", "(" + usdt_dcr.result.Bid + ") " + tousdt);

				final double tobtc = tousdt / usdt_btc.result.Ask;

				L.d("USDT to BTC", "(" + usdt_btc.result.Ask + ") " + tobtc);

				final double todcr = tobtc / btc_dcr.result.Ask;

				L.d("BTC to DCR", "(" + btc_dcr.result.Ask + ") " + todcr);
			}
			L.d();
			{
				// 1DCR
				final double dcr = 1;
				final double toBTC = btc_dcr.result.Bid * dcr;
				L.d("DCR to BTC", toBTC);

				final double toUSDT = usdt_btc.result.Bid * toBTC;

				L.d("BTC to USDT", toUSDT);

				final double toDCR = toUSDT / usdt_dcr.result.Ask;

				L.d("USDT to DCR", toDCR);

			}
			L.d();
			{
				// 1DCR
				final double dcr = 1;
				final double tousdt = usdt_dcr.result.Last * dcr;

				L.d("DCR to USDT", "(" + usdt_dcr.result.Last + ") " + tousdt);

				final double tobtc = tousdt / usdt_btc.result.Last;

				L.d("USDT to BTC", "(" + usdt_btc.result.Last + ") " + tobtc);

				final double todcr = tobtc / btc_dcr.result.Last;

				L.d("BTC to DCR", "(" + btc_dcr.result.Last + ") " + todcr);
			}
			L.d();
			{
				// 1DCR
				final double dcr = 1;
				final double toBTC = btc_dcr.result.Last * dcr;
				L.d("DCR to BTC", toBTC);

				final double toUSDT = usdt_btc.result.Last * toBTC;

				L.d("BTC to USDT", toUSDT);

				final double toDCR = toUSDT / usdt_dcr.result.Last;

				L.d("USDT to DCR", toDCR);

			}
		}

	}

}
