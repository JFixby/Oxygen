
package com.jfixby.scarabei.examples.btc.coinmarket;

import java.io.IOException;

import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
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

public class Coinmarketcap {

	public static final String fileName = "coinmarketcap.json";

	public static final List<Entry> getRanking () throws IOException {
		final File inputFile = LocalFileSystem.ApplicationHome().child("btc").child(fileName);
		final String jsonString = inputFile.readToString();
		final java.util.List<java.util.Map> list = Json.deserializeFromString(java.util.List.class, jsonString);
		final List<Entry> btx = Collections.newList();
		for (final java.util.Map map : list) {
			final JsonString json_i = Json.serializeToString(map);
			final Entry btc = Json.deserializeFromString(Entry.class, json_i);
			btx.add(btc);
		}
		return btx;
	}

	public static final void downloadRanking () throws IOException {
		final HttpURL url = Http.newURL("https://api.coinmarketcap.com/v1/ticker/");
		final HttpCallParams params = Http.newCallParams();
		params.setURL(url);
		params.setUseAgent(false);
		params.setMethod(METHOD.GET);
		final HttpCall call = Http.newCall(params);
// call.addRequestHeader("convert", "USD");
		call.addRequestHeader("limit", "500");
		final HttpCallExecutor exe = Http.newCallExecutor();
		final HttpCallProgress result = exe.execute(call);
		final String response = result.readResultAsString();
		L.d("response", response);
		final File storeFile = LocalFileSystem.ApplicationHome().child("btc").child(fileName);
		L.d("writing", storeFile);
		storeFile.writeString(response);
	}

}
