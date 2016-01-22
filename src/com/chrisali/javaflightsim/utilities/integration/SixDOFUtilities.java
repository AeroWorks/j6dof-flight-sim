package com.chrisali.javaflightsim.utilities.integration;

import java.util.EnumMap;

import com.chrisali.javaflightsim.enviroment.EnvironmentParameters;

public class SixDOFUtilities {
	public static double[][] body2Ned(double[] eulerAngles) {
		double body2NedDCM[][] = new double[3][3]; //[row][column]
		
		body2NedDCM[0][0] =  Math.cos(eulerAngles[1])*Math.cos(eulerAngles[2]);
		body2NedDCM[1][0] =  Math.cos(eulerAngles[1])*Math.sin(eulerAngles[2]);
		body2NedDCM[2][0] = -Math.sin(eulerAngles[1]);
		
		body2NedDCM[0][1] =  Math.sin(eulerAngles[0])*Math.sin(eulerAngles[1])*Math.cos(eulerAngles[2]) - Math.cos(eulerAngles[0])*Math.sin(eulerAngles[2]);
		body2NedDCM[1][1] =  Math.sin(eulerAngles[0])*Math.sin(eulerAngles[1])*Math.sin(eulerAngles[2]) + Math.cos(eulerAngles[0])*Math.cos(eulerAngles[2]);
		body2NedDCM[2][1] =  Math.sin(eulerAngles[0])*Math.cos(eulerAngles[1]);
		
		body2NedDCM[0][2] =  Math.cos(eulerAngles[0])*Math.sin(eulerAngles[1])*Math.cos(eulerAngles[2]) + Math.sin(eulerAngles[0])*Math.sin(eulerAngles[2]);
		body2NedDCM[1][2] =  Math.cos(eulerAngles[0])*Math.sin(eulerAngles[1])*Math.sin(eulerAngles[2]) - Math.sin(eulerAngles[0])*Math.cos(eulerAngles[2]);
		body2NedDCM[2][2] =  Math.cos(eulerAngles[0])*Math.cos(eulerAngles[1]);
				
		return body2NedDCM;
	}
	
	public static double[] getInertiaCoeffs(double[] inertiaVals) { //inertiaVals[]{Ix,Iy,Iz,Ixz}
		double[] inertiaCoeffs = new double[9];
		
		double gamma = (inertiaVals[0]*inertiaVals[2])-(Math.pow(inertiaVals[3], 2));
		
		inertiaCoeffs[0] = (((inertiaVals[1]-inertiaVals[2])*inertiaVals[2])-(Math.pow(inertiaVals[3], 2)))/gamma;
		inertiaCoeffs[1] = (inertiaVals[0]-inertiaVals[1]+inertiaVals[2])*inertiaVals[3]/gamma;
		inertiaCoeffs[2] = inertiaVals[2]/gamma;
		inertiaCoeffs[3] = inertiaVals[3]/gamma;
		inertiaCoeffs[4] = (inertiaVals[2]-inertiaVals[0])/inertiaVals[1];
		inertiaCoeffs[5] = inertiaVals[3]/inertiaVals[1];
		inertiaCoeffs[6] = 1/inertiaVals[1];
		inertiaCoeffs[7] = (inertiaVals[0]*(inertiaVals[0]-inertiaVals[1])+(Math.pow(inertiaVals[3], 2)))/gamma;
		inertiaCoeffs[8] = inertiaVals[0]/gamma;
		
		return inertiaCoeffs;
	}
	
	public static double[][] wind2Body(double[] windParameters) {
		double wind2BodyDCM[][] = new double[3][3]; //[row][column]
		
		wind2BodyDCM[0][0] =  Math.cos(windParameters[1])*Math.cos(windParameters[2]);
		wind2BodyDCM[1][0] =  Math.sin(windParameters[1]);  
		wind2BodyDCM[2][0] =  Math.cos(windParameters[1])*Math.sin(windParameters[2]);
		
		wind2BodyDCM[0][1] = -Math.sin(windParameters[1])*Math.cos(windParameters[2]);										
		wind2BodyDCM[1][1] =  Math.cos(windParameters[1]);
		wind2BodyDCM[2][1] = -Math.sin(windParameters[1])*Math.sin(windParameters[2]);
		
		wind2BodyDCM[0][2] = -Math.sin(windParameters[2]);
		wind2BodyDCM[1][2] =  0; 
		wind2BodyDCM[2][2] =  Math.cos(windParameters[2]);
				
		return wind2BodyDCM;
	}
	
	public static double[] getWindParameters(double[] linearVelocities) {
		double vTrue = Math.sqrt(Math.pow(linearVelocities[0],2) + Math.pow(linearVelocities[1],2) + Math.pow(linearVelocities[2],2));
		double beta = Math.asin(linearVelocities[1]/vTrue);
		double alpha = Math.atan(linearVelocities[2]/linearVelocities[0]);
		
		return SaturationLimits.limitWindParameters(new double[] {vTrue,beta,alpha});
	}
	
	public static double getAlphaDot(double[] linearVelocities, double[] sixDOFDerivatives) {
		return ((linearVelocities[0]*sixDOFDerivatives[2])-(linearVelocities[2]*sixDOFDerivatives[0]))
				/((Math.pow(linearVelocities[0], 2)+(Math.pow(linearVelocities[2], 2))));// = u*w_dot-w*u_dot/(u^2+w^2)
	}
	
	public static double getMach(double[] windParameters, EnumMap<EnvironmentParameters, Double> environmentParameters) {
		return windParameters[0]/environmentParameters.get(EnvironmentParameters.A);
	}

}