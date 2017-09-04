package com.rpbytes.tikichapas.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.rpbytes.tikichapas.TikiChapasGame;
import com.rpbytes.tikichapas.items.Item;

/**
 * Created by manoleichon on 14/07/16.
 */
public class BuyItemCard extends Card {

    private TextButton button;
    private Item item;
    private Dialog buyDialog;

    public BuyItemCard(Item item, final Stage stage, final TikiChapasGame game){
        setSkin(new Skin(Gdx.files.internal("skin/uiskin.json")));
        setItem(item);
        setBackground(new NinePatchDrawable(getSkin().getPatch("grey_panel")));

        this.add(new Label(getItem().getName(), getSkin(),"label")).padBottom(20f);
        this.row();
        this.add(getItem().getMainImage()).center().padBottom(30f).size(150, 150);
        this.row();

        if(item.isLocked()) {
            setButton(new TextButton("COMPRAR", getSkin()));
            this.add(getButton()).pad(10);
            getButton().addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    getBuyDialog().show(stage);
                }
            });
        }else
            this.add(new Label("COMPRADO",getSkin(),"label_grey")).padBottom(45f).padTop(30f);

        buyDialog = new Dialog("¿Estas seguro de comprarlo?",getSkin());
        getBuyDialog().button(new TextButton("Cancelar",getSkin(), "default_blue"));
        getBuyDialog().button(new TextButton("Comprar",getSkin()));
        getBuyDialog().text(new Label("Perderas "+getItem().getPrice()+" Monedas",getSkin(),"default"));
        getBuyDialog().getBackground().setMinWidth(400f);
        getBuyDialog().getBackground().setMinHeight(400f);

    }


    public TextButton getButton() {
        return button;
    }

    public void setButton(TextButton button) {
        this.button = button;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Dialog getBuyDialog(){
        return buyDialog;
    }

}
