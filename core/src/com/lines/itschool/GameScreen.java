package com.lines.itschool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.google.gson.Gson;

import java.awt.SystemColor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Dima on 08.02.2017.
 */

public class GameScreen implements Screen {
long nextline;
float timeline;
boolean render=false;
    float deltatime;
    int lasttypewall=-1;
    public static  List<Ball> attack;
    public static  List<Ball> wall;
    public static Map stage;
    public static LinkedList<Line> lines;
    public static List<PlaceBall> places;
    public static List<MainBall> mainballs;
    public static boolean stop=true;
    private static int maintype=-1;
    public static  int countball;
    ChooseFormBall form;
    Image pause;
    static Preferences preferences;
    float stime;
    long newattack;
    float timenext=4500;
    Level main;
    Gson m;
    Component.WindowView mywindow;
    Component.ProgressBar progress;
    long test;
    boolean nextlevel;
    int unline=-1;
    Interpolation interpolation =Interpolation.pow2Out;
    TextButtonScale transitionbtn;
    ArrayList<Component.Skill> skills;

// уставновка типа шара для запуска
    public static  void setMaintype(int type){
    maintype=type;
}
    public static int getMaintype(){
        return maintype;
    }


    public GameScreen(){
        countball=0;
        mainballs = new LinkedList<MainBall>();
        attack = new LinkedList<Ball>();
        wall = new LinkedList<Ball>();
        stage = new Map(new ScreenViewport(),false);
        lines=new LinkedList<Line>();
        places=new LinkedList<PlaceBall>();
         m = new Gson();
        main=new Level();
    }


 //добавления атакующего шара
    public  void AddAttack(){
    if (Configuration.MODE==1){
        int nexttype  = (int)(Math.random()*main.usestypes.size());
       attack.add(new Ball(false, Byte.decode(String.valueOf((int)((Math.random()*3)+1))),Byte.decode(String.valueOf(main.usestypes.get(nexttype))),main));
        }
        else {
        int numline=0;
        ArrayList<Integer> g = new ArrayList<Integer>();
        for (int i=0; i<3;i++){
          if (i!=unline){
              g.add(i+1);
          }
        }
        if (unline >= 0) {
             numline=(g.get((int)(Math.random()*2)));
        } else {
            numline=(int)((Math.random()*3)+1);
        }

        int nexttype  = (int)(Math.random()* Configuration.typesinfinity.size());

        attack.add(new Ball(false, Byte.decode(String.valueOf(numline)),Byte.decode(String.valueOf(Configuration.typesinfinity.get(nexttype))),main));
         //Изменяемое значение
         float speed = attack.get(attack.size()-1).speed;
        attack.get(attack.size()-1).MIN_SPEED=interpolation.apply(0.9f*speed,2.6f*speed, Configuration.k/150);
        if (Configuration.k+1<200){
            Configuration.k+=1;
        }
    }


        stage.addActor(attack.get(attack.size()-1));
        attack.get(attack.size()-1).Z=((attack.get(attack.size()-1).getNumLine()-1)*2)+1;
        stage.SortZIndex();
    }


// добавление шара пользователем
    public  void addDefended(int NumLine){
    if (maintype>=0) {
        lasttypewall = maintype;
        wall.add(new Ball(true, Byte.decode(String.valueOf(NumLine)), Byte.decode(String.valueOf(maintype)), main));
        stage.addActor(wall.get(wall.size() - 1));
        wall.get(wall.size() - 1).Z = ((wall.get(wall.size() - 1).getNumLine() - 1) * 2) + 1;
        stage.SortZIndex();
    }
    }


    public  void SaveLevel(){
        Configuration.SaveLevel(main);
    }


  //уничтожение одной линии для динамического режима игры
    public void HideOneLine() {
    if (unline<0){
         unline = (int) (Math.random() * 3);
        lines.get(unline).RunHide((int)timenext/2);
        places.get(unline).setTouchable(Touchable.disabled);
        for (int i=0; i<attack.size();i++){
            if (attack.get(i).getNumLine()==unline+1){
                attack.get(i).setAnimType(1);
            }
        }
        places.get(unline).setTouchable(Touchable.disabled);
        for (int i=0; i<wall.size();i++){
            if (wall.get(i).getNumLine()==unline+1){
                wall.get(i).setAnimType(1);
            }
        }
    }
    }

// генерация одной линии для динамического режима игры
    public void GenerateOneLine(){
    ArrayList<Line> help= new ArrayList<Line>();
    for (int i=0; i<lines.size();i++){
        if (i!=unline){
            help.add(lines.get(i));
        }
    }
    lines.get(unline).helpgenerate=help;
    lines.get(unline).Generate();
        lines.get(unline).RunShow((int)timenext/2);
    }



