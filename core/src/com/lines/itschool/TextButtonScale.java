package com.lines.itschool;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by Марина on 30.05.2017.
 */

public class TextButtonScale extends TextButton {
//класс в доработке но уже используеться для масштабирование текста
private float scaleX;
private  float scaleY;

    public void setScaleXY(float kx,float ky){
    scaleX=kx;
    scaleY=ky;
    }

    public TextButtonScale(String text, TextButtonStyle style) {
        super(text, style);
    }

    public TextButtonScale(String text, Skin skin) {
        super(text, skin);
    }

    @Override
    public void setSize(float width, float height) {
    if (scaleX!=0){
        getLabel().setFontScale(width*scaleX/MenuScreen.fontsize);
    }
    if (scaleY!=0){
        getLabel().setFontScale(height*scaleY/MenuScreen.fontsize);
    }

        super.setSize(width, height);
    }
}
