package com.lines.itschool;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.math.Interpolation;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Марина on 05.02.2017.
 */

public class MenuScreen  implements Screen {
    public static final int fontsize=140;
    int H=Gdx.graphics.getHeight();
    int W=Gdx.graphics.getWidth();
    float wbtn=W/1.6f;
    float hbtn=wbtn/3.5f;
    boolean render=false;
     static Map stage;
    public static MyGame game;
    Component.ScrollBallPane pane;
    static BitmapFont mainfont;
    static Level level;
    TextButtonScale startbtn;
    TextButtonScale levelbtn;
    TextButton.TextButtonStyle buutonstyle;
    Component.WindowView window;
    Label.LabelStyle mainlstyle;
    Component.GenerateButton generateButton;
    Configuration config;
    Component.SelectBoxLabel selectmode;
    Component.BigLine line;




    public void setGame(MyGame game) {
        this.game = game;
    }

    public MenuScreen() {
    if (mainfont==null){
    mainfont=LoadingScreen.font;
       }
    }

    @Override
    public void show() {
        Configuration.loadscreen=false;
        render=false;
        Gdx.input.setCatchBackKey(false);
        Configuration.pauseanimation=false;
        level=config.getLevel();
        mainlstyle=new Label.LabelStyle(mainfont,Color.WHITE);
        Table maintable = new Table();
        pane = new Component.ScrollBallPane(maintable,new ScrollPane.ScrollPaneStyle(),level);
        ArrayList<Integer> simpletypes = new ArrayList<Integer>();
        for (Integer i: level.types.keySet()) {
        simpletypes.add(i);
        }
        Collections.sort(simpletypes);
        for (int i=0; i<simpletypes.size();i++) {
            MainBall next = new MainBall(simpletypes.get(i));
            next.show=true;
            next.setSize((Gdx.graphics.getHeight()/5.2f),Gdx.graphics.getHeight()/5.2f);
            maintable.add(next).padRight(Gdx.graphics.getWidth()/7.2f);
            if (level.usestypes.contains(next.getType())) {
                pane.setListenerOnBall(next);
            }
        }
        maintable.row();
        for (int i=0; i<simpletypes.size();i++){
        Label label = new Label("",mainlstyle);
        label.setColor(Color.DARK_GRAY);
        label.setFontScale(Gdx.graphics.getHeight()/21f/fontsize);
            if (level.usestypes.contains(simpletypes.get(i))){
            label.setText(level.strtype.get(simpletypes.get(i)).toString());
            } else {
                label.setText("LEVEL "+level.types.get(simpletypes.get(i)));
            }
            label.setAlignment(Align.center);
            maintable.add(label).width(Gdx.graphics.getHeight()/5f).pad(Gdx.graphics.getWidth()/60,0,Gdx.graphics.getWidth()/35,Gdx.graphics.getWidth()/7.2f);
        }
        maintable.center().pad(0,Gdx.graphics.getHeight()/25,0,Gdx.graphics.getHeight()/25);
        stage = new Map(new ScreenViewport(),false);
        Gdx.input.setInputProcessor(stage);

        makeMenu();
        stage.addActor(pane);

        levelbtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                 Line.setC(9);
                level = new Level();
                super.clicked(event, x, y);
            }
        });





        //Actions

    }


