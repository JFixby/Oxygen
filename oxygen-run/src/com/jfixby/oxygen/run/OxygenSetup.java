
package com.jfixby.oxygen.run;

import java.io.IOException;

import com.jfixby.oxygen.AWSCredentials;
import com.jfixby.oxygen.OrderManagerSpecs;
import com.jfixby.oxygen.io.ApplicationHome;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.json.JsonString;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.aws.api.AWSCredentialsProvider;
import com.jfixby.scarabei.aws.api.s3.S3;
import com.jfixby.scarabei.aws.api.s3.S3Component;
import com.jfixby.scarabei.aws.api.s3.S3FileSystem;
import com.jfixby.scarabei.aws.api.s3.S3FileSystemConfig;
import com.jfixby.scarabei.red.log.SimpleFileLog;
import com.jfixby.scarabei.red.log.SimpleLogger;

public class OxygenSetup {
	private static void deployS3Bucket (final String child) throws IOException {

		final S3Component s3 = S3.invoke();
		final S3FileSystemConfig specs = s3.newFileSystemConfig();
		specs.setBucketName("com.jfixby.oxygen");//

		final File awsCredentialsFile = LocalFileSystem.ApplicationHome().parent().child("credentials")
			.child("aws-credentials.json");
		final JsonString credentialsJson = Json.newJsonString(awsCredentialsFile.readToString());
		final AWSCredentialsProvider awsKeys = Json.deserializeFromString(AWSCredentials.class, credentialsJson);
		specs.setAccessKeyID(awsKeys.getAccessKeyID());
		specs.setRegionName(awsKeys.getRegionName());
		specs.setSecretKeyID(awsKeys.getSecretKeyID());

		final S3FileSystem fileSystem = s3.newFileSystem(specs);

		final File applicationRoot = fileSystem.ROOT().child(child);
		applicationRoot.makeFolder();
		ApplicationHome.workingDir = applicationRoot;

	}

	public static void deploy (final String orderID) throws IOException {

		Debug.checkNull("orderID", orderID);

		OxygenSetup.deployS3Bucket(orderID);

		final File logFile = ApplicationHome.ApplicationLog(orderID);
		logFile.parent().makeFolder();
		L.replaceComponent(new SimpleLogger(new SimpleFileLog(logFile)));
	}

	public static OrderManagerSpecs readOrderManagerSpecs (final String orderMangerSpecsFileName) throws IOException {
		final File strategyFile = LocalFileSystem.ApplicationHome().child(orderMangerSpecsFileName);
		final String string = strategyFile.readToString();
		final OrderManagerSpecs specs = Json.deserializeFromString(OrderManagerSpecs.class, string);
		return specs;
	}

	public static void writeSpecs (final OrderManagerSpecs specs, final String orderMangerSpecsFileName) throws IOException {
		final File strategyFile = LocalFileSystem.ApplicationHome().child(orderMangerSpecsFileName);
		final JsonString json = Json.serializeToString(specs);
		L.d("writing", strategyFile);
		strategyFile.writeString(json.toString());

	}

}