    @Override
    public void show() {
    render=false;
        Gdx.input.setCatchBackKey(true);
    Configuration.k=1;
    stop=true;
    Configuration.pauseanimation=false;
        maintype=-1;
        Gdx.input.setInputProcessor(stage);
        preferences=Gdx.app.getPreferences("MAIN");
        String jsons =preferences.getString("Level");
        Level.JSONLevel jlev;
        jlev = new Level.JSONLevel();
            jlev =m.fromJson(jsons,Level.JSONLevel.class);
            main= new Level(jlev);
            if (Configuration.MODE==1){
            lines=main.lines;} else {
                ArrayList<Line> l = new ArrayList<Line>();
                for (int i=0;  i<3;i++){
                lines.add(i,new Line(i+1,i+1,main,l));
                    lines.get(i).Generate();
                    l.add(lines.get(i));
                }
            }

            for (int i = 0; i < 3; i++) {
                stage.addActor(lines.get(i));
                lines.get(i).Z = i * 2;
                places.add(new PlaceBall(i + 1));
                stage.addActor(places.get(i));
                places.get(i).Z = i * 2 + 1;
            }
        if (Configuration.MODE==1){
            transitionbtn=MyGame.mainscreen.levelbtn;
            stage.addActor(transitionbtn);
        } else {
            transitionbtn=InfinityScreen.btnstart;
            stage.addActor(transitionbtn);
        }
        Skin a = new Skin();
        a.add("pause",Configuration.gamemanager.get("Interface/pause_btn.png",Texture.class));
        pause = new Image(a.getDrawable("pause"));
        pause.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if ( !stage.isShowWindow()){
                    createWindow(3);
                    Configuration.pauseanimation=true;}

                super.clicked(event, x, y);
            }
        });

         if (Configuration.MODE==1){
             progress = new Component.ProgressBar(main.maxball);
             progress.Z=10;
             stage.addActor(progress);
        for (int i=0; i<main.usestypes.size();i++){
           mainballs.add(new MainBall(main.usestypes.get(i)));
        }} else {
             for (int i = 0; i< Configuration.typesinfinity.size(); i++){
                 mainballs.add(new MainBall(Configuration.typesinfinity.get(i)));
             }

         }
        form= new ChooseFormBall((LinkedList)mainballs);
        form.Z=7;
        stage.addActor(form);
        timenext=main.timenext;
        stime=0;
        newattack= System.currentTimeMillis();







        pause.setSize(Gdx.graphics.getWidth()*0.1f,Gdx.graphics.getWidth()*0.1f);
        skills = new ArrayList<Component.Skill>();
        float x=Gdx.graphics.getWidth()-pause.getWidth()*1.5f;
        for (int i=0; i<3;i++){
        if (i==0){
            pause.setPosition(x,Gdx.graphics.getHeight()-pause.getHeight()*1.2f);
            x-=pause.getWidth()*3;
            stage.addActor(pause);
        }
            Component.Skill s = new Component.Skill(i,true);
            s.setSize(Gdx.graphics.getWidth()*0.2f,Gdx.graphics.getWidth()*0.1f);
            s.setPosition(x,Gdx.graphics.getHeight()-pause.getHeight()*1.2f);
            x-=(s.getWidth()*1.25);
            stage.addActor(s);
            s.setTouchable(Touchable.childrenOnly);
            skills.add(s);
        }
        test=System.currentTimeMillis();
        stage.SortZIndex();
        timeline=timenext*3;
        AnimationShow();
        stage.draw();

            }

    @Override
    public void render(float delta) {
        deltatime=delta;


        if (!stop && !Configuration.pauseanimation){
        DoReaction();
            for (int i=0; i<attack.size();i++){
                if (places.get(0).getY()+places.get(0).getHeight()>attack.get(i).getY()){
                    stop=true;
                    attack.get(i).setAnimType(1);
                    attack.remove(i);
                    FailLevel();
                    break;
                }
            }
          if (Configuration.MODE==1)  {
        if ((System.currentTimeMillis()-newattack)>timenext) {
            newattack = System.currentTimeMillis();
            if (countball < main.maxball){
            timenext=interpolation.apply(main.timenext-50,timenext+50,countball/main.maxball);
            AddAttack();
        }
        }
        if (countball>=main.maxball && attack.isEmpty()){
            stop=true;
            createWindow(1);
            nextlevel=true;  // исправление
        }} else {
        if (System.currentTimeMillis()-nextline>timeline){
            HideOneLine();
            nextline=System.currentTimeMillis();
            timeline=timenext*3;
        }
              if ((System.currentTimeMillis()-newattack)>timenext) {
                      newattack = System.currentTimeMillis();
                      timenext=interpolation.apply(main.timenext,770, Configuration.k/200);
                      AddAttack();
              }
          }
        } else {
        if (countball==0 && attack.isEmpty()){
        for (int i=0; i<lines.size();i++) {
        if (lines.get(i).nextanimation) {
        if (!places.get(i).start) {
            places.get(i).startAnim();
        }
        }
        }
        boolean a=true;
        for (PlaceBall p:places) {
        if (!p.endanim) {a=false;}
        }
            if (a && !stage.isShowWindow()) {stop=false;
            form.startAnim();}
        }}
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(1,1,1,1);
        stage.act(delta);
        stage.draw();

    }


