package com.rpbytes.tikichapas.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by manoleichon on 20/02/17.
 */
public class ShowMovementReview extends Action {

    Label label;
    Dialog dialog;

    public ShowMovementReview(String labelString, Dialog dialog, Skin skin){
        this.label = new Label(labelString,skin);
        this.dialog = dialog;
    }

    @Override
    public boolean act(float delta) {
        return false;
    }
}
