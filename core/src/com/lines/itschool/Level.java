package com.lines.itschool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Interpolation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by Марина on 05.04.2017.
 */
//класс отвечающий за информации об уровне, линиях и доступных шариков, игровой баланс.
public class Level  {
    int NumLev;
    int attempts;
    static HashMap<Integer,Integer> types;
     HashMap<Integer,Integer> skills;
    ArrayList<Integer> usestypes= new ArrayList<Integer>();
    int generatecount;
    static float aslow=1;


    public LinkedList<Line> lines;
    int kmaxspeed;
    int kminspeed;
    int boost;
    int timenext;
    int maxball;
    int k;
    public static LinkedList strtype;

    public  void createStringTypes(){
       strtype = new LinkedList<String>();
       strtype.add("WATER BALL");
       strtype.add("FIRE BALL");
       strtype.add("METALL BALL");
       strtype.add("MAGNETIC BALL");
        strtype.add("WOODEN BALL");
        strtype.add("ACID BALL");
    }
    private void loadAllTypes(){
        types=new HashMap<Integer, Integer>();
        types.put(0,1);
        types.put(1,1);
        types.put(2,1);
        types.put(3,30);
        types.put(4,55);
        types.put(5,100);
    }




    public Level(JSONLevel main) {
        loadAllTypes();
        maxball = main.maxball;
        k = main.k;
        boost = main.boost;
        kmaxspeed = main.kmaxspeed;
        kminspeed = main.kminspeed;
        NumLev = main.NumLev;
        attempts = main.attempts;
        timenext = main.timenext;
        generatecount=main.generatecount;
        for (Integer a:main.types) {
        usestypes.add(a);
        }
        skills = new LinkedHashMap<Integer, Integer>();
        for (int i=0;i<3;i++){
            skills.put(i,main.skills.get(i));
        }
        Collections.sort(usestypes);
        lines = new LinkedList<Line>();
            if (main.lines.isEmpty()) {
                for (int i = 0; i < 3; i++) {
                    Line add = new Line(i + 1, i + 1, this);
                    add.Generate();
                    lines.add(add);
                }
            } else {
                for (int i = 0; i < main.lines.size(); i++) {
                    try {
                        main.lines.get(i).setMainLevel(this);
                        lines.add(main.lines.get(i).clone());
                    } catch (CloneNotSupportedException e) {
                    }
                }
            }
            createStringTypes();
    }


    public Level(){
       skills = new HashMap<Integer, Integer>();
       skills.put(0,5);
       skills.put(1,5);
       skills.put(2,5);
        loadAllTypes();
        maxball=15;
        k=2300;
        boost=200;
        kmaxspeed=9700;
        kminspeed=900;
        NumLev=1;
        timenext=1500;
        generatecount=2;
        usestypes.add(0);
        usestypes.add(1);
        usestypes.add(2);
       // usestypes.add(3);
        //usestypes.add(4);
       // usestypes.add(5);
        lines=new LinkedList<Line>();
        for (int i=0; i<3;i++){
            Line add=new Line(i+1,i+1,this);
            add.Generate();
            lines.add(add);
        }
        createStringTypes();
    }

    public int[] AddNextSkill(){
    int[] a  = new int[skills.size()];
    int random;
        if (NumLev % 3==0){
        random = (int)(Math.random()*4);
            skills.put(2,skills.get(2)+random);
            a[2]=random;
        }
        random = (int)(Math.random()*2);
        skills.put(1,skills.get(1)+random);
        a[1]=random;
        random = (int)(Math.random()*2);
        skills.put(0,skills.get(0)+random);
        a[0]=random;
        return a;
    }

    public int[] AddSkill(){
    int[] a   =  new int[skills.size()];
    int add =(int)( Math.random()*100);
    if (add>60){
    int  random1 = (int)(Math.random()*skills.size());
    int random2 = (int)(Math.random()*4);
      a[random1]  =random2;
      skills.put(random1,random2);
    }
    return a;
    }

     public void GenerateLines(){
     if (generatecount>0){
         generatecount-=1;
         for (int i=0; i<lines.size();i++){
             lines.get(i).Generate();
         }
     }
     }



    public void nextLevel(){ // повышение сложности
         NumLev=NumLev+1;
         generatecount+=1;
         float startlevel=0;
         float endlevel=0;
        for (Integer i: types.keySet()) {
            if (types.get(i)==NumLev && !usestypes.contains(types.get(i))) {
                addType(i);
                break;
            }
        }
         for (Integer i:types.keySet()){
             if (i.equals(Collections.max(usestypes))){
               startlevel=types.get(i);
               if (Collections.max(types.keySet()).equals(i)){
               // регулируемое значение
                   endlevel=startlevel+200;
               } else {
                   endlevel=types.get(i+1);
               }
               break;
             }
         }
         int maxt=Collections.max(usestypes);
         Interpolation interpolation = Interpolation.sineOut;

       timenext=(int)interpolation.apply(1500,770+(maxt-1)*20,((float)NumLev-startlevel)/(endlevel-startlevel));
       kminspeed=(int)interpolation.apply(900,2600,((float)NumLev-startlevel)/(endlevel-startlevel));
       maxball=(int)interpolation.apply(15+(maxt-1)*5,75,((float)NumLev-startlevel)/(endlevel-startlevel));

        for (int i=0; i<lines.size();i++){
            lines.get(i).Generate();
        }

        Configuration.SaveLevel(this);

    }

    public String loadDescriptionBall(int type){
        FileHandle file = Gdx.files.internal("Balls/"+type+".txt");
        String send = file.readString();
        return send;
    }

    public void addType(int type){
        usestypes.add(type);
        Collections.sort(usestypes);
    }

   static class JSONLevel{
   int generatecount;
        int NumLev;
        int attempts;
        LinkedList<Integer>types= new LinkedList<Integer>();
        public LinkedList<Line> lines=new LinkedList<Line>();
        LinkedList<Integer> skills = new LinkedList<Integer>();
        int kmaxspeed;
        int kminspeed;
        int boost;
        int timenext;
        int maxball;
        int k; // коэффициент сложности дорожек
       public JSONLevel(){
       }
        public JSONLevel(Level main){
        this.generatecount=main.generatecount;
            maxball=main.maxball;
            k=main.k;
            boost=main.boost;
            kmaxspeed=main.kmaxspeed;
            kminspeed=main.kminspeed;
            NumLev=main.NumLev;
            attempts=main.attempts;
            timenext=main.timenext;
            skills.clear();
            types.clear();
            lines.clear();
            for (int i=0; i<3;i++){
               skills.add(main.skills.get(i));
            }
            for (Integer a:main.usestypes) {
            types.add(a);
            }
            if (!main.lines.isEmpty()){
                for (int i=0; i<main.lines.size();i++) {
                    try {
                        lines.add(main.lines.get(i).clone());
                    } catch (Exception e){
                    }
                }
            }

        }



    }

}
