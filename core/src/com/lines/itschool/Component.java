package com.lines.itschool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;


/**
 * Created by Марина on 07.04.2017.
 */

public abstract class Component {

// этот класс содержит внутренние классы различных компонентов игры и интерфейса

    public static final class ScrollBallPane extends ScrollPane {

        Level level;
        Table table;


        public ScrollBallPane(Actor widget, ScrollPaneStyle style, Level level) {
            super(widget, style);
            table = (Table) widget;
            this.level = level;
            this.setWidth(Gdx.graphics.getWidth());
            this.setPosition(0, Gdx.graphics.getWidth() / 4.7f);
            this.setFadeScrollBars(false);
            this.setHeight(Gdx.graphics.getWidth() / 2.4f);
        }

        public void setListenerOnBall(final MainBall mainball) {
            mainball.addListener(new ClickListener() {

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    MyGame.mainscreen.showWindowTypeBall(mainball.getType());
                    super.clicked(event, x, y);
                }
            });

        }


        @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
        }
    }

    public static final class WindowView extends Window {
        boolean destroy;
        int typec; //комнадные коды. 1 - перейти меню 2 - следующий уровень   3 - заново 4 - продолжить


        public WindowView(String title, WindowStyle style) {
            super(title, style);
            this.setKeepWithinStage(false);
        }




        public void show() {
        destroy=false;
          float y=getY();
         setY(-Gdx.graphics.getHeight()*0.75f);
            MoveToAction show  = new MoveToAction();
           show.setPosition(getX(),y);
            show.setDuration(0.7f);
            show.setInterpolation(Interpolation.smooth2);
            this.addAction(show);
        }

        public void close(){
            MoveToAction close  = new MoveToAction();
            close.setPosition(getX(),-Gdx.graphics.getHeight()*0.75f);
            close.setDuration(0.7f);
            close.setInterpolation(Interpolation.smooth2);
           RunnableAction r = new RunnableAction(){
               @Override
               public void run() {
               Action();
               }
           };
            this.addAction(Actions.sequence(close,r));
        }


        public void Action(){
        destroy=true;
            if (MenuScreen.game.getScreen().equals(MyGame.gamescreen)){
                MyGame.gamescreen.stage.setFocusTouch();
            } else {
            MyGame.mainscreen.stage.setFocusTouch();}
        if (typec==1) {
            if (MyGame.gamescreen.nextlevel){
                MyGame.gamescreen.nextlevel=false;
                MyGame.gamescreen.main.nextLevel();
            }
            MyGame.gamescreen.SaveLevel();
            MyGame.mainscreen=new MenuScreen();
            MenuScreen.game.setScreen(MyGame.mainscreen);

        }
        if (typec==2) {
            MyGame.gamescreen.nextlevel=false;
            MyGame.gamescreen.main.nextLevel();
            MyGame.gamescreen.SaveLevel();
            MenuScreen.game.showGame();
        }
        if (typec==3) {
            MyGame.gamescreen.SaveLevel();
            MenuScreen.game.showGame();

            }
        if (typec==4) {
            GameScreen.stage.RemoveAllWindow();
            Configuration.pauseanimation=false;

        }
        }
    }

    public static final class ProgressBar extends Map.MyActor {
    float w;
    float h;
    int k;
    int count=0;
    Texture background;
    Texture progress;
    Texture ball;
    boolean animcount;
    float speed;
    float a;
    float l;
    float position;
    public ProgressBar(int count){
    background=Configuration.gamemanager.get("Interface/back_pro.png",Texture.class);
    progress=Configuration.gamemanager.get("Interface/pro.png",Texture.class);
    ball= Configuration.gamemanager.get("Interface/ball.png",Texture.class);
    this.w=Gdx.graphics.getWidth()*0.85f;
    this.h=w/31.4f;
    l=(w-(w*1.0125f-w))/count;
    speed=l/400;
    a=l/245;
    setPosition(Gdx.graphics.getWidth()/2-w/2,Gdx.graphics.getHeight()*0.89f);
    this.k=count;
    position=0;
    }
    public void AnimCount() {
    float s=0;
        if(count==k) {
            s=-h*0.75f;
        }
        if (position + speed <= l * count+s) {
            position += speed;
            speed += a;
        } else {
            position = l * count+s;
            speed = l /400;
            animcount = false;
        }

    }

    public void Step(){
    animcount=true;
    if(count!=k){
    count+=1;}
    }

        @Override
        public void draw(Batch batch, float parentAlpha) {
        if (animcount) {
        AnimCount();
        }
             batch.draw(background,getX(),getY(),w,h);
             batch.draw(progress,getX()+(w*1.0125f-w)/2+h*0.75f/2,getY()+h*0.25f,position,h*0.75f);
             batch.draw(ball,getX()+(w*1.0125f-w)/2,getY()+h*0.25f,h*0.75f,h*0.75f);
             batch.draw(ball,getX()+position+(w*1.0125f-w)/2,getY()+h*0.25f,h*0.75f,h*0.75f);
             super.draw(batch, parentAlpha);
        }
    }


    public static final class  Skill extends Group{
    int skilltype;
    Texture back;
    IconSkill img;
    Label counttext;

        // 0 - откат шариков назад
    // 1 - замедление
    // 2 - уничтожение
    public Skill(int type,boolean game){
  //  back =  new Texture("Interface/background_skills.png");
  back=Configuration.gamemanager.get("Interface/background_skills.png",Texture.class);
        counttext = new Label("",new Label.LabelStyle(MenuScreen.mainfont, Color.DARK_GRAY));
        addActor(counttext);

    this.skilltype=type;
    if (game){
    img  = new IconSkill(skilltype,counttext,this);
    counttext.setText(String.valueOf(img.count));} else {
        img = new IconSkill(skilltype,null,this);
    }
        addActor(img);
        img.setPosition(0,0);

    }



    public void setText(String text){
    Configuration.OptimalScale(counttext,getWidth()/2,getHeight());
    counttext.setText(text);
    }


        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(back,getX(),getY(),getWidth(),getHeight());
            super.draw(batch, parentAlpha);

        }


        @Override
        public void setSize(float width, float height) {
            counttext.setSize(height,height);
            counttext.setAlignment(Align.center);
             Configuration.OptimalScale(counttext,width*0.5f*1.05f,height*1.05f);
            counttext.setX(width/2);
            img.setSize(height,height);
            super.setSize(width, height);
        }

        public static class IconSkill extends Actor{
            int count;
            private final static long LOWTIME=2000;
        int type;
        Texture img;
            Timer time;
            Label main;
            Skill s;

        public IconSkill(int type, final Label main, Skill s){
        this.s=s;

        time = new Timer();
            count = MyGame.gamescreen.main.skills.get(type);
        this.type=type;
            switch (type){
                case 0://img  = new Texture("Interface/up_skill.png");
                img = Configuration.gamemanager.get("Interface/up_skill.png",Texture.class);
                    break;
                case 1: //img  = new Texture("Interface/clock_skill.png");
                    img = Configuration.gamemanager.get("Interface/clock_skill.png",Texture.class);
                    break;
                case 2:  //img = new Texture("Interface/destroy_skill.png");
                    img = Configuration.gamemanager.get("Interface/destroy_skill.png",Texture.class);
            } if (main!=null){
            this.main=main;
            final Skill skill=s;

            this.addListener(new ClickListener(){
                @Override
                public void clicked(final InputEvent event, float x, float y) {
                    Action();
                    final Actor m=event.getListenerActor();
                    event.getListenerActor().setTouchable(Touchable.disabled);
                    event.getListenerActor().addAction(Actions.sequence(Actions.moveBy(skill.getWidth()/2,0,0.25f,Interpolation.smooth),
                    Actions.moveBy(-skill.getWidth()/2,0,0.25f,Interpolation.smooth),Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    m.setTouchable(Touchable.enabled);
                                }
                            })));

                }
            });}
        }

            public  void Action(){
                int a =MyGame.gamescreen.main.skills.get(type);
                if (a>0){
                    MyGame.gamescreen.main.skills.put(type,a-1);
                    count-=1;
                    main.addAction(Actions.sequence(Actions.alpha(0,0.25f,Interpolation.smooth),Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            main.setText(String.valueOf(count));
                        }
                    }),Actions.alpha(1,0.25f,Interpolation.smooth)));

                    switch (type){
                        case  0: MyGame.gamescreen.AllAttacktoWall();
                            break;
                        case 1:
                            if (true){
                                time.delay(LOWTIME);
                                time.start();
                                MyGame.gamescreen.toLowAttackSpeed();
                                time.scheduleTask(new Timer.Task() {
                                    @Override
                                    public void run() {
                                        MyGame.gamescreen.toNormalAttackSpeed();
                                    }
                                },((float)LOWTIME-3)/1000);}
                            break;
                        case 2: MyGame.gamescreen.DestroyAttack();
                            break;
                    }}

            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
            batch.draw(img,getX(),getY(),getWidth(),getHeight());
                super.draw(batch, parentAlpha);
            }
        }
    }

    public static class GenerateButton  extends Group{
    private Texture background;
    Label count;
    int k;
    Label text;
    Level level;
        boolean listener;


    public GenerateButton(Level main){

    level= main;
    Label.LabelStyle lstyle = new Label.LabelStyle(MenuScreen.mainfont,Color.WHITE);
    k=main.generatecount;
    count = new Label(String.valueOf(k),lstyle);
    count.setAlignment(Align.center);
    count.addAction(Actions.alpha(0));

    text = new Label("NEW LINES",lstyle);
    text.setWrap(true);
    text.setAlignment(Align.center);
    text.setColor(Color.WHITE);
    this.addActor(count);
    this.addActor(text);
   background = Configuration.menumanager.get("Interface/dark_btn.png",Texture.class);

    }


        @Override
        public void setSize(float width, float height) {
           Configuration.OptimalScale(count,width*0.2f,height);
            count.setPosition(width*0.75f,height/2-count.getHeight()/2);
            text.setWidth(width);
            text.setAlignment(Align.center);
            text.setFontScale(height*0.52f/MenuScreen.fontsize);
            text.setPosition(0,height/2-text.getHeight()/2);
            super.setSize(width, height);
            addGenerateListener();
        }


        public void addGenerateListener(){
        if (!listener) {
        listener=true;
            text.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (k > 0) {
                        k -= 1;
                        level.GenerateLines();
                    }
                    text.setTouchable(Touchable.disabled);
                    count.moveBy(0,-count.getMinHeight()/2);
                   count.addAction(Actions.sequence(Actions.parallel(Actions.alpha(1,0.4f,Interpolation.sine),
                   Actions.moveBy(0,count.getMinHeight()/2,0.4f,Interpolation.sine)),Actions.run(new Runnable() {
                       @Override
                       public void run() {
                           count.setText(String.valueOf(k));
                       }
                   }),Actions.delay(0.4f),Actions.parallel(Actions.alpha(0,0.4f,Interpolation.sine),
                   Actions.moveBy(0,count.getMinHeight()/2,0.4f,Interpolation.sine)),Actions.moveBy(0,-count.getMinHeight()/2)));

                    text.addAction(Actions.sequence(Actions.moveBy(-count.getMinWidth(),0,0.5f,Interpolation.pow2Out),Actions.delay(0.4f),
                    Actions.moveBy(count.getMinWidth(),0,0.5f,Interpolation.sine),Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    text.setTouchable(Touchable.enabled);
                                }
                            })));
                    super.clicked(event, x, y);
                }
            });
        }

        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
        batch.draw(background,getX(),getY(),getWidth(),getHeight());
            super.draw(batch, parentAlpha);
        }
    }

    public static class SelectBoxLabel extends Group{
    ArrayList<Label> labels;
    Label activlabel;
    Texture background;

    public SelectBoxLabel(ArrayList<Label> array,Label activ){
    labels=array;
    activlabel=activ;
    labels.remove(activ);
    labels.add(0,activ);
    for (Label l:labels) {
    l.setColor(Color.WHITE);
        addActor(l);
    }
   // background = new Texture("Interface/dark_btn.png");
        background = Configuration.menumanager.get("Interface/dark_btn.png",Texture.class);


    }

        @Override
        public void setPosition(float x, float y) {
            super.setPosition(x, y);
            addSelectListener();
        }

        public void addSelectListener(){
    this.addListener(new ClickListener(){
        @Override
        public void clicked(final InputEvent event, float x, float y) {
            labels.remove(0);
            activlabel.addAction(Actions.parallel(Actions.moveBy(0,event.getListenerActor().getHeight()/2,0.5f,Interpolation.smooth),
            Actions.alpha(0,0.25f,Interpolation.smooth)));
            labels.add(activlabel);
            activlabel=labels.get(0);
            activlabel.moveBy(0,-event.getListenerActor().getHeight());
            labels.get(0).addAction(Actions.parallel(Actions.moveBy( 0,event.getListenerActor().getHeight()/2,
           0.25f,Interpolation.smooth),Actions.alpha(1,0.5f,Interpolation.smooth)));
            super.clicked(event, x, y);
            event.getListenerActor().setTouchable(Touchable.disabled);
            final Actor group = event.getListenerActor();
            event.getListenerActor().addAction(Actions.sequence(Actions.delay(0.5f),Actions.run(new Runnable() {
                @Override
                public void run() {
                    group.setTouchable(Touchable.enabled);
                }
            })));
            MyGame.mainscreen.ChangeMODE(false);
        }
    });

    }
        @Override
        public void draw(Batch batch, float parentAlpha) {
        batch.draw(background,getX(),getY(),getWidth(),getHeight());
            super.draw(batch, parentAlpha);
        }

        @Override
        public void setSize(float width, float height) {
            super.setSize(width, height);
            for (Label l: labels){
                l.setTouchable(Touchable.disabled);
                l.setFontScale(getHeight()*0.52f/MenuScreen.fontsize);
                l.setEllipsis(false);
                l.setSize(getWidth(),getHeight()/2);
                if (!l.equals(activlabel)){
                l.setPosition(getWidth()/2-l.getWidth()/2,getHeight()*0.75f);
                l.getColor().a=0;
                }
                l.setAlignment(Align.center);
            }
            activlabel.setPosition(getWidth()/2-labels.get(0).getWidth()/2,getHeight()*0.25f);

        }
    }

    public static final class BigLine extends Actor{
   private Texture img;
   private  boolean horizonal=false;
   TextureRegion imgr;
   private float rotate=0;
    public BigLine( boolean horizontal,boolean load){
    this.horizonal=horizontal;
    if (load){
        img = new Texture("H_LINE.png");
    } else {
  //
  img =Configuration.mainmanager.get("H_LINE.png",Texture.class);}

        if(!horizontal){
            rotate=90;
            imgr=new TextureRegion(img,300,300);
        }
    }


        @Override
        public void draw(Batch batch, float parentAlpha) {
             if (horizonal){
            batch.draw(img,getX(),getY(),getWidth(),getHeight()) ;} else {
                 batch.draw(imgr,getX(),getY(),getWidth()/2,0,getHeight(),getWidth(),1,1,rotate);
             }

            super.draw(batch, parentAlpha);
        }
    }

    public static  final class SectorCircle extends Circle{


        public SectorCircle(float x, float y, float radius,float g) {
            super(x, y, radius);
        }
    }



}
