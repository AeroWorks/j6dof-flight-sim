package com.chrisali.javaflightsim.setup;

public enum InitialConditions {
	INITU 	  ("initU"),
	INITV 	  ("initV"),
	INITW 	  ("initW"),
	INITN 	  ("initN"),
	INITE 	  ("initE"),
	INITD 	  ("initD"),
	INITPHI   ("initPhi"),
	INITTHETA ("initTheta"),
	INITPSI   ("initPsi"),
	INITP 	  ("initP"),
	INITQ 	  ("initQ"),
	INITR 	  ("initR");
	
	private final String initialCondition;
	
	InitialConditions(String initialCondition) {this.initialCondition = initialCondition;}
	
	public String toString() {return initialCondition;}
}