
package com.jfixby.oxygen;

import java.io.IOException;

import com.jfixby.oxygen.call.Pool;
import com.jfixby.oxygen.coin.CoinSign;
import com.jfixby.oxygen.io.ReadReys;
import com.jfixby.oxygen.keys.NiceHashAPIKey;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.err.Err;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.Sys;

public class OrderManager {

	private final Thread thread;
	private final Runnable updater = new Runnable() {
		@Override
		public void run () {
			try {
				OrderManager.this.update();
			} catch (final Throwable e) {
				Err.reportError(e);
				Sys.exit();
			}
		}

	};

	private long lastAction;
	private final ManagableOrder order;
	private final long checkPeriod;
	private final long takeActionPeriod;
	private final DecisionMaker targetProfitDecisionMaker;
	private final OrderManagerObserver observer;
	private final long breathingPeriod;
	private final NiceHashAPIKey niceHashKey;
	private final CoinSign shitCoinSymbol;

	public OrderManager (final OrderManagerSpecs specs, final TeamMates team, final Pool pool
// , final BTCUSDProvider btcUsd
	) throws IOException {
		this.thread = new Thread(this.updater);
		L.d("Deploying OrderManager");
		L.d(Json.serializeToString(specs));

		this.breathingPeriod = specs.breathingPeriod;
		Debug.checkNull("breathingPeriod", this.breathingPeriod);

		this.checkPeriod = specs.checkPeriod;
		Debug.checkNull("checkPeriod", this.checkPeriod);

		this.takeActionPeriod = specs.takeActionPeriod;
		Debug.checkNull("takeActionPeriod", this.takeActionPeriod);

		Debug.checkNull("niceHashKeyFileName", specs.niceHashKeyFileName);

		this.niceHashKey = ReadReys.readKey(specs.niceHashKeyFileName);

		this.shitCoinSymbol = CoinSign.resolve(specs.shitcoinSymbol);
		Debug.checkNull("shitCoinSymbol", this.shitCoinSymbol);

		this.order = new ManagableOrder(specs, this.shitCoinSymbol, this.niceHashKey, team, pool);
		Debug.checkNull("specs.strategy", specs.strategy);
		if (specs.strategy.equals(MedianSpeedDecisionMaker.NAME)) {
			this.targetProfitDecisionMaker = new MedianSpeedDecisionMaker(specs, this.niceHashKey);
		} else {
			this.targetProfitDecisionMaker = null;
			Err.reportError("Unsupported strategy: " + specs.strategy);
			Sys.exit();
		}
		this.observer = new OrderManagerObserver(this);

	}

	public void start () {
		this.thread.start();
	}

	private void update () throws Throwable {
		this.order.init();
		this.lastAction = 0L;
		while (true) {

			boolean checkSuccess = false;
			if (this.order.updateAsked() || this.order.timeSinceLastCheck() > this.checkPeriod) {
				checkSuccess = this.order.tryStatusCheck();
			}

			if (checkSuccess && this.targetProfitDecisionMaker.timeSinceLastSuccAction() > this.takeActionPeriod) {
				this.targetProfitDecisionMaker.tryToAjustStrategy(this.order);
			}

			this.observer.evaluate();

			if (this.targetProfitDecisionMaker.isTimeToExit()) {
				L.d("Exiting program");
				Sys.exit();
			}
			Sys.sleep(this.breathingPeriod);
		}

	}

}
