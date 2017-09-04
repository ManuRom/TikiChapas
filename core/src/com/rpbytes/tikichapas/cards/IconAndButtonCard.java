package com.rpbytes.tikichapas.cards;


import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * Created by manoleichon on 26/01/17.
 */
public class IconAndButtonCard extends Card {

    public TextButton category;
    public Image icon;

    public IconAndButtonCard(float width, float height, NinePatchDrawable background, Image icon, TextButton category){
        super(width,height,background);
        this.category = category;
        this.add(icon).center().padBottom(30f);
        this.row();
        this.add(category).center();
    }

    public TextButton getCategory() {
        return category;
    }

    public void setCategory(TextButton category) {
        this.category = category;
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

}
