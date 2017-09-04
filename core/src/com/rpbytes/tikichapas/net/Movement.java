package com.rpbytes.tikichapas.net;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.rpbytes.tikichapas.net.CapPosition;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by manoleichon on 26/12/16.
 */
public class Movement {

    public int capIndex;
    public float forceX, forceY;
    public Array<CapPosition> bodiesInitialsPositions;
    public Array<CapPosition> bodiesFinalsPositions;
    public int state;



    public int getCapIndex() {
        return capIndex;
    }

    public void setCapIndex(int capIndex) {
        this.capIndex = capIndex;
    }

    public Array<CapPosition> getBodiesInitialsPositions() {
        return bodiesInitialsPositions;
    }

    public void setBodiesInitialsPositions(Array<CapPosition> bodiesInitialsPositions) {
        this.bodiesInitialsPositions = bodiesInitialsPositions;
    }

    public float getForceY() {
        return forceY;
    }

    public void setForceY(float forceY) {
        this.forceY = forceY;
    }

    public float getForceX() {
        return forceX;
    }

    public void setForceX(float forceX) {
        this.forceX = forceX;
    }


    public Movement(JsonValue json){
        capIndex = json.getInt("cap_touched");
        forceX = json.getFloat("force_x");
        forceY = json.getFloat("force_y");
        state = json.getInt("state");

        JsonValue capPositionsJson = json.get("bodies_positions");
        bodiesInitialsPositions = new Array<CapPosition>();
        bodiesFinalsPositions = new Array<CapPosition>();

        for(JsonValue cap: capPositionsJson.iterator()){
            if(cap.getBoolean("end_movement") == false)
                bodiesInitialsPositions.add(new CapPosition(cap.getFloat("x"),
                                                            cap.getFloat("y"),
                                                            cap.getFloat("rotation"),
                                                            cap.getInt("index"),
                                                            cap.getFloat("angular_velocity"),
                                                            cap.getBoolean("end_movement")));
            else
                bodiesFinalsPositions.add(new CapPosition(cap.getFloat("x"),
                                                            cap.getFloat("y"),
                                                            cap.getFloat("rotation"),
                                                            cap.getInt("index"),
                                                            cap.getFloat("angular_velocity"),
                                                            cap.getBoolean("end_movement")));
        }

    }

    public Movement(JSONObject movement){
        capIndex = movement.getInt("cap_touched");
        forceX = (float) movement.getDouble("force_x");
        forceY = (float) movement.getDouble("force_y");
        state = movement.getInt("state");

        JSONArray capPositions = movement.getJSONArray("bodies_positions");
        //JsonValue capPositionsJson = movement.get("bodies_positions");
        bodiesInitialsPositions = new Array<CapPosition>();
        bodiesFinalsPositions = new Array<CapPosition>();
        for(int i = 0; i < capPositions.length(); i++) {
            JSONObject cap = new JSONObject(capPositions.get(i).toString());
            if(cap.getBoolean("end_movement") == false)
                bodiesInitialsPositions.add(new CapPosition((float) cap.getDouble("x"),
                        (float) cap.getDouble("y"),
                        (float) cap.getDouble("rotation"),
                        cap.getInt("index"),
                        (float) cap.getDouble("angular_velocity"),
                        cap.getBoolean("end_movement")));
            else
                bodiesFinalsPositions.add(new CapPosition((float) cap.getDouble("x"),
                        (float) cap.getDouble("y"),
                        (float) cap.getDouble("rotation"),
                        cap.getInt("index"),
                        (float) cap.getDouble("angular_velocity"),
                        (cap.getBoolean("end_movement"))));
        }

    }



}
