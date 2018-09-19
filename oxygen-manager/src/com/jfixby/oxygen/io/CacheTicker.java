
package com.jfixby.oxygen.io;

import java.io.IOException;

import com.jfixby.oxygen.call.market.Ticker;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.json.Json;

public class CacheTicker {

	public static void cache (final Ticker ticker, final String name) throws IOException {
		final File home = ApplicationHome.workingDir();
		final File cache = home.child("cache");
		final File tickers = cache.child("tickers");
		final File tickerFile = tickers.child(name + ".json");
		final String data = Json.serializeToString(ticker).toString();
		tickerFile.writeString(data);
	}

}
