package com.lines.itschool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Comparator;

/**
 * Created by Марина on 31.01.2017.
 */

public class Map extends com.badlogic.gdx.scenes.scene2d.Stage  {
    public static int CountLine = 3;
    public static int WidthLine;
    Texture back;
    private  boolean start;


    public Map(ScreenViewport screen,boolean start) {
        super(screen);
        this.start= start;
        if (!start){
         back =Configuration.mainmanager.get("back.jpg",Texture.class);}
        WidthLine = Gdx.app.getGraphics().getWidth() / CountLine;
    }

    @Override
    public void draw() {
        if (!start){
    this.getBatch().begin();
    this.getBatch().getColor().a=1;
   this.getBatch().draw(back,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
    this.getBatch().end();
    }

        super.draw();
    }

    @Override
    public boolean keyDown(int keyCode) {
        if(keyCode == Input.Keys.BACK){
        if (MenuScreen.class.isInstance(MenuScreen.game.getScreen()) && !MyGame.mainscreen.window.destroy){
            MyGame.mainscreen.window.close();
            return false;
        }  else{
            MyGame.mainscreen=new MenuScreen();
            MenuScreen.game.setScreen(MyGame.mainscreen);
            return false;
        }}else {
        return super.keyDown(keyCode);}
    }

    @Override
    public Array<Actor> getActors() {
        return super.getActors();
    }

    public void SortZIndex(){


        this.getActors().sort(new Comparator<Actor>() {
            @Override
            public int compare(Actor actor, Actor t1) {
                try {
                    MyActor one = (MyActor) actor;
                    MyActor two = (MyActor) t1;
                    if (one.Z>two.Z){
                        return 1;
                    } else {
                        if (one.Z<two.Z){
                            return -1;
                        } else {
                            return  0;
                        }
                    }

                } catch (ClassCastException e){
                    if (!MyActor.class.isInstance(actor)){
                        return  1;
                    } else {
                        if (!MyActor.class.isInstance(t1)){
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                }

            }
        });




    }

    public void setFocusTouchOnWindow(){
        for (int i=0; i<getActors().size;i++){
            if (!Component.WindowView.class.isInstance(getActors().get(i))){
                getActors().get(i).setTouchable(Touchable.disabled);

            }
        }
    }

    public void setFocusTouch(){
        for (int i=0; i<getActors().size;i++){
            if (!Component.WindowView.class.isInstance(getActors().get(i))){
                getActors().get(i).setTouchable(Touchable.enabled);
            } else {
            }
        }
    }

    public void removeLastActor(){
    getActors().removeIndex(getActors().size-1);
    }

    public void RemoveAllWindow(){
        for (int i=0; i<getActors().size;i++){
            if (Component.WindowView.class.isInstance(getActors().get(i))){
                getActors().removeIndex(i);
            }
        }
    }

    public boolean isShowWindow(){
    boolean next=false;
        for (int i=0; i<getActors().size;i++){
            if (Component.WindowView.class.isInstance(getActors().get(i))){
                next=true;
                break;
            }
        }
        return  next;
    }

    public static class MyActor extends Actor{
        public int Z;
        int createtime=0;
        int onetime=270;
        int nexttime=0;
        boolean endanim;


        public Boolean OverLaps(MyActor main){
            if ((main.getY()>=this.getY()) && (main.getY()<=(this.getY()+this.getHeight())) && (main.getX()>=this.getX()) && (main.getX()<=(this.getX()+this.getWidth())) ){
                return true;
            } else {
               if ((this.getY()>=main.getY()) && (this.getY()<=(main.getY()+main.getHeight())) && (this.getX()>=main.getX()) && (this.getX()<=(main.getX()+main.getWidth())) ){
                    return true;
                }
                return false;
            }

        }

        public Boolean InActor(float x,float y){
        boolean a=false;
        if (x>getX() && x<getX()+getWidth() && y>getY() && y<getY()+getHeight()){
        Gdx.app.log(" "+x+" "+y+" "+getWidth()+" "+getHeight()+" "+getX()+" "+getY(),"InActor");
            a=true;
        }
        return a;

        }



    }

    public static class Background extends MyActor{
    Texture img;
    public  Background(){
    img = new Texture("Interface/back.jpg");
    Z=-1;
    }

        @Override
        public void draw(Batch batch, float parentAlpha) {
        batch.draw(img,getX(),getY(),getWidth(),getHeight());
            super.draw(batch, parentAlpha);

        }


    }
















}
