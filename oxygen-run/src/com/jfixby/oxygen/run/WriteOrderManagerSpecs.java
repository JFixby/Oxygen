
package com.jfixby.oxygen.run;

import java.io.IOException;

import com.jfixby.oxygen.OrderManagerSpecs;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;
import com.jfixby.scarabei.api.time.TimeStream;
import com.jfixby.scarabei.aws.api.s3.S3;
import com.jfixby.scarabei.aws.desktop.s3.DesktopS3;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class WriteOrderManagerSpecs {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		SystemSettings.setExecutionMode(ExecutionMode.DEMO);
		S3.installComponent(new DesktopS3());

		final OrderManagerSpecs specs = new OrderManagerSpecs();

		specs.shitcoinSymbol = "DCR";
		specs.checkPeriod = TimeStream.MINUTE * 5;
		specs.niceHashKeyFileName = "nice-hash";
		specs.takeActionPeriod = TimeStream.MINUTE * 5;
		specs.targetProfitMinPercentage = 13.25f;
		specs.slowSpeed = "";
		specs.turboSpeed = "";
		specs.breathingPeriod = TimeStream.SECOND * 5;
		specs.orderID = "44";
		specs.region = "US";

		OxygenSetup.writeSpecs(specs, "example.json");

	}

}
