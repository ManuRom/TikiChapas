package com.rpbytes.tikichapas.listener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

/**
 * Created by manoleichon on 30/01/17.
 */
public class BeforeListener extends ClickListener {

    private Actor item;
    private Array items;
    private Table table;

    public BeforeListener(Actor item, Array items, Table table){
        this.item = item;
        this.items = items;
        this.table = table;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Cell<Actor> cell = table.getCell(item);
        for(int i=0;i<items.size;i++){
            if(items.get(i).equals(item)){
                if(i > 0){
                    cell.setActor((Actor) items.get(i-1));
                    setItem((Actor) items.get(i-1));
                    return;
                }else{
                    cell.setActor((Actor) items.get(items.size - 1));
                    setItem((Actor) items.get(items.size - 1));
                    return;
                }
            }
        }
    }

    public Actor getItem(){
        return this.item;
    }

    public void setItem(Actor item){
        this.item = item;
    }


}
