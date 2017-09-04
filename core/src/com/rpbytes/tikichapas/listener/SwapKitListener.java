package com.rpbytes.tikichapas.listener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.rpbytes.tikichapas.items.Team;

/**
 * Created by manoleichon on 15/05/17.
 */
public class SwapKitListener extends ClickListener {
    public Table table;
    private int kitId;

    public SwapKitListener(Table table){
        this.table = table;
        setKitId(1);
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Image kitHome = new Image(((Team)table.findActor("team")).getTextureHome());
        kitHome.setName("kit");
        Image kitAway = new Image(((Team)table.findActor("team")).getTextureVisitante());
        kitAway.setName("kit");
        if(getKitId()==1) {
            Gdx.app.log("kit Home", "entra");
            ((Image) table.findActor("kit")).setDrawable(kitAway.getDrawable());
            setKitId(2);
        }else{
            Gdx.app.log("kit Away", "entra");
            ((Image) table.findActor("kit")).setDrawable(kitHome.getDrawable());
            setKitId(1);
        }
    }

    public int getKitId() {
        return kitId;
    }

    public void setKitId(int kitId) {
        this.kitId = kitId;
    }


}
