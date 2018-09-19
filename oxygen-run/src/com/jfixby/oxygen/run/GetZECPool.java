
package com.jfixby.oxygen.run;

import java.io.IOException;

import com.jfixby.oxygen.call.pool.StratumPoolPerformance;
import com.jfixby.oxygen.zec.call.pool.copy.ZECPoolPerformance;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class GetZECPool {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		SystemSettings.setExecutionMode(ExecutionMode.DEMO);
		final StratumPoolPerformance pool = new ZECPoolPerformance().read();
		L.d("users", pool.users);
		L.d("pool", pool);
	}

}
