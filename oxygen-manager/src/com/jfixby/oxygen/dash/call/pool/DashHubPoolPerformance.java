
package com.jfixby.oxygen.dash.call.pool;

import java.io.IOException;

import com.jfixby.oxygen.Speed;
import com.jfixby.oxygen.SpeedMagnitude;
import com.jfixby.oxygen.call.Pool;
import com.jfixby.oxygen.call.pool.StratumPoolPerformance;
import com.jfixby.oxygen.call.pool.UserPerformance;
import com.jfixby.oxygen.coin.CoinSign;
import com.jfixby.oxygen.coin.ShitCoin;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.net.http.Http;
import com.jfixby.scarabei.api.net.http.HttpCall;
import com.jfixby.scarabei.api.net.http.HttpCallExecutor;
import com.jfixby.scarabei.api.net.http.HttpCallParams;
import com.jfixby.scarabei.api.net.http.HttpCallProgress;
import com.jfixby.scarabei.api.net.http.HttpURL;
import com.jfixby.scarabei.api.net.http.METHOD;
import com.jfixby.scarabei.api.strings.Strings;

public class DashHubPoolPerformance implements Pool {

	public static final String API_CALL = "https://dash.miningpoolhub.com/index.php";

	public static StratumPoolPerformance getP () throws IOException {
		final HttpCallParams param = Http.newCallParams();
		param.setMethod(METHOD.GET);
		param.setUseAgent(true);

// ?market=BTC-DCR
		final HttpURL http_url = Http.newURL(API_CALL);
		param.setURL(http_url);

		final HttpCall call = Http.newCall(param);
		call.addRequestHeader("page", "statistics");
		call.addRequestHeader("action", "pool");
		call.setServerTimeout(60000);
		final HttpCallExecutor exe = Http.newCallExecutor();
		final HttpCallProgress result;
		try {
// L.d("calling", http_url);
			result = exe.execute(call);
			final String html = result.readResultAsString();
// L.d("jsonString", jsonString);
			final StratumPoolPerformance performance = new StratumPoolPerformance();
			extract(html, performance);
// CachePool.cache(performance, "coinmine");
// final JsonString json = Json.newJsonString(jsonString);
// final Ticker ticker = Json.deserializeFromString(Ticker.class, json);
// CachePool.cache(ticker, market);
			return performance;

		} catch (final Throwable e) {
// e.printStackTrace();
			throw new IOException(e);
		}
	}

	private static void extract (final String html, final StratumPoolPerformance performance) throws IOException {
		{
			final int start = html.indexOf("Contributor Hashrates");

		}
		{
			final int start = html.indexOf("Contributor Hashrates");
			final int tableStart = html.indexOf("<tbody>", start) + "<tbody>".length();
			final int tableEnd = html.indexOf("</tbody>", tableStart);
			String tableContent = html.substring(tableStart, tableEnd);
			tableContent = tableContent.replaceAll("\n", "").replaceAll("\r", "").replaceAll("<tbody>", "").replaceAll(" ", "")
				.replaceAll("<trclass=\"odd\">", "").replaceAll("<trclass=\"even\">", "").replaceAll("<tdalign=\"right\">", "");
			final List<String> splits = Collections.newList();

// L.d(tableContent);

			final List<String> trList = Strings.split(tableContent, "<tdalign=\"center\">").filter( (x) -> x.length() > 0);
// L.d("trList", trList);
			for (final String line : trList) {
// L.d("line", line);
				final List<String> lines = Strings.split(line, "</td>").filter( (x) -> x.length() > 0);
// L.d(" ", lines);
				splits.addAll(lines);
			}
			if (trList.size() < 10) {
				L.e("html");
				L.e("trList", trList);
				throw new IOException("Failed to get pool performance");
			}
// L.d("splits", splits);

// L.d("trList", trList);
			for (final String line : trList) {
				final List<String> sections = Strings.split(line, "</td>");
// L.d("sections", sections);
				final UserPerformance user = new UserPerformance();
				performance.users.add(user);
				int i = 0;
				for (final String sec : sections) {
					final int begin = sec.lastIndexOf(">") + ">".length();
					final String data = sec.substring(begin, sec.length());
// L.d("data", data);
					if (i == 0) {
						user.rank = Integer.parseInt(data);
					}
					if (i == 1) {
						user.donor = false;
					}
					if (i == 2) {
						user.login = data;
					}
					if (i == 3) {

						final double hashesPsec = Double.parseDouble(data.replaceAll(",", ""));
						final SpeedMagnitude order = SpeedMagnitude.GSols;
						final Speed speed = Speed.newValue(hashesPsec, order);
						user.speed = speed;

					}
					if (i == 4) {
						user.coinsPerDay = ShitCoin.newCoin(Double.parseDouble(data.replaceAll(",", "")), CoinSign.DASH);
					}
					if (i == 5) {
// user.btcPerDay = Double.parseDouble(data.replaceAll(",", ""));
					}
					i++;
				}

				if (performance.users.size() == 0) {
					throw new IOException("Failed to get pool performance");
				}
			}
// L.d("user", user);
		}
	}

	@Override
	public StratumPoolPerformance read () throws IOException {
		return getP();
	}

}