// создание основного интерфейса
    public void makeMenu(){
        generateButton = new Component.GenerateButton(level);
    Skin start = new Skin(new TextureAtlas(Gdx.files.internal("Interface/data_start_btn.txt")));
    start.add("play",Configuration.menumanager.get("Interface/play_btn.png",Texture.class));




       start.add("level",Configuration.menumanager.get("Interface/level_btn.png",Texture.class));
       buutonstyle=new TextButton.TextButtonStyle(start.getDrawable("level"),start.getDrawable("level"),start.getDrawable("level"),mainfont);
       levelbtn = new TextButtonScale("LEVEL "+level.NumLev,buutonstyle);
       levelbtn.setScaleXY(0,0.6f);
       levelbtn.getLabel().setFontScale(hbtn*0.6f/fontsize);
       levelbtn.setSize(wbtn,hbtn);
       levelbtn.setPosition(W/2-levelbtn.getWidth()/2,levelbtn.getHeight()/4);
       stage.addActor(levelbtn);

        pane.setY(levelbtn.getY()+levelbtn.getHeight()+Line.SIZE_LINE/2);
        pane.setHeight(pane.getPrefHeight());

        line = new Component.BigLine(true,false);
        line.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()/5.2f*1.1f);
        line.setPosition(0,pane.getY()+pane.getHeight()-(line.getHeight()+Gdx.graphics.getHeight()/5.2f)/2);

        stage.addActor(line);
        //s
        buutonstyle=new TextButton.TextButtonStyle(start.getDrawable("play"),start.getDrawable("play"),start.getDrawable("play"),mainfont);
        startbtn= new TextButtonScale("PLAY",buutonstyle);
        startbtn.getLabel().setFontScale(hbtn*0.7f/fontsize);
        startbtn.setScaleXY(0,0.7f);
        startbtn.setSize(wbtn,hbtn);
        startbtn.setPosition(W/2-startbtn.getWidth()/2,H-startbtn.getHeight()*1.25f);
        startbtn.setY(pane.getY()+pane.getHeight()+(H-pane.getY()-pane.getHeight())/2-(hbtn*3+Line.SIZE_LINE)/2+hbtn*2+Line.SIZE_LINE);
        Gdx.app.log(""+(H-pane.getY()-pane.getHeight())/2+" "+(hbtn*3+Line.SIZE_LINE)/2,"   "+(pane.getY()+pane.getHeight()));
        startbtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AnimmationHide();
                startbtn.addAction(Actions.sequence(Actions.delay(1.2f),Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        Configuration.loadscreen=true;
                        if (Configuration.MODE==1){
                            Configuration.loadGameManager();} else {
                            Configuration.loadInfinityManager();
                        }
                    }
                })));
                super.clicked(event, x, y);
            }
        });
        stage.addActor(startbtn);



        Label infinitymode = new Label("INFINITY MODE",mainlstyle);
        Label normalmode = new Label("NORMAL MODE",mainlstyle);
        ArrayList<Label> add= new ArrayList<Label>();
        add.add(infinitymode);
        add.add(normalmode);
        if (Configuration.MODE == 1) {
            selectmode= new Component.SelectBoxLabel(add,normalmode);
        } else {
            selectmode= new Component.SelectBoxLabel(add,infinitymode);
        }
        selectmode.setSize(wbtn,hbtn);
        selectmode.setPosition(W/2-selectmode.getWidth()/2,startbtn.getY()-Line.SIZE_LINE/2-selectmode.getHeight());

        stage.addActor(selectmode);
        stage.addActor(generateButton);

        generateButton.setSize(wbtn,hbtn);
        generateButton.setPosition(W/2-startbtn.getWidth()/2,selectmode.getY()-selectmode.getHeight()-Line.SIZE_LINE/2);





    }

  // смена режима игры
    public void ChangeMODE(boolean start){
    if (start && Configuration.MODE==2 && generateButton.getTouchable().equals(Touchable.enabled)){
        generateButton.addAction(Actions.parallel(Actions.sizeTo(1,1,0.5f,Interpolation.smooth),Actions.moveBy(wbtn/2,hbtn/2,0.5f,Interpolation.smooth)));
        generateButton.setTouchable(Touchable.disabled);
        Configuration.MODE=2;
    } else  if (!start){
        if (selectmode.activlabel.getText().toString().equals("NORMAL MODE"))  {
            Configuration.MODE=1;
            generateButton.setTouchable(Touchable.enabled);
            generateButton.addAction(Actions.parallel(Actions.sizeTo(wbtn,hbtn,0.5f,Interpolation.smooth),Actions.moveBy(-wbtn/2,-hbtn/2,0.5f,Interpolation.smooth)));
        } else {
            generateButton.addAction(Actions.parallel(Actions.sizeTo(1,1,0.5f,Interpolation.smooth),Actions.moveBy(wbtn/2,hbtn/2,0.5f,Interpolation.smooth)));
            generateButton.setTouchable(Touchable.disabled);
            Configuration.MODE=2;
        }} else{

    }
    }


  // открытие окна с описание шара
    public  void showWindowTypeBall(int type){


        int H = Gdx.graphics.getHeight();
        int W = Gdx.graphics.getWidth();
        int widthw=W;
        int heihgthw=(int)(H/1.7f); // размеры
        Skin a = new Skin();   // подгрузка
        a.add("exit",new Texture(Gdx.files.internal("Interface/exit_btn.png")));
        Skin okbtn = new Skin(new TextureAtlas("Interface/data_ok_btn.txt"));
        TextButton okey  = new TextButton("OK",new TextButton.TextButtonStyle(okbtn.getDrawable("up"),okbtn.getDrawable("down"),okbtn.getDrawable("down"),mainfont));
        okey.setSize(widthw/3.5f,widthw/3.5f);
        okey.getLabel().setFontScale(widthw/5.5f/fontsize);

        Label desription = new Label(level.loadDescriptionBall(type),new Label.LabelStyle(mainfont,Color.BLACK));
        desription.setWrap(true);
        desription.setFontScale(((float)(heihgthw)/12)/fontsize);
        desription.setColor(Color.DARK_GRAY);

        Label title = new Label(Level.strtype.get(type).toString(), new Label.LabelStyle(mainfont,Color.WHITE));
        title.setFontScale(((float)(heihgthw)/6)/fontsize);
        title.setColor(Color.DARK_GRAY);
        title.setWrap(true);
        title.setAlignment(Align.center);



        a.add("back",Configuration.menumanager.get("Interface/backgroud_win.png",Texture.class));

        window = new Component.WindowView("",new Window.WindowStyle(mainfont,Color.WHITE,a.getDrawable("back")));
        window.setSize(widthw,heihgthw);


        okey.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                window.close();
                stage.setFocusTouch();
                super.clicked(event, x, y);
            }
        });

        MainBall addingball = new MainBall(type);
        addingball.show=true;
        addingball.setSize(heihgthw/3,heihgthw/3);
        Table add = new Table();
        add.pad(widthw/8);


        add.add(title).center().pad(0,heihgthw/60,heihgthw/30,heihgthw/60).fillX();
        add.row();

        add.add(addingball).center();
        add.row();

        add.add(desription).padTop(heihgthw/25).fill().center().minWidth((int)(widthw/1.2));
        add.setPosition(0,H/2-window.getHeight()/2);
        add.row();

       add.add(okey).center().padTop(heihgthw/25).size(widthw/3.5f,widthw/3.5f).getActor().getLabelCell().center();
        add.center().columnDefaults(0).pad(0,widthw/30,widthw/30,0);
        ScrollPane scrolladd  = new ScrollPane(add,new ScrollPane.ScrollPaneStyle());
        scrolladd.setFadeScrollBars(false);
        scrolladd.setScrollingDisabled(true,false);
        window.add(scrolladd).expandX().fillX().center();
        window.pad(widthw/30);
        window.getTitleLabel().setVisible(false);
        window.setMovable(false);
        window.setY((H-heihgthw)/2);
        window.show();
        stage.addActor(window);
        stage.setFocusTouchOnWindow();

    }

  // анимацияя закрытия
    public void AnimmationHide(){
    float d1=0.6f;
    float d2=0.75f;
    generateButton.setTouchable(Touchable.disabled);
    selectmode.setTouchable(Touchable.disabled);
    levelbtn.setTouchable(Touchable.disabled);
        startbtn.setTouchable(Touchable.disabled);
        pane.setTouchable(Touchable.disabled);
    generateButton.addAction(Actions.parallel(Actions.sizeTo(0.1f,0.1f,d1,Interpolation.smooth),
    Actions.moveBy(generateButton.getWidth()/2,generateButton.getHeight()/2,d1, Interpolation.smooth)));
    selectmode.addAction(Actions.parallel(Actions.sizeTo(0.1f,0.1f,d1,Interpolation.smooth),
    Actions.moveBy(selectmode.getWidth()/2,selectmode.getHeight()/2,d1,Interpolation.smooth)));
        line.addAction(Actions.parallel(Actions.moveTo(Gdx.graphics.getWidth(),line.getY(),d1,Interpolation.pow2),
        Actions.sizeTo(0,line.getHeight(),d1,Interpolation.pow2)));
    pane.addAction(Actions.moveBy(Gdx.graphics.getWidth(),0,d1,Interpolation.pow2));
    if (Configuration.MODE==1){

        startbtn.addAction(Actions.parallel(Actions.sizeTo(0.1f,0.1f,d1,Interpolation.smooth),
        Actions.moveBy(startbtn.getWidth()/2,startbtn.getHeight()/2,d1,Interpolation.smooth)));
        levelbtn.addAction(Actions.sequence(Actions.delay(d1/2),
        Actions.moveTo(levelbtn.getX(),Gdx.graphics.getHeight()/2,d2,
        Interpolation.smooth)));
    }
    if (Configuration.MODE==2){
    levelbtn.addAction(Actions.parallel(Actions.sizeTo(0.1f,0.1f,d1,Interpolation.smooth),
    Actions.moveBy(levelbtn.getWidth()/2,levelbtn.getHeight()/2,d1,Interpolation.smooth)));
    startbtn.addAction(Actions.sequence(Actions.delay(d1/2),
    Actions.moveTo(startbtn.getX(),H/10,d2,Interpolation.smooth)));

    }





    }
    // анимация появления
    public void AnimationShow(){
        selectmode.setTouchable(Touchable.disabled);

        startbtn.addAction(Actions.moveTo(startbtn.getX(),startbtn.getY(),0.6f,Interpolation.smooth));
        startbtn.addAction(Actions.sizeTo(startbtn.getWidth(),startbtn.getHeight(),0.6f,Interpolation.smooth));
        startbtn.moveBy(startbtn.getWidth()/2,startbtn.getHeight()/2);
        startbtn.setSize(0.1f,0.1f);

        selectmode.addAction(Actions.sequence(Actions.delay(0.4f),
                Actions.parallel(Actions.moveTo(selectmode.getX(),selectmode.getY(),0.6f,Interpolation.smooth),Actions.sizeTo(selectmode.getWidth(),
                        selectmode.getHeight(),0.6f,Interpolation.smooth))));
        selectmode.moveBy(selectmode.getWidth()/2,selectmode.getHeight()/2);
        selectmode.setSize(0.1f,0.1f);

        generateButton.addAction(Actions.sequence(Actions.delay(0.8f),
                Actions.parallel(Actions.moveTo(generateButton.getX(),generateButton.getY(),0.6f,Interpolation.smooth),Actions.sizeTo(generateButton.getWidth(),
                        generateButton.getHeight(),0.6f,Interpolation.smooth))));
        generateButton.moveBy(generateButton.getWidth()/2,generateButton.getHeight()/2);
        generateButton.setSize(0.1f,0.1f);


        line.addAction(Actions.sequence(Actions.delay(1.2f),Actions.parallel(
                Actions.sizeTo(line.getWidth(),line.getHeight(),1.25f,Interpolation.pow2Out),
                Actions.moveTo(0,line.getY(),1.25f,Interpolation.pow2Out))));
        line.setX(Gdx.graphics.getWidth());
        line.setSize(0,line.getHeight());


        pane.addAction(Actions.sequence(Actions.delay(1.5f),Actions.parallel(Actions.moveTo(pane.getX(),pane.getY(),1.25f,Interpolation.pow2Out))));
        pane.setX(pane.getWidth());


        levelbtn.addAction(Actions.sequence(Actions.delay(1.75f),Actions.parallel(Actions.moveTo(levelbtn.getX(),levelbtn.getY(),0.6f,Interpolation.smooth)
                ,Actions.sizeTo(levelbtn.getWidth(),levelbtn.getHeight(),0.6f,Interpolation.smooth)),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        selectmode.setTouchable(Touchable.enabled);
                        ChangeMODE(true);
                    }
                })));
        levelbtn.moveBy(levelbtn.getWidth()/2,levelbtn.getHeight()/2);
        levelbtn.setSize(0.1f,0.1f);
        levelbtn.getLabel().addAction(Actions.sequence(Actions.delay(1.75f),Actions.parallel(Actions.moveTo(levelbtn.getX(),levelbtn.getY(),0.6f,Interpolation.smooth)
                ,Actions.sizeTo(levelbtn.getWidth(),levelbtn.getHeight(),0.6f,Interpolation.smooth))));
        levelbtn.getLabel().moveBy(levelbtn.getLabel().getWidth()/2,levelbtn.getLabel().getHeight()/2);
        levelbtn.getLabel().setSize(0.1f,0.1f);

    }




    @Override
    public void render(float delta) {
    if (!render){
        AnimationShow();
        render=true;
    }
    if (Configuration.loadscreen && Configuration.MODE==1 && Configuration.gamemanager.update()){
    game.showGame();}
    if (Configuration.loadscreen && Configuration.MODE==2 && Configuration.infinitymanager.update()){
                game.setScreen(new InfinityScreen());}

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(1,1,1,1);
        stage.act(delta);
        stage.draw();
        if (window!=null) {
        if (window.destroy) {
        window=null;
        stage.RemoveAllWindow();
        }
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
    Configuration.SaveLevel(level);

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    Configuration.SaveLevel(level);


    }

    @Override
    public void dispose() {
        Configuration.SaveLevel(level);

    }









}
