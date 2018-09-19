
package com.jfixby.oxygen;

import java.io.IOException;

import com.jfixby.oxygen.call.GetNicehashMarket;
import com.jfixby.oxygen.call.Region;
import com.jfixby.oxygen.call.nicehash.DecreaseOrderPriceResult;
import com.jfixby.oxygen.call.nicehash.IncreaseOrderPriceResult;
import com.jfixby.oxygen.call.nicehash.OrderSpeedSetResult;
import com.jfixby.oxygen.coin.BTC;
import com.jfixby.oxygen.keys.NiceHashAPIKey;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.math.FloatMath;
import com.jfixby.scarabei.api.sys.Sys;

public class MedianSpeedDecisionMaker implements DecisionMaker {
	private long lastSuccAction;
	private long last;
	private final NiceHashAPIKey niceHashKey;
	private final Speed slowSpeed;
	private final Region region;
	private final Speed turboSpeed;
	Speed targetSpeed;
	private final BTC jump;
	private final Long sleep;

	public MedianSpeedDecisionMaker (final OrderManagerSpecs specs, final NiceHashAPIKey niceHashKey) {
		this.niceHashKey = niceHashKey;

		this.slowSpeed = Speed.parse(specs.slowSpeed);
		this.turboSpeed = Speed.parse(specs.turboSpeed);
		this.region = Region.resolve(specs.region);
		Debug.checkNull("slowSpeed", this.slowSpeed);
		Debug.checkNull("turboSpeed", this.turboSpeed);
		Debug.checkNull("region", this.region);
		this.targetSpeed = this.slowSpeed;
		this.critical_load = new Percentage(specs.criticalLoadPercentage);
		this.jump = new BTC(specs.criticalLoadBtcJump);
		this.sleep = specs.takeActionPeriod;
	}

	public static final String NAME = "MedianSpeed";
	private Percentage critical_load = null;

	@Override
	public long timeSinceLastSuccAction () {
		final long deltalastSuccAction = System.currentTimeMillis() - this.lastSuccAction;
		return deltalastSuccAction;
	}

