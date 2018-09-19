
package com.jfixby.oxygen.run;

import java.io.IOException;

import com.jfixby.oxygen.call.GetTicker;
import com.jfixby.oxygen.call.market.Ticker;
import com.jfixby.oxygen.call.pool.StratumPoolPerformance;
import com.jfixby.oxygen.coin.CoinSign;
import com.jfixby.oxygen.dcr.call.pool.DCRPoolPerformance;
import com.jfixby.oxygen.target.TargetPrice;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;
import com.jfixby.scarabei.aws.api.s3.S3;
import com.jfixby.scarabei.aws.desktop.s3.DesktopS3;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class BuildTargetPrice {
	public static final void main (final String[] arg) throws IOException {
		ScarabeiDesktop.deploy();
		SystemSettings.setExecutionMode(ExecutionMode.DEMO);

		S3.installComponent(new DesktopS3());

		final Ticker btcdcr = GetTicker.get(CoinSign.DECRED);
		L.d("btcdcr", btcdcr);

		final StratumPoolPerformance pool = new DCRPoolPerformance().read();
		L.d("users", pool.users);
		L.d("pool", pool);

		final TargetPrice price = new TargetPrice(btcdcr, pool);
		price.print();
// L.d("pool", pool);
	}

}
