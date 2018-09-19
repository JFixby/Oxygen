
package com.jfixby.oxygen;

import com.jfixby.oxygen.coin.CoinSign;
import com.jfixby.scarabei.api.debug.Debug;

public class OrderManagerSpecs {
	public String region;
	public String shitcoinSymbol;
	public String orderID;

	public String strategy;

	public Float targetProfitMinPercentage;
	public Float targetProfitMinWindowSizePercentage;

	public String slowSpeed;
	public String turboSpeed;

	public Long breathingPeriod;
	public Long checkPeriod;
	public Long takeActionPeriod;

	public String niceHashKeyFileName;

	public Float criticalLoadPercentage;

	public Double criticalLoadBtcJump;
// 0.0001

	public CoinSign shitcoinSymbol () {
		Debug.checkNull("shitcoinSymbol", this.shitcoinSymbol);
		return CoinSign.resolve(this.shitcoinSymbol);
	}
}
