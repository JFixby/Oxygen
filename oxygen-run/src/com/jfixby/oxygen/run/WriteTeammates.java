
package com.jfixby.oxygen.run;

import java.io.IOException;

import com.jfixby.oxygen.TeamMates;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.json.JsonString;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.strings.Strings;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class WriteTeammates {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		SystemSettings.setExecutionMode(ExecutionMode.DEMO);

		final String excel = "4582072	4582073	4582074	4582120	4582126	4582081	4582093	4582098	4582105	4582116";
		final List<String> ocs = Strings.split(excel, "	");
		final TeamMates team = new TeamMates();
		team.orders.addAll(ocs.toJavaList());
		final JsonString json = Json.serializeToString(team);

		L.d(json);

		final File file = LocalFileSystem.ApplicationHome().child("decred-team.json");
		L.d("writing", file);
		file.writeString(json.toString());
	}

}
