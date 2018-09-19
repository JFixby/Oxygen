
package com.jfixby.oxygen.io;

import java.io.IOException;

import com.jfixby.oxygen.call.pool.StratumPoolPerformance;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.json.Json;

public class CachePool {
	public static void cache (final StratumPoolPerformance ticker, final String name) throws IOException {
		final File home = ApplicationHome.workingDir();
		final File cache = home.child("cache");
		final File tickers = cache.child("pools");
		final File tickerFile = tickers.child(name + ".json");
		final String data = Json.serializeToString(ticker).toString();
		tickerFile.writeString(data);
	}
}