// проверка коллизий шаров
    private void DoReaction(){
        for (int i=0; i<attack.size();i++){
            if(attack.get(i).destroy){
                if (attack.get(i).main){
                    countball+=1;
                    if (progress!=null){
                    progress.Step();}
                }
                attack.remove(i);
                i-=1;
                continue;
            }
            if (attack.get(i).anim){
                continue;
            }
            attack.get(i).Start();

            for (int k=0; k<wall.size();k++){
                if (wall.get(k).destroy){
                    wall.remove(k);
                    k-=1;
                    continue;
                }
                if (wall.get(k).anim){
                    continue;
                }
                if (attack.get(i).OverLaps(wall.get(k))){
                    if (attack.get(i).getNumLine()==wall.get(k).getNumLine() ){
                        int  R = attack.get(i).Reaction(wall.get(k));
                        if (R==1){
                          // attack.get(i).remove();
                          // wall.get(k).remove();
                            break;
                        } else {
                            if (R==2){
                                attack.add(wall.get(k));
                               wall.remove(k);
                                break;
                            } else if (R==3){
                           // attack.get(i).remove();
                                break;
                            } else if (R==4){
                           // wall.get(k).remove();
                                break;
                            } else if (R==5) {
                                wall.add(attack.get(i));
                                attack.remove(i);
                                break;
                            } else if (R==6) {
                                Ball one =wall.get(k);
                                Ball two = attack.get(i);
                                wall.remove(k);
                                attack.remove(i);
                                wall.add(two);
                                attack.add(one);
                                break;
                            }
                        }
                    }
                }
            }
        }
        for (int i=0; i<wall.size();i++){
            if (wall.get(i).anim){
                continue;
            }
            if (wall.get(i).getY()>Gdx.graphics.getHeight()){
                wall.get(i).Dispose();
                wall.remove(i);
                i-=1;
            } else {
                wall.get(i).Start();
            }
        }
}
// game over((
    public void FailLevel(){
        stop=true;
        if (!stage.isShowWindow()){
        createWindow(2);
        stop=true;
        stage.setFocusTouchOnWindow();
        SaveLevel();}
    }
