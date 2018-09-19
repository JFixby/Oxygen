
package com.jfixby.oxygen.run;

import java.io.IOException;

import com.jfixby.oxygen.OrderManager;
import com.jfixby.oxygen.OrderManagerSpecs;
import com.jfixby.oxygen.TeamMates;
import com.jfixby.oxygen.call.Pool;
import com.jfixby.oxygen.coin.CoinSign;
import com.jfixby.oxygen.dash.call.pool.DashHubPoolPerformance;
import com.jfixby.oxygen.dcr.call.pool.DCRPoolPerformance;
import com.jfixby.oxygen.vtc.call.pool.VTCPoolPerformance;
import com.jfixby.oxygen.zec.call.pool.copy.ZECPoolPerformance;
import com.jfixby.scarabei.api.err.Err;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.Sys;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;
import com.jfixby.scarabei.aws.api.s3.S3;
import com.jfixby.scarabei.aws.desktop.s3.DesktopS3;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class RunOrderManager {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		SystemSettings.setExecutionMode(ExecutionMode.DEMO);

		S3.installComponent(new DesktopS3());

		final String orderMangerSpecsFileName = args[0];
		final String teamFileName = "team.json";
// final String orderMangerSpecsFileName = "dash-h-20.json";
		L.d("orderMangerSpecsFileName", orderMangerSpecsFileName);
		final OrderManagerSpecs specs = OxygenSetup.readOrderManagerSpecs(orderMangerSpecsFileName);
		OxygenSetup.deploy(specs.orderID);

		Pool pool;
		if (specs.shitcoinSymbol() == CoinSign.DECRED) {
			pool = new DCRPoolPerformance();
		} else if (specs.shitcoinSymbol() == CoinSign.VERTCOIN) {
			pool = new VTCPoolPerformance();
		} else if (specs.shitcoinSymbol() == CoinSign.ZCASH) {
			pool = new ZECPoolPerformance();
		} else if (specs.shitcoinSymbol() == CoinSign.DASH) {
			pool = new DashHubPoolPerformance();
		} else {
			Err.reportError(specs.shitcoinSymbol);
			pool = null;
			Sys.exit();
		}

		final OrderManager manager = new OrderManager(specs, TeamMates.readFromFile(teamFileName), pool);
		manager.start();
	}

}
