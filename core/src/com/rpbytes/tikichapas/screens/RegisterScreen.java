package com.rpbytes.tikichapas.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.User;
import com.rpbytes.tikichapas.net.Url;
import com.rpbytes.tikichapas.utils.Constants;
import com.rpbytes.tikichapas.utils.SkinKeys;
import com.rpbytes.tikichapas.utils.Text;

import org.json.JSONObject;

public class RegisterScreen extends AbstractScreen {

	public TextField username, password, repeatPassword, email;
	public Label usernameLabel, passwordLabel, repeatPasswordLabel, emailLabel;
	public Table table;
	public Button registerButton, backButton;


	public RegisterScreen(TikiChapasGame game) {
		setGame(game);
	}

	@Override
	public void show() {
		super.show();

		table = new Table();

		Label title = new Label(Text.REGISTER_USER_TITLE, getSkin(), SkinKeys.LABEL_TITLE);

		usernameLabel = new Label(Text.LABEL_USERNAME, getSkin(), SkinKeys.LABEL);
		passwordLabel = new Label(Text.LABEL_PASSWORD, getSkin(), SkinKeys.LABEL);
		repeatPasswordLabel = new Label(Text.LABEL_PASSWORD_REPEAT,getSkin(),SkinKeys.LABEL);
		emailLabel = new Label(Text.LABEL_EMAIL,getSkin(),SkinKeys.LABEL);

		username = new TextField("", getSkin());
		password = new TextField("",getSkin());
		password.setPasswordMode(true);
		password.setPasswordCharacter('*');
		repeatPassword = new TextField("",getSkin());
		repeatPassword.setPasswordMode(true);
		repeatPassword.setPasswordCharacter('*');
		email = new TextField("",getSkin());


		registerButton = new TextButton(Text.BTN_REGISTER, getSkin());
		backButton = new ImageButton(getSkin(),SkinKeys.BACK);

		Gdx.input.setInputProcessor(getMainStage());

		table.add(title).pad(Constants.PAD).top().colspan(2);
		table.row();
		table.add(usernameLabel).left().pad(5f, 5f, 5f, 15f);
		table.add(username).width(Constants.INPUT_WIDTH).pad(5f);
		table.row();
		table.add(passwordLabel).pad(5f, 5f, 5f, 15f).left();
		table.add(password).width(Constants.INPUT_WIDTH).pad(5f);
		table.row();
		table.add(repeatPasswordLabel).pad(5f, 5f, 5f, 15f).left();
		table.add(repeatPassword).pad(5f).width(Constants.INPUT_WIDTH);
		table.row();
		table.add(emailLabel).pad(5f, 5f, 5f, 15f).left();
		table.add(email).pad(5f).width(Constants.INPUT_WIDTH);
		table.row();
		table.add(backButton).space(10).left();
		table.add(registerButton).space(10).right();
		
		table.center();
		table.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);

		NinePatchDrawable background_table = new NinePatchDrawable(getSkin().getPatch(SkinKeys.GREY_PANEL));
		table.setHeight(515f);
		table.setWidth(900f);
		table.setPosition(getViewportWidth()/2,getViewportHeight()/2, Align.center);
		table.setBackground(background_table);

		getMainStage().addActor(table);
		
