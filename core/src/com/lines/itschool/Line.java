package com.lines.itschool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Created by Марина on 01.03.2017.
 */

public class Line extends Map.MyActor {
   transient ArrayList<Line> helpgenerate;
    private int EndLine;
    boolean nextanimation=false;
    private static int C=9;
    private static double K_H=0.3;
    public boolean hide=false;
    public transient static Texture H_LINE; //  код 1  (вектор = 1 или -1) -1 влево и 1 вправо
    //   код 2  (вектор = 0)
  static transient public  Texture R_LINE;  // код 3  (вектор меняется)
    public LinkedList<SectionLine> line;
    public static int WIDTH_AIR= Gdx.graphics.getWidth(); // ширина области создания линий
    public int HEIGHT_AIR = Gdx.graphics.getHeight(); // высота области создания линий
    public static int SIZE_LINE =WIDTH_AIR/C;
    private double k;
    transient Level main;

    final   static int SIZE_IMG=300;
    public int num;

    public static void  setC(int c){
    C=c;
    }

    public static  double getK_H(){
        return K_H;
    }
    public LinkedList<SectionLine> getSectionLine(){
        return line;
    }
    public Line(){
    }
    public void setMainLevel(Level main){
        this.main=main;
    }


    public Line( int EndLine,int num, Level main){
        onetime=180;
        this.main=main;
        this.EndLine=EndLine;
        this.num =num;
        line = new LinkedList<SectionLine>();
       // H_LINE = new Texture("H_LINE.png");
       // R_LINE = new Texture("R_LINE.png");
       H_LINE = Configuration.mainmanager.get("H_LINE.png",Texture.class);
       R_LINE = Configuration.mainmanager.get("R_LINE.png",Texture.class);
        k=(main.k);
        k=k/1000;
        helpgenerate= new ArrayList<Line>();
        for(int i=0; i<num-1;i++){
         helpgenerate.add(main.lines.get(i));
        }

    }

    public Line(int EndLine, int num, Level main, ArrayList<Line> helpgenerate){
        onetime=180;
        this.main=main;
        this.EndLine=EndLine;
        this.num =num;
        line = new LinkedList<SectionLine>();
        H_LINE = Configuration.mainmanager.get("H_LINE.png",Texture.class);
        R_LINE = Configuration.mainmanager.get("R_LINE.png",Texture.class);
        k=(main.k);
        k=k/1000;
        this.helpgenerate=helpgenerate;

    }


