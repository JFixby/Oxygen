
package com.jfixby.oxygen.io;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;

public class ApplicationHome {

	static public File workingDir = null;

	public static File workingDir () {
		Debug.checkNull("ApplicationHome", workingDir);
		return workingDir;
	}

	public static File ApplicationLog (final String orderID) {

		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		final String logFileName = sdf.format(new Date()) + ".log";
		final File appHome = LocalFileSystem.ApplicationHome();
		return appHome.child("log").child(orderID).child(logFileName);
	}

}
