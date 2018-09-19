
package com.jfixby.oxygen.run;

import java.io.IOException;

import com.jfixby.oxygen.MarketShare;
import com.jfixby.oxygen.MarketShareEntry;
import com.jfixby.oxygen.MarketShareFork;
import com.jfixby.oxygen.Percentage;
import com.jfixby.oxygen.SpeedMagnitude;
import com.jfixby.oxygen.TeamMates;
import com.jfixby.oxygen.call.Algo;
import com.jfixby.oxygen.call.GetNicehashMarket;
import com.jfixby.oxygen.call.GetTicker;
import com.jfixby.oxygen.call.Region;
import com.jfixby.oxygen.call.market.Ticker;
import com.jfixby.oxygen.call.nicehash.NicehashOrders;
import com.jfixby.oxygen.call.pool.StratumPoolPerformance;
import com.jfixby.oxygen.coin.BTC;
import com.jfixby.oxygen.coin.CoinSign;
import com.jfixby.oxygen.dash.call.pool.DashHubPoolPerformance;
import com.jfixby.oxygen.target.TargetPrice;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.err.Err;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class GetDashMarket {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		SystemSettings.setExecutionMode(ExecutionMode.DEMO);
		final NicehashOrders market = GetNicehashMarket.getOrders(Algo.X11, Region.resolve("EU"), SpeedMagnitude.GSols);
		L.d("nicehash", market.result.orders);

		final Ticker btcdcr = GetTicker.get(CoinSign.DASH);

// final StratumPoolPerformance pool = new DashCoinMinePoolPerformance().read();
		final StratumPoolPerformance pool = new DashHubPoolPerformance().read();

		final TargetPrice price = new TargetPrice(btcdcr, pool);
		final Percentage targetSharePercent = new Percentage(50f);
		final MarketShare share = new MarketShare(market, btcdcr, pool, TeamMates.readFromFile("team.json"));

		for (int i = 0; i < share.listSize() - 1; i++) {
			L.d();
			final MarketShareEntry e1 = share.getEntry(i);
			final MarketShareEntry e2 = share.getEntry(i + 1);
			if (e1.price().v == e2.price().v) {
				Err.reportError("");
			}
			L.d("e1", e1);
			L.d("e2", e2);

			final BTC mid = new BTC((e1.price().v + e2.price().v) / 2f);
			{
				final MarketShareFork fork1 = share.getOrdersByPrice(mid);
				L.d("fork1", mid);
				L.d("aboveTarget", fork1.aboveTarget);
				L.d("belowOrEqualTarget", fork1.belowOrEqualTarget);

				Debug.checkTrue(fork1.aboveTarget == e2);
				Debug.checkTrue(fork1.belowOrEqualTarget == e1);
			}
			{
				final MarketShareFork fork1 = share.getOrdersByPrice(e1.price());
				L.d("fork2", mid);
				L.d("aboveTarget", fork1.aboveTarget);
				L.d("belowOrEqualTarget", fork1.belowOrEqualTarget);
				Debug.checkTrue(fork1.aboveTarget == e2);
				Debug.checkTrue(fork1.belowOrEqualTarget == e1);
			}
		}

		L.d("-------------------------------------------------------------------------------------------");

		for (int i = 0; i < share.listSize() - 1; i++) {
			final MarketShareEntry e1 = share.getEntry(i);
			final MarketShareEntry e2 = share.getEntry(i + 1);
			if (e1.accumulatedSpeedShare().v == e2.accumulatedSpeedShare().v) {
				continue;
			}
			L.d();

			L.d("e1", e1);
			L.d("e2", e2);

			final Percentage midShare = new Percentage((e1.accumulatedSpeedShare().v + e2.accumulatedSpeedShare().v) / 2f);
			{
				final MarketShareFork fork1 = share.getOrderByShare(midShare);
				L.d("fork1", midShare);
				L.d("aboveTarget", fork1.aboveTarget);
				L.d("belowOrEqualTarget", fork1.belowOrEqualTarget);

				Debug.checkTrue(fork1.aboveTarget == e2);
				Debug.checkTrue(fork1.belowOrEqualTarget == e1);
			}
			{
				final MarketShareFork fork1 = share.getOrderByShare(e1.accumulatedSpeedShare());
				L.d("fork2", midShare);
				L.d("aboveTarget", fork1.aboveTarget);
				L.d("belowOrEqualTarget", fork1.belowOrEqualTarget);
				Debug.checkTrue(fork1.aboveTarget == e2);
				Debug.checkTrue(fork1.belowOrEqualTarget == e1);
			}
		}

		share.print();

		L.d("BTC/DCR BID", btcdcr.result.Bid);

		L.d("Pool performance for Th/s x24H", pool.perFlowUnitX24H());
		L.d(" BTC for Th/s x24H", btcdcr.toBTC(pool.perFlowUnitX24H().coinsX24H()));

		final MarketShareFork fork = share.getOrderByShare(targetSharePercent);
		L.d("order with targetShare=" + targetSharePercent + "");
		L.d("                       ", fork.aboveTarget);
		L.d("                       ", fork.belowOrEqualTarget);

		final MarketShareEntry min = share.getMinShareOrder();

		final MarketShareEntry max = share.getMaxShareOrder();

		{
			final MarketShareFork fork1 = share.getOrderByShare(new Percentage(101f));
			L.d("aboveTarget", fork1.aboveTarget);
			L.d("belowOrEqualTarget", fork1.belowOrEqualTarget);

		}
		{
			final MarketShareFork fork1 = share.getOrderByShare(new Percentage(-101f));
			L.d("aboveTarget", fork1.aboveTarget);
			L.d("belowOrEqualTarget", fork1.belowOrEqualTarget);

		}

	}

}
