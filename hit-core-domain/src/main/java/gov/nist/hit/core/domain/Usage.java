package gov.nist.hit.core.domain;

public enum Usage {

	R, RE, O, C, X, B, W, CE;

	public String value() {
		return name();
	}

	public static Usage fromValue(String v) {
		try{
			return valueOf(v);
		}catch (IllegalArgumentException e){
			return valueOf("C"); // ????
		}
	}

}