	@Override
	public void tryToAjustStrategy (final ManagableOrder order) {
		try {

			final BTC upperBound = order.getUpperBoundPrice();
			final BTC lowerBound = order.getLowerBoundPrice();
			final BTC currentPrice = order.getCurrentPrice();
			final BTC recommendedMaxPrice = order.getRecMaxPrice();

			Debug.checkTrue(recommendedMaxPrice.v <= upperBound.v);
			Debug.checkTrue(lowerBound.v <= recommendedMaxPrice.v);

			L.d(
				"--------------------------------------------------------------------------------------------------------------------------------------------------------");

// L.d(" ", "action required");
			final boolean overpay = currentPrice.v > upperBound.v;
			if (overpay) {
				L.d("currentPrice(" + currentPrice + ") > upperBound(" + upperBound + ")");
				// BEGIN PANIC MODE
				L.d("warning: overpay detected");
				this.setSpeed(order, this.slowSpeed);
				L.d("action: decrease price");
				final DecreaseOrderPriceResult decrease = GetNicehashMarket.decreaseOrderPrice(order.shitCoinSymbol.algo,
					(this.region), this.niceHashKey.id, this.niceHashKey.key, order.ID());
				if (decrease.result.success != null) {
					L.d("                      ", decrease.result.success);
				} else {
					L.d("                      ", decrease.result.error);
				}

				order.askUpdate();
				return;
			}
			// END PANIC MODE

			this.setSpeed(order, this.turboSpeed);

			final boolean belowLowerBound = currentPrice.v < lowerBound.v;

			if (belowLowerBound) {
				L.d("currentPrice(" + currentPrice + ") < lowerBound(" + lowerBound + ")");
				final BTC setPrice = lowerBound;

				this.increasePriceTo(setPrice, order);
				return;
			}

			final boolean aboveRec = currentPrice.v > recommendedMaxPrice.v;
			if (aboveRec) {
				L.d("currentPrice(" + currentPrice + ") > recommendedMaxPrice(" + recommendedMaxPrice + ")");
				L.d("warning: slight overpay detected");
				L.d("action: decrease price");
				final DecreaseOrderPriceResult decrease = GetNicehashMarket.decreaseOrderPrice(order.shitCoinSymbol.algo,
					(this.region), this.niceHashKey.id, this.niceHashKey.key, order.ID());
				if (decrease.result.success != null) {
					L.d("                      ", decrease.result.success);
				} else {
					L.d("                      ", decrease.result.error);
				}

				order.askUpdate();
				return;
			}

			final Percentage load = order.getLoad();
			if (load.share() > 1) {
				L.d("load ", load + " is above 100% ");
				L.d("warning: slight overpay detected");
				L.d("action: decrease price");
				final DecreaseOrderPriceResult decrease = GetNicehashMarket.decreaseOrderPrice(order.shitCoinSymbol.algo,
					(this.region), this.niceHashKey.id, this.niceHashKey.key, order.ID());
				if (decrease.result.success != null) {
					L.d("                      ", decrease.result.success);
				} else {
					L.d("                      ", decrease.result.error);
				}

				order.askUpdate();
				return;
			}

			final boolean priceIsGood = currentPrice.v <= recommendedMaxPrice.v && currentPrice.v >= lowerBound.v;
			L.d("lowerBound(" + lowerBound + ") <= currentPrice(" + currentPrice + ") <= recommendedMaxPrice(" + recommendedMaxPrice
				+ ")");
			if (priceIsGood) {
				L.d("price is good");
			}

			if (load.v < (this.critical_load).v) {
				L.d("load ", load + " is below " + this.critical_load);
				final BTC setPrice = new BTC(currentPrice.v + this.jump.v);
				if (currentPrice.v < recommendedMaxPrice.v && setPrice.v < recommendedMaxPrice.v) {

					final boolean succ = this.increasePriceTo(setPrice, order);
					if (!succ) {
						return;
					}

				}
			}

			L.d("OK", "Sleep " + this.sleep);
			this.lastSuccAction = Sys.SystemTime().currentTimeMillis();

		} catch (final Throwable e) {
			L.e(e.toString());
		}
	}

	private boolean increasePriceTo (final BTC setPrice, final ManagableOrder order) throws IOException {
		L.d("action: increase price to", setPrice);
		final IncreaseOrderPriceResult increased = GetNicehashMarket.increaseOrderPrice(order.shitCoinSymbol.algo, this.region,
			this.niceHashKey.id, this.niceHashKey.key, order.ID(), setPrice);
		if (increased.result.success != null) {
			L.d("                      ", increased.result.success);
		} else {
			L.d("                      ", increased.result.error);
		}
		order.askUpdate();
		return increased.result.success != null;
	}

	private void setSpeed (final ManagableOrder order, final Speed speed) throws IOException {
		this.targetSpeed = speed;
		final Speed SPEED_EPSILON = order.speedEpsilon();
		final Speed currentSpeedLimit = order.getSpeedLimit();
		final double diff = FloatMath.abs(currentSpeedLimit.kHashPerSecond() - speed.kHashPerSecond());
		if (diff < SPEED_EPSILON.kHashPerSecond()) {
			return;
		}
		L.d("action: set speed from " + currentSpeedLimit, speed);
		final OrderSpeedSetResult speedSet = GetNicehashMarket.setOrderSpeedLimit(order.shitCoinSymbol.algo, (this.region),
			this.niceHashKey.id, this.niceHashKey.key, order.ID(), speed);
		if (speedSet.result.success != null) {

			L.d("                      ", speedSet.result.success);
		} else {
			L.d(" ", speedSet.result.error);
		}
	}

	@Override
	public boolean isTimeToExit () {
		return false;
	}

}
