
package com.jfixby.oxygen.call.nicehash;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.jfixby.oxygen.Percentage;
import com.jfixby.oxygen.Speed;
import com.jfixby.oxygen.SpeedMagnitude;
import com.jfixby.oxygen.call.Algo;
import com.jfixby.oxygen.coin.BTC;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.err.Err;

public class NicehashOrder {
	private double limit_speed;
	private boolean alive;
	private double price;
	public String id;
	private int type;
	private int workers;
	private int algo;
	private double accepted_speed;// Gh/s
	public SpeedMagnitude speedMagnitude;

	@Override
	public String toString () {
		return "" + this.algo() + " #" + this.id + " type=" + this.type() + " price= " + this.price() + " workers= "
			+ this.workers() + " limit_speed= " + this.speed_limit() + " accepted_speed= " + this.accepted_speed();
	}

	private String alive () {
		return this.alive ? "+" : "-";
	}

	private String workers () {
		final NumberFormat format = new DecimalFormat("00000");
		return format.format(this.workers).replaceAll("\\G0", " ");
// return this.workers + "";
	}

	private String type () {
		if (this.type == 1) {
			return "F";
		}
		if (this.type == 0) {
			return "S";
		}
		Err.reportError("Unknown order type:" + this.type);
		return null;
	}

	public Algo algo () {
		return Algo.resolve(this.algo);
	}

	public Speed accepted_speed () {
		return Speed.newValue(this.accepted_speed * SpeedMagnitude.GHpS.order, SpeedMagnitude.GHpS).setOrder(this.speedMagnitude);
	}

	public BTC price () {
		return new BTC(this.price);
	}

	public Speed speed_limit () {
		Debug.checkNull("speedMagnitude", this.speedMagnitude);
		if (this.limit_speed == 0) {
			return Speed.newValue(9999, this.speedMagnitude);
		}
		return Speed.newValue(this.limit_speed * this.speedMagnitude.order, this.speedMagnitude);
	}

	public Percentage getLoad () {
		final double d = this.speed_limit().kHashPerSecond();
		final Float x = (float)(this.accepted_speed().kHashPerSecond() * 100 / d);
		return new Percentage(x);
	}

	public boolean isFixed () {
		return this.type().equalsIgnoreCase("F");
	}

}
