
package com.jfixby.oxygen.run;

import java.io.IOException;

import com.jfixby.oxygen.call.pool.StratumPoolPerformance;
import com.jfixby.oxygen.dash.call.pool.DashCoinMinePoolPerformance;
import com.jfixby.oxygen.dash.call.pool.DashHubPoolPerformance;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class GetDashPoolPerformence {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		SystemSettings.setExecutionMode(ExecutionMode.DEMO);
		{
			final StratumPoolPerformance pool = new DashCoinMinePoolPerformance().read();
			L.d("users", pool.users);
			L.d("pool", pool);
		}
		{
			final StratumPoolPerformance pool = new DashHubPoolPerformance().read();
			L.d("users", pool.users);
			L.d("pool", pool);
		}
	}

}