//создание окна (пауза, проигрыш, прохождение уровня)
    public void createWindow(int type){
        TextButton tomenu;
        TextButton resume;
        TextButton restart;
        TextButton next;
        Label numball;
        Label inform;
        int H=Gdx.graphics.getHeight();
        int W= Gdx.graphics.getWidth();
        float width=W*0.75f;
        float heigth=H/2;
        BitmapFont mainfont=MyGame.mainscreen.mainfont;
        Image ball = new Image(new Texture("D_BALL.png"));
        Label.LabelStyle labelStyle = new Label.LabelStyle(mainfont, Color.WHITE);
        int wb=(int)(width/2.2f);
        int hb=(int)(width/2.2f*2/3);
            Skin skinmenu = new Skin();
            skinmenu.add("menu",Configuration.gamemanager.get("Interface/red_btn.png",Texture.class));
            skinmenu.add("background",new Texture("Interface/backgroud_win.png"));
          tomenu = new TextButton("MENU", new TextButton.TextButtonStyle(skinmenu.getDrawable("menu"), skinmenu.getDrawable("menu"), skinmenu.getDrawable("menu"), mainfont));
          tomenu.setSize(wb,hb);
          tomenu.getLabel().setFontScale(heigth/9/MenuScreen.fontsize);
            Skin skinother = new Skin();
            skinother.add("blue",Configuration.gamemanager.get("Interface/blue_btn.png",Texture.class));
            mywindow = new Component.WindowView("",new Window.WindowStyle(mainfont,Color.WHITE,skinmenu.getDrawable("background")));
       mywindow.setSize(width,heigth);
        mywindow.setPosition(W/2-mywindow.getWidth()/2,H/3);
        mywindow.setMovable(false);
        mywindow.pad(width/30);
        mywindow.center();
         ball.setSize(heigth/5,heigth/5);


        tomenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               mywindow.typec=1;
                mywindow.close();
                super.clicked(event, x, y);
            }
        });



            if (type == 1) {
            int[] skills ;
            skills = main.AddNextSkill();
                next = new TextButton("NEXT", new TextButton.TextButtonStyle(skinother.getDrawable("blue"), skinother.getDrawable("blue"), skinother.getDrawable("blue"), mainfont));
               // next.getLabel().setFontScale(heigth/9/MenuScreen.fontsize);
               Configuration.OptimalScale(next.getLabel(),wb,hb);
               next.getLabel().setAlignment(Align.center);
                next.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        mywindow.typec=2;
                        mywindow.close();
                        super.clicked(event, x, y);
                    }

                });
                nextlevel=true;
                inform = new Label("LEVEL " + main.NumLev + " COMPLETED", labelStyle);
                inform.setWidth(width*0.7f);
                inform.setWrap(true);
                inform.setColor(Color.DARK_GRAY);
                inform.setFontScale(heigth/8.5f/MenuScreen.fontsize);
                inform.setAlignment(Align.center);
                numball = new Label(String.valueOf(main.maxball), labelStyle);
                numball.addAction(CustomAction.ScoreAction.scoreAction(main.maxball,numball));
                numball.setFontScale(heigth/5/MenuScreen.fontsize);
                numball.setColor(Color.DARK_GRAY);
                mywindow.add(inform).fill().center().colspan(2).pad(H/80).width(width*0.8f).row();
                mywindow.add(numball);
                mywindow.add(ball).size(ball.getHeight(),ball.getHeight()).row();
                Table skilltable= new Table();
                skilltable.setWidth(width);
                int z=0;
                for (int i=0; i<skills.length;i++){
                Component.Skill add = new Component.Skill(i,true);
                add.setSize(width/3.5f,width/7);

                    if (skills[i]!=0){
                    add.setText("+"+skills[i]);
                      skilltable.add(add).pad(add.getWidth()/15);
                      add.setTouchable(Touchable.disabled);
                      z+=1;
                    }
                }
                skilltable.setWidth(width);
                if (z!=0){
                mywindow.add(skilltable).width(width).colspan(2).row();
                    tomenu.setScale(0.85f);
                    next.setScale(0.85f);
                    mywindow.add(tomenu).size(wb*0.85f,hb*0.85f);
                    mywindow.add(next).size(wb*0.85f,hb*0.85f);
                     } else {
                    mywindow.add(tomenu).pad(H/80).size(wb,hb);
                    mywindow.add(next).pad(H/80).size(wb,hb);
                }


            }
            if (type == 2) {
                int[] skills =main.AddSkill() ;
                restart = new TextButton("RESTART", new TextButton.TextButtonStyle(skinother.getDrawable("blue"), skinother.getDrawable("blue"), skinother.getDrawable("blue"), mainfont));
               // restart.getLabel().setFontScale(heigth/10/MenuScreen.fontsize);
               Configuration.OptimalScale(restart.getLabel(),wb,hb);
               restart.getLabel().setAlignment(Align.center);
                restart.addListener(new ClickListener(){
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        mywindow.typec=3;
                        mywindow.close();
                        return true;
                    }
                });

                inform = new Label("GAME OVER", labelStyle);
                inform.setWrap(true);
                inform.setColor(Color.DARK_GRAY);
                inform.setFontScale(heigth/9f/MenuScreen.fontsize);
                inform.setAlignment(Align.center);
                numball = new Label(String.valueOf(GameScreen.countball), labelStyle);
                numball.setFontScale(heigth/4/MenuScreen.fontsize);
                numball.setColor(Color.DARK_GRAY);
                numball.addAction(CustomAction.ScoreAction.scoreAction(countball,numball));
                tomenu.getLabel().setFontScale(heigth/8.5f/MenuScreen.fontsize);
                mywindow.add(inform).fill().center().colspan(2).pad(H/40).row();
                mywindow.add(numball);
                mywindow.add(ball).size(ball.getHeight(),ball.getHeight()).row();
                if (Configuration.MODE==2){
                if (countball> Configuration.getRecordInfinity()){
                    Configuration.setRecord(countball);
                }
                    Label record  = new Label("BEST "+String.valueOf(Configuration.getRecordInfinity()),labelStyle);
                    record.setFontScale(heigth/9f/MenuScreen.fontsize);
                    record.setWrap(true);
                    record.setAlignment(Align.center);
                    record.setColor(Color.DARK_GRAY);
                    mywindow.add(record).pad(heigth/100).colspan(2).center().fill().row();
                    inform.setFontScale(heigth/9/MenuScreen.fontsize);
                }


                Table skilltable= new Table();
                skilltable.setWidth(width);
                int z=0;
                for (int i=0; i<skills.length;i++){
                    Component.Skill add = new Component.Skill(i,true);
                    add.setSize(width/3.5f,width/7);

                    if (skills[i]!=0){
                        add.setText("+"+skills[i]);
                        skilltable.add(add).pad(add.getWidth()/15);
                        add.setTouchable(Touchable.disabled);
                        z+=1;
                    }
                }
                skilltable.setWidth(width);
                if (z!=0 && Configuration.MODE==1){
                    mywindow.add(skilltable).width(width).colspan(2).row();
                    tomenu.setScale(0.85f);
                    restart.setScale(0.85f);
                    mywindow.add(tomenu).size(wb*0.85f,hb*0.85f);
                    mywindow.add(restart).size(wb*0.85f,hb*0.85f);
                } else {
                if (Configuration.MODE==2){
                    mywindow.add(tomenu).size(wb*0.9f,hb*0.9f).pad(H/100);
                    mywindow.add(restart).size(wb*0.9f,hb*0.9f).pad(H/100);
                    restart.getLabel().setFontScale(wb*0.9f/restart.getText().toString().length()*1.5f/MenuScreen.fontsize);}
                     else {
                    mywindow.add(tomenu).size(wb,hb).pad(H/100);
                    mywindow.add(restart).size(wb,hb).pad(H/100);
                    restart.getLabel().setFontScale(wb*0.9f/restart.getText().toString().length()*1.5f/MenuScreen.fontsize);
                }
                }



            }
            if (type == 3) {
            if (Configuration.MODE==1){
                inform = new Label("LEVEL " + main.NumLev, labelStyle);} else {
                inform = new Label("INFINITY MODE", labelStyle);
            }
                inform.setWrap(true);
                inform.setColor(Color.DARK_GRAY);
                inform.setFontScale(heigth/8.5f/MenuScreen.fontsize);
                inform.setAlignment(Align.center);
                resume = new TextButton("RESUME", new TextButton.TextButtonStyle(skinother.getDrawable("blue"), skinother.getDrawable("blue"), skinother.getDrawable("blue"), mainfont));
               // resume.getLabel().setFontScale(heigth/10/MenuScreen.fontsize);
               Configuration.OptimalScale(resume.getLabel(),wb,hb);
               resume.getLabel().setAlignment(Align.center);
                resume.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        mywindow.typec=4;
                        mywindow.close();
                        super.clicked(event, x, y);
                    }
                });
                mywindow.add(inform).fill().center().colspan(2).pad(H/40).row();
                mywindow.add(tomenu).size(wb,hb).padTop(H/50);
                mywindow.add(resume).size(wb,hb).padTop(H/50);
                mywindow.setHeight(H/3);
                mywindow.setY(H/2);
            }
            if (type==4){
                inform = new Label("GAME OVER", labelStyle);
                inform.setWrap(true);
                inform.setColor(Color.DARK_GRAY);
                inform.setFontScale(heigth/8.5f/MenuScreen.fontsize);
                inform.setAlignment(Align.center);






            }
            stage.setFocusTouchOnWindow();
            mywindow.show();
            stage.addActor(mywindow);
            Configuration.pauseanimation=true;
            Configuration.SaveLevel(main);




    }

  // все  аткающие шары назад
    public void AllAttacktoWall(){
    for (int i=0; i<attack.size();i++){
      attack.get(i).ChangeDaA();
      wall.add(attack.get(i));
      attack.remove(i);
      i-=1;
    }
    }

    public void toNormalAttackSpeed(){
   Level.aslow=Level.aslow*2;
    }

    public void toLowAttackSpeed(){
   Level.aslow=Level.aslow/2;
    }

    public void DestroyAttack(){
    for (Ball b:attack){
        b.setAnimType(1);
    }
    }

    public void AnimationShow(){
    transitionbtn.addAction(Actions.sequence(Actions.delay(1f),
    Actions.moveTo(transitionbtn.getX(),0-transitionbtn.getHeight(),0.8f,Interpolation.smooth)));
    pause.addAction(Actions.parallel(Actions.sizeTo(pause.getWidth(),pause.getHeight(),0.6f,Interpolation.smooth),
    Actions.moveTo(pause.getX(),pause.getY(),0.6f,Interpolation.smooth)));
    pause.moveBy(pause.getWidth()/2,pause.getHeight()/2);
    pause.setSize(0,0);
    for (int i=0; i<skills.size();i++){
        skills.get(i).addAction(Actions.sequence(Actions.delay(0.4f*(i+1)),
        Actions.parallel(Actions.moveTo(skills.get(i).getX(),skills.get(i).getY(),0.6f,Interpolation.smooth),
        Actions.sizeTo(skills.get(i).getWidth(),skills.get(i).getHeight(),0.6f,Interpolation.smooth))));
        skills.get(i).moveBy(skills.get(i).getWidth()/2,skills.get(i).getHeight()/2);
        skills.get(i).setSize(0.1f,0.1f);
    }
    if (Configuration.MODE==1){
    progress.addAction(Actions.sequence(Actions.delay(1f),Actions.parallel(Actions.sizeTo(progress.getWidth(),progress.getHeight(),0.6f,Interpolation.smooth),
    Actions.moveTo(progress.getX(),progress.getY(),0.6f,Interpolation.smooth))));
    progress.setWidth(0);
    progress.moveBy(progress.getWidth()/2,0);}
    for (int i=0; i<lines.size();i++){
        lines.get(i).RunShow(lines.get(i).getSectionLine().size()*180);
    }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
    Configuration.pauseanimation=true;
    }

    @Override
    public void resume() {
    if ( !stage.isShowWindow()){
        createWindow(3);}
    }

    @Override
    public void hide() {

        SaveLevel();
    }

    @Override
    public void dispose() {
     if (nextlevel){
         main.nextLevel();
         nextlevel=false;
         SaveLevel();
     }
    }

