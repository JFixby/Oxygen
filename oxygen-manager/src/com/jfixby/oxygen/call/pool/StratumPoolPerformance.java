
package com.jfixby.oxygen.call.pool;

import java.util.ArrayList;

import com.jfixby.scarabei.api.err.Err;

public class StratumPoolPerformance implements PoolPerformance {

	@Override
	public String toString () {
		return "PoolPerformance [performance=" + this.perFlowUnitX24H() + "]";
	}

	public ArrayList<UserPerformance> users = new ArrayList<>();

	@Override
	public Performance perFlowUnitX24H () {
		if (this.users.size() == 0) {
			Err.reportError("No data");
		}
		Performance min = this.users.get(0).performance().copy();
		for (final UserPerformance user : this.users) {
			if (min.coinsX24H().v > user.performance().coinsX24H().v) {
				min = user.performance().copy();
			}
		}
		return min;
	}

}
