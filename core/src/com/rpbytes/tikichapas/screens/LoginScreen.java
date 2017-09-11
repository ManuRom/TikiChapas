package com.rpbytes.tikichapas.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.assets.Assets;
import com.rpbytes.tikichapas.utils.Constants;
import com.rpbytes.tikichapas.actions.LoadingAnimation;
import com.rpbytes.tikichapas.User;
import com.rpbytes.tikichapas.net.Url;
import com.rpbytes.tikichapas.utils.Text;

import org.json.JSONObject;

import java.util.Date;

public class LoginScreen extends AbstractScreen {
	

	public Table loginTable, moreTable;
	public TextField email, password;
	public TextButton loginButton, registerButton, notConnectionButton;
	public Label nickLabel, passwordLabel, registerInfo;

	public Dialog loading;
	public LoadingAnimation loadingAnimation;

	public LoginScreen(TikiChapasGame game) {
		setGame(game);
	}
	

	@Override
	public void render(float delta) {
		super.render(delta);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void show() {
		super.show();

		loginTable = new Table();

		nickLabel = new Label("Email",getSkin(),"label");
		passwordLabel = new Label("Contraseña",getSkin(),"label");

		email = new TextField("",getSkin());
		password = new TextField("",getSkin());
		password.setPasswordMode(true);
		password.setPasswordCharacter('*');

		loginButton = new TextButton("Entrar", getSkin());

		moreTable = new Table();

		registerInfo = new Label("¿No tienes una cuenta? Registrate aquí",getSkin(),"default");

		registerButton = new TextButton("Registrate", getSkin());
		notConnectionButton = new TextButton("Jugar sin Conexion", getSkin());

		//Loading
		loading = new Dialog("Cargando",getSkin());
		TextureRegion[] arrayLoadingTextures = new TextureRegion[3];
		arrayLoadingTextures[0] = Assets.botonPartidoRapido;//TODO
		arrayLoadingTextures[1] = Assets.botonPartidoEnRed;//TODO
		arrayLoadingTextures[2] = Assets.botonOpciones;//TODO
		loadingAnimation = new LoadingAnimation(arrayLoadingTextures);
		loading.getContentTable().add(loadingAnimation).center();


		Gdx.input.setInputProcessor(getMainStage());

		loginTable.add(nickLabel).left().width(250f).padBottom(5f);
		loginTable.add(email).width(300f).padBottom(5f);
		loginTable.row();
		loginTable.add(passwordLabel).width(250f).padBottom(5f).spaceBottom(5f);
		loginTable.add(password).width(300f).padBottom(5f).spaceBottom(5f);
		loginTable.row();
		loginTable.add(loginButton).right().colspan(2);
		//scoreBoard.add(registerButton).size(250,50);
		//scoreBoard.row();
		//scoreBoard.add(notConnectionButton).size(250,50).colspan(2);
		
		loginTable.center();
		//loginTable.setPosition(getViewportWidth()/2, viewportHeight*2);


		NinePatchDrawable backgroundTable = new NinePatchDrawable(getSkin().getPatch("grey_panel"));
		loginTable.setHeight(400f);
		loginTable.setWidth(800f);
		loginTable.setPosition(getViewportWidth()/2, getViewportHeight()*2, Align.center);
		loginTable.setBackground(backgroundTable);

		getMainStage().addActor(loginTable);

		moreTable.add(registerButton).pad(Constants.PAD);
		moreTable.add(notConnectionButton).pad(Constants.PAD);
		moreTable.center();
		moreTable.setPosition(getViewportWidth()/2, -getViewportHeight(), Align.center);


		getMainStage().addActor(moreTable);


		loginTable.addAction(Actions.moveToAligned(getViewportWidth()/2, getViewportHeight()/2 + 50,Align.center,0.75f));

		moreTable.addAction(Actions.moveToAligned(getViewportWidth()/2,120,Align.center,0.75f));

		loginButton.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				sendPostLoginRequest();
			  }	
		});

		registerInfo.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				getGame().setScreen(new RegisterScreen(getGame()));
			}
		});

		registerButton.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				getGame().setScreen(new RegisterScreen(getGame()));
			}
			
		});
		
		notConnectionButton.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				getGame().setOffline(true);
				getGame().setScreen(new MainMenuScreen(getGame()));
			}
		});

		if(Gdx.app.getPreferences("data").contains("token")){
			if(Gdx.app.getPreferences("data").contains("lastLogin")){
				Date currentDate = new Date(TimeUtils.millis());
				long tokenLife = 3300000;
				Date lastLoginDate = new Date(Gdx.app.getPreferences("data").getLong("lastLogin")+ tokenLife);

				if(lastLoginDate.before(currentDate)){
					//NEW SCREEN
				}else{
					//REFRESH TOKEN
				}
			}
		}

	}

	public void sendPostLoginRequest(){
		JSONObject content = new JSONObject();
		content.put("email",email.getText());
		content.put("password",password.getText());
		Gdx.app.log("content", content.toString());

		Net.HttpRequest request = new Net.HttpRequest();
		request.setMethod(Net.HttpMethods.POST);
		request.setUrl(Url.BASE_URL+Url.AUTH_PATH);
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
				if(httpResponse.getStatus().getStatusCode()== HttpStatus.SC_UNAUTHORIZED){
					Dialog dialog = new Dialog("Error",getSkin());
					dialog.text(new Label(Text.INVALID_CREDENTIALS,getSkin(),"default"));
					dialog.button(new TextButton(Text.BTN_TRY_AGAIN,getSkin(),"default"));
					dialog.getBackground().setMinWidth(350f);
					dialog.getBackground().setMinHeight(350f);
					dialog.show(getMainStage());
				}
				if (httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK) {
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
			}

			@Override
			public void failed(Throwable t) {
				Dialog dialog = new Dialog("Error",getSkin());
				dialog.text(new Label(Text.SERVER_UNAVAILABLE,getSkin(),"default"));
				dialog.button(new TextButton(Text.BTN_TRY_AGAIN,getSkin(),"default"));
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
				Dialog dialog = new Dialog("Error",getSkin());
				dialog.text(new Label(Text.SERVER_UNAVAILABLE,getSkin(),"default"));
				dialog.button(new TextButton(Text.BTN_TRY_AGAIN,getSkin(),"default"));
				dialog.getBackground().setMinWidth(350f);
				dialog.getBackground().setMinHeight(350f);
				dialog.show(getMainStage());
			}

			@Override
			public void cancelled() {

			}
		});
	}


	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}

}
