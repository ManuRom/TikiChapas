package com.rpbytes.tikichapas.match;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.Action;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.User;
import com.rpbytes.tikichapas.dialogs.EndMatchDialog;
import com.rpbytes.tikichapas.entities.Cap;
import com.rpbytes.tikichapas.net.CapPosition;
import com.rpbytes.tikichapas.net.OnlineMatchNotification;
import com.rpbytes.tikichapas.net.Url;
import com.rpbytes.tikichapas.screens.MainMenuScreen;
import com.rpbytes.tikichapas.utils.Constants;
import com.rpbytes.tikichapas.net.Movement;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by manoleichon on 19/12/16.
 */
public class OnlineMatch extends Match {

    public int stateTurn, matchId;
    public User homeUser, awayUser;
    public Array<Movement> movements;
    public int userMovementId;
    public boolean simulating, waitRival, isGoal, isMovement;
    public Dialog movementReview, simulatingMovement, endTurn;
    //public Skin skin;
    public Label yourTurnLabel, yourMovementLabel, waitRivalLabel, firstMovementLabel, secondMovementLabel;
    public Array<String> movementReviews;

    public OnlineMatch(TikiChapasGame game, MatchSettings matchSettings, int homeGoals, int awayGoals, Array<Movement> movements, int matchId, int stateTurn, int turnHome, User homeUser, User awayUser) {
        super(game, matchSettings);
        this.movements = movements;
        this.stateTurn = stateTurn;
        this.matchId = matchId;
        this.homeTurn = turnHome==1?true:false;
        this.awayTurn = turnHome==0?true:false;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.homeUser = homeUser;
        this.awayUser = awayUser;
        waitRival = false;
        isGoal = false;
        isMovement = false;
    }

    @Override
    public void show() {
        super.show();
        setOnline(true);
        scoreboard.getHudScoreboard().setHomeName(homeUser.getUsername());
        scoreboard.getHudScoreboard().setAwayName(awayUser.getUsername());

        movementReview = new Dialog("Jugada del Oponente",skin);
        simulatingMovement = new Dialog("Simulando",skin);
        endTurn = new Dialog("Esperando jugada rival",skin);

        yourTurnLabel = new Label("Tu turno",skin,"animation_goal");

        yourTurnLabel.setPosition(viewportWidth/2,viewportHeight*2, Align.center);
        stageHud.addActor(yourTurnLabel);

        yourMovementLabel = new Label("Primer movimiento",skin,"animation_goal");

        yourMovementLabel.setPosition(Constants.RIVAL_MOVEMENT_LABEL_X, Constants.RIVAL_MOVEMENT_LABEL_Y);
        stageMatch.addActor(yourMovementLabel);

        waitRivalLabel = new Label("Esperando jugada rival",skin, "label");

        waitRivalLabel.setPosition(Constants.RIVAL_MOVEMENT_LABEL_X, Constants.RIVAL_MOVEMENT_LABEL_Y);
        stageMatch.addActor(waitRivalLabel);

        canMove = false;

        stateMatch = state.noActive;

        if(stateTurn == 1){
            yourTurnLabel.addAction(Actions.sequence(moveToAligned(viewportWidth / 2, viewportHeight / 2, Align.center, 0.5f), delay(2f), moveToAligned(viewportWidth / 2, viewportHeight * 2, Align.center, 0.5f), new Action() {
                @Override
                public boolean act(float delta) {

                    stateMatch = state.active;
                    return true;
                }
            }));
        }

        movementReviews = new Array<String>();

        if(stateTurn == 2){
            Gdx.app.log("Movements", Integer.toString(movements.size));
            simulating = true;
            for(Movement movement: movements){
                switch (movement.state){
                    case 1:
                        movementReviews.add("Movimiento incompleto");
                        break;
                    case 2:
                        movementReviews.add("Esta vez te has librado. El rival no ha marcado.");
                        break;
                    case 3:
                        movementReviews.add("Te han Marcado! Vamos no puedes perder este partido!");
                        break;
                    case 4:
                        movementReviews.add("Se ha marcado en propia puerta! Aprovecha su bajon!");
                        break;
                }
            }
            movementReview.getBackground().setMinWidth(900);
            movementReview.getBackground().setMinHeight(550);
            TextButton omit = new TextButton("Omitir",skin);
            movementReview.button(omit);
            omit.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(stateMatch == state.end){
                        if(game.getUser().getId() == homeUser.getId()) {
                            if (scoreboard.getHomeGoals() == matchSetting.getGolesMax())
                                createEndMatchDialog(true);
                            else
                                createEndMatchDialog(false);
                        }

                        if(game.getUser().getId() == awayUser.getId()) {
                            if (awayGoals ==  matchSetting.getGolesMax())
                                createEndMatchDialog(true);
                            else
                                createEndMatchDialog(false);
                        }

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
                    if(movements.first().bodiesFinalsPositions.size==0)
                        setEntitiesPositionsFromMovement(movements.first().bodiesInitialsPositions);
                    else
                        setEntitiesPositionsFromMovement(movements.first().bodiesFinalsPositions);
                    yourTurnLabel.addAction(moveTo(30f, Constants.RIVAL_MOVEMENT_LABEL_Y));
                    simulating = false;
                    stateMatch = state.active;
                }
            });

