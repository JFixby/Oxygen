
package com.jfixby.oxygen.run;

import java.io.IOException;

import com.jfixby.oxygen.SpeedMagnitude;
import com.jfixby.oxygen.call.Algo;
import com.jfixby.oxygen.call.GetNicehashMarket;
import com.jfixby.oxygen.call.Region;
import com.jfixby.oxygen.call.nicehash.NicehashOrder;
import com.jfixby.oxygen.call.nicehash.NicehashOrders;
import com.jfixby.oxygen.coin.BTC;
import com.jfixby.oxygen.io.ReadReys;
import com.jfixby.oxygen.keys.NiceHashAPIKey;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class IncreaseOrderPrice {

	public static void main (final String[] args) throws IOException {

		ScarabeiDesktop.deploy();
		SystemSettings.setExecutionMode(ExecutionMode.DEMO);
		final NiceHashAPIKey key = ReadReys.readKey("nice-hash");
		final String apiKey = key.key;
		final String myId = key.id;
		final NicehashOrders nicehash = GetNicehashMarket.getMyOrders(Algo.DECRED, Region.resolve(""), myId, apiKey,
			SpeedMagnitude.THpS);
// L.d("my orders", nicehash.result.orders);
		final NicehashOrder order = nicehash.result.orders.get(0);
		L.d("edit order", order);
		final BTC incPrice = new BTC(order.price().v + 0.0001);
		GetNicehashMarket.increaseOrderPrice(order.algo(), Region.resolve(""), myId, apiKey, order.id, incPrice);
	}

}