		registerButton.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				if(username.getText().equals("") || email.getText().equals("") || password.getText().equals("")) {
					showEmptyFieldDialog();
				} else {
					if (password.getText().equals(repeatPassword.getText())) {
						sendPostRegisterRequest();
					} else {
						Dialog passwordDifferents = new Dialog(Text.ERROR_TITLE, getSkin());
						passwordDifferents.text(new Label(Text.PASSWORD_REPEAT, getSkin(), SkinKeys.DEFAULT));
						passwordDifferents.button(new TextButton(Text.BTN_ACCEPT, getSkin()));
						passwordDifferents.getBackground().setMinHeight(250f);
						passwordDifferents.getBackground().setMinWidth(250f);
						passwordDifferents.show(getMainStage());
					}
				}
			}	
		});

		backButton.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				getGame().setScreen(new LoginScreen(getGame()));
			}
		});
	}

	@Override
	public void render(float delta) {
		super.render(delta);
	}

	public void sendPostRegisterRequest(){
		JSONObject content = new JSONObject();
		content.put("username",username.getText());
		content.put("password",password.getText());
		content.put("email",email.getText());
		Gdx.app.log("content", content.toString());

		Net.HttpRequest request = new Net.HttpRequest();
		request.setMethod(Net.HttpMethods.POST);
		request.setUrl(Url.BASE_URL+Url.REGISTER_PATH);
		request.setContent(content.toString());
		request.setHeader("Content-Type", "application/json");
		request.setHeader("Accept", "application/json");

		Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
			@Override
			public void handleHttpResponse(Net.HttpResponse httpResponse) {
				if(httpResponse.getStatus().getStatusCode()== HttpStatus.SC_INTERNAL_SERVER_ERROR){
					Dialog dialog = new Dialog("Error",getSkin());
					dialog.text(new Label(Text.SERVER_FAILED,getSkin(),"default"));
					dialog.button(new TextButton(Text.BTN_TRY_AGAIN,getSkin(),"default"));
					dialog.getBackground().setMinWidth(350f);
					dialog.getBackground().setMinHeight(350f);
					dialog.show(getMainStage());
				}
				if (httpResponse.getStatus().getStatusCode() == HttpStatus.SC_CREATED) {
					JSONObject response = new JSONObject(httpResponse.getResultAsString());
					Gdx.app.log("response", response.toString());
					Gdx.app.log("status", String.valueOf(httpResponse.getStatus().getStatusCode()));
					Gdx.app.log("token",response.getString("token"));
					getGame().setToken(response.getString("token"));
					Preferences preferences = Gdx.app.getPreferences("data");
					preferences.putString("token", response.getString("token"));
					preferences.putLong("lastLogin", TimeUtils.millis());
					preferences.flush();
					sendGetUserRequest();
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
				Dialog dialog = new Dialog(Text.ERROR_TITLE,getSkin());
				dialog.text(new Label(Text.SERVER_UNAVAILABLE,getSkin(),SkinKeys.DEFAULT));
				dialog.button(new TextButton(Text.BTN_TRY_AGAIN,getSkin(),SkinKeys.DEFAULT));
				dialog.getBackground().setMinWidth(350f);
				dialog.getBackground().setMinHeight(350f);
				dialog.show(getMainStage());
			}

			@Override
			public void cancelled() {

			}
		});

	}

	public void sendGetUserRequest(){

		Net.HttpRequest request = new Net.HttpRequest();
		request.setMethod(Net.HttpMethods.GET);
		request.setUrl(Url.BASE_URL+Url.USER_PATH);
		request.setHeader("Authorization","Bearer "+Gdx.app.getPreferences("data").getString("token"));

		Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
			@Override
			public void handleHttpResponse(Net.HttpResponse httpResponse) {
				if(httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK){
					JSONObject response = new JSONObject(httpResponse.getResultAsString());
					getGame().setUser(new User(response.getJSONObject("user")));
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							getGame().setOffline(false);
							getGame().setScreen(new ItemScreen(getGame()));
						}
					});
				}
			}

			@Override
			public void failed(Throwable t) {
				Gdx.app.log("failed user request", t.getLocalizedMessage().toString());
				Dialog dialog = new Dialog(Text.ERROR_TITLE,getSkin());
				dialog.text(new Label(Text.SERVER_UNAVAILABLE,getSkin(),SkinKeys.DEFAULT));
				dialog.button(new TextButton(Text.BTN_TRY_AGAIN,getSkin(),SkinKeys.DEFAULT));
				dialog.getBackground().setMinWidth(350f);
				dialog.getBackground().setMinHeight(350f);
				dialog.show(getMainStage());
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

	private void showResponseFailedDialog(String error){
		Dialog responseFailed = new Dialog(Text.ERROR_TITLE, getSkin());
		responseFailed.text(new Label(error, getSkin(), SkinKeys.DEFAULT));
		responseFailed.button(new TextButton(Text.BTN_ACCEPT, getSkin()));
		responseFailed.getBackground().setMinHeight(250f);
		responseFailed.getBackground().setMinWidth(250f);
		responseFailed.show(getMainStage());
	}
}
