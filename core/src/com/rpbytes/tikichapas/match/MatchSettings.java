package com.rpbytes.tikichapas.match;

import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.assets.Assets;
import com.rpbytes.tikichapas.items.BallItem;
import com.rpbytes.tikichapas.items.Formation;
import com.rpbytes.tikichapas.items.Item;
import com.rpbytes.tikichapas.items.Stadium;
import com.rpbytes.tikichapas.items.Team;

import org.json.JSONObject;


public class MatchSettings {
	
	protected int golesMax=3, turnosMax=10;
	protected Team teamLocal, teamVisitante;
	public enum tipoCamiseta {HOME, AWAY} 
	public enum tipoJugador { HUMANO, IA }
	private tipoJugador local, visitante;
	private tipoCamiseta equipacionLocal=tipoCamiseta.HOME, equipacionVisitante=tipoCamiseta.HOME;
	private boolean goalMatch=true;
	private Stadium stadium = new Stadium(-5, Assets.cadizTerreno, Assets.cadizTribuna, Assets.cadizPreferencia, Assets.cadizFondoNorte, Assets.cadizFondoSur, Assets.cadizEsq1, Assets.cadizEsq2, Assets.cadizEsq3, Assets.cadizEsq4);
	private BallItem ball;
	private Formation homeFormation, awayFormation;
	
	public MatchSettings(int golesMax, Team teamLocal, Team teamVisitante, tipoJugador local, tipoJugador visitante, BallItem ball){
		this.golesMax = golesMax;
		this.teamLocal = teamLocal;
		this.teamVisitante = teamVisitante;
		this.local= local;
		this.visitante = visitante;
		this.ball = ball;
	}

	public MatchSettings(Team teamLocal, Team teamVisitante, tipoJugador local, tipoJugador visitante, BallItem ball){
		this.teamLocal = teamLocal;
		this.teamVisitante = teamVisitante;
		this.local= local;
		this.visitante = visitante;
		this.ball = ball;
	}
	
	public MatchSettings(Team teamLocal, Team teamVisitante) {
		this.teamLocal = teamLocal;
		this.teamVisitante = teamVisitante;
		this.local = tipoJugador.HUMANO;
		this.visitante = tipoJugador.HUMANO;
		this.ball = null;
	}

	public MatchSettings(JSONObject match, TikiChapasGame game){
		for(Item item : game.getItems()){
			if(item instanceof Team){
				if(item.getId() == match.getInt("team_home_id")) {
					this.teamLocal = (Team) item;
					if(match.getInt("kit_home") == 1)
						equipacionLocal = tipoCamiseta.HOME;
					else
						equipacionLocal = tipoCamiseta.AWAY;
				}
				if(item.getId() == match.getInt("team_away_id")) {
					this.teamVisitante = (Team) item;
					if(match.getInt("kit_away") == 1)
						equipacionVisitante = tipoCamiseta.HOME;
					else
						equipacionVisitante = tipoCamiseta.AWAY;
				}
			}
			if(item instanceof BallItem){
				if(item.getId() == match.getInt("ball_id"))
					this.ball = (BallItem) item;
			}

			if(item instanceof Formation){
				if(item.getId() == match.getInt("formation_home_id")) {
					Formation home = new Formation(item.getId(), item.getName(), item.getPrice(), item.isLocked(), ((Formation) item).duplicateFormationData());
					setHomeFormation(home);
				}
				if(item.getId() == match.getInt("formation_away_id")) {
					Formation away = new Formation(item.getId(), item.getName(), item.getPrice(), item.isLocked(), ((Formation) item).duplicateFormationData());
					away.setFormationToAway();
					setAwayFormation(away);
				}
			}
		}

		setJugadorLocal(tipoJugador.HUMANO);
		setJugadorVisitante(tipoJugador.HUMANO);
	}

	public int getGolesMax() {
		return golesMax;
	}

	public void setGolesMax(int golesMax) {
		this.golesMax = golesMax;
	}

	public int getTurnosMax(){
		return turnosMax;
	}
	
	public void setTurnosMax(int turnosMax){
		this.turnosMax = turnosMax;
	}
	
	public Team getTeamLocal() {
		return teamLocal;
	}

	public void setTeamLocal(Team teamLocal) {
		this.teamLocal = teamLocal;
	}

	public Team getTeamVisitante() {
		return teamVisitante;
	}

	public void setTeamVisitante(Team teamVisitante) {
		this.teamVisitante = teamVisitante;
	}
	
	public void setJugadorLocal(tipoJugador t) {
		this.local= t;
	}
	
	public tipoJugador getJugadorLocal(){
		return this.local;
	}
	
	public void setJugadorVisitante(tipoJugador t) {
		this.visitante= t;
	}
	
	public tipoJugador getJugadorVisitante(){
		return this.visitante;
	}


	public Stadium getStadium() {
		return stadium;
	}

	public void setStadium(Stadium stadium){
		this.stadium = stadium;
	}

	public tipoCamiseta getEquipacionLocal() {
		return equipacionLocal;
	}

	public void setEquipacionLocal(tipoCamiseta equipacionLocal) {
		this.equipacionLocal = equipacionLocal;
		if(this.equipacionLocal == tipoCamiseta.HOME)
			teamLocal.setIsHome(true);
		else
			teamLocal.setIsHome(false);
	}

	public tipoCamiseta getEquipacionVisitante() {
		return equipacionVisitante;
	}

	public void setEquipacionVisitante(tipoCamiseta equipacionVisitante) {
		this.equipacionVisitante = equipacionVisitante;
		if(this.equipacionVisitante == tipoCamiseta.HOME)
			teamVisitante.setIsHome(true);
		else
			teamVisitante.setIsHome(false);
	}

	public boolean isGoalMatch() {
		return goalMatch;
	}

	public void setGoalMatch(boolean goalMatch) {
		this.goalMatch = goalMatch;
	}

	public BallItem getBall() {
		return ball;
	}

	public void setBall(BallItem ball) {
		this.ball = ball;
	}

	public Formation getHomeFormation() {
		return homeFormation;
	}

	public void setHomeFormation(Formation homeFormation) {
		this.homeFormation = homeFormation;
	}

	public Formation getAwayFormation() {
		return awayFormation;
	}

	public void setAwayFormation(Formation awayFormation) {
		this.awayFormation = awayFormation;
	}
	

	
	
}
