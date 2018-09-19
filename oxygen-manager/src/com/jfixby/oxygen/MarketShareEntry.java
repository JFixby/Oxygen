
package com.jfixby.oxygen;

import com.jfixby.oxygen.call.market.Ticker;
import com.jfixby.oxygen.call.nicehash.NicehashOrder;
import com.jfixby.oxygen.coin.BTC;
import com.jfixby.oxygen.coin.ShitCoin;
import com.jfixby.scarabei.api.collections.Collection;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.EditableCollection;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.strings.Strings;

public class MarketShareEntry {

	private final List<NicehashOrder> ordersGroup = Collections.newList();
	private final BTC groupPrice;
	private final MarketShare marketShare;
	private Speed speedAccBelow;
// private final Speed speedDemandAccBelow = new Speed(0d);
// private final BTCUSD btcusd;
	private final Ticker ticker;

	public MarketShareEntry (final MarketShare marketShare, final BTC price,
// final BTCUSD btcusd,
		final Ticker btcdcr) {
		this.marketShare = marketShare;
		this.groupPrice = price.copy();
// this.btcusd = this.btcusd;
		this.ticker = btcdcr;
		this.speedAccBelow = Speed.newValue(0, SpeedMagnitude.THpS);

	}

	public Percentage accumulatedSpeedShare () {
		return new Percentage(
			(float)(100f * this.accumulatedSpeed().kHashPerSecond() / this.marketShare.totalSpeed.kHashPerSecond()));
	}

	public final Percentage profit () {
		final Percentage profit = this.marketShare.price.getPercantageFromPrice(this.groupPrice);
		return profit;
	}

	@Override
	public String toString () {
		final String ID = this.topOrder().id;
		final String logout = this.sign(this.topOrder()) + Strings.padLeft(ID, " ", 4)//
			+ " " + Strings.padLeft(this.price() + "", " ", 10)//
			+ " " + Strings.padLeft(this.priceShitCoin() + "", " ", 4)//
			+ " profit: " + Strings.padLeft(this.profit() + "", " ", 7)//
			+ " supply: " + Strings.padLeft(this.groupSpeed() + "", " ", 10)//
			+ " load: " + Strings.padLeft(this.load() + "", " ", 10)//
			// + " demand: " + Strings.padLeft(this.groupDemand() + "", " ", 10)//
			// + " accumalated speed " + Strings.padLeft(this.accumulatedSpeed() + "", " ", 10)//
			// + " accumalated demand " + Strings.padLeft(this.accumulatedDemand() + "", " ", 10)//
			+ " acc speed share " + Strings.padLeft(this.accumulatedSpeedShare() + "", " ", 10)//
			+ " all orders: " + this.orderIDs()//
		;
		return logout;
	}

// private USD priceUSD () {
// final BTC btc = this.groupPrice.copy();
// return this.btcusd.toUSD(btc);
// }

	private ShitCoin priceShitCoin () {
		final BTC btc = this.groupPrice.copy();
		final ShitCoin shitcoin = this.ticker.fromBTC(btc);
// if (shitcoin.sign != CoinSign.DECRED) {
// Err.reportError("Sign mistamch: ticker=" + this.ticker.symbol + " coin=" + CoinSign.DECRED);
// Sys.exit();
// }
// return new DCR(shitcoin.v);
		return shitcoin;
	}

	private String sign (final NicehashOrder order) {
		if (this.marketShare.team.orders.contains(order.id)) {
			return "♠";
		}
		return "#";
	}

	private EditableCollection<String> orderIDs () {
		final List<String> orders = Collections.newList();
		Collections.convertCollection(this.ordersGroup, orders, order -> this.sign(order) + order.id);
		return orders;
	}

	private Percentage load () {
		final double d = this.groupDemand().kHashPerSecond();
		final Float x = (float)(this.groupSpeed().kHashPerSecond() * 100 / d);
		return new Percentage(x);
	}

	public BTC price () {
		return this.groupPrice.copy();
	}

	public Speed accumulatedSpeed () {
		return this.groupSpeed().add(this.speedAccBelow);
	}

// public Speed accumulatedDemand () {
// return new Speed(this.groupDemand().v + this.speedDemandAccBelow.v);
// }

	public Speed groupSpeed () {

		Debug.checkTrue(this.ordersGroup.size() > 0);
		Speed groupSpeed = Speed.newValue(0d, this.ordersGroup.getLast().accepted_speed().order);
		for (final NicehashOrder order : this.ordersGroup) {
			groupSpeed = groupSpeed.add(order.accepted_speed());
		}
		return groupSpeed;
	}

	public Speed groupDemand () {
		Debug.checkTrue(this.ordersGroup.size() > 0);
		Speed groupDemand = Speed.newValue(0d, this.ordersGroup.getLast().speed_limit().order);
		for (final NicehashOrder order : this.ordersGroup) {
			groupDemand = groupDemand.add(order.speed_limit());
		}
		return groupDemand;
	}

	public void setSpeedAccBelow (final Speed accBelow) {
		this.speedAccBelow = accBelow.copy();
	}

// public void setSpeedDemandBelow (final Speed demandBelow) {
// this.speedDemandAccBelow.v = demandBelow.copy().v;
// }

	public void addOrder (final NicehashOrder order) {
		Debug.checkTrue(this.price().v == order.price().v);
		this.ordersGroup.add(order);
	}

	public int size () {
		return this.ordersGroup.size();
	}

	public NicehashOrder topOrder () {
		return this.ordersGroup.getLast();
	}

	public Collection<NicehashOrder> listOrders () {
		return this.ordersGroup;
	}

}
