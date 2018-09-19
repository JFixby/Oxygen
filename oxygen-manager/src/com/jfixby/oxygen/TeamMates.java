
package com.jfixby.oxygen;

import java.io.IOException;
import java.util.ArrayList;

import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.json.Json;

public class TeamMates {

	public ArrayList<String> orders = new ArrayList<>();

	public static final TeamMates readFromFile (final String fileName) throws IOException {
		final File teamFile = LocalFileSystem.ApplicationHome().child(fileName);
		final String json = teamFile.readToString();
		final TeamMates team = Json.deserializeFromString(TeamMates.class, json);
		return team;
	}
}
