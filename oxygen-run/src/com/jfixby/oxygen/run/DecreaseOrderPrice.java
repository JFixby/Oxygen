
package com.jfixby.oxygen.run;

import java.io.IOException;

import com.jfixby.oxygen.call.Algo;
import com.jfixby.oxygen.call.GetNicehashMarket;
import com.jfixby.oxygen.call.Region;
import com.jfixby.oxygen.call.nicehash.DecreaseOrderPriceResult;
import com.jfixby.oxygen.io.ReadReys;
import com.jfixby.oxygen.keys.NiceHashAPIKey;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class DecreaseOrderPrice {

	public static void main (final String[] args) throws IOException {

		ScarabeiDesktop.deploy();
		SystemSettings.setExecutionMode(ExecutionMode.DEMO);
		final NiceHashAPIKey key = ReadReys.readKey("nice-hash");
		final String apiKey = key.key;
		final String myId = key.id;
		final String orderID = "4269337";
		OxygenSetup.deploy(orderID);
// L.d("my orders", nicehash.result.orders);
		final DecreaseOrderPriceResult result = GetNicehashMarket.decreaseOrderPrice(Algo.DECRED, Region.resolve(""), myId, apiKey,
			orderID);
		L.d("result", result);
	}

}
