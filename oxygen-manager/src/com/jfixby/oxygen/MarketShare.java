
package com.jfixby.oxygen;

import java.util.Comparator;

import com.jfixby.oxygen.call.market.Ticker;
import com.jfixby.oxygen.call.nicehash.NicehashOrder;
import com.jfixby.oxygen.call.nicehash.NicehashOrders;
import com.jfixby.oxygen.call.pool.PoolPerformance;
import com.jfixby.oxygen.coin.BTC;
import com.jfixby.oxygen.target.TargetPrice;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.err.Err;
import com.jfixby.scarabei.api.log.L;

public class MarketShare {

	private static Comparator<NicehashOrder> priceComparator = new Comparator<NicehashOrder>() {
		@Override
		public int compare (final NicehashOrder o1, final NicehashOrder o2) {
			return Double.compare(o1.price().v, o2.price().v);
		}
	};

	final Speed totalSpeed;
	private final List<MarketShareEntry> entries;
	final TargetPrice price;

	final TeamMates team;

// private final BTCUSD btcUsd;

	public MarketShare (final NicehashOrders nicehashMarket, final Ticker btcdcr, final PoolPerformance pool,
		final TeamMates team) {
		Debug.checkTrue("nicehashMarket.result.orders.size > 0", nicehashMarket.result.orders.size() > 0);
// this.btcUsd = btcUsd;
// Debug.checkNull(btcUsd);
		this.team = team;
		List<NicehashOrder> list = Collections.newList(nicehashMarket.result.orders);
		list = list.filter(order -> !order.isFixed());
		list.sort(priceComparator);
//
		this.price = new TargetPrice(btcdcr, pool);
//
		this.entries = Collections.newList();
		int k = 0;
		BTC currentPrice = new BTC(0d);
		for (int i = 0; i < list.size(); i++) {
			final NicehashOrder order = list.getElementAt(i);
			if (order.price().v != currentPrice.v) {
				currentPrice = order.price().copy();
				final MarketShareEntry entry = new MarketShareEntry(this, currentPrice, btcdcr);
				entry.addOrder(order);
				if (k != 0) {
					final MarketShareEntry entryPrevious = this.entries.getElementAt(k - 1);
					Debug.checkTrue(entryPrevious.size() > 0);

					final Speed accBelow = entryPrevious.accumulatedSpeed().copy();
// final Speed demandBelow = entryPrevious.accumulatedDemand().copy();
					entry.setSpeedAccBelow(accBelow);
// entry.setSpeedDemandBelow(demandBelow);
				}
				this.entries.add(entry);

				k++;
			} else {
				final MarketShareEntry entry = this.entries.getElementAt(k - 1);
				entry.addOrder(order);
			}

		}

		this.totalSpeed = this.entries.getLast().accumulatedSpeed().copy();

	}

	public void print () {
		L.d("MarketShare", this.entries);
		L.d("total speed:", this.totalSpeed);
	}

	interface ForkCondition {
		boolean isFork (MarketShareEntry entry);
	}

	public MarketShareFork getOrderByShare (final Percentage targetSharePercent) {
		Debug.checkTrue("entries.size > 0", this.entries.size() > 0);
		final ForkCondition shareForkDetector = (entry) -> entry.accumulatedSpeedShare().share() > targetSharePercent.share();
		final MarketShareFork fork = this.findFork(shareForkDetector);
		if (fork.aboveTarget != null) {
			Debug.checkTrue(fork.aboveTarget.accumulatedSpeedShare().share() > targetSharePercent.share());
		}
		if (fork.belowOrEqualTarget != null) {
			Debug.checkTrue(fork.belowOrEqualTarget.accumulatedSpeedShare().share() <= targetSharePercent.share());
		}
		return fork;

	}

	private MarketShareFork findFork (final ForkCondition forkDetector) {
		final MarketShareFork fork = new MarketShareFork();
		for (final MarketShareEntry entry : this.entries) {
			if (forkDetector.isFork(entry)) {
				fork.aboveTarget = entry;
				break;
			} else {
				fork.belowOrEqualTarget = entry;

			}
		}

		if (fork.belowOrEqualTarget == null || fork.aboveTarget == null) {
			L.e("bad fork!");
			L.e("           forkDetector", forkDetector);
			L.e("     belowOrEqualTarget", fork.belowOrEqualTarget);
			L.e("            aboveTarget", fork.aboveTarget);
		}
		return fork;
	}

