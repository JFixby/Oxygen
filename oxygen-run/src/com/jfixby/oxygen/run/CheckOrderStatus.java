
package com.jfixby.oxygen.run;

import java.io.IOException;

import com.jfixby.oxygen.ManagableOrder;
import com.jfixby.oxygen.OrderManagerSpecs;
import com.jfixby.oxygen.TeamMates;
import com.jfixby.oxygen.coin.CoinSign;
import com.jfixby.oxygen.dcr.call.pool.DCRPoolPerformance;
import com.jfixby.oxygen.io.ReadReys;
import com.jfixby.oxygen.keys.NiceHashAPIKey;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;
import com.jfixby.scarabei.aws.api.s3.S3;
import com.jfixby.scarabei.aws.desktop.s3.DesktopS3;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class CheckOrderStatus {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		SystemSettings.setExecutionMode(ExecutionMode.DEMO);

		S3.installComponent(new DesktopS3());

		final String orderMangerSpecsFileName = "4361505.json";
		L.d("orderMangerSpecsFileName", orderMangerSpecsFileName);
		final OrderManagerSpecs specs = OxygenSetup.readOrderManagerSpecs(orderMangerSpecsFileName);
		OxygenSetup.deploy(specs.orderID);

		final NiceHashAPIKey niceHashKey = ReadReys.readKey(specs.niceHashKeyFileName);
		final CoinSign coin = specs.shitcoinSymbol();
		final ManagableOrder order = new ManagableOrder(specs, coin, niceHashKey, TeamMates.readFromFile(""),
			new DCRPoolPerformance());
		L.d();
		order.tryStatusCheck();
	}

}
