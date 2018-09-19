
package com.jfixby.oxygen.call;

import java.io.IOException;

import com.jfixby.oxygen.Speed;
import com.jfixby.oxygen.SpeedMagnitude;
import com.jfixby.oxygen.call.nicehash.DecreaseOrderPriceResult;
import com.jfixby.oxygen.call.nicehash.IncreaseOrderPriceResult;
import com.jfixby.oxygen.call.nicehash.NicehashOrder;
import com.jfixby.oxygen.call.nicehash.NicehashOrders;
import com.jfixby.oxygen.call.nicehash.OrderSpeedSetResult;
import com.jfixby.oxygen.coin.BTC;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.json.JsonString;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.net.http.Http;
import com.jfixby.scarabei.api.net.http.HttpCall;
import com.jfixby.scarabei.api.net.http.HttpCallExecutor;
import com.jfixby.scarabei.api.net.http.HttpCallParams;
import com.jfixby.scarabei.api.net.http.HttpCallProgress;
import com.jfixby.scarabei.api.net.http.HttpURL;
import com.jfixby.scarabei.api.net.http.METHOD;

public class GetNicehashMarket {
	public static final String API_CALL = "https://api.nicehash.com/api";
	public static final String API_CALL_HTML = "https://www.nicehash.com/marketplace/";

	public static NicehashOrders getOrders (final Algo algo, final Region region, final SpeedMagnitude speedMagnitude)
		throws IOException {
		return getOrdersJSON(algo, region, speedMagnitude);
	}

