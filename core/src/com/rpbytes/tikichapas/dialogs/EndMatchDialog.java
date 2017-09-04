package com.rpbytes.tikichapas.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.rpbytes.tikichapas.utils.Constants;
import com.rpbytes.tikichapas.match.CenterScoreboard;
import com.rpbytes.tikichapas.utils.Text;

/**
 * Created by manoleichon on 13/03/17.
 */
public class EndMatchDialog extends Table {

    public static final int GOAL_COINS = 20;
    public static final int WIN_COINS = 250;
    public static final int LOSE_COINS = 0;
    public static final int WIDTH = 600;
    public static final int HEIGHT_ONLINE = 500;
    public static final int HEIGHT_OFFLINE = 410;



    public Label goalsLabel , winLabel , loseLabel , totalLabel, goalsValueLabel, winValueLabel, loseValueLabel ,totalValueLabel;
    public int goals, total;
    public TextButton mainMenu;
    public CenterScoreboard centerScoreboard;

    public EndMatchDialog(Skin skin, CenterScoreboard centerScoreboard, int goals, boolean win, boolean online){
        super();
        setSkin(skin);
        setBackground(new NinePatchDrawable(getSkin().getPatch("grey_panel")));
        if(online)
            setSize(WIDTH,HEIGHT_ONLINE);
        else
            setSize(WIDTH,HEIGHT_OFFLINE);
        this.centerScoreboard = centerScoreboard;


        mainMenu = new TextButton("Volver al menu principal", getSkin());
        if(online) {
            goalsLabel= new Label("Goles",getSkin());
            winLabel = new Label("Partido ganado", getSkin());
            loseLabel = new Label("Partido perdido", getSkin());
            totalLabel = new Label("Total", getSkin());

            this.goals = goals * GOAL_COINS;
            goalsValueLabel = new Label(Integer.toString(this.goals), getSkin());
            add(centerScoreboard).colspan(2);
            row();
            add(goalsLabel);
            add(goalsValueLabel);
            row();
            if (win) {
                total = this.goals + this.WIN_COINS;
                totalValueLabel = new Label(Integer.toString(total), getSkin());
                winValueLabel = new Label(Integer.toString(WIN_COINS), getSkin());
                add(winLabel);
                add(winValueLabel);
            } else {
                total = this.goals + this.LOSE_COINS;
                totalValueLabel = new Label(Integer.toString(total), getSkin());
                loseValueLabel = new Label(Integer.toString(LOSE_COINS), getSkin());
                add(loseLabel);
                add(loseValueLabel);
            }
            row();
            add(totalLabel);
            add(totalValueLabel);
            row();
            add(mainMenu).colspan(2);
        }else{
            Label endMatchLabel = new Label(Text.END_MATCH, getSkin());
            add(centerScoreboard).pad(Constants.PAD);
            row();
            add(endMatchLabel).pad(Constants.PAD);
            row();
            add(mainMenu).pad(Constants.PAD);
        }
        setVisible(false);
    }

}