// класс отвещяющий за отрисвку платформ
    static class PlaceBall extends Map.MyActor{
        float createtime;
        int numline;
        Animation<TextureAtlas.AtlasRegion> makeanim;
        static int deltasize=(int)(Line.SIZE_LINE*1.75);
        boolean start=false;
             public PlaceBall(int num){
        endanim=false;
        TextureAtlas one  = new TextureAtlas("Animation/place_ball/data_one_p.txt");
        TextureAtlas two = new TextureAtlas("Animation/place_ball/data_two_p.txt");
        Array<TextureAtlas.AtlasRegion> main =one.getRegions();
        main.addAll(two.getRegions());
        makeanim= new Animation(0.033f,main);
            numline= num;
            setSize(Gdx.graphics.getWidth()/4f,Gdx.graphics.getWidth()/4f);
            setPosition(Gdx.graphics.getWidth()/3*(numline-1)+Gdx.graphics.getWidth()/6-getWidth()/2,
                    (int)(Line.getK_H()*Gdx.graphics.getHeight()-getHeight()/2));
           // setPosition(Line.SIZE_LINE*4*(num-1)+(int)(Line.SIZE_LINE*1.5)-deltasize/2,(int)(Line.getK_H()*Gdx.graphics.getHeight())-deltasize);
           // setSize(Line.SIZE_LINE+deltasize,Line.SIZE_LINE+deltasize);
            AddListener();
            }
        public void startAnim(){
        createtime=0;
        start=true;
        }
        public void AddListener(){
            this.addListener(new ClickListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (maintype!=-1) {
                        MyGame.gamescreen.addDefended(numline);
                    }
                    return true;
                }
            });
        }


        @Override
        public void draw(Batch batch, float parentAlpha) {
        if (start){
        if (!endanim && !Configuration.pauseanimation) {
            createtime +=MyGame.gamescreen.deltatime;
            if (makeanim.isAnimationFinished(createtime)) {
                endanim = true;
            }
            batch.draw(makeanim.getKeyFrame(createtime), getX(), getY(), getWidth(), getHeight());
        } else {
            batch.draw(makeanim.getKeyFrame(100), getX(), getY(), getWidth(), getHeight());
        }

        }}
    }
