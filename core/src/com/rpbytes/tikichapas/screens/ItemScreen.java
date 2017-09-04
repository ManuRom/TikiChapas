package com.rpbytes.tikichapas.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.items.BallItem;
import com.rpbytes.tikichapas.items.Formation;
import com.rpbytes.tikichapas.items.Team;

import com.rpbytes.tikichapas.net.Url;

import org.json.JSONArray;
import org.json.JSONObject;


public class ItemScreen extends AbstractScreen {
	
	Label state;
	
	public ItemScreen(TikiChapasGame game){
		setGame(game);
		
	}
	
	public void show() {
		super.show();

		state = new Label("", getSkin());
		state.setPosition(getViewportWidth()/2, getViewportHeight()/2, Align.center);
		getMainStage().addActor(state);
		sendGetItemsRequest();
	}

	public void render(float delta) {
		super.render(delta);

/*		downloadItems();
		downloadItemsUser();*/
	}
	
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	public void sendGetItemsRequest(){
		Net.HttpRequest request = new Net.HttpRequest();
		request.setMethod(Net.HttpMethods.GET);
		request.setUrl(Url.BASE_URL+Url.ITEMS_ALL_PATH);
		request.setHeader("Authorization","Bearer "+Gdx.app.getPreferences("data").getString("token"));

		Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
			@Override
			public void handleHttpResponse(Net.HttpResponse httpResponse) {
				if(httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK){
					JSONObject response = new JSONObject(httpResponse.getResultAsString());
					JSONArray items = response.getJSONArray("items");
					Gdx.app.log("items", items.toString());
					JSONArray formations = response.getJSONArray("formations");
					for(int i=0;i<items.length();i++){

						JSONObject item = new JSONObject(items.get(i).toString());
						Gdx.app.log("item",item.toString());
						switch (item.getInt("type_id")){
							case 1:
								getGame().getItems().add(new Team(item));
								break;
							case 2:
								getGame().getItems().add(new BallItem(item));
								break;
							case 3:
								for(int j=0;j<formations.length();j++){
									JSONObject formation = new JSONObject(formations.get(j).toString());
									if(formation.getInt("item_id") == item.getInt("id")){
										getGame().getItems().add(new Formation(item,formation));
										break;
									}
								}
								break;
							case 4:
								break;
							default:
								break;
						}
					}
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							getGame().setScreen(new MainMenuScreen(getGame()));
						}
					});
				}
			}

			@Override
			public void failed(Throwable t) {
				Gdx.app.log("failed Items", t.getLocalizedMessage().toString());
			}

			@Override
			public void cancelled() {
				Gdx.app.log("Cancelled Items","cancelled");
			}
		});
	}
	
	/*public void downloadItems(){
		if(connection.ongoing){
			if(connection.finished){
				state.setText("Items Recibidos");

				dataItems = connection.jsonResponse.fromJson(Data.class, connection.response);
				
				//game.setItems(register.jsonResponse.readValue("items", Array.class, new JsonReader().parse()));
				connection.ongoing = false;
				connection.finished = false;
				for(int i=0;i< dataItems.items.size;i++){
					switch (dataItems.items.get(i).type_id) {
					case 1:
						System.out.println("Crear equipo");
						getGame().getItems().add(new Team(dataItems.items.get(i).id, dataItems.items.get(i).name, dataItems.items.get(i).price, true));
						break;
					case 2:
						System.out.println("Crear ball");
						getGame().getItems().add(new BallItem(dataItems.items.get(i).id, dataItems.items.get(i).name, dataItems.items.get(i).price, true));
						break;
					case 3:
						System.out.println("Crear Formacion");
						for(int j = 0;j < dataItems.formations.size;j++){
							if(dataItems.formations.get(j).item_id == dataItems.items.get(i).id){
								getGame().getItems().add(new Formation(dataItems.items.get(i).id, dataItems.items.get(i).name, dataItems.items.get(i).price, true, dataItems.formations.get(j)));
							}
						}
						break;
					default:
						break;
					}
				}
				connection2.sendGetRequest(url2,Gdx.app.getPreferences("data").getString("token"));
				connection2.ongoing = true;
			}else{
				state.setText("Descargando items");
			}
		}
	}
	public void downloadItemsUser(){
		if(connection2.ongoing){
			if(connection2.finished){
				dataItemsUser = connection2.jsonResponse.fromJson(Data.class, connection2.response);
				connection2.ongoing = false;
				connection2.finished = false;
				for(int j=0;j<getGame().getItems().size;j++){
					for(int i=0;i<dataItemsUser.items_user.size;i++){
						    if(dataItemsUser.items_user.get(i).item_id == getGame().getItems().get(j).getId()){
								getGame().getItems().get(j).setLocked(false);
							}
					}
				}
				//DEBUG MODE
				for(int i=0;i<getGame().getItems().size;i++){
					System.out.println(getGame().getItems().get(i).getName()+", "+getGame().getItems().get(i).isLocked());
				}
				//------------
				
				getGame().setScreen(new MainMenuScreen(getGame()));
				
			}else{
				state.setText("Actualizando items");
			}
		}
	}*/

}
