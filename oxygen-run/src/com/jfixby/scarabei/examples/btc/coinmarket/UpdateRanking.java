
package com.jfixby.scarabei.examples.btc.coinmarket;

import java.io.IOException;

import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class UpdateRanking {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		SystemSettings.setExecutionMode(ExecutionMode.DEMO);
		Coinmarketcap.downloadRanking();
		final List<Entry> rank = Coinmarketcap.getRanking();
		L.d("rank", rank);
	}

}
