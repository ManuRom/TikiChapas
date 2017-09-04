package com.rpbytes.tikichapas.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.utils.Constants;
import com.rpbytes.tikichapas.net.Url;
import com.rpbytes.tikichapas.utils.SkinKeys;
import com.rpbytes.tikichapas.utils.Text;

import org.json.JSONObject;

/**
 * Created by manoleichon on 13/08/17.
 */
public class UpdateUserScreen extends BaseMenuScreen {

    public Screen prevScreenType;
    public TextButton update;
    public TextField username, password, repeatPassword, email;
    public Label usernameLabel, passwordLabel, repeatPasswordLabel, emailLabel;
    public CheckBox updatePassword;

    public UpdateUserScreen(TikiChapasGame game, Screen prevScreenType){
        setGame(game);
        this.prevScreenType = prevScreenType;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void initScrollTable() {

        usernameLabel = new Label(Text.LABEL_USERNAME, getSkin(), SkinKeys.LABEL);
        emailLabel = new Label(Text.LABEL_EMAIL,getSkin(),SkinKeys.LABEL);

        passwordLabel = new Label(Text.LABEL_PASSWORD, getSkin(), SkinKeys.LABEL);
        passwordLabel.setVisible(false);
        repeatPasswordLabel = new Label(Text.LABEL_PASSWORD_REPEAT,getSkin(),SkinKeys.LABEL);
        repeatPasswordLabel.setVisible(false);

        updatePassword = new CheckBox(Text.UPDATE_PASSWORD, getSkin());

        username = new TextField(getGame().getUser().getUsername(), getSkin());
        email = new TextField(getGame().getUser().getEmail(),getSkin());

        password = new TextField("",getSkin());
        password.setPasswordMode(true);
        password.setPasswordCharacter('*');
        password.setVisible(false);
        repeatPassword = new TextField("",getSkin());
        repeatPassword.setPasswordMode(true);
        repeatPassword.setPasswordCharacter('*');
        repeatPassword.setVisible(false);

        tableScroll.background(getSkin().getDrawable(SkinKeys.GREY_PANEL));

        tableScroll.add(usernameLabel).left().pad(Constants.PAD, Constants.PAD,Constants.PAD/3,Constants.PAD);
        tableScroll.add(username).padBottom(Constants.PAD/3).width(Constants.INPUT_WIDTH).left();
        tableScroll.row();
        tableScroll.add(emailLabel).left().pad(Constants.PAD, Constants.PAD,Constants.PAD/3,Constants.PAD);
        tableScroll.add(email).padBottom(Constants.PAD/3).width(Constants.INPUT_WIDTH).left();
        tableScroll.row();
        tableScroll.add(updatePassword).pad(Constants.PAD).colspan(2).center();
        tableScroll.row();
        tableScroll.center();

        updatePassword.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(updatePassword.isChecked())
                    changePasswordVisibility(true);
                else
                    changePasswordVisibility(false);
            }
        });

    }

    @Override
    public void initFooterTable() {
        prevScreen = new ImageButton(getSkin(), SkinKeys.BACK);
        update = new TextButton(Text.BTN_SAVE_CHANGES,getSkin());
        Table tableFooterLeft = new Table();
        Table tableFooterRight = new Table();

        tableFooterLeft.add(prevScreen).left().padLeft(Constants.HEADER_BUTTONS_PAD).expand();
        tableFooterRight.add(update).right().padRight(Constants.HEADER_BUTTONS_PAD).expand();

        tableFooter.setWidth(getViewportWidth());
        tableFooter.add(tableFooterLeft).padBottom(Constants.HEADER_BUTTONS_PAD).left().width((getViewportWidth()/2));
        tableFooter.add(tableFooterRight).padBottom(Constants.HEADER_BUTTONS_PAD).right().width((getViewportWidth()/2));

        update.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if(username.getText().equals("") || email.getText().equals("")){
                    showEmptyFieldDialog();
                } else {

                    JSONObject content = new JSONObject();
                    content.put("username", username.getText());
                    content.put("email", email.getText());

                    if (updatePassword.isChecked()) {
                        if (password.getText().equals("")) {
                            showEmptyFieldDialog();
                        } else {
                            if (!password.getText().equals(repeatPassword.getText())) {
                                Dialog passwordDifferents = new Dialog(Text.ERROR_TITLE, getSkin());
                                passwordDifferents.text(new Label(Text.PASSWORD_REPEAT, getSkin(), SkinKeys.DEFAULT));
                                passwordDifferents.button(new TextButton(Text.BTN_ACCEPT, getSkin()));
                                passwordDifferents.getBackground().setMinHeight(250f);
                                passwordDifferents.getBackground().setMinWidth(250f);
                                passwordDifferents.show(getMainStage());

                            } else {
                                content.put("password", password.getText());
                                sendPutUserRequest(content);
                            }
                        }
                    } else {
                        sendPutUserRequest(content);
                    }
                }


            }
        });

        prevScreen.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(prevScreenType);
            }
        });
    }

    private void changePasswordVisibility(boolean enabled){
        passwordLabel.setVisible(enabled);
        password.setVisible(enabled);
        repeatPasswordLabel.setVisible(enabled);
        repeatPassword.setVisible(enabled);

        if(enabled) {
            tableScroll.add(passwordLabel).left().pad(Constants.PAD, Constants.PAD,Constants.PAD/3,Constants.PAD);
            tableScroll.add(password).padBottom(Constants.PAD/3).width(Constants.INPUT_WIDTH).left();
            tableScroll.row();
            tableScroll.add(repeatPasswordLabel).left().pad(Constants.PAD, Constants.PAD,Constants.PAD/3,Constants.PAD);
            tableScroll.add(repeatPassword).padBottom(Constants.PAD/3).width(Constants.INPUT_WIDTH).left();
        } else {
            tableScroll.removeActor(passwordLabel);
            tableScroll.removeActor(password);
            tableScroll.removeActor(repeatPasswordLabel);
            tableScroll.removeActor(repeatPassword);
            tableScroll.getCells().removeIndex(tableScroll.getCells().size-1);
            tableScroll.getCells().removeIndex(tableScroll.getCells().size-1);
            tableScroll.getCells().removeIndex(tableScroll.getCells().size-1);
            tableScroll.getCells().removeIndex(tableScroll.getCells().size-1);
        }
    }

    private void sendPutUserRequest(JSONObject content){
        Net.HttpRequest request = new Net.HttpRequest();
        request.setMethod(Net.HttpMethods.PUT);
        request.setUrl(Url.BASE_URL+Url.USER_PATH);
        request.setContent(content.toString());
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");
        request.setHeader("Authorization","Bearer "+Gdx.app.getPreferences("data").getString("token"));


        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                if(httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK) {
                    showResponseOkDialog();
                    getUserRequest();
                }
                if(httpResponse.getStatus().getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
                    JSONObject response = new JSONObject(httpResponse.getResultAsString());
                    if( response.getString("error").equals("email_duplicate") )
                        showResponseFailedDialog(Text.EMAIL_DUPLICATE);
                    if( response.getString("error").equals("username_duplicate") )
                        showResponseFailedDialog(Text.USERNAME_DUPLICATE);
                }
            }

            @Override
            public void failed(Throwable t) {
                showResponseFailedDialog(Text.SERVER_UNAVAILABLE);
            }

            @Override
            public void cancelled() {

            }
        });
    }

    private void showEmptyFieldDialog(){
        Dialog emptyField = new Dialog(Text.ERROR_TITLE, getSkin());
        emptyField.text(new Label(Text.EMPTY_FIELD, getSkin(), SkinKeys.DEFAULT));
        emptyField.button(new TextButton(Text.BTN_ACCEPT, getSkin()));
        emptyField.getBackground().setMinHeight(250f);
        emptyField.getBackground().setMinWidth(250f);
        emptyField.show(getMainStage());
    }

    private void showResponseOkDialog(){
        Dialog responseOk = new Dialog("", getSkin());
        responseOk.text(new Label(Text.UPDATE_DATA_CORRECTLY, getSkin(), SkinKeys.DEFAULT));
        responseOk.button(new TextButton(Text.BTN_ACCEPT, getSkin()));
        responseOk.getBackground().setMinHeight(250f);
        responseOk.getBackground().setMinWidth(250f);
        responseOk.show(getMainStage());
    }

    private void showResponseFailedDialog(String error){
        Dialog responseFailed = new Dialog(Text.ERROR_TITLE, getSkin());
        responseFailed.text(new Label(error, getSkin(), SkinKeys.DEFAULT));
        responseFailed.button(new TextButton(Text.BTN_ACCEPT, getSkin()));
        responseFailed.getBackground().setMinHeight(250f);
        responseFailed.getBackground().setMinWidth(250f);
        responseFailed.show(getMainStage());
    }

}
