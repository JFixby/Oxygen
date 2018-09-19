
package com.jfixby.oxygen.run;

import java.io.IOException;

import com.jfixby.oxygen.Speed;
import com.jfixby.oxygen.SpeedMagnitude;
import com.jfixby.oxygen.call.Algo;
import com.jfixby.oxygen.call.GetNicehashMarket;
import com.jfixby.oxygen.call.Region;
import com.jfixby.oxygen.call.nicehash.OrderSpeedSetResult;
import com.jfixby.oxygen.io.ReadReys;
import com.jfixby.oxygen.keys.NiceHashAPIKey;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;
import com.jfixby.scarabei.aws.api.s3.S3;
import com.jfixby.scarabei.aws.desktop.s3.DesktopS3;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class EditOrderSpeed {

	public static void main (final String[] args) throws IOException {

		ScarabeiDesktop.deploy();
		SystemSettings.setExecutionMode(ExecutionMode.DEMO);
		S3.installComponent(new DesktopS3());

		final String orderID = "4502238";
		OxygenSetup.deploy(orderID);

		final NiceHashAPIKey key = ReadReys.readKey("nice-hash");
		final String apiKey = key.key;
		final String myId = key.id;

		final Speed orderSpeed = Speed.newValue(0.7, SpeedMagnitude.THpS).oneUnit().mult(0.33);
		L.d("orderSpeed", orderSpeed);
		L.d("orderSpeed", orderSpeed.ajusted());
		L.d("orderSpeed", orderSpeed.kHashPerSecond());
// Sys.exit();
		final OrderSpeedSetResult result = GetNicehashMarket.setOrderSpeedLimit(Algo.DECRED, Region.resolve("EU"), myId, apiKey,
			orderID, orderSpeed);
		L.d("result", result);
	}

}
