package com.lines.itschool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.collision.Segment;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Марина on 19.05.2017.
 */

public class InfinityScreen implements Screen  {
Slider sliderball;
Table mainballs;
static TextButtonScale btnstart;
Level main;
Skin sliderskin;
Map stage;
TextButton.TextButtonStyle buttonstyle;
ArrayList<Integer> visibleballs;
ArrayList<MainBall> allballs;
Label select;
float H = Gdx.graphics.getHeight();
float W = Gdx.graphics.getWidth();
boolean render;



// экран отвещяющий за выбор шариков для бесконечного режива игры






    public void InfinityScreen(){




    }






    @Override
    public void show() {
    Configuration.loadscreen=false;
    render=false;
        Gdx.input.setCatchBackKey(true);
        main = Configuration.getLevel();
        sliderskin = new Skin();
       // Texture img =new Texture("Interface/knob.png");
      //  img.setFilter(Configuration.mainfilter,Configuration.mainfilter);
        sliderskin.add("knob",Configuration.infinitymanager.get("Interface/knob.png",Texture.class));
        //img = new Texture("Interface/slider.png");
        //img.setFilter(Configuration.mainfilter,Configuration.mainfilter);
        sliderskin.add("back",Configuration.infinitymanager.get("Interface/slider.png",Texture.class));
        Slider.SliderStyle sliderStyle= new Slider.SliderStyle(sliderskin.getDrawable("back"),sliderskin.getDrawable("knob"));
        sliderball = new Slider(2,main.usestypes.size(),1,false,sliderStyle);


        sliderball.setSize(W*0.75f,W*0.75f/8.73f);
        sliderball.getStyle().background.setMinWidth(sliderball.getWidth());
        sliderball.getStyle().background.setMinHeight(sliderball.getHeight());
        sliderball.setTouchable(Touchable.enabled);
        sliderball.setAnimateInterpolation(Interpolation.smoother);

        select = new Label("SELECT BALLS",new Label.LabelStyle(MenuScreen.mainfont, Color.DARK_GRAY));
        select.setFontScale(H/14/ MenuScreen.fontsize);
        select.setWrap(true);

        select.setWidth(W*0.8f);


        mainballs = new Table();

        Skin start = new Skin();
        start.add("start",Configuration.menumanager.get("Interface/play_btn.png",Texture.class));
        buttonstyle=new TextButton.TextButtonStyle(start.getDrawable("start"),start.getDrawable("start"),start.getDrawable("start"), MenuScreen.mainfont);

        stage  = new Map(new ScreenViewport(),false);
        stage.addActor(select);
        Gdx.input.setInputProcessor(stage);

        btnstart= new TextButtonScale("PLAY",buttonstyle);
        btnstart.getLabel().setFontScale(MyGame.mainscreen.hbtn*0.7f/ MenuScreen.fontsize);
        btnstart.setSize(W/1.6f,W/1.6f/3.5f);
        btnstart.setPosition(W/2-btnstart.getWidth()/2,H/10f);


     sliderball.setPosition(W/2-sliderball.getWidth()/2,H*0.75f);
     sliderball.setValue(sliderball.getMaxValue());
     sliderball.getStyle().knob.setMinHeight(sliderball.getHeight()*0.8f);
     sliderball.getStyle().knob.setMinWidth(sliderball.getHeight()*0.8f);
     sliderball.setAnimateDuration(0.4f);

     select.setPosition(W/2-select.getWidth()/2,sliderball.getY()+ Line.SIZE_LINE/2+sliderball.getStyle().background.getMinHeight());
     select.setAlignment(Align.center);



     allballs = new ArrayList<MainBall>();
     visibleballs=new ArrayList<Integer>();

     for (int i=0; i<main.usestypes.size();i++){
     MainBall m = new MainBall(main.usestypes.get(i));
     m.setSize(W/4,W/4);
     m.show=true;
     m.setTouchable(Touchable.disabled);
     m.setOrigin(m.getWidth()/2,m.getHeight()/2);
     allballs.add(m);
     visibleballs.add(main.usestypes.get(i));
     mainballs.add(m).size(W/4,W/4).pad(W/40).center();
     if ((i+1)%3==0){
         mainballs.row();
     }
     }
     mainballs.center();
     mainballs.setWidth(W);
     mainballs.setPosition(0,H*0.5f);
     mainballs.setTransform(false);




    stage.addActor(btnstart);
    stage.addActor(mainballs);
    stage.addActor(sliderball);
    }
    public void AnimationShow(){
        select.addAction(Actions.parallel(Actions.moveTo(select.getX(),select.getY(),0.6f,Interpolation.smooth)));
        select.setY(Gdx.graphics.getHeight());



        sliderball.addAction(Actions.sequence(Actions.delay(0.5f),
                Actions.parallel(Actions.moveTo(sliderball.getX(),sliderball.getY(),0.6f,Interpolation.smooth),
                        Actions.sizeTo(sliderball.getWidth(),sliderball.getHeight(),0.6f,Interpolation.smooth)),
                        Actions.delay(0.4f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                               addSliderListener();
                            }
                        })));
        sliderball.moveBy(sliderball.getWidth()/2,sliderball.getHeight()/2);
        sliderball.setSize(0,0);

        for (int i=0; i<allballs.size();i++) {
            final MainBall m =allballs.get(i);
            m.setVisible(false);
            allballs.get(i).getActions().clear();


            m.addAction(Actions.sequence(Actions.delay(0.8f+i*0.2f),Actions.parallel(Actions.sizeTo(0,0),Actions.moveBy(W/8,W/8)),
            Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            m.setVisible(true);
                        }
                    }),
                    Actions.parallel(Actions.moveBy(-W/8, -W/8, 0.5f, Interpolation.smooth),
                            Actions.sizeTo(W/4,W/4,0.5f,Interpolation.smooth))));


        }
    }

    public void AnimationClose(){
        select.addAction(Actions.parallel(Actions.moveTo(select.getX(),Gdx.graphics.getHeight(),0.6f,Interpolation.smooth)));

        sliderball.addAction(Actions.sequence(Actions.delay(0.05f),
                Actions.parallel(Actions.moveBy(sliderball.getWidth()/2,Gdx.graphics.getHeight()*1.1f,0.6f,Interpolation.smooth),
                        Actions.sizeTo(0,0,0.6f,Interpolation.smooth)),Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        sliderball.setVisible(false);
                    }
                })));

        for (int i=0; i<allballs.size();i++) {
            MainBall m =allballs.get(i);
            m.setVisible(true);
            allballs.get(i).getActions().clear();
            m.addAction(Actions.sequence(Actions.delay(i*0.1f),
                    Actions.parallel(Actions.moveBy(W/8, W/8, 0.5f, Interpolation.smooth),
                            Actions.sizeTo(0,0,0.5f,Interpolation.smooth))));


        }

    }


    private void addSliderListener(){
    sliderball.addListener(new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          Slider s=(Slider)actor;
          for (int i=2; i<s.getValue();i++){
              if (!visibleballs.contains(i)){
              MainBall m = allballs.get(i);
                  m.setPosition(mainballs.getCell(m).getActorX()+W/8,mainballs.getCell(m).getActorY()+W/8);
              m.setSize(0,0);
              allballs.get(i).setVisible(true);
              allballs.get(i).getActions().clear();
              allballs.get(i).addAction(Actions.parallel(Actions.sizeTo(W/4,W/4,0.5f,Interpolation.smooth),Actions.moveBy(m.getWidth()/2-W/8,m.getWidth()-W/8,0.5f,Interpolation.smooth)));
              visibleballs.add(i);
              Collections.sort(visibleballs);
              }
          }
          for (int i=(int)(s.getValue());i<allballs.size();i++){
          final  int k=i;
          if (visibleballs.contains(allballs.get(i).getType())){
              MainBall m = allballs.get(i);
             m.setPosition(mainballs.getCell(m).getActorX(),mainballs.getCell(m).getActorY());
             m.setSize(W/4,W/4);
              visibleballs.remove(new Integer(i));
              allballs.get(i).getActions().clear();
              allballs.get(i).addAction(Actions.sequence(Actions.parallel(Actions.sizeTo(0,0,0.5f,Interpolation.smooth),
              Actions.moveBy(m.getWidth()/2,m.getWidth()/2,0.5f,Interpolation.smooth)),Actions.run(new Runnable() {
                  @Override
                  public void run() {
                      allballs.get(k).setVisible(false);
                  }
              })));}
          }

        }
    });
        btnstart.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AnimationClose();
                btnstart.getLabel().addAction(Actions.sequence(Actions.parallel(Actions.moveBy(0,btnstart.getHeight()/2,0.2f,Interpolation.smooth),
                Actions.alpha(0,0.2f,Interpolation.smooth)),Actions.run(new Runnable() {
                    @Override
                    public void run() {
                    btnstart.getLabel().setAlignment(Align.center);
                        btnstart.setText("INFINITY MODE");
                        Configuration.OptimalScale(btnstart.getLabel(),btnstart.getWidth()*0.8f,btnstart.getHeight()*0.6f);
                    }
                }),Actions.moveBy(0,-btnstart.getHeight()/2),Actions.parallel(Actions.moveBy(0,btnstart.getHeight()/2,0.2f,Interpolation.smooth),Actions.alpha(1,0.2f,Interpolation.smooth))));
            btnstart.addAction(Actions.sequence(Actions.delay(0.5f),
            Actions.moveTo(btnstart.getX(),Gdx.graphics.getHeight()/2,0.4f,Interpolation.smooth),Actions.run(new Runnable() {
                @Override
                public void run() {
                    Configuration.typesinfinity=new ArrayList<Integer>();
                    for (int i=0; i<visibleballs.size();i++){
                        Configuration.typesinfinity.add(visibleballs.get(i));}
                    Configuration.loadscreen=true;
                    Configuration.loadGameManager();
                }
            })));

                super.clicked(event, x, y);
            }
        });


    }

    @Override
    public void render(float delta) {
    if (Configuration.loadscreen && Configuration.gamemanager.update()){
        MenuScreen.game.showGame();
    }
    if (!render){
        render=true;
        AnimationShow();
    }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(1,1,1,1);
        stage.act(delta);
        stage.draw();


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
