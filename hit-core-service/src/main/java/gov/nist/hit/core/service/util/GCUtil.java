package gov.nist.hit.core.service.util;

public class GCUtil {

	public static void performGC() {
		System.gc();
		System.runFinalization();
	}

}
