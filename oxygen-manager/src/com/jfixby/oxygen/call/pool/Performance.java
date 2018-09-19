
package com.jfixby.oxygen.call.pool;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.jfixby.oxygen.Speed;
import com.jfixby.oxygen.coin.ShitCoin;
import com.jfixby.scarabei.api.debug.Debug;

public class Performance {

	private final ShitCoin coins;
	private final Speed speed;
	private final ShitCoin coins24H;

	public Performance (final ShitCoin coinsX24H, final Speed speed) {
		this.coins = Debug.checkNull(coinsX24H.copy());
		this.speed = Debug.checkNull(speed.copy());

		final double coins = this.coins.v;
		final double speedV = this.speed.ajusted();

		this.coins24H = ShitCoin.newCoin(coins / speedV, this.coins.sign);

	}

	public Speed speedUnit () {
		return this.speed.oneUnit();
	}

	public ShitCoin coinsX24H () {
		return this.coins24H.copy();
	}

	public Speed speed () {
		return this.speed;
	}

	public static String formatedSpeed (final double speed) {
		final NumberFormat format = new DecimalFormat("000.0000");
		return format.format(speed).replaceAll("\\G0", " ");
	}

	@Override
	public String toString () {
		return "" + formatedSpeed(this.coinsX24H().v) + " " + this.coins.sign.symbol + " for 1 " + this.speed.order.sign + " x24H";
	}

	public Performance copy () {
		return new Performance(this.coins, this.speed);
	}

}
