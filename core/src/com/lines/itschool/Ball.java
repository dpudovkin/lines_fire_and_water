package com.lines.itschool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;


// 0 - WaterBall
// 1 - FireBall
// 2 - etc


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.lines.itschool.Map.*;

/**
 * Created by Dmitriy on 27.01.2017.
 */

public class Ball extends MyActor {
    private boolean notaction;
    private int temp=0;
    public byte type;
    private static int SIZE_H = Line.SIZE_LINE;
    private static int SIZE_W = SIZE_H;
    private byte NumLine;
    private final int kspeed=360;
    private float kmaxspeed=3;
    private float kminspeed=0.4f;
    protected float speed=(float)Gdx.graphics.getHeight()/kspeed;
    private float boost;
    protected float MAX_SPEED=(float)(speed*kmaxspeed);
    protected float MIN_SPEED=(float)(speed*kminspeed);
    Texture img;
    Texture z;
    private boolean Defended;
    private float sx=0;
    private float sy=0;
    private Traectory traectory;
    private boolean unboost=false;
    boolean destroy=false;
    boolean main=false;
    int animtype=-1;
    float texturex=1;
    int countanimation;
    boolean show=true;
    boolean anim=false;
    Indicator indicator;




    //   waterball; 0
    // fireball; 1
    // metallball; 2
    //  3   - magnit ball
    // 4 - wooden ball
    // 5 - acid ball

public int getNumLine(){
    return NumLine;
}



    public Ball(boolean Defended, byte NumLine, byte type, Level main){

    if (type==2){
        z =Configuration.gamemanager.get("MZ_BALL.png",Texture.class);
    } else if (type==4){
        z =Configuration.gamemanager.get("WDZ_BALL.png",Texture.class);
    }
    //установка сокрости и ускорение ис параметров уровня
        this.type=type;
        this.Defended=Defended;
        this.NumLine=NumLine;
        boost=((float)main.boost)/1000;
        kmaxspeed=(float)main.kmaxspeed/1000;
        kminspeed=(float)main.kminspeed/1000;
        MAX_SPEED=speed*kmaxspeed;
        MIN_SPEED=speed*kminspeed;
        for (int i = 0; i< GameScreen.lines.size(); i++){
          if (GameScreen.lines.get(i).num==NumLine){
              traectory=new Traectory(GameScreen.lines.get(i).getSectionLine());
              break;
          }
        }
        SetAD();
        traectory.SetStartXY();
        setPosition(sx,sy);
        setSize(SIZE_H,SIZE_W);
        //определение текстуры
        switch (type) {
            case 0:  //img = new Texture("W_BALL.png");
            img = Configuration.mainmanager.get("W_BALL.png",Texture.class);
            break;
            case 1:   //img= new Texture("F_BALL.png");
            img =  Configuration.mainmanager.get("F_BALL.png",Texture.class);
            break;
            case 2:    //img = new Texture("M_BALL.png");
               img = Configuration.mainmanager.get("M_BALL.png",Texture.class);
            break;
            case 3:  //img = new Texture("MG_BALL.png");
                img=Configuration.mainmanager.get("MG_BALL.png",Texture.class);
            break;
            case 4:  //img = new Texture("WD_BALL.png");
                img= Configuration.mainmanager.get("WD_BALL.png",Texture.class);
            break;
            case 5:   //img = new Texture("A_BALL.png");
                img= Configuration.mainmanager.get("A_BALL.png",Texture.class);
            break;
        }
        if (!Defended){
            this.main=true;
        }
        indicator = new Indicator(type,this);

    }
      // Animatiotype
      // 1 - уничтожение
    public void setAnimType(int type){
    if (!anim){
    animtype=type;
    switch (type){
        case 1: anim=true;
        break;
    }
    }}

