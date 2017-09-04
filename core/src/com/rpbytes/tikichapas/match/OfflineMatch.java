package com.rpbytes.tikichapas.match;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.dialogs.EndMatchDialog;
import com.rpbytes.tikichapas.entities.Cap;
import com.rpbytes.tikichapas.screens.MainMenuScreen;

/**
 * Created by manoleichon on 27/12/16.
 */
public class OfflineMatch extends Match {

    com.rpbytes.tikichapas.match.PlayerIA playerIAHome, playerIAAway;

    public OfflineMatch(TikiChapasGame game, com.rpbytes.tikichapas.match.MatchSettings matchSettings){
        super(game,matchSettings);
    }

    @Override
    public void show() {
        super.show();
        setOnline(false);
        if(matchSetting.getJugadorLocal().equals(com.rpbytes.tikichapas.match.MatchSettings.tipoJugador.IA)){
            playerIAHome = new com.rpbytes.tikichapas.match.PlayerIA(matchSetting.getTeamLocal(), caps);
        }
        if(matchSetting.getJugadorVisitante().equals(com.rpbytes.tikichapas.match.MatchSettings.tipoJugador.IA)){
            playerIAAway = new com.rpbytes.tikichapas.match.PlayerIA(matchSetting.getTeamVisitante(), caps);
        }
    }

    @Override
    public void listenChangeTurn() {
        if(turnCount >1 && homeTurn && canMove){
            homeTurn =false;
            awayTurn =true;
            turnCount =0;
            turnsCount +=1;
        }
        if(turnCount >1 && awayTurn && canMove){
            awayTurn = false;
            homeTurn = true;
            turnCount =0;
            turnsCount +=1;
        }
    }

    @Override
    public void listenCanMove() {
        if(matchSetting.getJugadorLocal().equals(com.rpbytes.tikichapas.match.MatchSettings.tipoJugador.HUMANO) && matchSetting.getJugadorVisitante().equals(com.rpbytes.tikichapas.match.MatchSettings.tipoJugador.HUMANO))
            canMove = areStoppedCapsAndBall();
        else{
            if(matchSetting.getJugadorLocal().equals(com.rpbytes.tikichapas.match.MatchSettings.tipoJugador.IA) && homeTurn){
                canMove = false;
                if(areStoppedCapsAndBall()){
                    if(turnCount<2) {
                        playerIAHome.IAMueveTapon(ball, world, goalRight);
                        turnCount++;
                    }else{
                        changeTurn(true);
                    }
                }
            }else {
                if (matchSetting.getJugadorVisitante().equals(com.rpbytes.tikichapas.match.MatchSettings.tipoJugador.IA) && awayTurn) {
                    canMove = false;
                    if (areStoppedCapsAndBall()) {
                        if(turnCount<2){
                            playerIAAway.IAMueveTapon(ball, world, goalLeft);
                            turnCount++;
                        }else{
                            changeTurn(false);
                        }
                    }
                }else{
                    canMove = areStoppedCapsAndBall();
                }
            }
        }

    }


    public void listenGoal(){
        if(isHomeGoal()){
            stateMatch = state.noActive;
            homeGoals++;
            //world.clearForces();
            scoreboard.setHomeGoals(homeGoals);
            ball.stopEntity();
            ball.moveScene2D=true;
            ball.moveToInitPosition();
            ball.moveScene2D=false;
            for(Cap cap : caps) {
                cap.setBodyToFormation();
            }

            if (homeTurn){
                changeTurn(true);
            }else {
                turnCount=0;
                turnsCount+=1;
            }
            this.animateGoalScore(true);
        }
        if(isAwayGoal()){
            stateMatch = state.noActive;
            awayGoals++;
            //world.clearForces();

            scoreboard.setAwayGoals(awayGoals);
            ball.stopEntity();
            ball.moveScene2D = true;
            ball.moveToInitPosition();
            ball.moveScene2D = false;
            for(Cap cap : caps){
                cap.setBodyToFormation();
            }

            if (awayTurn){
                changeTurn(false);
            }else {
                turnCount=0;
                turnsCount+=1;
            }
            this.animateGoalScore(false);
        }
    }

    @Override
    public void createEndMatchDialog() {
        Gdx.app.log("End Match","Create end match dialog");
        endMatchDialog = new EndMatchDialog(skin,scoreboard.getCenterScoreboard(),scoreboard.getHomeGoals(),true,false);
        endMatchDialog.setPosition(viewportWidth/2,viewportHeight/2, Align.center);
        stageHud.addActor(endMatchDialog);
        endMatchDialog.mainMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        endMatchDialog.setVisible(true);
    }

    @Override
    public void sendMovementRequest(float forceX, float forceY, int capIndex, boolean endMovement) {}
}
