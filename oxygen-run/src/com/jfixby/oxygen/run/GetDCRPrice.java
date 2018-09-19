
package com.jfixby.oxygen.run;

import java.io.IOException;

import com.jfixby.oxygen.call.GetTicker;
import com.jfixby.oxygen.call.market.Ticker;
import com.jfixby.oxygen.coin.CoinSign;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class GetDCRPrice {
	public static final void main (final String[] arg) throws IOException {
		ScarabeiDesktop.deploy();
		SystemSettings.setExecutionMode(ExecutionMode.DEMO);
		final Ticker btcdcr = GetTicker.get(CoinSign.VERTCOIN);
		L.d(btcdcr);
	}

}
