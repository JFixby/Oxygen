
package com.jfixby.oxygen.run;

import java.io.IOException;

import com.jfixby.oxygen.SpeedMagnitude;
import com.jfixby.oxygen.call.Algo;
import com.jfixby.oxygen.call.GetNicehashMarket;
import com.jfixby.oxygen.call.Region;
import com.jfixby.oxygen.call.nicehash.NicehashOrders;
import com.jfixby.oxygen.io.ReadReys;
import com.jfixby.oxygen.keys.NiceHashAPIKey;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;
import com.jfixby.scarabei.aws.api.s3.S3;
import com.jfixby.scarabei.aws.desktop.s3.DesktopS3;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class GetMyOrders {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		SystemSettings.setExecutionMode(ExecutionMode.DEMO);
		S3.installComponent(new DesktopS3());

		OxygenSetup.deploy("0000");

		final NiceHashAPIKey key = ReadReys.readKey("nice-hash");
		final String apiKey = key.key;
		final String myId = key.id;
		final NicehashOrders nicehash = GetNicehashMarket.getMyOrders(Algo.DECRED, Region.resolve("EU"), myId, apiKey,
			SpeedMagnitude.THpS);
		L.d("my orders", nicehash.result.orders);
	}

}