            movementReview.show(stageHud);
            showMovementReview();
        }

        if(stateTurn == 3){
            listenEnd();
            if(stateMatch == state.end){
                if(game.getUser().getId() == homeUser.getId()) {
                    if (scoreboard.getHomeGoals() == matchSetting.getGolesMax())
                        createEndMatchDialog(true);
                    else
                        createEndMatchDialog(false);
                }

                if(game.getUser().getId() == awayUser.getId()) {
                    if (awayGoals ==  matchSetting.getGolesMax())
                        createEndMatchDialog(true);
                    else
                        createEndMatchDialog(false);
                }

                endMatchDialog.setPosition(viewportWidth/2,viewportHeight/2, Align.center);
                stageHud.addActor(endMatchDialog);
                endMatchDialog.mainMenu.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        game.setScreen(new MainMenuScreen(game));
                    }
                });
            }else {
                simulating = true;
                if (movements.first().state == 4) {
                    turnCount = 0;
                } else {
                    turnCount = 1;
                }
                /*secondMovementLabel = new Label("Continuar Jugada", skin, "animation_goal");
                secondMovementLabel.setPosition(viewportWidth / 2, viewportHeight * 2, Align.center);
                stageHud.addActor(secondMovementLabel);*/
                startMatch.setText("Continuar Jugada");

                if (movements.first().bodiesFinalsPositions.size == 0)
                    setEntitiesPositionsFromMovement(movements.first().bodiesInitialsPositions);
                else
                    setEntitiesPositionsFromMovement(movements.first().bodiesFinalsPositions);

                startMatch.addAction(Actions.sequence(Actions.delay(2f), new Action() {
                    @Override
                    public boolean act(float delta) {
                        simulating = false;
                        stateMatch = state.active;
                        return true;
                    }
                }));
                /*secondMovementLabel.addAction(Actions.sequence(Actions.moveToAligned(viewportWidth / 2, viewportHeight / 2, Align.center, 0.5f), Actions.delay(2f), Actions.moveToAligned(viewportWidth / 2, viewportHeight * 2, Align.center, 0.5f), new Action() {
                    @Override
                    public boolean act(float delta) {
                        simulating = false;
                        stateMatch = state.active;
                        return true;
                    }
                }));*/
            }

        }

    }


    @Override
    public void listenChangeTurn() {
        if(turnCount >1 && homeTurn && canMove){
            Gdx.app.log("ChangeTurn","home");
            changeTurn(true);
            waitRival = true;
            waitRivalLabel.addAction(moveTo(10f, yourTurnLabel.getY(), 0.3f));
            sendPutMatchRequest();
            //TODO - Mostrar esperando jugada rival
        }
        if(turnCount >1 && awayTurn && canMove){
            Gdx.app.log("ChangeTurn","away");
            changeTurn(false);
            waitRival = true;
            waitRivalLabel.addAction(moveTo(10f, yourTurnLabel.getY(), 0.3f));
            sendPutMatchRequest();
            //TODO - Mostrar esperando jugada rival

        }
    }


    public void listenGoal(){
        if(isHomeGoal()) {
            isGoal = true;
            stateMatch = state.noActive;
            homeGoals++;
            scoreboard.setHomeGoals(homeGoals);
            ball.stopEntity();
            ball.moveScene2D = true;
            ball.moveToInitPosition();
            ball.moveScene2D = false;
            for (Cap cap : caps) {
                cap.setBodyToFormation();
            }
            if (homeTurn){
                Gdx.app.log("Goal","normal");
                sendPutMovement(userMovementId, 3);
                changeTurn(true);
                waitRival = true;
            }else {
                Gdx.app.log("Goal","propia");
                sendPutMovement(userMovementId, 4);
                turnCount=0;
                turnsCount+=1;
            }
            sendPutMatchRequest();
            animateGoalScore(true);

        }
        if(isAwayGoal()) {
            isGoal = true;
            stateMatch = state.noActive;
            awayGoals++;
            //world.clearForces();

            scoreboard.setAwayGoals(awayGoals);
            ball.stopEntity();
            ball.moveScene2D = true;
            ball.moveToInitPosition();
            ball.moveScene2D = false;

            for (Cap cap : caps) {
                cap.setBodyToFormation();
            }
            if (awayTurn){
                Gdx.app.log("Goal","normal");
                sendPutMovement(userMovementId, 3);
                changeTurn(false);
                waitRival = true;
            }else {
                Gdx.app.log("Goal","propia");
                sendPutMovement(userMovementId, 4);
                turnCount=0;
                turnsCount+=1;
            }
            sendPutMatchRequest();
            animateGoalScore(false);
        }
    }

    public void listenCanMove() {

        if(!isGoal && isMovement && areStoppedCapsAndBall()) {
            Gdx.app.log("Send Put Movement", "Entra");
            isMovement = false;
            sendPutMovement(userMovementId, 2);
        }

        if(isGoal && !isMovement){
            isGoal = false;
        }

        if(!areStoppedCapsAndBall() && touchUpMovement)
            touchUpMovement=false;

        if(touchUpMovement)
            canMove=false;
        else
            canMove=areStoppedCapsAndBall();

        if(waitRival)
            canMove=false;
    }

    @Override
    public void createEndMatchDialog() {
        Gdx.app.log("End Match","Create end match dialog");
        endMatchDialog = new EndMatchDialog(skin,scoreboard.getCenterScoreboard(),scoreboard.getHomeGoals(),true, true);
        endMatchDialog.setPosition(viewportWidth/2,viewportHeight/2, Align.center);
        stageHud.addActor(endMatchDialog);
        endMatchDialog.mainMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Button) event.getListenerActor()).setDisabled(true);
                boolean thereIsNotification=false;
                for (OnlineMatchNotification notification:game.getNotifications()) {
                    if(notification.matchId == matchId && notification.typeId == 2) {
                        notification.sendUpdateNotificationRequest();
                        thereIsNotification=true;
                        break;
                    }
                }
                if(!thereIsNotification)
                    game.setScreen(new MainMenuScreen(game));
            }
        });
        endMatchDialog.setVisible(true);
    }

    public void createEndMatchDialog(boolean win){
        Gdx.app.log("End Match","Create end match dialog");
        int goals = 0;
        if(scoreboard.getHomeGoals() == 3) {
            goals = win ? scoreboard.getHomeGoals() : scoreboard.getAwayGoals();
        }
        if(scoreboard.getAwayGoals() == 3) {
            goals = win ? scoreboard.getAwayGoals() : scoreboard.getHomeGoals();
        }

        endMatchDialog = new EndMatchDialog(skin, scoreboard.getCenterScoreboard(),goals ,win , true);
        endMatchDialog.setPosition(viewportWidth/2,viewportHeight/2, Align.center);
        stageHud.addActor(endMatchDialog);
        endMatchDialog.mainMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Button) event.getListenerActor()).setDisabled(true);
                boolean thereIsNotification=false;
                for (OnlineMatchNotification notification:game.getNotifications()) {
                    if(notification.matchId == matchId && notification.typeId == 2) {
                        notification.sendUpdateNotificationRequest();
                        thereIsNotification=true;
                        break;
                    }
                }
                if(!thereIsNotification)
                    game.setScreen(new MainMenuScreen(game));
            }
        });
        endMatchDialog.setVisible(true);
    }

    public void sendPutMatchRequest(){
        String url = Url.BASE_URL + Url.MATCH_PATH + matchId;
        JSONObject requestContent = new JSONObject();
        requestContent.put("goals_home", homeGoals);
        requestContent.put("goals_away", awayGoals);
        requestContent.put("turn_home", homeTurn?1:0);

        Net.HttpRequest request = new Net.HttpRequest();

        request.setContent(requestContent.toString());
        Gdx.app.log("Put Match url",url);
        Gdx.app.log("Put Match Content",requestContent.toString());
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");
        request.setMethod(Net.HttpMethods.PUT);
        request.setUrl(url);
        request.setHeader("Authorization","Bearer "+ Gdx.app.getPreferences("data").getString("token"));
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Gdx.app.log("Put Match Status Code", Integer.toString(httpResponse.getStatus().getStatusCode()));
                if(httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK){
                    Gdx.app.log("Put Match Response",httpResponse.getResultAsString());
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("Put Match", t.getLocalizedMessage());
            }

            @Override
            public void cancelled() {

            }
        });


    }

    public void sendPutMovement(int movementId, int state){
        String url = Url.BASE_URL + Url.MOVEMENT_PATH + "/" + movementId;
        JSONArray bodiesPositions = new JSONArray();
        for(int i=0; i< caps.size;i++){

            JSONObject bodyPosition = new JSONObject();
            bodyPosition.put("x",caps.get(i).getBody().getPosition().x);
            bodyPosition.put("y",caps.get(i).getBody().getPosition().y);
            bodyPosition.put("rotation",caps.get(i).getBody().getAngle());
            bodyPosition.put("index",i);
            bodyPosition.put("angular_velocity",caps.get(i).getBody().getAngularVelocity());
            bodyPosition.put("end_movement",true);
            bodiesPositions.put(bodyPosition);

            if(i == caps.size-1){
                JSONObject ballPosition = new JSONObject();
                ballPosition.put("x", ball.getBody().getPosition().x);
                ballPosition.put("y", ball.getBody().getPosition().y);
                ballPosition.put("rotation", ball.getBody().getAngle());
                ballPosition.put("index",i+1);
                ballPosition.put("angular_velocity", ball.getBody().getAngularVelocity());
                ballPosition.put("end_movement",true);
                bodiesPositions.put(ballPosition);
            }
        }

        JSONObject requestContent = new JSONObject();
        requestContent.put("state",state);
        requestContent.put("bodies_positions", bodiesPositions);

        Net.HttpRequest request = new Net.HttpRequest();

        request.setContent(requestContent.toString());
        Gdx.app.log("Put Movement url",url);
        Gdx.app.log("Put Movement Content",requestContent.toString());
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");
        request.setMethod(Net.HttpMethods.PUT);
        request.setUrl(url);
        request.setHeader("Authorization","Bearer "+ Gdx.app.getPreferences("data").getString("token"));
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Gdx.app.log("Put Movement Status Code", Integer.toString(httpResponse.getStatus().getStatusCode()));
                if(httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK){
                    Gdx.app.log("Put Movement","ok: movement="+userMovementId);
                }else{
                    Gdx.app.log("Put Movement Status Code",String.valueOf(httpResponse.getStatus().getStatusCode()));
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("Put Movement", t.getLocalizedMessage());
            }

            @Override
            public void cancelled() {

            }
        });

    }

    public void sendMovementRequest(float forceX, float forceY, int capIndex, boolean endMovement) {

        //MovementJson movement = new com.rpbytes.tikichapas.net.MovementJson(caps, ball, forceX, forceY,capIndex,matchId,game.getUser().getId(), endMovement);
        JSONObject movement = new JSONObject();
        movement.put("user_id", game.getUser().getId());
        movement.put("force_x",forceX);
        movement.put("force_y",forceY);
        movement.put("cap_touched", capIndex);
        movement.put("match_id", matchId);

        JSONArray bodiesPositions = new JSONArray();
        for(int i=0; i< caps.size;i++){

            JSONObject bodyPosition = new JSONObject();
            bodyPosition.put("x",caps.get(i).getBody().getPosition().x);
            bodyPosition.put("y",caps.get(i).getBody().getPosition().y);
            bodyPosition.put("rotation",caps.get(i).getBody().getAngle());
            bodyPosition.put("index",i);
            bodyPosition.put("angular_velocity",caps.get(i).getBody().getAngularVelocity());
            bodyPosition.put("end_movement",endMovement);
            bodiesPositions.put(bodyPosition);

            if(i == caps.size-1){
                JSONObject ballPosition = new JSONObject();
                ballPosition.put("x", ball.getBody().getPosition().x);
                ballPosition.put("y", ball.getBody().getPosition().y);
                ballPosition.put("rotation", ball.getBody().getAngle());
                ballPosition.put("index",i+1);
                ballPosition.put("angular_velocity", ball.getBody().getAngularVelocity());
                ballPosition.put("end_movement",endMovement);
                bodiesPositions.put(ballPosition);
            }
        }
        movement.put("bodies_positions",bodiesPositions);


        String url = Url.BASE_URL + Url.MOVEMENT_PATH;
        Net.HttpRequest request = new Net.HttpRequest();
        Gdx.app.log("movement",movement.toString());
        request.setContent(movement.toString());
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");
        request.setMethod(Net.HttpMethods.POST);
        request.setUrl(url);
        request.setHeader("Authorization","Bearer "+ Gdx.app.getPreferences("data").getString("token"));
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                if(httpResponse.getStatus().getStatusCode() == HttpStatus.SC_CREATED){
                    String response = httpResponse.getResultAsString();
                    Json jsonResponse = new Json();
                    userMovementId = jsonResponse.readValue("id",Integer.class,new JsonReader().parse(response));
                    Gdx.app.log("Post Movement", "ok");
                    isMovement = true;
                }else{
                    Gdx.app.log("Post Movement Status Code",String.valueOf(httpResponse.getStatus().getStatusCode()));
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("Post Movement",t.getLocalizedMessage());
            }

            @Override
            public void cancelled() {

            }
        });
    }



    private void setEntitiesPositionsFromMovement(Array<CapPosition> bodiesPositions){
        for(int i=0;i<caps.size;i++){
            caps.get(i).getBody().setTransform(bodiesPositions.get(i).getX(),
                    bodiesPositions.get(i).getY(),
                    bodiesPositions.get(i).getRotation());
            caps.get(i).getBody().setAngularVelocity(bodiesPositions.get(i).getAngularVelocity());
            if(i == caps.size-1){
                ball.getBody().setTransform(bodiesPositions.get(i+1).getX(),
                       bodiesPositions.get(i+1).getY(),
                       bodiesPositions.get(i+1).getRotation());
                ball.getBody().setAngularVelocity(bodiesPositions.get(i+1).getAngularVelocity());
            }
        }
    }


    private void showMovementReview(){
        Image loading = new Image(new Texture(Gdx.files.internal("pack/loading.png")));
        loading.setOrigin(loading.getWidth()/2,loading.getHeight()/2);
        loading.addAction(Actions.forever(Actions.rotateTo(360f,1f)));
        switch (movements.size){
            case 1:
                movementReview.addAction(new SequenceAction(new Action() {
                    @Override
                    public boolean act(float delta) {
                        Image loading = new Image(new Texture(Gdx.files.internal("pack/loading.png")));
                        loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);
                        loading.addAction(Actions.forever(Actions.rotateTo(360f, 1f)));

                        Table loadingTable = new Table();
                        Label label = new Label("Simulando movimiento", skin);
                        loadingTable.add(loading).size(64f).padRight(10f);
                        loadingTable.add(label);
                        movementReview.getContentTable().add(loadingTable);
                        return true;
                    }
                }, Actions.delay(2f), new Action() {
                    @Override
                    public boolean act(float delta) {
                        Label label = new Label(movementReviews.peek(), skin);
                        label.setVisible(true);
                        Gdx.app.log("Label 1", movementReviews.peek());
                        movementReview.getContentTable().removeActor(movementReview.getContentTable().getCells().peek().getActor());
                        movementReview.getContentTable().add(label).height(64f);
                        //movementReview.getContentTable().getCell(label).getActor().addAction(new SequenceAction(Actions.alpha(0), Actions.delay(2f), Actions.fadeIn(1f)));
                        return true;
                    }
                },new Action() {
                    @Override
                    public boolean act(float delta) {
                        TextButton button =(TextButton) movementReview.getButtonTable().getCells().first().getActor();
                        button.setText("Jugar");
                        return true;
                    }
                }));
                break;
            case 2:
                movementReview.addAction(new SequenceAction(
                        new SequenceAction(new Action() {
                            @Override
                            public boolean act(float delta) {
                                Image loading = new Image(new Texture(Gdx.files.internal("pack/loading.png")));
                                loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);
                                loading.addAction(Actions.forever(Actions.rotateTo(360f, 1f)));

                                Table loadingTable = new Table();
                                Label label = new Label("Simulando movimiento", skin);
                                loadingTable.add(loading).size(64f).padRight(10f);
                                loadingTable.add(label);
                                movementReview.getContentTable().add(loadingTable);
                                return true;
                            }
                        }, Actions.delay(2f), new Action() {
                            @Override
                            public boolean act(float delta) {
                                Label label = new Label(movementReviews.peek(), skin);
                                label.setVisible(true);
                                Gdx.app.log("Label 1", movementReviews.peek());
                                movementReview.getContentTable().removeActor(movementReview.getContentTable().getCells().peek().getActor());
                                movementReview.getContentTable().add(label).height(64f);
                                //movementReview.getContentTable().getCell(label).getActor().addAction(new SequenceAction(Actions.alpha(0), Actions.delay(2f), Actions.fadeIn(1f)));
                                return true;
                            }
                        }), new SequenceAction(new Action() {
                    @Override
                    public boolean act(float delta) {
                        Image loading = new Image(new Texture(Gdx.files.internal("pack/loading.png")));
                        loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);
                        loading.addAction(Actions.forever(Actions.rotateTo(360f, 1f)));

                        Table loadingTable = new Table();
                        Label label = new Label("Simulando movimiento", skin);
                        loadingTable.add(loading).size(64f).padRight(10f);
                        loadingTable.add(label);
                        movementReview.getContentTable().add(loadingTable);
                        return true;
                    }
                }, Actions.delay(2f), new Action() {
                    @Override
                    public boolean act(float delta) {
                        Label label = new Label(movementReviews.first(), skin);
                        label.setVisible(true);
                        Gdx.app.log("Label 2", movementReviews.first());
                        movementReview.getContentTable().removeActor(movementReview.getContentTable().getCells().peek().getActor());
                        movementReview.getContentTable().add(label).height(64f);
                        //movementReview.getContentTable().getCell(label).getActor().addAction(new SequenceAction(Actions.alpha(0), Actions.delay(4f), Actions.fadeIn(1f)));
                        return true;
                    }
                }), new Action() {
                    @Override
                    public boolean act(float delta) {
                        TextButton button =(TextButton) movementReview.getButtonTable().getCells().first().getActor();
                        button.setText("Jugar");
                        return true;
                    }
                }));
                break;
            case 3:
                movementReview.addAction(new SequenceAction(
                        new SequenceAction(new Action() {
                            @Override
                            public boolean act(float delta) {
                                Image loading = new Image(new Texture(Gdx.files.internal("pack/loading.png")));
                                loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);
                                loading.addAction(Actions.forever(Actions.rotateTo(360f, 1f)));

                                Table loadingTable = new Table();
                                Label label = new Label("Simulando movimiento", skin);
                                loadingTable.add(loading).size(64f).padRight(10f);
                                loadingTable.add(label);
                                movementReview.getContentTable().add(loadingTable);
                                return true;
                            }
                        }, Actions.delay(2f), new Action() {
                            @Override
                            public boolean act(float delta) {
                                Label label = new Label(movementReviews.peek(), skin);
                                label.setVisible(true);
                                Gdx.app.log("Label 1", movementReviews.peek());
                                movementReview.getContentTable().removeActor(movementReview.getContentTable().getCells().peek().getActor());
                                movementReview.getContentTable().add(label).height(64f);
                                //movementReview.getContentTable().getCell(label).getActor().addAction(new SequenceAction(Actions.alpha(0), Actions.delay(2f), Actions.fadeIn(1f)));
                                return true;
                            }
                        }), new SequenceAction(new Action() {
                    @Override
                    public boolean act(float delta) {
                        Image loading = new Image(new Texture(Gdx.files.internal("pack/loading.png")));
                        loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);
                        loading.addAction(Actions.forever(Actions.rotateTo(360f, 1f)));

                        Table loadingTable = new Table();
                        Label label = new Label("Simulando movimiento", skin);
                        loadingTable.add(loading).size(64f).padRight(10f);
                        loadingTable.add(label);
                        movementReview.getContentTable().add(loadingTable);
                        return true;
                    }
                }, Actions.delay(2f), new Action() {
                    @Override
                    public boolean act(float delta) {
                        Label label = new Label(movementReviews.get(1), skin);
                        label.setVisible(true);
                        Gdx.app.log("Label 2", movementReviews.get(1));
                        movementReview.getContentTable().removeActor(movementReview.getContentTable().getCells().peek().getActor());
                        movementReview.getContentTable().add(label).height(64f);
                        //movementReview.getContentTable().getCell(label).getActor().addAction(new SequenceAction(Actions.alpha(0), Actions.delay(4f), Actions.fadeIn(1f)));
                        return true;
                    }
                }), new SequenceAction(new Action() {
                    @Override
                    public boolean act(float delta) {
                        Image loading = new Image(new Texture(Gdx.files.internal("pack/loading.png")));
                        loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);
                        loading.addAction(Actions.forever(Actions.rotateTo(360f, 1f)));

                        Table loadingTable = new Table();
                        Label label = new Label("Simulando movimiento", skin);
                        loadingTable.add(loading).size(64f).padRight(10f);
                        loadingTable.add(label);
                        movementReview.getContentTable().add(loadingTable);
                        return true;
                    }
                }, Actions.delay(2f), new Action() {
                    @Override
                    public boolean act(float delta) {
                        Label label = new Label(movementReviews.first(), skin);
                        label.setVisible(true);
                        Gdx.app.log("Label 1", movementReviews.first());
                        movementReview.getContentTable().removeActor(movementReview.getContentTable().getCells().peek().getActor());
                        movementReview.getContentTable().add(label).height(64f);
                        //movementReview.getContentTable().getCell(label).getActor().addAction(new SequenceAction(Actions.alpha(0), Actions.delay(2f), Actions.fadeIn(1f)));
                        return true;
                    }
                }),new Action() {
                    @Override
                    public boolean act(float delta) {
                        TextButton button =(TextButton) movementReview.getButtonTable().getCells().first().getActor();
                        button.setText("Jugar");
                        return true;
                    }
                }));
                break;
            case 4:
                movementReview.addAction(new SequenceAction(
                        new SequenceAction(new Action() {
                            @Override
                            public boolean act(float delta) {
                                Image loading = new Image(new Texture(Gdx.files.internal("pack/loading.png")));
                                loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);
                                loading.addAction(Actions.forever(Actions.rotateTo(360f, 1f)));

                                Table loadingTable = new Table();
                                Label label = new Label("Simulando movimiento", skin);
                                loadingTable.add(loading).size(64f).padRight(10f);
                                loadingTable.add(label);
                                movementReview.getContentTable().add(loadingTable);
                                return true;
                            }
                        }, Actions.delay(2f), new Action() {
                            @Override
                            public boolean act(float delta) {
                                Label label = new Label(movementReviews.peek(), skin);
                                label.setVisible(true);
                                Gdx.app.log("Label 1", movementReviews.peek());
                                movementReview.getContentTable().removeActor(movementReview.getContentTable().getCells().peek().getActor());
                                movementReview.getContentTable().add(label).height(64f);
                                //movementReview.getContentTable().getCell(label).getActor().addAction(new SequenceAction(Actions.alpha(0), Actions.delay(2f), Actions.fadeIn(1f)));
                                return true;
                            }
                        }), new SequenceAction(new Action() {
                    @Override
                    public boolean act(float delta) {
                        Image loading = new Image(new Texture(Gdx.files.internal("pack/loading.png")));
                        loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);
                        loading.addAction(Actions.forever(Actions.rotateTo(360f, 1f)));

                        Table loadingTable = new Table();
                        Label label = new Label("Simulando movimiento", skin);
                        loadingTable.add(loading).size(64f).padRight(10f);
                        loadingTable.add(label);
                        movementReview.getContentTable().add(loadingTable);
                        return true;
                    }
                }, Actions.delay(2f), new Action() {
                    @Override
                    public boolean act(float delta) {
                        Label label = new Label(movementReviews.get(2), skin);
                        label.setVisible(true);
                        Gdx.app.log("Label 2", movementReviews.get(2));
                        movementReview.getContentTable().removeActor(movementReview.getContentTable().getCells().peek().getActor());
                        movementReview.getContentTable().add(label).height(64f);
                        //movementReview.getContentTable().getCell(label).getActor().addAction(new SequenceAction(Actions.alpha(0), Actions.delay(4f), Actions.fadeIn(1f)));
                        return true;
                    }
                }), new SequenceAction(new Action() {
                    @Override
                    public boolean act(float delta) {
                        Image loading = new Image(new Texture(Gdx.files.internal("pack/loading.png")));
                        loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);
                        loading.addAction(Actions.forever(Actions.rotateTo(360f, 1f)));

                        Table loadingTable = new Table();
                        Label label = new Label("Simulando movimiento", skin);
                        loadingTable.add(loading).size(64f).padRight(10f);
                        loadingTable.add(label);
                        movementReview.getContentTable().add(loadingTable);
                        return true;
                    }
                }, Actions.delay(2f), new Action() {
                    @Override
                    public boolean act(float delta) {
                        Label label = new Label(movementReviews.get(1), skin);
                        label.setVisible(true);
                        Gdx.app.log("Label 1", movementReviews.get(1));
                        movementReview.getContentTable().removeActor(movementReview.getContentTable().getCells().peek().getActor());
                        movementReview.getContentTable().add(label).height(64f);
                        //movementReview.getContentTable().getCell(label).getActor().addAction(new SequenceAction(Actions.alpha(0), Actions.delay(2f), Actions.fadeIn(1f)));
                        return true;
                    }
                }),new SequenceAction(new Action() {
                    @Override
                    public boolean act(float delta) {
                        Image loading = new Image(new Texture(Gdx.files.internal("pack/loading.png")));
                        loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);
                        loading.addAction(Actions.forever(Actions.rotateTo(360f, 1f)));

                        Table loadingTable = new Table();
                        Label label = new Label("Simulando movimiento", skin);
                        loadingTable.add(loading).size(64f).padRight(10f);
                        loadingTable.add(label);
                        movementReview.getContentTable().add(loadingTable);
                        return true;
                    }
                }, Actions.delay(2f), new Action() {
                    @Override
                    public boolean act(float delta) {
                        Label label = new Label(movementReviews.first(), skin);
                        label.setVisible(true);
                        Gdx.app.log("Label 1", movementReviews.first());
                        movementReview.getContentTable().removeActor(movementReview.getContentTable().getCells().peek().getActor());
                        movementReview.getContentTable().add(label).height(64f);
                        //movementReview.getContentTable().getCell(label).getActor().addAction(new SequenceAction(Actions.alpha(0), Actions.delay(2f), Actions.fadeIn(1f)));
                        return true;
                    }
                }), new Action() {
                    @Override
                    public boolean act(float delta) {
                        TextButton button =(TextButton) movementReview.getButtonTable().getCells().first().getActor();
                        button.setText("Jugar");
                        return true;
                    }
                }));
                break;

            case 5:
                movementReview.addAction(new SequenceAction(
                        new SequenceAction(
                                new SequenceAction(new Action() {
                                    @Override
                                    public boolean act(float delta) {
                                        Image loading = new Image(new Texture(Gdx.files.internal("pack/loading.png")));
                                        loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);
                                        loading.addAction(Actions.forever(Actions.rotateTo(360f, 1f)));

                                        Table loadingTable = new Table();
                                        Label label = new Label("Simulando movimiento", skin);
                                        loadingTable.add(loading).size(64f).padRight(10f);
                                        loadingTable.add(label);
                                        movementReview.getContentTable().add(loadingTable);
                                        return true;
                                    }
                                }, Actions.delay(2f), new Action() {
                                    @Override
                                    public boolean act(float delta) {
                                        Label label = new Label(movementReviews.peek(), skin);
                                        label.setVisible(true);
                                        Gdx.app.log("Label 1", movementReviews.peek());
                                        movementReview.getContentTable().removeActor(movementReview.getContentTable().getCells().peek().getActor());
                                        movementReview.getContentTable().add(label).height(64f);
                                        //movementReview.getContentTable().getCell(label).getActor().addAction(new SequenceAction(Actions.alpha(0), Actions.delay(2f), Actions.fadeIn(1f)));
                                        return true;
                                    }
                                }), new SequenceAction(new Action() {
                            @Override
                            public boolean act(float delta) {
                                Image loading = new Image(new Texture(Gdx.files.internal("pack/loading.png")));
                                loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);
                                loading.addAction(Actions.forever(Actions.rotateTo(360f, 1f)));

                                Table loadingTable = new Table();
                                Label label = new Label("Simulando movimiento", skin);
                                loadingTable.add(loading).size(64f).padRight(10f);
                                loadingTable.add(label);
                                movementReview.getContentTable().add(loadingTable);
                                return true;
                            }
                        }, Actions.delay(2f), new Action() {
                            @Override
                            public boolean act(float delta) {
                                Label label = new Label(movementReviews.get(3), skin);
                                label.setVisible(true);
                                Gdx.app.log("Label 2", movementReviews.get(3));
                                movementReview.getContentTable().removeActor(movementReview.getContentTable().getCells().peek().getActor());
                                movementReview.getContentTable().add(label).height(64f);
                                //movementReview.getContentTable().getCell(label).getActor().addAction(new SequenceAction(Actions.alpha(0), Actions.delay(4f), Actions.fadeIn(1f)));
                                return true;
                            }
                        }), new SequenceAction(new Action() {
                            @Override
                            public boolean act(float delta) {
                                Image loading = new Image(new Texture(Gdx.files.internal("pack/loading.png")));
                                loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);
                                loading.addAction(Actions.forever(Actions.rotateTo(360f, 1f)));

                                Table loadingTable = new Table();
                                Label label = new Label("Simulando movimiento", skin);
                                loadingTable.add(loading).size(64f).padRight(10f);
                                loadingTable.add(label);
                                movementReview.getContentTable().add(loadingTable);
                                return true;
                            }
                        }, Actions.delay(2f), new Action() {
                            @Override
                            public boolean act(float delta) {
                                Label label = new Label(movementReviews.get(2), skin);
                                label.setVisible(true);
                                Gdx.app.log("Label 1", movementReviews.get(2));
                                movementReview.getContentTable().removeActor(movementReview.getContentTable().getCells().peek().getActor());
                                movementReview.getContentTable().add(label).height(64f);
                                //movementReview.getContentTable().getCell(label).getActor().addAction(new SequenceAction(Actions.alpha(0), Actions.delay(2f), Actions.fadeIn(1f)));
                                return true;
                            }
                        }),new SequenceAction(new Action() {
                            @Override
                            public boolean act(float delta) {
                                Image loading = new Image(new Texture(Gdx.files.internal("pack/loading.png")));
                                loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);
                                loading.addAction(Actions.forever(Actions.rotateTo(360f, 1f)));

                                Table loadingTable = new Table();
                                Label label = new Label("Simulando movimiento", skin);
                                loadingTable.add(loading).size(64f).padRight(10f);
                                loadingTable.add(label);
                                movementReview.getContentTable().add(loadingTable);
                                return true;
                            }
                        }, Actions.delay(2f), new Action() {
                            @Override
                            public boolean act(float delta) {
                                Label label = new Label(movementReviews.get(1), skin);
                                label.setVisible(true);
                                Gdx.app.log("Label 1", movementReviews.get(1));
                                movementReview.getContentTable().removeActor(movementReview.getContentTable().getCells().peek().getActor());
                                movementReview.getContentTable().add(label).height(64f);
                                //movementReview.getContentTable().getCell(label).getActor().addAction(new SequenceAction(Actions.alpha(0), Actions.delay(2f), Actions.fadeIn(1f)));
                                return true;
                            }
                        })
                        ),
                        new SequenceAction(
                                new SequenceAction(new Action() {
                                    @Override
                                    public boolean act(float delta) {
                                        Image loading = new Image(new Texture(Gdx.files.internal("pack/loading.png")));
                                        loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);
                                        loading.addAction(Actions.forever(Actions.rotateTo(360f, 1f)));

                                        Table loadingTable = new Table();
                                        Label label = new Label("Simulando movimiento", skin);
                                        loadingTable.add(loading).size(64f).padRight(10f);
                                        loadingTable.add(label);
                                        movementReview.getContentTable().add(loadingTable);
                                        return true;
                                    }
                                }, Actions.delay(2f), new Action() {
                                    @Override
                                    public boolean act(float delta) {
                                        Label label = new Label(movementReviews.first(), skin);
                                        label.setVisible(true);
                                        Gdx.app.log("Label 1", movementReviews.first());
                                        movementReview.getContentTable().removeActor(movementReview.getContentTable().getCells().peek().getActor());
                                        movementReview.getContentTable().add(label).height(64f);
                                        //movementReview.getContentTable().getCell(label).getActor().addAction(new SequenceAction(Actions.alpha(0), Actions.delay(2f), Actions.fadeIn(1f)));
                                        return true;
                                    }
                                }),new Action() {
                            @Override
                            public boolean act(float delta) {
                                TextButton button = (TextButton) movementReview.getButtonTable().getCells().first().getActor();
                                button.setText("Jugar");
                                return true;
                            }
                        })));
                break;
            case 6:
                movementReview.addAction(new SequenceAction(
                        new SequenceAction(
                                new SequenceAction(new Action() {
                                    @Override
                                    public boolean act(float delta) {
                                        Image loading = new Image(new Texture(Gdx.files.internal("pack/loading.png")));
                                        loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);
                                        loading.addAction(Actions.forever(Actions.rotateTo(360f, 1f)));

                                        Table loadingTable = new Table();
                                        Label label = new Label("Simulando movimiento", skin);
                                        loadingTable.add(loading).size(64f).padRight(10f);
                                        loadingTable.add(label);
                                        movementReview.getContentTable().add(loadingTable);
                                        return true;
                                    }
                                }, Actions.delay(2f), new Action() {
                                    @Override
                                    public boolean act(float delta) {
                                        Label label = new Label(movementReviews.peek(), skin);
                                        label.setVisible(true);
                                        Gdx.app.log("Label 1", movementReviews.peek());
                                        movementReview.getContentTable().removeActor(movementReview.getContentTable().getCells().peek().getActor());
                                        movementReview.getContentTable().add(label).height(64f);
                                        //movementReview.getContentTable().getCell(label).getActor().addAction(new SequenceAction(Actions.alpha(0), Actions.delay(2f), Actions.fadeIn(1f)));
                                        return true;
                                    }
                                }), new SequenceAction(new Action() {
                            @Override
                            public boolean act(float delta) {
                                Image loading = new Image(new Texture(Gdx.files.internal("pack/loading.png")));
                                loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);
                                loading.addAction(Actions.forever(Actions.rotateTo(360f, 1f)));

                                Table loadingTable = new Table();
                                Label label = new Label("Simulando movimiento", skin);
                                loadingTable.add(loading).size(64f).padRight(10f);
                                loadingTable.add(label);
                                movementReview.getContentTable().add(loadingTable);
                                return true;
                            }
                        }, Actions.delay(2f), new Action() {
                            @Override
                            public boolean act(float delta) {
                                Label label = new Label(movementReviews.get(4), skin);
                                label.setVisible(true);
                                Gdx.app.log("Label 2", movementReviews.get(4));
                                movementReview.getContentTable().removeActor(movementReview.getContentTable().getCells().peek().getActor());
                                movementReview.getContentTable().add(label).height(64f);
                                //movementReview.getContentTable().getCell(label).getActor().addAction(new SequenceAction(Actions.alpha(0), Actions.delay(4f), Actions.fadeIn(1f)));
                                return true;
                            }
                        }), new SequenceAction(new Action() {
                            @Override
                            public boolean act(float delta) {
                                Image loading = new Image(new Texture(Gdx.files.internal("pack/loading.png")));
                                loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);
                                loading.addAction(Actions.forever(Actions.rotateTo(360f, 1f)));

                                Table loadingTable = new Table();
                                Label label = new Label("Simulando movimiento", skin);
                                loadingTable.add(loading).size(64f).padRight(10f);
                                loadingTable.add(label);
                                movementReview.getContentTable().add(loadingTable);
                                return true;
                            }
                        }, Actions.delay(2f), new Action() {
                            @Override
                            public boolean act(float delta) {
                                Label label = new Label(movementReviews.get(3), skin);
                                label.setVisible(true);
                                Gdx.app.log("Label 1", movementReviews.get(3));
                                movementReview.getContentTable().removeActor(movementReview.getContentTable().getCells().peek().getActor());
                                movementReview.getContentTable().add(label).height(64f);
                                //movementReview.getContentTable().getCell(label).getActor().addAction(new SequenceAction(Actions.alpha(0), Actions.delay(2f), Actions.fadeIn(1f)));
                                return true;
                            }
                        }),new SequenceAction(new Action() {
                            @Override
                            public boolean act(float delta) {
                                Image loading = new Image(new Texture(Gdx.files.internal("pack/loading.png")));
                                loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);
                                loading.addAction(Actions.forever(Actions.rotateTo(360f, 1f)));

                                Table loadingTable = new Table();
                                Label label = new Label("Simulando movimiento", skin);
                                loadingTable.add(loading).size(64f).padRight(10f);
                                loadingTable.add(label);
                                movementReview.getContentTable().add(loadingTable);
                                return true;
                            }
                        }, Actions.delay(2f), new Action() {
                            @Override
                            public boolean act(float delta) {
                                Label label = new Label(movementReviews.get(2), skin);
                                label.setVisible(true);
                                Gdx.app.log("Label 1", movementReviews.get(2));
                                movementReview.getContentTable().removeActor(movementReview.getContentTable().getCells().peek().getActor());
                                movementReview.getContentTable().add(label).height(64f);
                                //movementReview.getContentTable().getCell(label).getActor().addAction(new SequenceAction(Actions.alpha(0), Actions.delay(2f), Actions.fadeIn(1f)));
                                return true;
                            }
                        })
                        ),
                        new SequenceAction(
                                new SequenceAction(new Action() {
                                    @Override
                                    public boolean act(float delta) {
                                        Image loading = new Image(new Texture(Gdx.files.internal("pack/loading.png")));
                                        loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);
                                        loading.addAction(Actions.forever(Actions.rotateTo(360f, 1f)));

                                        Table loadingTable = new Table();
                                        Label label = new Label("Simulando movimiento", skin);
                                        loadingTable.add(loading).size(64f).padRight(10f);
                                        loadingTable.add(label);
                                        movementReview.getContentTable().add(loadingTable);
                                        return true;
                                    }
                                }, Actions.delay(2f), new Action() {
                                    @Override
                                    public boolean act(float delta) {
                                        Label label = new Label(movementReviews.get(1), skin);
                                        label.setVisible(true);
                                        Gdx.app.log("Label 1", movementReviews.get(1));
                                        movementReview.getContentTable().removeActor(movementReview.getContentTable().getCells().peek().getActor());
                                        movementReview.getContentTable().add(label).height(64f);
                                        //movementReview.getContentTable().getCell(label).getActor().addAction(new SequenceAction(Actions.alpha(0), Actions.delay(2f), Actions.fadeIn(1f)));
                                        return true;
                                    }
                                }), new SequenceAction(new Action() {
                            @Override
                            public boolean act(float delta) {
                                Image loading = new Image(new Texture(Gdx.files.internal("pack/loading.png")));
                                loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);
                                loading.addAction(Actions.forever(Actions.rotateTo(360f, 1f)));

                                Table loadingTable = new Table();
                                Label label = new Label("Simulando movimiento", skin);
                                loadingTable.add(loading).size(64f).padRight(10f);
                                loadingTable.add(label);
                                movementReview.getContentTable().add(loadingTable);
                                return true;
                            }
                        }, Actions.delay(2f), new Action() {
                            @Override
                            public boolean act(float delta) {
                                Label label = new Label(movementReviews.first(), skin);
                                label.setVisible(true);
                                Gdx.app.log("Label 1", movementReviews.first());
                                movementReview.getContentTable().removeActor(movementReview.getContentTable().getCells().peek().getActor());
                                movementReview.getContentTable().add(label).height(64f);
                                //movementReview.getContentTable().getCell(label).getActor().addAction(new SequenceAction(Actions.alpha(0), Actions.delay(2f), Actions.fadeIn(1f)));
                                return true;
                            }
                        }),new Action() {
                            @Override
                            public boolean act(float delta) {
                                TextButton button = (TextButton) movementReview.getButtonTable().getCells().first().getActor();
                                button.setText("Jugar");
                                return true;
                            }
                        })));
                break;
        }
    }
}
