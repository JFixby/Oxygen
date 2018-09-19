
package com.jfixby.oxygen.call.pool;

import com.jfixby.oxygen.Speed;
import com.jfixby.oxygen.coin.ShitCoin;

public class UserPerformance {

	public int rank;
	public boolean donor;
	public String login;
	public Speed speed;
	public ShitCoin coinsPerDay;
	private double btcPerDay;

	@Override
	public String toString () {
		return "UserPerformance[" + this.tHPerSec() + " coins/Day= " + this.coinsPerDay() + " performance= " + (this.performance())
			+ " rank= " + this.rank + " login= " + this.login + "]";
	}

	private Speed tHPerSec () {
		return (this.speed);
	}

	private ShitCoin coinsPerDay () {
		return this.coinsPerDay;
	}

	public Performance performance () {
		return new Performance(this.coinsPerDay, this.speed);
	}

}
