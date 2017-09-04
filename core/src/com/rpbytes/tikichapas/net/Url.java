package com.rpbytes.tikichapas.net;

/**
 * Created by Manuel Romero Pino on 26/05/16.
 */
public class Url {
    public static final String PROTOCOL_URL = "http://";

    //public static final String BASE_URL = PROTOCOL_URL+"192.168.1.26:8000/api/";
    //public static final String BASE_URL = PROTOCOL_URL+"192.168.1.15:8000/api/";
    public static final String BASE_URL = PROTOCOL_URL+"192.168.0.161:8000/api/";
    //public static final String BASE_URL = PROTOCOL_URL+"192.168.1.19:8000/api/";

    //public static final String BASE_URL = PROTOCOL_URL+"10.0.2.2:8000/api/";
    //public static final String BASE_URL = PROTOCOL_URL+"localhost:8000/api/";

    public static final String AUTH_PATH =  "auth";

    public static final String REGISTER_PATH =  "register";

    //User routes
    //$app->get('user/{user_id?}','UserController@getUser');
    public static final String USER_PATH =  "user/";
    //$app->put('user/{user_id?}','UserController@updateUser');
    //$app->delete('user/{user_id}','UserController@deleteUser');
    public static final String USER_MATCHES_PATH = "user/matches";
    //$app->get('user/matches', 'UserController@getMatches');
    //$app->get('user/items', 'UserController@getItems');
    public static final String USER_ITEMS_PATH =  "user/items";
    //$app->post('user/find/match' , 'UserController@findMatch');
    public static final String USER_FIND_MATCH_PATH = "user/find/match";

    //$app->post('user/buy/item/{item_id}', 'UserController@buyItem');
    public static final String USER_BUY_ITEM_PATH = "user/buy/item/";


    //Match routes
    public static final String FIND_MATCH_PATH = "match/find";
    //$app->post('match/{user_home_id}/{user_away_id?}','MatchController@createMatch');
    public static final String MATCH_PATH = "match/";

//    $app->get('match/{match_id}','MatchController@getMatch');
//    $app->put('match/{match_id}','MatchController@upateMatch');
//    $app->delete('match/{match_id}','MatchController@deleteMatch');

    //Movement routes
    public static final String MOVEMENT_PATH = "movement";

    //Item routes
//    $app->get('item/{item_id}','ItemController@getItem');
    public static final String ITEM_PATH = "item/";
//    $app->post('item','ItemController@createItem');
//    $app->put('item/{item_id}','ItemController@updateItem');
//    $app->delete('item/{item_id}','ItemController@deleteItem');
//
//    $app->get('items','ItemController@getAllItems');
    public static final String ITEMS_ALL_PATH = "items";
//
//    //Formation routes
    public static final String FORMATION_PATH = "formation/";
//    $app->get('formation/{formation_id}','FormationController@getFormation');
//    $app->post('formation','FormationController@createFormation');
//    $app->put('formation/{formation_id}','FormationController@updateFormation');
//    $app->delete('formation/{formation_id}','FormationController@deleteFormation');
//
//    $app->get('formations','FormationController@getFormations');
    public static final String FORMATIONS_PATH = "formations";

    public static final String NOTIFICATIONS_PATH = "notifications";
    public static final String NOTIFICATION_PATH = "notification/";
    public static final String RANKING_URL = "ranking";
}
