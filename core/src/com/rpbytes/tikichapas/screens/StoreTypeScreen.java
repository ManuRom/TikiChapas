package com.rpbytes.tikichapas.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.cards.BuyItemCard;
import com.rpbytes.tikichapas.utils.Constants;
import com.rpbytes.tikichapas.items.Item;
import com.rpbytes.tikichapas.net.Url;
import com.rpbytes.tikichapas.utils.SkinKeys;
import com.rpbytes.tikichapas.utils.Text;

import java.util.Iterator;

/**
 * Created by manoleichon on 14/07/16.
 */
public class StoreTypeScreen extends BaseMenuScreen {

    public Class type;
    public Array<BuyItemCard> buyItemCards;
    public Dialog loading, error;
    public boolean showLoadingDialog=true;

    public StoreTypeScreen(TikiChapasGame game, Class type){
        setGame(game);
        this.type = type;
    }

    public void show(){
        super.show();
        addedRequestsToItems();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        //connectionsListener();
    }

    @Override
    public void initScrollTable() {
        buyItemCards = new Array<BuyItemCard>();
        loading = new Dialog(Text.LOADING_TITLE,getSkin());
        error = new Dialog(Text.ERROR_TITLE,getSkin());
        error.text(new Label(Text.NOT_ENOUGH_MONEY,getSkin(),SkinKeys.LABEL_GREY));
        error.button(new TextButton(Text.BTN_ACCEPT, getSkin()));
        error.getBackground().setMinWidth(300f);

        boolean isFirstItem = true;
        for(Iterator<Item> i = getGame().getItems().iterator(); i.hasNext();){
            Item item = i.next();
            if(item.getClass().equals(type)){
                BuyItemCard card = new BuyItemCard(item,getMainStage(),getGame());
                buyItemCards.add(card);
                if(isFirstItem) {
                    tableScroll.add(card).size(Constants.CARD_WIDTH, Constants.CARD_HEIGHT).padLeft(Constants.CARD_PAD * 2).padRight(Constants.CARD_PAD);
                    isFirstItem = false;
                }else {
                    if(i.hasNext())
                        tableScroll.add(card).size(Constants.CARD_WIDTH, Constants.CARD_HEIGHT).padLeft(Constants.CARD_PAD).padRight(Constants.CARD_PAD);
                    else
                        tableScroll.add(card).size(Constants.CARD_WIDTH, Constants.CARD_HEIGHT).padLeft(Constants.CARD_PAD).padRight(Constants.CARD_PAD*2);
                }
            }
        }
    }

    @Override
    public void initFooterTable() {
        prevScreen = new ImageButton(getSkin(), SkinKeys.BACK);
        Table tableFooterLeft = new Table();

        tableFooterLeft.add(prevScreen).left().padLeft(15f).expand();

        tableFooter.setWidth(getViewportWidth());
        tableFooter.add(tableFooterLeft).padBottom(15f).width((getViewportWidth()));

        prevScreen.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new StoreScreen(getGame()));
            }
        });
    }

    private void addedRequestsToItems(){
        for(Cell cell :tableScroll.getCells()){
            if(cell.getActor() instanceof BuyItemCard){
                final BuyItemCard card = (BuyItemCard) cell.getActor();
                card.getBuyDialog().getButtonTable().getCells().peek().getActor().addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Net.HttpRequest request = new Net.HttpRequest();
                        request.setMethod(Net.HttpMethods.POST);
                        request.setUrl(Url.BASE_URL+Url.USER_BUY_ITEM_PATH+ card.getItem().getId());
                        request.setHeader("Authorization","Bearer "+ Gdx.app.getPreferences("data").getString("token"));

                        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
                            @Override
                            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                                if(httpResponse.getStatus().getStatusCode() == HttpStatus.SC_CREATED){
                                    Gdx.app.postRunnable(new Runnable() {
                                        @Override
                                        public void run() {
                                            getGame().getUser().setCoins(getGame().getUser().getCoins()-card.getItem().getPrice());
                                            for(Item item: getGame().getItems()) {
                                                if(item.getId()==card.getItem().getId())
                                                    item.setLocked(false);
                                            }
                                            getGame().setScreen(new StoreTypeScreen(getGame(),type));
                                        }
                                    });

                                }
                                if (httpResponse.getStatus().getStatusCode() == HttpStatus.SC_LOCKED){
                                    error.show(getMainStage());
                                }
                            }

                            @Override
                            public void failed(Throwable t) {
                                Gdx.app.log("failed buy item "+card.getItem().getName(),t.getLocalizedMessage());
                            }

                            @Override
                            public void cancelled() {

                            }
                        });
                    }
                });
            }
        }
    }

}
