
package com.jfixby.oxygen.run;

import java.io.IOException;

import com.jfixby.oxygen.AWSCredentials;
import com.jfixby.oxygen.keys.NiceHashAPIKey;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;
import com.jfixby.scarabei.aws.api.s3.S3;
import com.jfixby.scarabei.aws.desktop.s3.DesktopS3;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class WriteKeys {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		SystemSettings.setExecutionMode(ExecutionMode.DEMO);
		S3.installComponent(new DesktopS3());
// OxygenSetup.deploy(args);

		final NiceHashAPIKey key = new NiceHashAPIKey();
		key.key = "";
		key.id = "";

// ReadReys.writeKey(key, "nice-hash");

		final File awsCredentialsFile = LocalFileSystem.ApplicationHome().parent().child("credentials")
			.child("aws-credentials.json");

		final AWSCredentials aws = new AWSCredentials();
		aws.accessKeyID = "";
		aws.regionName = "";
		aws.secretKeyID = "";
		L.d("awsCredentialsFile", awsCredentialsFile);
		awsCredentialsFile.writeString(Json.serializeToString(aws).toString());

	}

}