    public void Generate(){
        line.clear();
        int x_a =(int)(float)(Gdx.graphics.getWidth()/3*(EndLine-1)+Gdx.graphics.getWidth()/6-SIZE_LINE/2);
        int y_a=(int)(HEIGHT_AIR*K_H);
        int next;
        boolean cont=false;
        int vector = 0; // если 0 - вверх, если 1 - горизонтально
        while(y_a<HEIGHT_AIR+SIZE_LINE){
            next = (int) (Math.random() * k) + 1;
            if (next==3 && y_a<HEIGHT_AIR*K_H+SIZE_LINE*2){
                continue;
            }
           if (!helpgenerate.isEmpty() && Gdx.graphics.getHeight()-y_a>SIZE_LINE & y_a>HEIGHT_AIR*K_H+SIZE_LINE*2){
                for (int i=0; i<helpgenerate.size();i++){
                            for (int k=0; k<helpgenerate.get(i).getSectionLine().size();k++){
                                    SectionLine s = helpgenerate.get(i).getSectionLine().get(k);

                               if (s.type!=3) {
                                        if (s.x == x_a && s.y==y_a && vector==0 & s.vector!=vector){
                                            line.add(new SectionLine(x_a,y_a,2,0,0));
                                            y_a=y_a+SIZE_LINE;
                                            cont=true;
                                            break;
                                        }

                                        if ( s.x == x_a && s.y==y_a && vector==1 && x_a<(C-2)*SIZE_LINE & s.vector!=vector){
                                            line.add(new SectionLine(x_a,y_a,1,0,1));
                                            x_a=x_a+SIZE_LINE;
                                            cont=true;
                                            break;
                                        }

                                    if (vector==-1 & x_a>SIZE_LINE*3 & s.x == x_a-SIZE_LINE & s.y==y_a & s.vector!=vector){
                                        x_a=x_a-SIZE_LINE;
                                        line.add( new SectionLine(x_a,y_a,1,0,-1));
                                        cont=true;
                                        break;
                                    }
                                }
                                if (s.type==3){
                                    if (s.x == x_a && s.y==y_a+SIZE_LINE && vector==0){

                                        if (x_a<=SIZE_LINE*3){
                                                line.add(new SectionLine(x_a, y_a, 3, 0, 1)); //  add section line
                                                x_a = x_a + SIZE_LINE;
                                                vector=1;
                                             //  next=1;
                                                 cont=true;
                                                 break;
                                        } else {
                                            if (x_a>=SIZE_LINE*(C-3)) {
                                                    line.add(new SectionLine(x_a,y_a,3,270,-1)); //  add section line
                                                    vector=-1;
                                                   // next=-1;
                                                 cont=true;
                                                    break;
                                            } else {
                                              int rotate = (int)(Math.random()*2.30);
                                                if (rotate==2){

                                                    line.add(new SectionLine(x_a,y_a,3,270,-1));
                                                    vector=-1;
                                                   // next=-1;
                                                } else {
                                                    line.add(new SectionLine(x_a,y_a,3,0,1));
                                                    x_a=x_a+SIZE_LINE;
                                                    vector=1;
                                                   // next=1;
                                                }
                                                cont=true;
                                                 break;
                                            }
                                        }
                                }
                                    if (s.x == x_a-SIZE_LINE*2 && s.y==y_a && vector ==-1){
                                    x_a=x_a-SIZE_LINE;
                                    line.add(new SectionLine(x_a,y_a,3,90,0));
                                    y_a=y_a+SIZE_LINE;
                                    vector=0;
                                       // next=2;
                                    cont=true;
                                    break;
                                }
                                    if (s.x == x_a+SIZE_LINE && s.y==y_a && vector==1){
                                    line.add(new SectionLine(x_a,y_a,3,180,0));
                                    y_a=y_a+SIZE_LINE;
                                    vector=0;
                                       // next=2;
                                    cont=true;
                                    break;
                                }
                            }

                            }

                    if (cont){
                        cont=false;
                        break;
                    }


                }

            }

           if (Gdx.graphics.getHeight()-y_a<SIZE_LINE){
               if (vector!=0){
                   next=3;
               } else{
                   next=2;
               }
            }

            if (next==1){
                if (vector == 0 ){
                    continue;
                }
                if (vector==-1 && x_a<SIZE_LINE*2){
                    continue;
                }
                if (vector==1 && x_a>SIZE_LINE*(C-2)){
                    continue;
                }
                if (vector ==-1){
                    x_a=x_a-SIZE_LINE;
                    line.add( new SectionLine(x_a,y_a,1,0,-1));
                } else {
                    line.add(new SectionLine(x_a,y_a,1,0,1));
                    x_a=x_a+SIZE_LINE;
                }

            }
            if (next==2){
                if (vector!=0){
                    continue;
                }
                line.add(new SectionLine(x_a,y_a,2,0,0));
                y_a=y_a+SIZE_LINE;
            }
            if (next==3){
                int rotate;
                if (vector==0){
                    rotate = (int)(Math.random()*2.49);
                    if (rotate==2){
                        if (x_a<SIZE_LINE*2){
                            continue;
                        }
                        line.add(new SectionLine(x_a,y_a,3,270,-1));
                        vector=-1;
                    } else {
                        if (x_a>SIZE_LINE*(C-2)){
                            continue;
                        }
                        line.add(new SectionLine(x_a,y_a,3,0,1));
                        x_a=x_a+SIZE_LINE;
                        vector=1;
                    }

                                 } else {
                    if (vector==-1){
                        x_a=x_a-SIZE_LINE;
                        line.add(new SectionLine(x_a,y_a,3,90,0));
                        y_a=y_a+SIZE_LINE;
                        vector=0;
                    }
                    if (vector==1){
                        line.add(new SectionLine(x_a,y_a,3,180,0));
                        y_a=y_a+SIZE_LINE;
                        vector=0;
                    }

                }

            }

        }

    }

    public void RunHide(int onetime){
    hide=true;
    nexttime=0;
    createtime=0;
    endanim=false;
    this.onetime=onetime/line.size();
    }

    public void RunShow(int onetime){
        hide=false;
        nexttime=0;
        createtime=0;
        endanim=false;
        this.onetime=onetime/line.size();
    }

