package com.rpbytes.tikichapas.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.rpbytes.tikichapas.User;
import com.rpbytes.tikichapas.dialogs.ConfigDialog;
import com.rpbytes.tikichapas.dialogs.NotificationDialog;
import com.rpbytes.tikichapas.net.OnlineMatchNotification;
import com.rpbytes.tikichapas.net.Url;
import com.rpbytes.tikichapas.utils.Constants;
import com.rpbytes.tikichapas.utils.Text;

import org.json.JSONObject;

public abstract class BaseMenuScreen extends AbstractScreen {

	public ScrollPane scroll;
	public Table tableContainer, tableHeader, tableScroll, tableFooter;
	public ImageButton  prevScreen, notifications, store, config;
	public TextButton nextScreen;
	public ConfigDialog configDialog;
	public NotificationDialog notificationDialog;
	public Label numberNotifications, username, userCoins;


	@Override
	public void show() {
		super.show();
		//Table base
		tableContainer = new Table();

		tableContainer.align(Align.center);
		tableContainer.setPosition(0, 0);

		//Table header
		tableHeader = new Table();

		//Table Scroll
		tableScroll = new Table();

		//Table Footer
		tableFooter = new Table();

		//Scroll
		scroll = new ScrollPane(tableScroll, getSkin());

		//Added to tableHeader
		initHeaderTable();

		//Added to tableScroll
		initScrollTable();

		//Added to tableFooter
		initFooterTable();

		//Create tableContainer
		initContainerTable();

		getMainStage().addActor(tableContainer);
		tableContainer.setFillParent(true);

		if(!getGame().isOffline())
			getNotificationsRequest();

		Gdx.input.setInputProcessor(getMainStage());

	}

	private void initHeaderTable(){
		tableHeader.setWidth(getViewportWidth());
		Table tableHeaderRight = new Table();
		Table tableHeaderLeft = new Table();

		if(!getGame().isOffline()) {

			notifications = new ImageButton(getSkin(), "mail");
			store = new ImageButton(getSkin(), "store");
			config = new ImageButton(getSkin(), "config");

			userCoins = new Label(Integer.toString(getGame().getUser().getCoins()), getSkin(), "header");
			username = new Label(getGame().getUser().getUsername(), getSkin(), "header");
			tableHeaderLeft.add(store).right().padRight(15).padTop(15).padLeft(15);
			tableHeaderLeft.add(new Image(getSkin().getDrawable("ic_coins"))).spaceTop(20).padTop(20).padLeft(15);
			tableHeaderLeft.add(userCoins).height(60).padTop(25).spaceLeft(20).expand().left();
			tableHeaderRight.add(username).expand().right().height(60).padTop(25).spaceRight(20);
			tableHeaderRight.add(notifications).right().padRight(15).padTop(15);
			tableHeaderRight.add(config).right().padRight(15).padTop(15);

			tableHeader.add(tableHeaderLeft).left().width((getViewportWidth() / 2));
			tableHeader.add(tableHeaderRight).right().width((getViewportWidth() / 2));


			configDialog = new ConfigDialog(Text.CONFIG_DIALOG, getSkin(), getGame());

			notificationDialog = new NotificationDialog(Text.NOTIFICATION_DIALOG, getSkin());

			notifications.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					System.out.println("Notifications click");
					notificationDialog.show(getMainStage());
				}
			});

			store.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					getGame().setScreen(new StoreScreen(getGame()));
				}
			});

			config.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					System.out.println("Config click");
					configDialog.show(getMainStage());
				}
			});
		}else{
			config = new ImageButton(getSkin(), "config");
			tableHeaderLeft.add().expandX();
			tableHeaderRight.add().expandX();
			tableHeaderRight.add(config).right().padRight(15).padTop(15);

			tableHeader.add(tableHeaderLeft).left().width((getViewportWidth() / 2));
			tableHeader.add(tableHeaderRight).right().width((getViewportWidth() / 2));
			configDialog = new ConfigDialog(Text.CONFIG_DIALOG, getSkin(), getGame());
			config.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					configDialog.show(getMainStage());
				}
			});
		}
	}

	public abstract void initScrollTable();

	public abstract void initFooterTable();

	private void initContainerTable(){
		tableContainer.add(tableHeader).top().expandX();
		tableContainer.row();
		tableContainer.add(scroll).expand();
		tableContainer.row();
		tableContainer.add(tableFooter).bottom();
	}

	@Override
	public void render(float delta) {
		super.render(delta);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width,height);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	public void getNotificationsRequest(){
		String url = Url.BASE_URL + Url.NOTIFICATIONS_PATH;
		Net.HttpRequest request = new Net.HttpRequest();

		request.setMethod(Net.HttpMethods.GET);
		request.setUrl(url);
		request.setHeader("Authorization","Bearer "+ Gdx.app.getPreferences("data").getString("token"));
		Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
			@Override
			public void handleHttpResponse(Net.HttpResponse httpResponse) {
				Gdx.app.log("Get Notifications Status Code", Integer.toString(httpResponse.getStatus().getStatusCode()));
				if(httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK){
					String response = httpResponse.getResultAsString();
					JsonValue jsonNotifications = new JsonReader().parse(response).get("notifications");
					Gdx.app.log("Get Notification response",response);
					for( JsonValue notification: jsonNotifications.iterator()){
						notificationDialog.addNotification(new OnlineMatchNotification(notification.getInt("id"),
								                                                       notification.getString("rival_username"),
								                                                       notification.getInt("match_id"),
								                                                       notification.getInt("type_id"),
																					   getSkin(),getGame()));
					}


					if(notificationDialog.unreadNotifications>0) {
						numberNotifications = notificationDialog.getNumberNotifications();

						numberNotifications.setPosition(1060,615,Align.center);
						Image background = new Image(getSkin().getDrawable("ic_number_mail_background"));
						background.setPosition(1060,615,Align.center);
						getMainStage().addActor(background);
						getMainStage().addActor(numberNotifications);
						getGame().setNotifications(notificationDialog.notifications);
					}else{
						notificationDialog.getContentTable().add(new Label("No tienes notificaciones nuevas",getSkin()));
					}
				}
			}

			@Override
			public void failed(Throwable t) {
				Gdx.app.log("Get Notifications",t.getLocalizedMessage());
			}

			@Override
			public void cancelled() {

			}
		});
	}

	public void getUserRequest(){

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
							username.setText(getGame().getUser().getUsername());
							((Table)tableHeader.getCells().peek().getActor()).getCell(username).setActor(username);
							userCoins.setText(Integer.toString(getGame().getUser().getCoins()));
							((Table)tableHeader.getCells().first().getActor()).getCell(userCoins).setActor(userCoins);
							configDialog = new ConfigDialog(Text.CONFIG_DIALOG, getSkin(), getGame());
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

}
