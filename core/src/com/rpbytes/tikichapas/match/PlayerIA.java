package com.rpbytes.tikichapas.match;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.rpbytes.tikichapas.entities.Ball;
import com.rpbytes.tikichapas.entities.Cap;
import com.rpbytes.tikichapas.items.Team;

public class PlayerIA {

	public Team team;
	public Array<Cap> caps;

	public PlayerIA(Team team, Array<Cap> caps){
		this.team = team;
		this.caps = new Array<Cap>();
		for (Cap cap: caps) {
			if(cap.getEquipo().equals(team)){
				this.caps.add(cap);
			}
		}
	}
	
	public Cap IASelectTapon(Cap t1, Cap t2, Cap t3, Cap t4){
		float puntuacion;
		Cap salida = null;
		puntuacion = t1.getIAPuntuacion();
		if(t2.getIAPuntuacion() < puntuacion)
			puntuacion = t2.getIAPuntuacion();
		if(t3.getIAPuntuacion() < puntuacion)
			puntuacion = t3.getIAPuntuacion();
		if(t4.getIAPuntuacion() < puntuacion)
			puntuacion = t4.getIAPuntuacion();
		
		if(puntuacion == t1.getIAPuntuacion())
			salida = t1;
		if(puntuacion == t2.getIAPuntuacion())
			salida = t2;
		if(puntuacion == t3.getIAPuntuacion())
			salida = t3;
		if(puntuacion == t4.getIAPuntuacion())
			salida = t4;
		
		return salida;
	}

	public void IAMueveTapon(Cap t, float touchX, float touchY){
		t.moverTapon(touchX, touchY);
	}

	public void IAMueveTapon(Ball ball, World world, Goal goal){
		for (Cap cap: caps) {
			cap.calculaPuntuacion(world, ball, goal);
		}
		float puntuacion = caps.first().getIAPuntuacion();
		Cap capMove = caps.first();
		for (Cap cap: caps) {
			if(cap.getIAPuntuacion()>=puntuacion) {
				puntuacion = cap.getIAPuntuacion();
				capMove = cap;
			}
		}
		capMove.moverTapon(capMove.calculaIAFuerzaX(world, ball,goal), capMove.calculaIAFuerzaY(world, ball,goal));
	}
	
}