	public MarketShareFork getOrdersByPrice (final BTC targetPrice) {
		Debug.checkTrue("entries.size > 0", this.entries.size() > 0);
		final ForkCondition priceForkDetector = (entry) -> entry.price().v > targetPrice.v;
		final MarketShareFork fork = this.findFork(priceForkDetector);
		if (fork.aboveTarget != null) {
			Debug.checkTrue(fork.aboveTarget.price().v > targetPrice.v);
		}
		if (fork.belowOrEqualTarget != null) {
			Debug.checkTrue(fork.belowOrEqualTarget.price().v <= targetPrice.v);
		}
		return fork;
	}

	public MarketShareEntry getMinShareOrder () {
		return this.entries.getElementAt(0);
	}

	public int listSize () {
		return this.entries.size();
	}

	public MarketShareEntry getEntry (final int i) {
		return this.entries.getElementAt(i);
	}

	public MarketShareEntry getMaxShareOrder () {
		return this.entries.getLast();
	}

	public BTC priceForShare (final Percentage targetSpeedShare) {
		final MarketShareFork fork = this.getOrderByShare(targetSpeedShare);

// share.print();

		final MarketShareEntry aboveTarget = fork.aboveTarget;
		final MarketShareEntry belowOrEqualTarget = fork.belowOrEqualTarget;

		if (aboveTarget != null) {
			Debug.checkTrue(aboveTarget.accumulatedSpeedShare().v >= targetSpeedShare.v);
		}
		if (belowOrEqualTarget != null) {
			Debug.checkTrue(targetSpeedShare.v >= belowOrEqualTarget.accumulatedSpeedShare().v);
		}

// L.d(" order above", aboveTarget.index() + " " + aboveTarget.topOrder());
// L.d(" ",
// "speed share:" + aboveTarget.accumulatedSpeedShare() + " profit: " + aboveTarget.profit());
//
// L.d(" order below", belowOrEqualTarget.index() + " " + belowOrEqualTarget.topOrder());
// L.d(" ",
// "speed share:" + belowOrEqualTarget.accumulatedSpeedShare() + " profit: " + belowOrEqualTarget.profit());

// final MarketShareEntry top = aboveTarget.next();

		final double magnetInterval = (aboveTarget.accumulatedSpeedShare().share()
			- belowOrEqualTarget.accumulatedSpeedShare().share());
		final double upperBoundMagnet = 1d
			- (aboveTarget.accumulatedSpeedShare().share() - targetSpeedShare.share()) / magnetInterval;
		final double lowerBoundMagnet = 1d
			- (targetSpeedShare.share() - belowOrEqualTarget.accumulatedSpeedShare().share()) / magnetInterval;

		final BTC result = BTC.linearPrice(aboveTarget.price(), upperBoundMagnet, belowOrEqualTarget.price(), lowerBoundMagnet);

		return result;
	}

	public BTC priceForSpeed (final Speed targetSpeed) {
		Debug.checkNull("totalSpeed", this.totalSpeed);

		final Percentage targetSpeedShare = new Percentage(
			(float)(100 * targetSpeed.kHashPerSecond() / this.totalSpeed.kHashPerSecond()));
		if (targetSpeedShare.v > 100f || targetSpeedShare.v == Float.NaN) {
			targetSpeedShare.v = 99f;
		}
		L.d(" market speed", this.totalSpeed);
		L.d(" target speed", targetSpeed);
		L.d(" target speed market share", targetSpeedShare);

		final BTC result = this.priceForShare(targetSpeedShare);

		return result;
	}

	public MarketShareEntry getEntry (final BTC currentPrice) {
		for (final MarketShareEntry e : this.entries) {
			if (e.price().v == currentPrice.v) {
				return e;
			}
		}
		L.e("target price not found", currentPrice);
		L.e("market", this.entries);
		Err.reportError("Not found " + currentPrice + " " + this.entries);
		return null;
	}

}
