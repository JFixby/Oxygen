
package com.jfixby.scarabei.examples.btc.coinmarket;

import java.math.BigDecimal;

public class Entry {
	public String id;
	public String symbol;
	public int rank;
	public BigDecimal price_usd;
	public BigDecimal market_cap_usd;
// public BigDecimal price_eur;
// public BigDecimal market_cap_eur;
	public BigDecimal percent_change_24h = BigDecimal.valueOf(Integer.MIN_VALUE);
	public static final BigDecimal BD_100 = BigDecimal.valueOf(100);

	public String toCSV () {
		return this.rank + "	" + this.id + "	" + this.symbol + "	" + this.price_usd + "	"
			+ this.market_cap_usd.divide(new BigDecimal("1000000")) + "	";
	}

	@Override
	public String toString () {
		return "Entry [id=" + this.id + ", symbol=" + this.symbol + ", rank=" + this.rank + ", price_usd=" + this.price_usd
			+ ", market_cap_usd=" + this.market_cap_usd + ", percent_change_24h=" + this.percent_change_24h + "]";
	}

// public String toCSV (final double amount) {
// return this.rank + " " + this.id + " " + this.symbol + " " + this.price_eur + " " + this.market_cap_eur + " "
// + this.percent_change_24h.divide(this.BD_100) + " " + amount;
// }

}
