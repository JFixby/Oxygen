
package com.jfixby.oxygen;

import java.io.IOException;

import com.jfixby.oxygen.call.GetNicehashMarket;
import com.jfixby.oxygen.call.GetTicker;
import com.jfixby.oxygen.call.Pool;
import com.jfixby.oxygen.call.Region;
import com.jfixby.oxygen.call.market.Ticker;
import com.jfixby.oxygen.call.nicehash.NicehashOrder;
import com.jfixby.oxygen.call.nicehash.NicehashOrders;
import com.jfixby.oxygen.call.pool.Performance;
import com.jfixby.oxygen.call.pool.PoolPerformance;
import com.jfixby.oxygen.coin.BTC;
import com.jfixby.oxygen.coin.CoinSign;
import com.jfixby.oxygen.keys.NiceHashAPIKey;
import com.jfixby.oxygen.target.TargetPrice;
import com.jfixby.scarabei.api.collections.Collection;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.err.Err;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.math.FloatMath;

public class ManagableOrder {
	private static Percentage ALLOWED_TARGET_PERCENTAGE = new Percentage(7f);
	public final CoinSign shitCoinSymbol;
	private final String orderID;
	private final NiceHashAPIKey niceHashKey;
	private NicehashOrder orderStatus;
	private long lastStatusCheck = 0;
	private BTC lowerBound;
	private BTC upperBound;
	private final Percentage minTargetPercentage;
// private double recommendedPrice;
	private boolean updateAsked;
	private final String region;
	private final Percentage targetProfitMinWindowSizePercentage;
	private final Speed turboSpeed;
	private BTC recommendedMaxPrice;
	BTC criticalLoadBtcJump = BTC.STEP.copy();
// Set<String> ignoreOrders;
	Percentage criticalLoad = new Percentage(0f);
	private final TeamMates team;
	private List<NicehashOrder> competirs;
	final Pool pool;
	private Speed speedEpsilon;

	public ManagableOrder (final OrderManagerSpecs specs, final CoinSign shitCoinSymbol, final NiceHashAPIKey niceHashKey,
		final TeamMates team, final Pool pool
// , final BTCUSDProvider btcUsd
	) throws IOException {
		this.minTargetPercentage = new Percentage(specs.targetProfitMinPercentage);
// this.ignoreOrders = Collections.newSet();
// this.btc_usd = btcUsd;
// Debug.checkNull("btc_usd", this.btc_usd);
// this.ignoreOrders.addAll(this.ignoreOrders);
		this.pool = pool;
		this.team = team;
		Debug.checkNull("team", this.team);

		this.criticalLoad = new Percentage(specs.criticalLoadPercentage);

		this.criticalLoadBtcJump = new BTC(specs.criticalLoadBtcJump);

		Debug.checkNull("targetPercentageBottom", this.minTargetPercentage);

		this.targetProfitMinWindowSizePercentage = new Percentage(specs.targetProfitMinWindowSizePercentage);
		Debug.checkNull("targetProfitMinWindowSizePercentage", this.targetProfitMinWindowSizePercentage);

		this.turboSpeed = Speed.parse(specs.turboSpeed);
		Debug.checkNull("turboSpeed", this.turboSpeed);

		if (this.minTargetPercentage.v < ALLOWED_TARGET_PERCENTAGE.v) {
			Err.reportError("TargetPercentage min is below allowed: " + this.minTargetPercentage);
		}

		this.shitCoinSymbol = Debug.checkNull("shitCoinSymbol", shitCoinSymbol);

		this.orderID = specs.orderID;
		Debug.checkNull("orderID", this.orderID);

		this.region = specs.region;
		Debug.checkNull("region", this.region);

		this.niceHashKey = niceHashKey;

	}

	public void init () throws IOException {
		this.lastStatusCheck = 0;
// final NicehashOrder order = this.getOrder();
// L.d("managing order", order);
	}

// static private NicehashOrder getOrder (final SpeedMagnitude speedMagnitude) throws IOException {
// final NicehashOrders orders = GetNicehashMarket.getMyOrders((this.shitCoinSymbol.algo), Region.resolve(this.region),
// this.niceHashKey.id, this.niceHashKey.key, speedMagnitude);
// List<NicehashOrder> ordersList = Collections.newList(orders.result.orders);
// ordersList = ordersList.filter(order -> order.id.equals(this.orderID));
//
// if (ordersList.size() != 1) {
// final NicehashOrders reorders = GetNicehashMarket.getMyOrders((this.shitCoinSymbol.algo), Region.resolve(this.region),
// this.niceHashKey.id, this.niceHashKey.key, speedMagnitude);
// L.d("reorders", reorders.result.orders);
// Err.reportError("Order not found: " + this.orderID);
// }
//
// final NicehashOrder order = ordersList.getLast();
// return order;
// }

	public Speed speedEpsilon () {
		return this.speedEpsilon;
	}