	private static NicehashOrders getOrdersJSON (final Algo algo, final Region region, final SpeedMagnitude speedMagnitude)
		throws IOException {
		Debug.checkNull("speedOrder", speedMagnitude);
		final HttpCallParams param = Http.newCallParams();
		param.setMethod(METHOD.GET);
		param.setUseAgent(true);

// ?market=BTC-DCR
		final HttpURL http_url = Http.newURL(API_CALL);
		param.setURL(http_url);

		final HttpCall call = Http.newCall(param);
		call.addRequestHeader("method", "orders.get");
		call.addRequestHeader("location", region.code);
		call.addRequestHeader("algo", algo.code + "");
		call.setServerTimeout(30000L);
		final HttpCallExecutor exe = Http.newCallExecutor();
		final HttpCallProgress result;
		try {
// L.d("calling", http_url);
			result = exe.execute(call);
			final String jsonString = result.readResultAsString();
// L.d("jsonString", jsonString);
			final JsonString json = Json.newJsonString(jsonString);
// Json.printPretty(json);
			final NicehashOrders morrde = Json.deserializeFromString(NicehashOrders.class, json);
			for (final NicehashOrder order : morrde.result.orders) {
				order.speedMagnitude = speedMagnitude;
			}
// Json.printPretty(json);
// L.d("json", json);

			return morrde;

		} catch (final Throwable e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	public static NicehashOrders getMyOrders (final Algo algo, final Region region, final String myID, final String apiKey,
		final SpeedMagnitude speedMagnitude) throws IOException {
		Debug.checkNull("speedOrder", speedMagnitude);

		final HttpCallParams param = Http.newCallParams();
		param.setMethod(METHOD.GET);
		param.setUseAgent(true);

// ?market=BTC-DCR
		final HttpURL http_url = Http.newURL(API_CALL);
		param.setURL(http_url);

		final HttpCall call = Http.newCall(param);
		call.addRequestHeader("method", "orders.get");
		call.addRequestHeader("key", apiKey);
		call.addRequestHeader("id", myID);
		call.addRequestHeader("my", "");
		call.addRequestHeader("location", region.code);
		call.addRequestHeader("algo", algo.code + "");
		call.setServerTimeout(30000L);
		call.setServerTimeout(60000);
		final HttpCallExecutor exe = Http.newCallExecutor();
		final HttpCallProgress result;
		try {
// L.d("calling", http_url);
			result = exe.execute(call);
			final String jsonString = result.readResultAsString();
// L.d("jsonString", jsonString);
			final JsonString json = Json.newJsonString(jsonString);
// Json.printPretty(json);
			final NicehashOrders morrde = Json.deserializeFromString(NicehashOrders.class, json);
			for (final NicehashOrder order : morrde.result.orders) {
				order.speedMagnitude = speedMagnitude;
			}
			// Json.printPretty(json);
// L.d("json", json);

			return morrde;

		} catch (final Throwable e) {
// e.printStackTrace();
			throw new IOException(e);
		}
	}

	public static OrderSpeedSetResult setOrderSpeedLimit (final Algo algo, final Region region, final String myID,
		final String apiKey, final String orderID, final Speed orderSpeedLimit) throws IOException {
		final HttpCallParams param = Http.newCallParams();
		param.setMethod(METHOD.GET);
		param.setUseAgent(true);

// ?market=BTC-DCR
		final HttpURL http_url = Http.newURL(API_CALL);
		param.setURL(http_url);

		final HttpCall call = Http.newCall(param);
		call.addRequestHeader("method", "orders.set.limit");
		call.addRequestHeader("key", apiKey);
		call.addRequestHeader("id", myID);
		call.addRequestHeader("order", orderID);
		call.addRequestHeader("my", "");
		final String limit = orderSpeedLimit.ajusted() + "";
// L.d("limit", limit);
// Sys.exit();
		call.addRequestHeader("limit", limit);
		call.addRequestHeader("location", region.code);
		call.addRequestHeader("algo", algo.code + "");
		call.setServerTimeout(30000L);
		call.setServerTimeout(60000);
		final HttpCallExecutor exe = Http.newCallExecutor();
		final HttpCallProgress result;
		try {
			L.d("calling", http_url);
			result = exe.execute(call);
			final String jsonString = result.readResultAsString();
// L.d("jsonString", jsonString);
			final JsonString json = Json.newJsonString(jsonString);
// Json.printPretty(json);
			final OrderSpeedSetResult morrde = Json.deserializeFromString(OrderSpeedSetResult.class, json);
// Json.printPretty(json);
// L.d("json", json);

			return morrde;

		} catch (final Throwable e) {
// e.printStackTrace();
			throw new IOException(e);
		}
	}

	public static IncreaseOrderPriceResult increaseOrderPrice (final Algo algo, final Region region, final String myId,
		final String apiKey, final String orderID, final BTC incPrice) throws IOException {

		final HttpCallParams param = Http.newCallParams();
		param.setMethod(METHOD.GET);
		param.setUseAgent(true);

// ?market=BTC-DCR
		final HttpURL http_url = Http.newURL(API_CALL);
		param.setURL(http_url);

		final HttpCall call = Http.newCall(param);
		call.addRequestHeader("method", "orders.set.price");
		call.addRequestHeader("key", apiKey);
		call.addRequestHeader("id", myId);
		call.addRequestHeader("order", orderID);
		call.addRequestHeader("my", "");
		call.addRequestHeader("price", (incPrice.v) + "");
		call.addRequestHeader("location", region.code);
		call.addRequestHeader("algo", algo.code + "");
		call.setServerTimeout(30000L);
		call.setServerTimeout(60000);
		final HttpCallExecutor exe = Http.newCallExecutor();
		final HttpCallProgress result;
		try {
// L.d("calling", http_url);
			result = exe.execute(call);
			final String jsonString = result.readResultAsString();
// L.d("jsonString", jsonString);
			final JsonString json = Json.newJsonString(jsonString);
// Json.printPretty(json);
			final IncreaseOrderPriceResult morrde = Json.deserializeFromString(IncreaseOrderPriceResult.class, json);
// Json.printPretty(json);
// L.d("json", json);

			return morrde;

		} catch (final Throwable e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	public static DecreaseOrderPriceResult decreaseOrderPrice (final Algo algo, final Region region, final String myId,
		final String apiKey, final String orderID) throws IOException {

		final HttpCallParams param = Http.newCallParams();
		param.setMethod(METHOD.GET);
		param.setUseAgent(true);

// ?market=BTC-DCR
		final HttpURL http_url = Http.newURL(API_CALL);
		param.setURL(http_url);

		final HttpCall call = Http.newCall(param);
		call.addRequestHeader("method", "orders.set.price.decrease");
		call.addRequestHeader("key", apiKey);
		call.addRequestHeader("id", myId);
		call.addRequestHeader("order", orderID);
		call.addRequestHeader("my", "");
		call.addRequestHeader("location", region.code);
		call.addRequestHeader("algo", algo.code + "");

		call.setServerTimeout(60000);
		final HttpCallExecutor exe = Http.newCallExecutor();
		final HttpCallProgress result;
		try {
// L.d("calling", http_url);
			result = exe.execute(call);
			final String jsonString = result.readResultAsString();
// L.d("jsonString", jsonString);
			final JsonString json = Json.newJsonString(jsonString);
// Json.printPretty(json);
			final DecreaseOrderPriceResult morrde = Json.deserializeFromString(DecreaseOrderPriceResult.class, json);
// Json.printPretty(json);
// L.d("json", json);

			return morrde;

		} catch (final Throwable e) {
// e.printStackTrace();
			throw new IOException(e);
		}
	}

}
