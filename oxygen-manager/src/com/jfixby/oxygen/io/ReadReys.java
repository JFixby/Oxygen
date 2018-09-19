
package com.jfixby.oxygen.io;

import java.io.IOException;

import com.jfixby.oxygen.keys.NiceHashAPIKey;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.log.L;

public class ReadReys {

	public static void writeKey (final NiceHashAPIKey key, final String name) throws IOException {
		final File niceHashKeys = ApplicationHome.workingDir().parent().child("keys").child(name + ".json");
		final String json = Json.serializeToString(key).toString();
		niceHashKeys.writeString(json);
	}

	public static NiceHashAPIKey readKey (final String name) throws IOException {
		final File niceHashKeys = ApplicationHome.workingDir().parent().child("keys").child(name + ".json");
		L.d("reading", niceHashKeys);
		final String jsonString = niceHashKeys.readToString();
		final NiceHashAPIKey key = Json.deserializeFromString(NiceHashAPIKey.class, jsonString);
		Debug.checkNull("NiceHashAPIKey.id", key.id);
		Debug.checkNull("NiceHashAPIKey.key", key.key);
		L.d("loaded key", key);
		return key;
	}

}
