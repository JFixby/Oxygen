
package com.jfixby.oxygen;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.strings.Strings;

public class Speed {

	private final Double khashPerSec;
	public SpeedMagnitude order;

	Speed (final Double kHash_per_second, final SpeedMagnitude order) {
		Debug.checkNull(kHash_per_second);
		this.order = order;
		Debug.checkNull(order);
		this.khashPerSec = kHash_per_second;
	}

	public static String formatedSpeed (final double speed) {
		final NumberFormat format = new DecimalFormat("0000.00");
		return format.format(speed).replaceAll("\\G0", " ");
	}

	@Override
	public String toString () {
		return formatedSpeed(this.khashPerSec / this.order.order) + " " + this.order.sign;
	}

	public Speed copy () {
		return new Speed(this.khashPerSec, this.order);
	}

	public static Speed newValue (final double khash_per_second, final SpeedMagnitude order) {
		return new Speed(khash_per_second, order);
	}

	public double ajusted () {
		return this.khashPerSec / this.order.order;
	}

	public double kHashPerSecond () {
		return this.khashPerSec;
	}

	public static Speed parse (final String speedString) {
		final List<String> parts = Strings.split(speedString, " ");
// L.d("parts", parts);
		final double v = Double.parseDouble(parts.getElementAt(0));
		final SpeedMagnitude speedMagnitude = SpeedMagnitude.parse(parts.getElementAt(1));
		final double khash_per_second = v * speedMagnitude.order;
		return Speed.newValue(khash_per_second, speedMagnitude);
	}

	public Speed oneUnit () {
		return new Speed(1d * this.order.order, this.order);
	}

	public Speed add (final Speed add) {
		Debug.checkNull("add", add);
		return Speed.newValue(this.khashPerSec + add.kHashPerSecond(), this.order);
	}

	public Speed setOrder (final SpeedMagnitude newOrder) {
		this.order = newOrder;
		return this;
	}

	public Speed mult (final double d) {
		return new Speed(this.khashPerSec * d, this.order);
	}

}
