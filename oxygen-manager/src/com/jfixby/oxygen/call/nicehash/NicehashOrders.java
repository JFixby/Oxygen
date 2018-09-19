
package com.jfixby.oxygen.call.nicehash;

import java.io.IOException;

public class NicehashOrders {
	public String method;
	public NicehashOrdersResult result;

	public NicehashOrder findMyOrder (final String orderID) throws IOException {
		for (final NicehashOrder order : this.result.orders) {
			if (order.id.equalsIgnoreCase(orderID)) {
				return order;
			}
		}
		throw new IOException("order not found: " + orderID);
	}

}
