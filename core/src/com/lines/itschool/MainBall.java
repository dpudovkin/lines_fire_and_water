package com.lines.itschool;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


/**
 * Created by Марина on 24.02.2017.
 */

public class MainBall extends Map.MyActor {
    public int Z;
    private  int SIZE_H;
    private  int SIZE_W;
    private  static int deltaSize=Line.SIZE_LINE/2;
   Texture img;
   int createcount=0;
   int nexttime=0;
   float x,y;
   boolean endanim;
   boolean dispose;
   boolean show;
   int boostcreate=1;
   int typeanim; // 1 - create 2 - увеличение 3 - уменьшене 4 - уничтожение
   private  static float deltatime;


    private int type;

    public static void setDeltaSize(int deltaSiz){
    deltaSize=deltaSiz;
    }

    public void setType(int type){
    this.type=type;
    }

    public void setDeltatime(float deltatime){
    this.deltatime=deltatime;
    }


    public void setListener(){
        this.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(MyGame.gamescreen.getMaintype()!=type ){
                if (GameScreen.getMaintype()!=-1) {
                for (MainBall a: GameScreen.mainballs) {
                if (a.getType()==MyGame.gamescreen.getMaintype()) {
                    a.setAnimationType(3);
                    if (MyGame.gamescreen.lasttypewall==a.type && MyGame.gamescreen.form.usequeu){
                       a.dispose=true;
                    }
                    break;
                }
                }
                }
                if ( !MyGame.gamescreen.stop){
                GameScreen.setMaintype(type);
                boostcreate=5;
                setAnimationType(2);}}
                return true;
            }
        });
    }




    public void setAnimationType(int animationType){
        this.typeanim=animationType;
        switch (typeanim) {
            case 1: show=true;
                setTouchable(Touchable.disabled);
            SIZE_H=MyGame.gamescreen.form.getSizeBall();
                break;
            case 2: SIZE_H=(int)getHeight();
            break;
            case 3: SIZE_H=(int)getHeight();
            break;
            case 4: createcount=0;
                setTouchable(Touchable.disabled);
            break;
        }
        createcount=0;

    }

    public void nextAnimDispose(){
        nexttime += deltatime*1000;
        createcount += 1;
        if (nexttime >= deltatime) {
            if (getWidth() * (1 - createcount * 0.02f) >Line.SIZE_LINE/2) {
                float w = getWidth();
                float h = getHeight();
                setWidth(getWidth() * (1 - createcount * 0.02f));
                setHeight(getHeight() *(1 - createcount * 0.02f));
                setPosition(getX() + (w-getWidth()) / 2, getY() + (h-getHeight()) / 2);

            } else {
                createcount=0;
                setPosition(getX()+(getWidth()-Line.SIZE_LINE/2)/2,getY()+(getHeight()-Line.SIZE_LINE/2)/2);
                setSize(Line.SIZE_LINE/2,Line.SIZE_LINE/2);
                show=false;
                endanim = true;
                typeanim=-1;
                dispose=false;
                MyGame.gamescreen.form.nextType(type);
                setTouchable(Touchable.enabled);
            }
            nexttime = 0;
        }

    }

    public void nextAnimActiveType(){
        nexttime += deltatime*1000;
        createcount += 1;
        if (nexttime >= deltatime) {
            if (getWidth() * (1.05f+createcount*0.004f) <= SIZE_H+deltaSize) {
                float w=getWidth();
                float h=getHeight();
                setWidth(getWidth() * (1.05f+createcount*0.004f));
                setHeight(getHeight() * (1.05f+createcount*0.004f));
                setPosition(getX()-(getWidth()-w)/2,getY()-(getHeight()-h)/2);

            } else {
                createcount=0;
                setPosition(getX()-(SIZE_H+Line.SIZE_LINE/2-getWidth())/2,getY()-(SIZE_H+Line.SIZE_LINE/2-getHeight())/2);
                setSize(SIZE_H+Line.SIZE_LINE/2, SIZE_H+Line.SIZE_LINE/2);
                endanim = true;
                typeanim=-1;
            }
            nexttime = 0;
        }

    }

    public void nextAnimUnActiveType(){
        nexttime += deltatime*1000;
        createcount += 1;
        if (nexttime >= deltatime) {
            if (getWidth() * (1-createcount*0.004f) >= SIZE_H-deltaSize) {
                float w=getWidth();
                float h=getHeight();
                setWidth(getWidth() * (1-createcount*0.004f));
                setHeight(getHeight() * (1-createcount*0.004f));
                setPosition(getX()+(w-getWidth())/2,getY()+(h-getHeight())/2);

            } else {
                createcount=0;
                setPosition(getX()+(getWidth()-SIZE_H+Line.SIZE_LINE/2)/2,getY()+(getHeight()-SIZE_H+Line.SIZE_LINE/2)/2);
                setSize(SIZE_H-Line.SIZE_LINE/2, SIZE_H-Line.SIZE_LINE/2);
                endanim = true;
                typeanim=-1;
                if (dispose){
                    setAnimationType(4);
                }
            }
            nexttime = 0;
        }
    }

    public void nextAnimcreate(){
        nexttime += deltatime*1000;
        createcount += 1;

        if (nexttime >= deltatime) {
            if (getWidth() * (1.05f+createcount*0.004f*boostcreate) <= SIZE_H) {
            float w=getWidth();
            float h=getHeight();
                setWidth(getWidth() * (1.05f+createcount*0.004f*boostcreate));
                setHeight(getHeight() * (1.05f+createcount*0.004f*boostcreate));
                setPosition(getX()-(getWidth()-w)/2,getY()-(getHeight()-h)/2);

            } else {
                setTouchable(Touchable.enabled);
                createcount=0;
                boostcreate=5;
                setSize(SIZE_H, SIZE_H);
                setPosition(x,y);
                endanim = true;
                typeanim=-1;
            }
            nexttime = 0;
        }
    }

    public void setActivType(){
        setSize(this.getWidth()+deltaSize,this.getHeight()+deltaSize);
        setPosition(getX()-deltaSize,getY()-deltaSize);
    }

    public void setUnActiveType(){
        setSize(this.getWidth()-deltaSize,this.getHeight()-deltaSize);
        setPosition(getX()+deltaSize,getY()+deltaSize);
    }


    public void setSize(int sizeh){
        SIZE_H=sizeh;
        SIZE_W=sizeh;
        x=getX();
        y=getY();
        setPosition(getX()+(SIZE_W-getWidth())/2,getY()+(SIZE_H-getHeight())/2);
    }
    public int getType(){
        return type;
    }

    public void ReTypeImg(){
        switch (type) {
            case 0:  //img = new Texture("W_BALL.png");
                img = Configuration.mainmanager.get("W_BALL.png",Texture.class);
                break;
            case 1:   //img= new Texture("F_BALL.png");
                img =  Configuration.mainmanager.get("F_BALL.png",Texture.class);
                break;
            case 2:    //img = new Texture("M_BALL.png");
                img= Configuration.mainmanager.get("M_BALL.png",Texture.class);
                break;
            case 3:  //img = new Texture("MG_BALL.png");
                img= Configuration.mainmanager.get("MG_BALL.png",Texture.class);
                break;
            case 4:  //img = new Texture("WD_BALL.png");
                img= Configuration.mainmanager.get("WD_BALL.png",Texture.class);
                break;
            case 5:   //img = new Texture("A_BALL.png");
               img= Configuration.mainmanager.get("A_BALL.png",Texture.class);
                break;
        }
        setSize(Line.SIZE_LINE/2,Line.SIZE_LINE/2);
        img.setFilter(Configuration.mainfilter,Configuration.mainfilter);
    }



    public MainBall(int type){
        this.type=type;
        ReTypeImg();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
    if (typeanim!=-1 && !Configuration.pauseanimation) {
    switch (typeanim) {
            case 1: nextAnimcreate();
                break;
            case 2: nextAnimActiveType();
                break;
            case 3: nextAnimUnActiveType();
                break;
        case 4: nextAnimDispose();
        break;
    }
    }
       if (show){
        batch.draw(img, getX(),getY(), getWidth(), getHeight());}
    }









}