
package com.jfixby.oxygen;

public interface DecisionMaker {

	long timeSinceLastSuccAction ();

	void tryToAjustStrategy (ManagableOrder order);

	boolean isTimeToExit ();

}