    private  void AnimationDestroy(){
        countanimation += 1;
            if (texturex * (1 + countanimation * 0.05f) <(Line.SIZE_LINE/4)) {
               texturex=(texturex*(1 + countanimation * 0.05f));
            } else {
                countanimation =0;
                texturex=Line.SIZE_LINE/4;
                show=false;
                animtype=-1;
                destroy=true;
            }

    }
 // установка направления
    public void SetAD(){
        if(Defended){
            speed=(int)(speed*2.5);
           for (int i=0;i<traectory.main.size();i++){
                traectory.main.get(i).SetA(1);

            }
        } else {
            traectory.ReChange();
            for (int i=0;i<traectory.main.size();i++){
                traectory.main.get(i).SetA(-1);
            }
        }
    }
 //смена направления
    public void ChangeDaA(){
   Defended=!Defended;
   if (Defended){
       traectory.ReChange();
   }
        SetAD();
        traectory.n=traectory.main.size()-traectory.n;
        traectory.l=Line.SIZE_LINE-traectory.l;
    }



    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (destroy){Dispose();}
    if (animtype>0){
    indicator.animate=true;
        if (animtype==1){AnimationDestroy();
        batch.draw(img, getX()+texturex,getY()+texturex, getWidth()-texturex*2,getHeight()-texturex*2);
            if (type==4 || type==2){
                indicator.draw(batch, parentAlpha);
                batch.draw(z,getX()+texturex,getY()+texturex,getWidth()-texturex*2,getHeight()-texturex*2);
            }}
    } else {
        if (show) {

            batch.draw(img, sx, sy, getWidth(), getHeight());
            if (type==4 || type==2){
                indicator.draw(batch, parentAlpha);
                batch.draw(z,getX(),getY(),getWidth(),getHeight());
                }
        }
    }
    }

    //метод для движение шара - постоянно вызываеться при визуализции
    public void Start(){
       if(!Defended){
            AttackBoost();
        }
        traectory.Next(speed*Gdx.graphics.getDeltaTime()*50* Level.aslow);
        setPosition(sx,sy);
    }
    //ускорение для атакающих шаров
    private void AttackBoost(){
        if (Gdx.graphics.getHeight()-sy<Gdx.graphics.getHeight()/5.5f) {
            if (speed < MAX_SPEED){
                speed+=(boost);
            }
            unboost=false;
        } else {
            if (!unboost){
                unboost=true;;
                boost=boost*5;
            }
                    if (speed-(boost)>MIN_SPEED) {
                        speed-=(boost);
                    }
        }

    }



   //уничтожение шара
    public void Dispose(){
        this.setVisible(false);
        traectory=null;
    }

    // 0 - бездействие
    // 1 - уничтожение обоих
    // 2 - ошибка игрока - переход шара в Attack
    // 3 - удаление атакующего
    // 4  - удаление зашитного
    // 5 - переход атакующего в защитный
    // 6 -  оба шара меняються сторономи


    // метод вызываеться при визуализации если шары на одной линии столкнулись друг с другом
    public int Reaction(Ball a){
    float defendedspeed=Gdx.graphics.getHeight()/kspeed*2.5f;  //от Attack к Defended
        if((this.type==0 && a.type==1) || (a.type==0 && this.type==1)){
            this.setAnimType(1);
            a.setAnimType(1);
            return 1;
        }

        if (a.type==3 && this.type==3){
            a.ChangeDaA();
            this.ChangeDaA();
            return 6;
        }
        if (this.type==a.type){
            a.ChangeDaA();
            a.speed=a.MIN_SPEED*10f;
            return 2;
        }
        if (a.notaction && this.notaction) {
        return  0;
        }

        if (this.type==0) {
            if (a.type==2) {
                a.temp-=1;
                if (a.temp>=0){
               a.indicator.HideIndicator();
                    this.setAnimType(1);
                    return 3;
                } else{
                    a.setAnimType(1);
                    return 4;
                }
            }
            if (a.type==3) {
            a.setAnimType(1);
            return  4;
            }

            if (a.type==4) {
            a.temp+=1;
            a.indicator.ShowIndicator();
            this.setAnimType(1);
            return 3;
            }

            if (a.type==5) {
               a.setAnimType(1);
               return 4;
            }
        }



        if (this.type==1) {
            if (a.type==2) {
                a.temp += 1;
                a.indicator.ShowIndicator();
                this.setAnimType(1);
                return 3;
            }
            if (a.type==3) {
                a.setAnimType(1);
                return  4;
            }
            if (a.type==4) {
            a.temp-=1;
            a.indicator.HideIndicator();
            if (a.temp>=0) {
            this.setAnimType(1);
            return 3;
            } else {
            a.setAnimType(1);
            return 4;
            }
            }

            if (a.type==5) {
                this.setAnimType(1);
                a.setAnimType(1);
                return  1;
            }
        }


        // Metall ball attack
        if (this.type==2 ){
            if (a.type==1) {
                this.temp += 1;
                this.indicator.ShowIndicator();
                a.setAnimType(1);
                return 4;
            }
            if (a.type==0){
                this.temp-=1;
                this.indicator.HideIndicator();
                if (this.temp>=0){
                    a.setAnimType(1);
                    return 4;
                } else{
                    this.setAnimType(1);
                    return 3;
                }
            }
            if (a.type==3) {
                this.ChangeDaA();
                this.speed=(int)(defendedspeed);
                a.ChangeDaA();
                a.speed=(int)(defendedspeed/2);  // test *2f
                a.MIN_SPEED=(int)(kminspeed*defendedspeed/2.5f*1.5f); //////warning
                a.notaction=true;
                notaction=true;
                return 6;
            }

            if (a.type==4) {
                if (this.speed > defendedspeed / 8) {
                    this.speed = speed * 0.9f;
                    return 4;
                }
            a.setAnimType(1);
            return  4;
            }

            if (a.type==5) {
                this.setAnimType(1);
                if (a.speed > defendedspeed / 8) {
                    a.speed = speed * 0.9f;
                    return 4;
                }
            }
        }



        // Magnit ball attack
        if (this.type==3) {


        if (a.type==2) {
            this.ChangeDaA();
            this.speed=(int)(defendedspeed);
            a.ChangeDaA();
            a.speed=(int)(defendedspeed/2);  // test *2f
            a.MIN_SPEED=(int)(kminspeed*defendedspeed/2.5f*1.5f); //////warning
            a.notaction=true;
            notaction=true;
            return 6;
        } else {
        this.setAnimType(1);
        return  3;
        }
        }



        // Wooden ball attack
        if (this.type==4) {
            if (a.type == 0) {
                a.setAnimType(1);
                this.temp += 1;
                this.indicator.ShowIndicator();
                return 4;
            }

            if (a.type == 1) {
                this.temp -= 1;
                this.indicator.HideIndicator();
                if (this.temp >= 0) {
                    a.setAnimType(1);
                    return 4;
                } else {
                    this.setAnimType(1);
                    return 3;
                }
            }

            if (a.type == 2) {
                if (a.speed>=defendedspeed/8f){
                    a.speed=speed*0.9f;
                }
                this.setAnimType(1);
                return 3;
            }

            if (a.type == 3) {
                a.setAnimType(1);
                return 4;
            }

            if (a.type==5) {
                this.setAnimType(1);
                return  3;
            }

        }

        // acid ball attack
        if (this.type==5) {
        if (a.type==0){
            this.setAnimType(1);
            return 3;
        }
        if (a.type==1){
            a.setAnimType(1);
            this.setAnimType(1);
            return 1;
        }
        if (a.type==2){
            a.setAnimType(1);
            if (this.speed>=defendedspeed/8f){
                this.speed=speed*0.9f;
                }
            return  4;
        }
        if (a.type==3){
            a.setAnimType(1);
            return  4;
        }

        if (a.type==4){
            a.setAnimType(1);
            return  4;
        }
        }








        return 0;

    }

   private Circle r1 = new Circle();
   private Circle r2 = new Circle();
    // метод определения столкновения
    public Boolean OverLaps(Ball main){
    float k=0.9f;
        r1.setPosition(this.getX()+this.getWidth()*(1-k)/2,this.getY()+this.getHeight()*(1-k)/2);
        r2.setPosition(main.getX()+main.getWidth()*(1-k)/2,main.getY()+main.getHeight()*(1-k)/2);
    r1.setRadius(this.getWidth()*k/2);
    r2.setRadius(main.getWidth()*k/2);
    if (Intersector.overlaps(r1,r2)){
        return  true;
    } else {
        return  false;
    }
    /*r1.setSize(this.getWidth()*k,this.getHeight()*k);
    r2.setSize(main.getWidth()*k,main.getHeight()*k);

    if (r1.overlaps(r2)){
        return  true;
    } else {
        return  false;
    }*/





    }



