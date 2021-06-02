package com.lines.itschool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Json;
import com.google.gson.Gson;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayList;

/**
 * Created by Dmitriy on 17.05.2017.
 */

public class Configuration {
    public static int MODE=1;
    public static ArrayList<Integer> typesinfinity;
    public static boolean pauseanimation=false;
    public static  int record;
    public static float k=1;
    public static Texture.TextureFilter mainfilter = Texture.TextureFilter.Linear;
    public static AssetManager menumanager;
    public static AssetManager gamemanager;
    public static AssetManager infinitymanager;
    public static AssetManager mainmanager;
    public static boolean loadscreen=false;
  //  public static int
    // 1- normal
    // 2 - infinity
    //даннный класс отвечает за хранения информации, которая не связана с уровнем игры,
    // и за загрузку текстур в другом потоке

  // возвращает рекорд бесконечного режима игры
    public static int getRecordInfinity(){
    Preferences preferences = Gdx.app.getPreferences("MAIN");
   record = preferences.getInteger("record");
     return record;
    }
    // устанавливает рекорд
    public static void setRecord(int r){
    Preferences preferences = Gdx.app.getPreferences("MAIN");
    preferences.putInteger("record",r);
    record=r;
    preferences.flush();
    }

    public Configuration() {
    }

    //метод который масштабирует текст под выделенную область (бессмыслено наследоваться от Label ради одного метода)
    public static void OptimalScale(Label l,float w,float h){
        float k1=w*0.8f/l.getText().toString().length()*1.5f/MenuScreen.fontsize;
        float k2 =h*0.8f/MenuScreen.fontsize;

        if (l.getHeight()*k1<h*0.8f && l.getWidth()*k2<w*0.9f ){
            l.setFontScale(h*0.8f/MenuScreen.fontsize);
        } else {
            l.setFontScale(k1);}
    }

 // сохранение уровня
    public static void SaveLevel(Level main){
    Preferences preferences = Gdx.app.getPreferences("MAIN");
        Json a = new Json();
        Level.JSONLevel jlev = new Level.JSONLevel(main);
        preferences.putString("Level",a.toJson(jlev, Level.JSONLevel.class));
        preferences.flush();
    }
    //получение уровня или создание если его еще нет (при первом запуске)
    public static Level getLevel(){
    Level level;
    Gson m = new Gson();
    Level.JSONLevel jlev;
        Preferences preferences = Gdx.app.getPreferences("MAIN");
      // preferences.putBoolean("Start",false);
        String jsons =preferences.getString("Level");
        if (preferences.getBoolean("Start")){ // инциализация уровня
            jlev =m.fromJson(jsons,Level.JSONLevel.class);
            level= new Level(jlev);
        } else {
            preferences.putBoolean("Start",true);
            m=new Gson();
            level=new Level();
            jlev=new Level.JSONLevel(level);
            preferences.putString("Level",m.toJson(jlev,Level.JSONLevel.class));
        }
        preferences.flush();
        return  level;
    }

    //загрузка тектур в различные менеджеры
    public static  void loadMenuManager(){
    menumanager= new AssetManager();
        TextureLoader.TextureParameter tparam = new TextureLoader.TextureParameter();
        tparam.minFilter= Texture.TextureFilter.Linear;
        tparam.magFilter= Texture.TextureFilter.Linear;
        menumanager.load("Interface/arrow.png",Texture.class,tparam);
        menumanager.load("Interface/back_generate.png",Texture.class,tparam);
        menumanager.load("Interface/backgroud_win.png",Texture.class,tparam);
        menumanager.load("Interface/dark_btn.png",Texture.class,tparam);
        menumanager.load("Interface/level_btn.png",Texture.class,tparam);
        menumanager.load("Interface/play_btn.png",Texture.class,tparam);

    }

    public static void loadGameManager(){
    gamemanager = new AssetManager();
        TextureLoader.TextureParameter tparam = new TextureLoader.TextureParameter();
        tparam.minFilter= Texture.TextureFilter.Linear;
        tparam.magFilter= Texture.TextureFilter.Linear;
        gamemanager.load("PLACE_BALL.png",Texture.class,tparam);
        gamemanager.load("Interface/backgroud_win.png",Texture.class,tparam);
        gamemanager.load("Interface/background_skills.png",Texture.class,tparam);
        gamemanager.load("Interface/ball.png",Texture.class,tparam);
        gamemanager.load("Interface/clock_skill.png",Texture.class,tparam);
        gamemanager.load("Interface/destroy_skill.png",Texture.class,tparam);
        gamemanager.load("Interface/pause_btn.png",Texture.class,tparam);
        gamemanager.load("Interface/pro.png",Texture.class,tparam);
        gamemanager.load("Interface/up_skill.png",Texture.class,tparam);
        gamemanager.load("Interface/back_pro.png",Texture.class,tparam);
        gamemanager.load("BACK_F_BALL.png",Texture.class,tparam);
        gamemanager.load("BACK_W_BALL.png",Texture.class,tparam);
        gamemanager.load("MZ_BALL.png",Texture.class,tparam);
        gamemanager.load("WDZ_BALL.png",Texture.class,tparam);
        gamemanager.load("Interface/blue_btn.png",Texture.class,tparam);
        gamemanager.load("Interface/red_btn.png",Texture.class,tparam);
    }

    public static void loadInfinityManager(){
    infinitymanager = new AssetManager();
        TextureLoader.TextureParameter tparam = new TextureLoader.TextureParameter();
        tparam.minFilter= Texture.TextureFilter.Linear;
        tparam.magFilter= Texture.TextureFilter.Linear;
        infinitymanager.load("Interface/knob.png",Texture.class,tparam);
        infinitymanager.load("Interface/slider.png",Texture.class,tparam);
    }

    public static  void loadMainManager(){
    mainmanager = new AssetManager(new InternalFileHandleResolver());
        TextureLoader.TextureParameter tparam = new TextureLoader.TextureParameter();
        tparam.minFilter= Texture.TextureFilter.Linear;
        tparam.magFilter= Texture.TextureFilter.Linear;
        FreetypeFontLoader.FreeTypeFontLoaderParameter myMainFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
       // myMainFont.fontFileName = "Roboto.ttf";

        //mainmanager.load("Fonts/Roboto.ttf", Fr.class, myMainFont);
        //Balls
        mainmanager.load("A_BALL.png",Texture.class,tparam);
        mainmanager.load("D_BALL.png",Texture.class,tparam);
        mainmanager.load("F_BALL.png",Texture.class,tparam);
        mainmanager.load("H_LINE.png",Texture.class,tparam);
        mainmanager.load("M_BALL.png",Texture.class,tparam);
        mainmanager.load("MG_BALL.png",Texture.class,tparam);
        mainmanager.load("R_LINE.png",Texture.class,tparam);
        mainmanager.load("W_BALL.png",Texture.class,tparam);
        mainmanager.load("WD_BALL.png",Texture.class,tparam);
        mainmanager.load("back.jpg",Texture.class,tparam);

    }


}