    public void HideLine(Batch batch,float parentAlpha){
    if (hide){
        if (!Configuration.pauseanimation) {
            nexttime += Gdx.graphics.getDeltaTime() * 1000;
            if (nexttime >= onetime) {
                nexttime = 0;
                createtime += onetime;
            }
        }

        for (int i = 0; i <line.size(); i++) {
        if (i<createtime/onetime+1){} else {
            line.get(i).draw(batch,parentAlpha);
        }
        }
        if (line.size()-createtime/onetime<1) {
            endanim = false;
            hide=false;
            MyGame.gamescreen.GenerateOneLine();
            MyGame.gamescreen.places.get(MyGame.gamescreen.unline).setTouchable(Touchable.enabled);
        } else {
            line.get(createtime/onetime).draw(batch,(1-(float)(nexttime)/(float)onetime));
        }

    }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (endanim && !hide) {
            for (int i = 0; i < line.size(); i++) {
                line.get(i).draw(batch, parentAlpha);
            }
        } else {
        if (!hide){
        if (!Configuration.pauseanimation){
        nexttime+=Gdx.graphics.getDeltaTime()*1000;
        if (nexttime>=onetime) {
        nexttime=0;
        createtime+=onetime;
        }
        if (line.size()-createtime/onetime<=2) {
        nextanimation=true;
        }}
            for (int i = line.size()-1; i >line.size()-1-(createtime / onetime); i--) {
                line.get(i).draw(batch, parentAlpha);
            }
            if (line.size()-1-createtime/onetime<0) {
            if ( MenuScreen.game.getScreen().equals(MyGame.gamescreen) && num-1==MyGame.gamescreen.unline){
                MyGame.gamescreen.unline=-1;
            }
                endanim = true;
            } else {
            line.get(line.size()-1-createtime/onetime).draw(batch,((float)(nexttime)/(float)onetime));
           // line.get(line.size()-1-createtime/onetime).animation=true;
            }
        } else{
        HideLine(batch,parentAlpha);
        }}
    }

    @Override
    protected Line clone() throws CloneNotSupportedException {
        Line a = new Line(EndLine,num,main);
        for (int i=0; i<line.size(); i++) {
            a.line.add(line.get(i).clone());
        }
        return a;
    }

    public static  class SectionLine {
         int x;
         int y;
         int type;
         int rotate;
        public transient TextureRegion img;
        int vector;
        int lx;
        int ly;
        float size;
        boolean animation;
        SectionLine(){


        }
        public void chooseTextureRegion(){
            if (type ==1){
                img=new TextureRegion(H_LINE,SIZE_IMG,SIZE_IMG);
            } else {
                if (type==2){
                    img=new TextureRegion(H_LINE,SIZE_IMG,SIZE_IMG);
                    this.rotate=90;
                } else {
                    img = new TextureRegion(R_LINE, SIZE_IMG, SIZE_IMG);
                }
            }
            size=SIZE_LINE;
        }

        public SectionLine(int x,int y,int type,int rotate,int vector){
            this.vector=vector;
            this.x=x;
            this.y=y;
            this.type=type;
            this.rotate=rotate;
           chooseTextureRegion();
            SetA(1);

        }



        public void SetA(int a){
            if (a==1){
                if (type==1){
                    lx=SIZE_LINE*vector;
                    ly=0;
                } else {
                    if (type==2){
                        lx=0;
                        ly=SIZE_LINE;
                    } else {
                       if (rotate==0){
                           lx=0;
                           ly=SIZE_LINE;
                        }
                        if (rotate==90){
                            ly=0;
                            lx=SIZE_LINE*-1;
                        }
                        if (rotate==180){
                            ly=0;
                            lx=SIZE_LINE;
                        }
                        if (rotate==270){
                           ly=SIZE_LINE;
                            lx=0;
                        }
                    }
                }
            } else{
               // vector=vector*-1;
                if (type==1){
                    lx=SIZE_LINE*vector*-1;
                    ly=0;
                } else {
                    if (type==2){
                        lx=0;
                        ly=SIZE_LINE*-1;
                    } else {
                        if (rotate==0){
                            lx=SIZE_LINE*-1;
                            ly=0;
                        }
                        if (rotate==90){
                            ly=SIZE_LINE*-1;
                            lx=0;
                        }
                        if (rotate==180){
                            ly=SIZE_LINE*-1;
                            lx=0;
                        }
                        if (rotate==270){
                            ly=0;
                            lx=SIZE_LINE;
                        }
                    }
                }
            }

        }

        public void draw(Batch batch, float parentAlpha) {
       byte k =1;
        int add=(int)(size-size*parentAlpha)/2;
        if (parentAlpha<1 && (vector==0 || (type==3 & vector!=1))){
        k=-1;
        }
            batch.draw(img,x+add*k,y+add,size/2,size/2,size*parentAlpha,size*parentAlpha,1,1,rotate);
        }

       @Override
       public SectionLine clone() throws CloneNotSupportedException {
           SectionLine next = new SectionLine(this.x,this.y,this.type,this.rotate,this.vector);
           return next;

       }


   }






}
