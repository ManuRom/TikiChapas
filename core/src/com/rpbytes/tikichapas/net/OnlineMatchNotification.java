package com.rpbytes.tikichapas.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.screens.LoadingMatchScreen;
import com.rpbytes.tikichapas.screens.MainMenuScreen;

import org.json.JSONObject;

/**
 * Created by manoleichon on 20/02/17.
 */
public class OnlineMatchNotification extends Notification {
    public TextButton button;
    public int matchId;
    public TikiChapasGame game;
    public String rivalUsername;

    public OnlineMatchNotification(int notificationId, String rivalUsername, int matchId,int typeId, Skin skin, TikiChapasGame game){
        this.id = notificationId;
        this.matchId = matchId;
        this.game = game;
        this.rivalUsername = rivalUsername;
        this.typeId = typeId;
        this.buttonBuilder(skin);
    }

    public void buttonBuilder(Skin skin){
        if(typeId == 1){
            this.button = new TextButton("Jugar",skin);
        }else
            this.button = new TextButton("Ver",skin);
        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new LoadingMatchScreen(getGame(), matchId));
            }
        });
    }

    public String messageBuilder(){
        if(typeId == 1)
                return this.rivalUsername+" "+"ha jugado su turno.";
        if(typeId == 2)
                return "El partido contra "+this.rivalUsername+" ha terminado.";
        if(typeId == 3)
                return "El partido contra "+this.rivalUsername+" ha caducado.";
        return "";
    }

    public TextButton getButton(){
        return this.button;
    }

    public TikiChapasGame getGame(){
        return this.game;
    }

    public void sendUpdateNotificationRequest(){
        final Net.HttpRequest request = new Net.HttpRequest();
        request.setMethod(Net.HttpMethods.PUT);
        request.setUrl(Url.BASE_URL+Url.NOTIFICATION_PATH+id);
        Gdx.app.log("Update notification", request.getUrl());
        request.setHeader("Authorization", getGame().getPreferences().getString("token"));
        Gdx.app.log("Update Notification","Send request");
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                JSONObject response = new JSONObject(httpResponse.getResultAsString());
                if(httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK){
                    if(response.getString("status").equals("updated")){
                        Gdx.app.log("Update Notification", "updated");
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                getGame().setScreen(new MainMenuScreen(getGame()));
                            }
                        });
                    }
                }else{
                    Gdx.app.log("Update notification status code", Integer.toString(httpResponse.getStatus().getStatusCode()));
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("Update Notification failed", t.getLocalizedMessage());
            }

            @Override
            public void cancelled() {

            }
        });
    }
}