// класс отвещяющий за расположение шариков для запуска и их анимацию
    static class ChooseFormBall extends Map.MyActor {
        Queue<Integer> queue;
        boolean usequeu;
        private LinkedList<MainBall> main;
       // private int between = (int)(Line.SIZE_LINE/2f);
       private int between =(int)((Gdx.graphics.getWidth()-Gdx.graphics.getWidth()/3.5f*3)/4);
        private int SIZE_BALL;

        public int getSizeBall(){
        return SIZE_BALL;
        }
        public ChooseFormBall( LinkedList<MainBall> add){
        main=new LinkedList<MainBall>();
             if (add.size()>3){
                 usequeu=true;
                 queue=new Queue<Integer>();
                 for (int i=0; i<add.size();i++) {
                 int i1 = (int)(Math.random()*(add.size()-1));
                 int i2 = (int)(Math.random()*(add.size()-1));
                 Collections.swap(add,i1,i2);
                 }
                 for (int i=0; i<3;i++) {
                 main.add(add.get(i));
                 }
                 for (int i=3; i<add.size();i++) {
                 queue.addFirst(add.get(i).getType());
                 }
             } else {
                 main=add;
             }

           /* SIZE_BALL=(Gdx.graphics.getWidth()-(main.size()+1)*between)/main.size();
            if (SIZE_BALL>Line.getK_H()*Gdx.graphics.getHeight()-between-(PlaceBall.deltasize+Line.SIZE_LINE)/2) {
                SIZE_BALL = (int) (Gdx.graphics.getHeight() * Line.getK_H()) - between*2-(PlaceBall.deltasize+Line.SIZE_LINE)/2;
            }*/
            SIZE_BALL=(int)(Gdx.graphics.getWidth()/3.5f);
            if (SIZE_BALL+between*2>Gdx.graphics.getHeight()*Line.getK_H()-Gdx.graphics.getWidth()/8
            ){
                SIZE_BALL=(int)(Gdx.graphics.getHeight()*Line.getK_H()-Gdx.graphics.getWidth()/8-between*2);

            }

            int l=(Gdx.graphics.getWidth()-SIZE_BALL*main.size()-between*(main.size()+1))/2;
            for (int i=0; i<main.size();i++){
                l=l+between;
                main.get(i).setListener();
                main.get(i).setPosition(l,between);
                main.get(i).setSize(SIZE_BALL);
                stage.addActor(main.get(i));
              //  main.get(i).addDragListener();
                l=l+SIZE_BALL;
            }

        }
        public void startAnim(){
        for (int i=0; i<main.size();i++) {
        main.get(i).setAnimationType(1);
        }
        }



        public void nextType(int type){
        if (usequeu){
        for (MainBall m: main){
            if (m.getType()==type){
             queue.addFirst(m.getType());
             m.setType(queue.removeLast());
             m.ReTypeImg();
             m.setAnimationType(1);
             break;
            }
        }
        }
        }
        @Override
        public void draw(Batch batch, float parentAlpha) {
            for (int i=0; i<main.size();i++) {
                main.get(i).setDeltatime(MyGame.gamescreen.deltatime);
            }
        }

    }


}
