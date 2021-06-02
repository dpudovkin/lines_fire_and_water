package com.lines.itschool;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.IntAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created by Марина on 11.05.2017.
 */

public abstract class CustomAction {
// класс в котором собраны классы некотрых действий актеров
public static  class ScoreAction extends IntAction{
private static float DURATION = 2f;
Label label;

public static ScoreAction scoreAction(int score, Label label){
ScoreAction action = Actions.action(ScoreAction.class);
action.setStart(0);
action.setEnd(score);
action.setDuration(2f);
action.label=label;
return  action;
}
    @Override
    protected void update(float percent) {
        super.update(percent);
        if (label!=null){
            label.setText(String.valueOf(getValue()));

        }
    }
}

public static class AddAction{

public static void AddSkillAction(Component.Skill s){

    MoveToAction moveToAction = new MoveToAction();
    moveToAction.setPosition(s.getX(),s.getY());
    moveToAction.setDuration(2.5f);
    moveToAction.setInterpolation(Interpolation.sine);
    s.addAction(moveToAction);
    s.setY(s.getY()*1.1f);
    }




}





}
