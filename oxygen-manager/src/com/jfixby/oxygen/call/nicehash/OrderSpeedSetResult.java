
package com.jfixby.oxygen.call.nicehash;

public class OrderSpeedSetResult {
// {"result":{"success":"New order limit set to: 1.00"},"method":"orders.set.limit"}
	public EditOrderResult result;

	@Override
	public String toString () {
		return "" + this.result + "";
	}

}
