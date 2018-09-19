
package com.jfixby.oxygen.call;

import java.io.IOException;

import com.jfixby.oxygen.call.market.Ticker;
import com.jfixby.oxygen.coin.CoinSign;
import com.jfixby.oxygen.coin.MarketPair;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.json.JsonString;
import com.jfixby.scarabei.api.net.http.Http;
import com.jfixby.scarabei.api.net.http.HttpCall;
import com.jfixby.scarabei.api.net.http.HttpCallExecutor;
import com.jfixby.scarabei.api.net.http.HttpCallParams;
import com.jfixby.scarabei.api.net.http.HttpCallProgress;
import com.jfixby.scarabei.api.net.http.HttpURL;
import com.jfixby.scarabei.api.net.http.METHOD;

public class GetTicker {

	static final String API_CALL = "https://bittrex.com/api/v1.1/public/getticker";

	public static Ticker get (final MarketPair pair) throws IOException {
		final HttpCallParams param = Http.newCallParams();
		param.setMethod(METHOD.GET);
		param.setUseAgent(true);

// ?market=BTC-DCR
		final String API_CALL = "https://bittrex.com/api/v1.1/public/getticker";

		final HttpURL http_url = Http.newURL(API_CALL);
		param.setURL(http_url);

		final HttpCall call = Http.newCall(param);

		call.addRequestHeader("market", pair.toAPIParam());
		call.setServerTimeout(60000);
		final HttpCallExecutor exe = Http.newCallExecutor();
		final HttpCallProgress result;
		try {
// L.d("calling", http_url);
			result = exe.execute(call);
			final String jsonString = result.readResultAsString();
			final JsonString json = Json.newJsonString(jsonString);

			final Ticker ticker = Json.deserializeFromString(Ticker.class, json);
			ticker.pair = pair;
			// CacheTicker.cache(ticker, market);
			return ticker;

		} catch (final Throwable e) {
// e.printStackTrace();
			throw new IOException(e);
		}

	}

	public static Ticker get (final CoinSign shitCoinSymbol) throws IOException {
		final HttpCallParams param = Http.newCallParams();
		param.setMethod(METHOD.GET);
		param.setUseAgent(true);

// ?market=BTC-DCR
		final HttpURL http_url = Http.newURL(API_CALL);
		param.setURL(http_url);

		final HttpCall call = Http.newCall(param);
		call.addRequestHeader("market", "BTC-" + shitCoinSymbol.symbol);
		call.setServerTimeout(60000);
		final HttpCallExecutor exe = Http.newCallExecutor();
		final HttpCallProgress result;
		try {
// L.d("calling", http_url);
			result = exe.execute(call);
			final String jsonString = result.readResultAsString();
			final JsonString json = Json.newJsonString(jsonString);
			final Ticker ticker = Json.deserializeFromString(Ticker.class, json);
			ticker.symbol = shitCoinSymbol;
			// CacheTicker.cache(ticker, market);
			return ticker;

		} catch (final Throwable e) {
// e.printStackTrace();
			throw new IOException(e);
		}

	}

}
