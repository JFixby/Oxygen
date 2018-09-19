
package com.jfixby.oxygen;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.strings.Strings;

public class Percentage {
	public float v;

	public Percentage (final Float value) {
		Debug.checkNull(value);
		this.v = value;
	}

	public static String formatPercent (final Percentage p) {

		final double value = p.v;

		final NumberFormat format = new DecimalFormat("0000.00");

		if (value < 0) {
			String pos = format.format(-value).replaceAll("\\G0", " ");
			final int block = pos.length();
			pos = pos.replace(" ", "");
			pos = "-" + pos;
			final String prefix = Strings.prefix(" ", block - pos.length());
			pos = prefix + pos;
			return pos;
		}

		return format.format(value).replaceAll("\\G0", " ");

	}

	@Override
	public String toString () {
		return formatPercent(this) + "%";
	}

	public double share () {
		return this.v / 100f;
	}

}
