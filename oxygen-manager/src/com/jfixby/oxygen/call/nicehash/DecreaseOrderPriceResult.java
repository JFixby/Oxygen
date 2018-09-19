
package com.jfixby.oxygen.call.nicehash;

public class DecreaseOrderPriceResult {
	public EditOrderResult result;
	public String method;

	@Override
	public String toString () {
		return "method: " + this.method + " -> " + this.result + "";
	}
}
