
package com.jfixby.oxygen;

import com.jfixby.scarabei.aws.api.AWSCredentialsProvider;

public class AWSCredentials implements AWSCredentialsProvider {

	public String accessKeyID;
	public String secretKeyID;
	public String regionName;

	@Override
	public String getAccessKeyID () {
		return this.accessKeyID;
	}

	@Override
	public String getSecretKeyID () {
		return this.secretKeyID;
	}

	@Override
	public String getRegionName () {
		return this.regionName;
	}

}
