
package com.jfixby.oxygen.call;

import java.io.IOException;

import com.jfixby.oxygen.call.pool.PoolPerformance;

public interface Pool {

	PoolPerformance read () throws IOException;

}
