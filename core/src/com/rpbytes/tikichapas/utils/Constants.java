package com.rpbytes.tikichapas.utils;

import com.badlogic.gdx.Gdx;

public class Constants {

	//Conversion factors
	public static final float METERS_TO_PIXELS = 360f;
	public static final float PIXELS_TO_METERS = 1/METERS_TO_PIXELS;

	public static final int NUMERO_TOTAL_TAPONES = 5;

	//Finger size
	public static final float FINGER_WIDTH = 30; //In Pixels

	//Goals size
	public static final float GOAL_WIDTH = 0.1f; //In meters
	public static final float GOAL_HEIGHT = 0.4f; //In meters

	//Pitch size
	public static final float PITCH_WIDTH = 2f; //In meters
	public static final float PITCH_HEIGHT = 1.2f; //In meters

	//Border size
	public static final float BOX_WIDTH = 0.1f; //In meters
	public static final float CORNER_SIZE = 0.05f;

	//Border Stadium box2D attributes
	public static final float STADIUM_RESTITUTION = 1f;
	public static final float STADIUM_FRICTION = 1f;
	public static final float STADIUM_DENSITY =1f;

	//Ball size
	public static final float BALL_RADIUS=0.03f;

	//Caps sizes
	public static final float GK_CAP_RADIUS = 0.07f; //In meters
	public static final float NORMAL_CAP_RADIUS = 0.05f; //In meters
	public static final float HARD_CAP_RADIUS = 0.07f; //In meters
	public static final float SPEED_CAP_RADIUS = 0.04f; //In meters
	public static final float TECNHICAL_CAP_RADIUS = 0.05f; //In meters

	//Caps fixture attributes
	public static final float GK_CAP_DENSITY = 0.8f;
	public static final float NORMAL_CAP_DENSITY = 0.6f;
	public static final float HARD_CAP_DENSITY = 0.5f;
	public static final float SPEED_CAP_DENSITY = 0.4f;
	public static final float TECNHICAL_CAP_DENSITY = 0.6f;

	public static final float GK_CAP_FRICTION = 0.5f;
	public static final float NORMAL_CAP_FRICTION = 0.5f;
	public static final float HARD_CAP_FRICTION = 1f;
	public static final float SPEED_CAP_FRICTION = 0.8f;
	public static final float TECNHICAL_CAP_FRICTION = 1f;

	public static final float GK_CAP_RESTITUTION = 0.5f;
	public static final float NORMAL_CAP_RESTITUTION = 0.5f;
	public static final float HARD_CAP_RESTITUTION = 1f;
	public static final float SPEED_CAP_RESTITUTION = 1f;
	public static final float TECNHICAL_CAP_RESTITUTION = 1f;

	public static final float LOADING_LABEL_X = 30f;
	public static final float LOADING_LABEL_Y = 30f;

	//MATCH LABELS
	public static final float RIVAL_MOVEMENT_LABEL_X = -800f;
	public static final float RIVAL_MOVEMENT_LABEL_Y = Gdx.graphics.getHeight()+500f;

	//CARD DEFAULT SIZES
	public static final float CARD_WIDTH = 350f;
	public static final float CARD_HEIGHT = 375f;
	public static final float CARD_SETTING_HEIGHT = 315f;

	//ICON SIZES
	public static final float SETTING_ICON_SIZE = 70f;
	public static final float CARD_CONTENT_SIZE = 150f;

	//PADDING DEFAULT VALUES
	public static final float PAD = 15f;
	public static final float HEADER_BUTTONS_PAD = 15f;
	public static final float CARD_PAD = 30f;

	//INPUT
	public static final float INPUT_WIDTH = 350f;
}
