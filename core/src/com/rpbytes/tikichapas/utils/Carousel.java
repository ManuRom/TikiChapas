package com.rpbytes.tikichapas.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.rpbytes.tikichapas.items.Formation;

/**
 * Created by manoleichon on 1/07/17.
 */
public class Carousel<T>{
    private Array<T> items;
    private Cell cell;
    private Skin skin;

    public Carousel(Array<T> items,Cell cell, Skin skin){
        this.items = items;
        this.cell = cell;
        this.skin = skin;
    }


    public void next(){
        for (int i=0;i<getItems().size;i++) {
            if(getItems().get(i) instanceof Formation) {
                if (((Formation) getItems().get(i)).getName().equals(((Label) ((Table) getCell().getActor()).getCells().first().getActor()).getText().toString())) {
                    if (i == getItems().size - 1) {
                        ((Table) getCell().getActor()).getCells().first().setActor(new Label(((Formation)getItems().get(0)).getName(), getSkin(), SkinKeys.LABEL));
                        ((Table) getCell().getActor()).getCells().peek().setActor(((Formation) getItems().get(0)).getMainImage());
                        break;
                    } else {
                        ((Table) getCell().getActor()).getCells().first().setActor(new Label(((Formation)getItems().get(i + 1)).getName(), getSkin(), SkinKeys.LABEL));
                        ((Table) getCell().getActor()).getCells().peek().setActor(((Formation) getItems().get(i + 1)).getMainImage());
                        break;
                    }
                }
            }
        }
    }

    public void before(){
        Gdx.app.log("Items size", Integer.toString(getItems().size));
        for (int i=0;i<getItems().size;i++) {
            if(getItems().get(i) instanceof Formation) {
                Gdx.app.log("Iterator name", ((Formation) getItems().get(i)).getName());
                Gdx.app.log("Cell name", ((Label) ((Table) getCell().getActor()).getCells().first().getActor()).getText().toString());
                if (((Formation) getItems().get(i)).getName().equals(((Label) ((Table) getCell().getActor()).getCells().first().getActor()).getText().toString())) {
                    if (i == 0) {
                        Gdx.app.log("Before", ((Formation)getItems().get(getItems().size - 1)).getName());
                        ((Table) getCell().getActor()).getCells().first().setActor(new Label(((Formation)getItems().get(getItems().size - 1)).getName(), getSkin(), SkinKeys.LABEL));
                        ((Table) getCell().getActor()).getCells().peek().setActor(((Formation)getItems().get(getItems().size - 1)).getMainImage());
                        break;
                    } else {
                        Gdx.app.log("Before", ((Formation)getItems().get(i - 1)).getName());
                        ((Table) getCell().getActor()).getCells().first().setActor(new Label(((Formation)getItems().get(i - 1)).getName(), getSkin(), SkinKeys.LABEL));
                        ((Table) getCell().getActor()).getCells().peek().setActor(((Formation)getItems().get(i - 1)).getMainImage());
                        break;
                    }
                }
            }
        }
    }

    public T getCurrentActor(){
        T formation = items.first();
        for (T item: items) {
            if(item instanceof Formation){
                if(((Formation)item).getMainImage() == ((Table) getCell().getActor()).getCells().peek().getActor()){
                    formation = item;
                    Gdx.app.log("Current Formation selected",Integer.toString(((Formation) formation).getId()));
                    return formation;
                }
            }
        }
        return formation;
    }

    public void setCurrentActor(Actor actor){
        cell.setActor(actor);
    }

    public Array<T> getItems(){
        return items;
    }

    public Cell getCell(){
        return cell;
    }

    public Skin getSkin(){
        return this.skin;
    }
}