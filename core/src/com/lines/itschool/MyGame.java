package com.lines.itschool;


import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public  class MyGame extends Game {
	 SpriteBatch batch;
    static MenuScreen mainscreen;
    static GameScreen gamescreen;
    static int C=9;

   public MyGame(int C,double a){
  this.C=C;
   }

   public MyGame(){
   C=9;
   }
    @Override
	public void create () {
		batch = new SpriteBatch();
        mainscreen = new MenuScreen();
        mainscreen.setGame(this);
        setScreen(new LoadingScreen());


    }
    @Override
    public void render(){

        super.render();
    }

      public void showGame(){
          gamescreen = new GameScreen();
          this.setScreen(gamescreen);
      }


}