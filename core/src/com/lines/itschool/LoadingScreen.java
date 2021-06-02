package com.lines.itschool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayList;


/**
 * Created by Марина on 02.06.2017.
 */

public class LoadingScreen implements Screen {
static boolean loadfont;
static BitmapFont font;
ThreadFont threadFont;
Map stage;
boolean load=false;
boolean animation=false;
ArrayList<Component.BigLine> lines;


    @Override
    public void show() {
        stage = new Map(new ScreenViewport(),true);
    lines=new ArrayList<Component.BigLine>();
    for (int i=0; i<3;i++){
        lines.add(new Component.BigLine(false,true));
        stage.addActor(lines.get(i));
        lines.get(i).setSize(Gdx.graphics.getWidth()/3,0);
        lines.get(i).setPosition(i*Gdx.graphics.getWidth()/3+Gdx.graphics.getWidth()/3-lines.get(i).getWidth()/2,0);

        if (i!=2){
            lines.get(i).addAction(Actions.sequence(Actions.delay(0.2f+i*1f),
             Actions.sizeTo(lines.get(i).getWidth(),Gdx.graphics.getHeight()*1.2f,1.2f, Interpolation.pow2Out)));
            }
        else {
            lines.get(i).addAction(Actions.sequence(Actions.delay(0.2f+i*1f),
                    Actions.sizeTo(lines.get(i).getWidth(),Gdx.graphics.getHeight()*1.2f,1.2f, Interpolation.pow2Out),Actions.run(new Runnable() {
                        @Override
                        public void run() {
                           animation=true;
                        }
                    })));
        }
        }

    threadFont = new ThreadFont();
    threadFont.start();


        Configuration.loadMenuManager();
        Configuration.loadMainManager();
        load=true;
       // stage.addActor();


    }

    @Override
    public void render(float delta) {
    if (loadfont && Configuration.mainmanager.update() && Configuration.menumanager.update() && load && animation ){
      load=false;
      MenuScreen.game.mainscreen=new MenuScreen();
      MenuScreen.game.setScreen(MenuScreen.game.mainscreen);

    }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(1,1,1,1);
        stage.act(Gdx.graphics.getDeltaTime());
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

    class ThreadFont extends Thread{

    @Override
    public void run() {
        final FreetypeFontLoader.FreeTypeFontLoaderParameter myMainFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        myMainFont.fontParameters.size = MenuScreen.fontsize/4*3;
        myMainFont.fontParameters.minFilter= Texture.TextureFilter.Linear;
        myMainFont.fontParameters.magFilter= Texture.TextureFilter.Linear;
        final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Roboto.ttf"));
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                font = generator.generateFont(myMainFont.fontParameters);
                font.setColor(Color.WHITE);
                loadfont=true;
            }
        });
        super.run();
    }
}
}
