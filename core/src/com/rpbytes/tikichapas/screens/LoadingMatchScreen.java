package com.rpbytes.tikichapas.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.net.Movement;
import com.rpbytes.tikichapas.User;
import com.rpbytes.tikichapas.net.Url;
import com.rpbytes.tikichapas.match.MatchSettings;
import com.rpbytes.tikichapas.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by manoleichon on 26/12/16.
 */
public class LoadingMatchScreen extends AbstractScreen {

    public int matchId;
    public Label loading;

    public LoadingMatchScreen(TikiChapasGame game, int matchId){
        this.setGame(game);
        this.matchId = matchId;
    }


    @Override
    public void show() {
        super.show();
        loading = new Label("Cargando Partida",getSkin());
        loading.setPosition(Constants.LOADING_LABEL_X , Constants.LOADING_LABEL_Y);
        getMainStage().addActor(loading);

        sendGetMatchRequest();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
/*
        getMatchListener();
*/
    }

    private void sendGetMatchRequest(){
        Net.HttpRequest request = new Net.HttpRequest();
        request.setMethod(Net.HttpMethods.GET);
        request.setUrl(Url.BASE_URL+Url.MATCH_PATH+matchId);
        request.setHeader("Accept", "application/json");
        request.setHeader("Authorization","Bearer "+ Gdx.app.getPreferences("data").getString("token"));

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                JSONObject response = new JSONObject(httpResponse.getResultAsString());
                if(httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK){
                    final JSONObject matchValue = response.getJSONObject("match");
                    JSONArray movementsValue = response.getJSONArray("movements");
                    final MatchSettings settings = new MatchSettings(matchValue,getGame());
                    final Array<Movement> movements = new Array<Movement>();

                    for(int i = 0; i < movementsValue.length(); i++){
                        JSONObject movement = new JSONObject(movementsValue.get(i).toString());
                        movements.add(new Movement(movement));
                    }

                    final User userHome = new User(matchValue.getJSONObject("home_user"));
                    final User userAway = new User(matchValue.getJSONObject("away_user"));
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            getGame().setScreen(new MatchScreen(getGame(),settings,matchValue.getInt("goals_home"),matchValue.getInt("goals_away"),movements,matchValue.getInt("id"),matchValue.getInt("state_turn"),matchValue.getInt("turn_home"), userHome,userAway));
                        }
                    });
                }else{
                    Gdx.app.log("Loading Match status Code",String.valueOf(httpResponse.getStatus().getStatusCode()));
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("Loading Match failed",t.getLocalizedMessage());
            }

            @Override
            public void cancelled() {

            }
        });
    }

}