	public boolean tryStatusCheck () {
		try {
// this.orderStatus = this.getOrder(this.turboSpeed.order);
			final PoolPerformance pool = this.pool.read();
			final Performance performance = pool.perFlowUnitX24H();
			final Ticker ticker = GetTicker.get(this.shitCoinSymbol);
			final NicehashOrders nicehashMarket = GetNicehashMarket.getOrders(this.shitCoinSymbol.algo, Region.resolve(this.region),
				this.turboSpeed.order);
			this.orderStatus = nicehashMarket.findMyOrder(this.orderID);
// final BTCUSD btc_usd = this.btc_usd.getRate();

			L.d(
				"-------------------------------------------------------------------------------------------------------------------------");
			L.d("order status: " + this.region.toUpperCase(), this.orderStatus);
			L.d();

			L.d(ticker);
// L.d(btc_usd);
			L.d();

			L.d("Pool performance", performance);
			L.d("                ", ticker.toBTC(pool.perFlowUnitX24H().coinsX24H()));
			final Speed speedUnit = pool.perFlowUnitX24H().speedUnit();
// L.d(" ", speedUnit);
			this.speedEpsilon = speedUnit.mult(0.01d);
// L.d(" ", this.speedEpsilon);

// Sys.exit();
			L.d();
			L.d("Price bounds");
			final TargetPrice price = new TargetPrice(ticker, pool);
			final Percentage upperBoundProfitPercentage = this.minTargetPercentage;

			{

				this.upperBound = price.buildThsx24HBTCPrice(this.minTargetPercentage);
				final Percentage upperBoundProfitPercentage2 = price.getPercantageFromPrice(this.upperBound);
				this.upperBound = new BTC(FloatMath.roundToDigit(this.upperBound.v, 4));
				L.d("          UPPER", " profit: " + upperBoundProfitPercentage + " | " + upperBoundProfitPercentage2 + " price: "
					+ this.upperBound + " per " + performance.speedUnit());
			}

			final MarketShare share = new MarketShare(nicehashMarket, ticker, pool, this.team);

			{
				final BTC currentPrice = this.orderStatus.price();
				final Percentage currentPercentage = price.getPercantageFromPrice(currentPrice);
				L.d("        CURRENT",
					" profit: " + currentPercentage + " price: " + currentPrice + " per " + performance.speedUnit());

				final MarketShareEntry entry = share.getEntry(currentPrice);
				L.d("               ", entry);
				if (entry == null) {
					throw new IOException();
				}
// if()
				this.competirs = entry.listOrders()
					.filter(order -> !(this.team.orders.contains(order.id) || this.orderID.equals(order.id)));
				final Collection<NicehashOrder> competitors = this.listCompetitors();
				if (competitors.size() > 0) {
					L.d("    competitors", competitors);
				}
			}

			Percentage lowerBoundProfitPercentage = null;
			final Percentage load = this.orderStatus.getLoad();
			L.d("           speed", this.orderStatus.accepted_speed());
			L.d("            load", load);

			{

				final Speed targetSpeed = this.turboSpeed;

				this.lowerBound = share.priceForSpeed(targetSpeed);

				double result = FloatMath.roundToDigit(this.lowerBound.v, 4);
				final double now = this.getCurrentPrice().v;
				if (result == now && load.v < this.criticalLoad.v) {
					L.d("bad lower bound", new BTC(result));
					result = result + this.criticalLoadBtcJump.v;
					L.d("        jump to", new BTC(result));
				}
				this.lowerBound = new BTC(result);

				lowerBoundProfitPercentage = price.getPercantageFromPrice(this.lowerBound);

				L.d("          LOWER",
					" profit: " + lowerBoundProfitPercentage + " price: " + this.lowerBound + " per " + performance.speedUnit());

				final Percentage windowPercentage = new Percentage(lowerBoundProfitPercentage.v - upperBoundProfitPercentage.v);
				if (windowPercentage.v < this.targetProfitMinWindowSizePercentage.v) {
					L.d("                    window size",
						windowPercentage + " is below allowed size " + this.targetProfitMinWindowSizePercentage);

					lowerBoundProfitPercentage.v = upperBoundProfitPercentage.v + this.targetProfitMinWindowSizePercentage.v;
					this.lowerBound = price.buildThsx24HBTCPrice(lowerBoundProfitPercentage);
					this.lowerBound = new BTC(FloatMath.roundToDigit(this.lowerBound.v, 4));
					;
					L.d("   ajust  LOWER",
						" profit: " + lowerBoundProfitPercentage + " price: " + this.lowerBound + " per " + performance.speedUnit());
				}

				Debug.checkTrue(this.lowerBound.v <= this.upperBound.v);
				Debug.checkTrue(upperBoundProfitPercentage.v <= lowerBoundProfitPercentage.v);
			}
			{
				final Percentage recommendedPercentage = new Percentage(
					lowerBoundProfitPercentage.v - this.targetProfitMinWindowSizePercentage.v);
				this.recommendedMaxPrice = price.buildThsx24HBTCPrice(recommendedPercentage);

				final double result = FloatMath.roundToDigit(this.recommendedMaxPrice.v, 4);

				this.recommendedMaxPrice = new BTC(result);
				L.d("RECOMMENDED MAX",
					" profit: " + recommendedPercentage + " price: " + this.recommendedMaxPrice + " per " + performance.speedUnit());

			}
			this.updateAsked = false;
			this.lastStatusCheck = System.currentTimeMillis();

		} catch (final Throwable e) {
			e.printStackTrace();
			L.e(e.toString());
			return false;
		}
		return true;
	}

// final BTCUSDProvider btc_usd;

	public long timeSinceLastCheck () {
		final long deltaStatusCheck = System.currentTimeMillis() - this.lastStatusCheck;
		return deltaStatusCheck;
	}

	public CoinSign algo () {
		return this.shitCoinSymbol;
	}

	public String ID () {
		return this.orderID;
	}

	public void askUpdate () {
		this.updateAsked = true;
	}

	public boolean updateAsked () {
		return this.updateAsked;
	}

	public BTC getUpperBoundPrice () {
		return this.upperBound;
	}

	public BTC getLowerBoundPrice () {
		return this.lowerBound;
	}

	public BTC getCurrentPrice () {
		return this.orderStatus.price();
	}

	public BTC getRecMaxPrice () {
		return this.recommendedMaxPrice;
	}

	public Speed getSpeedLimit () {
		return this.orderStatus.speed_limit();
	}

	public Collection<NicehashOrder> listCompetitors () {
		return this.competirs;
	}

	public Percentage getLoad () {
		return this.orderStatus.getLoad();
	}

}