//класс отвечающий за отрисовку нагретого железного шара или мокрого деревянного, индикатор
// показывает эти свойства
    private static class Indicator extends Actor {
    Texture img;
    Ball ball;
    boolean animate=false;



        public Indicator(int type,Ball ball){
        this.ball=ball;
        setSize(0,0);
        setPosition(ball.getX()+ball.getWidth()/2,ball.getY()+ball.getHeight());
        if (type==2){
        img =Configuration.gamemanager.get("BACK_F_BALL.png",Texture.class);
        }
        if (type==4){
            img =Configuration.gamemanager.get("BACK_W_BALL.png",Texture.class);
        }
        }

        public void ShowIndicator(){
        if (ball.temp>0){
           setSize(ball.getWidth(),ball.getHeight());
        }
        }

        public void HideIndicator(){
            if (ball.temp<=0){
              setSize(0,0);
            }
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
        if (!animate){
        batch.draw(img,ball.getX()+ball.getWidth()/2-getWidth()/2,ball.getY()+ball.getHeight()/2-getHeight()/2
        ,getWidth(),getHeight());} else {
        if (ball.temp<=0){
            setSize(0,0);
            setVisible(false);
        }
            batch.draw(img,ball.getX()+ball.getWidth()/2-getWidth()/2+ball.texturex,ball.getY()+ball.getHeight()/2-getHeight()/2
            +ball.texturex
                    ,getWidth()-ball.texturex*2,getHeight()-ball.texturex*2);
        }
            super.draw(batch, parentAlpha);
        }

        @Override
        public void setSize(float width, float height) {

            super.setSize(width, height);
        }
    }

    // класс отвещающий за построение траектории движения шара из линий
    class Traectory {
        public List<Line.SectionLine> main;
        private float l; // длинна пути
        private int n; // количество элементов
        public int start=0;


        public Traectory(LinkedList<Line.SectionLine> main){
            this.main = new LinkedList<Line.SectionLine>();
            for (int i=0;i<main.size();i++){
                try {
                    this.main.add((Line.SectionLine) main.get(i).clone());
                } catch(CloneNotSupportedException e){
                    Gdx.app.log("CloneException","");
                }
            }
            l=0;
            n=1;
        }
        public void  ReChange(){
           // List<Line.SectionLine> next = new LinkedList<Line.SectionLine>();
            Collections.reverse(main);
          // for (int i=main.size()-1;i>0;i--){
             //  next.add(main.get(i));
         //  }
            //start=main.size()-1;


        }
        public void SetStartXY(){
            sx=main.get(start).x;
            sy=main.get(start).y;

        }
        public void Next(float s1){
            float s=s1;
            if (sy==main.get(main.size()-2).y){
                n-=1;
            }
            for (int i=n; i<main.size();i++){
                if (main.get(i).ly==0){
                   if (l+s>=Math.abs(main.get(i).lx)){
                       if(n==main.size()-1){
                           break;
                       }
                       sx=sx+(Math.abs(main.get(i).lx)-l)*(main.get(i).lx/(Math.abs(main.get(i).lx)));
                       s=l+s-Math.abs(main.get(i).lx);
                       l=0;
                       //continue;
                   } else {
                       l=l+s;
                       n=i;
                       sx=sx+s*(main.get(i).lx/(Math.abs(main.get(i).lx)));
                       break;
                   }
                } else {
                    if (main.get(i).lx==0){
                        if (l+s>=Math.abs(main.get(i).ly)){
                            if(n==main.size()-1){
                                break;
                            }
                            sy=sy+(Math.abs(main.get(i).ly)-l)*(main.get(i).ly/(Math.abs(main.get(i).ly)));
                            s=l+s-Math.abs(main.get(i).ly);
                            l=0;
                             //определитель направления
                           // continue;
                        } else {
                            l=l+s;
                            n=i;
                            sy=sy+s*((main.get(i).ly)/Math.abs(main.get(i).ly)); // определитель направления
                            break;
                        }
                    }

                }

            }

        }



    }

}
