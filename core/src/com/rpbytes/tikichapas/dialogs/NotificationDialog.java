package com.rpbytes.tikichapas.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.rpbytes.tikichapas.net.OnlineMatchNotification;

/**
 * Created by manoleichon on 20/02/17.
 */
public class NotificationDialog extends Dialog {

    public int unreadNotifications=0;
    public Array<OnlineMatchNotification> notifications;
    public boolean canHide = false;
    public Table tableScroll;
    public ScrollPane scroll;

    @Override
    public float getPrefWidth() {
        return 900;
    }

    @Override
    public float getPrefHeight() {
        return 600;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(canHide)
            this.hide();
    }

    public NotificationDialog(String title, Skin skin) {
        super(title, skin);
        notifications = new Array<OnlineMatchNotification>();
        tableScroll = new Table();
        scroll = new ScrollPane(tableScroll, getSkin());
        getContentTable().add(scroll).padTop(80);
        getContentTable().setFillParent(true);
        createCloseButton();
    }

    public Label getNumberNotifications(){
        if(unreadNotifications>9)
            return new Label("+9",getSkin(),"notification");
        else
            return new Label(Integer.toString(unreadNotifications),getSkin(),"notification");
    }

    public void addNotification(OnlineMatchNotification notification){
        Table row = new Table();
        notifications.add(notification);
        row.add(new Label(notification.messageBuilder(), getSkin()));
        row.add(notification.getButton());
        tableScroll.add(row);
        tableScroll.row();
        unreadNotifications++;
        Gdx.app.log("Unread notifications", Integer.toString(unreadNotifications));
    }

    public void createCloseButton(){
        TextButton closeButton = new TextButton("Cerrar",getSkin());
        button(closeButton);
        /*closeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String url = Url.BASE_URL + Url.NOTIFICATIONS_PATH;
                Net.HttpRequest request = new Net.HttpRequest();

                JSONArray jsonArray = new JSONArray();

                for (Notification notification:notifications){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", notification.id);
                    jsonArray.put(jsonObject);
                }

                JSONObject requestContent = new JSONObject();
                requestContent.put("notifications", jsonArray);

                request.setMethod(Net.HttpMethods.PUT);
                request.setContent(requestContent.toString());
                request.setHeader("Content-Type", "application/json");
                request.setHeader("Accept", "application/json");

                request.setUrl(url);
                request.setHeader("Authorization","Bearer "+ Gdx.app.getPreferences("data").getString("token"));
                Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
                    @Override
                    public void handleHttpResponse(Net.HttpResponse httpResponse) {
                        Gdx.app.log("Put Notifications Status Code", Integer.toString(httpResponse.getStatus().getStatusCode()));
                        if(httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK){
                            Gdx.app.log("Send Put Notifications","Response ok");
                            canHide =true;
                        }
                    }

                    @Override
                    public void failed(Throwable t) {
                        Gdx.app.log("Put Notifications","Failed");
                    }

                    @Override
                    public void cancelled() {

                    }
                });
            }
        });*/

    }
}
