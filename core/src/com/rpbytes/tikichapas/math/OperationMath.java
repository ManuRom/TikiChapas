package com.rpbytes.tikichapas.math;

public class OperationMath {

	public static float distanciaEntreDosPuntos(float p1X, float p1Y, float p2X, float p2Y){
		return (float) Math.sqrt(Math.pow(p1X-p2X, 2) + Math.pow(p1Y-p2Y, 2));
	}
	
}
