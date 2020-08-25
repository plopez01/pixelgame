import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class pixelgamesecurity extends PApplet {



SoundFile music;
SoundFile menusnd;
SoundFile menusel;
SoundFile diesnd;

Pixel[] objects = new Pixel[600];

int[] warnpos = new int[50];

int warnlen = 0;

boolean lazer = false;

int lazerintensity = 255;


Player p;
PFont ufont;
PFont sfont;
int objectnum=0;

int c1;
int c2;

int[] gradientBuffer = new int[10];

//Define key array to detect key press
boolean[] keys;
boolean cleanbuffer = false;
boolean released = true;
long[] lastime = new long[10];

long currentime = 0;

int phase = 0;

int maxlives = 0;

int rate = 300;
int vel = 10;//10
int size = 20;

int[] constrain = new int[2];

int bgcolor = 0;

int pcolorR = 255;

int pcolorG = 255;

int pcolorB = 255;

int barrierspos = 0;

int maindirection = 0;

int spawnpoint = 0;

int lives = 0;

boolean dead = false;;

boolean menu = true;

boolean gradient = false;

boolean rgbsplit = false;

int parpadeo = 0;
int parpadeocount = 0;

int cursorpos = -95;

int selmenu = 0;

long startime = 0;

public void setup(){
  
  noStroke();
  
  keys=new boolean[5];
  keys[0]=false;
  keys[1]=false;
  keys[2]=false;
  keys[3]=false;
  keys[4]=false;
  
  constrain[0] = 0;
  constrain[1] = width;
  
  c1 = color(0, 0, 0);
  c2 = color(0, 0, 0);
  
  lastime[0] = 0;
  lastime[1] = 0;
  lastime[2] = 0;
  lastime[3] = 0;
  lastime[4] = 0;
  lastime[5] = 0;
  lastime[6] = 0;
  lastime[7] = 0;
  lastime[8] = 0;
  lastime[9] = 0;
  
  gradientBuffer[0] = 0;
  gradientBuffer[1] = 0;
  gradientBuffer[2] = 0;
  
  ufont = createFont("hachicro.ttf", 25);
  sfont = createFont("Arial.ttf", 10);
  music = new SoundFile(this, "music.wav");
  menusnd = new SoundFile(this, "menusnd.wav");
  menusel = new SoundFile(this, "menusel.wav");
  diesnd = new SoundFile(this, "diesnd.wav");
  
  
  p = new Player(width/2, height/2, 10, 4, color(72, 229, 255));
  //music.play();
}

public void draw(){
  background(bgcolor);
  if(gradient){
   for(int y= 0; y < height; y++){
     float n = map(y, 0, height, 0, 1);
     int newc = lerpColor(c1, c2, n);
     stroke(newc);
     line(0, y, width, y);
    }
    noStroke();
  }
  if(dead){
    objectnum = 0;
    vel = 0;
    size = 0;
    p.pcolor = color(255, 0, 0);
    if(currentime-lastime[4] > 800){
      p.pcolor = color(0);
      if(currentime-lastime[3] > 10){
        lastime[3] = currentime;
        bgcolor+=10;
        if(bgcolor > 250){
            //delay(1000);
            menu=true;
            dead = false;
            rate = 300;
            vel = 10;
            size = 20;
            phase = 0;
            bgcolor = 0;
            warnlen = 0;
            rgbsplit = false;
            lazer = false;
            
            lazerintensity = 255;

            barrierspos = 0;
            
            maindirection = 0;
            
            spawnpoint = 0;
            constrain[0] = 0;
            constrain[1] = width;
            
            gradient = false;
            
            lastime[0] = 0;
            lastime[1] = 0;
            lastime[2] = 0;
            lastime[3] = 0;
            lastime[4] = 0;
            lastime[5] = 0;
            lastime[6] = 0;
            lastime[7] = 0;
            lastime[8] = 0;
            lastime[9] = 0;
            
            pcolorR = 255;

            pcolorG = 255;
          
            pcolorB = 255;

            
            p.pcolor = color(72, 229, 255);
            for(int i = 0; i<objects.length; i++){
              if(objects[i] != null){
                objects[i] = null;
              }
                
          }
        }
      }
    }
  }
          
  
  if(menu){
    textFont(ufont);
    fill(255);
    rect(width/2-165, height/2+cursorpos, 15, 10);
    fill(0, 255, 0);
    text("Easy - 15 Hits", width/2-140, height/2-80);
    fill(0, 255, 255);
    text("Normal - 10 Hits", width/2-140, height/2-30);
    fill(255, 0, 0);
    text("Hard - 5 Hits", width/2-140, height/2+30);
    fill(200, 0, 0);
    text("No hit - 0 Hits", width/2-140, height/2+80);
    
    
    switch(selmenu){
     case 0:
     cursorpos = -95;
     maxlives = 15;
     break;
     
     case 1:
     cursorpos = -45;
     maxlives = 10;
     break;
     
     case 2:
     cursorpos = 12;
     maxlives = 5;
     break;
     
     case 3:
     cursorpos = 65;
     maxlives = 1;
     break;
    }
    
    if(keys[0] && released){
      released = false;
      selmenu--;
      menusnd.play();
    }
    
    if(keys[1] && released){
      released = false;
      selmenu++;
      menusnd.play();
    }
    
    if(selmenu > 3) selmenu = 0;
    if(selmenu < 0) selmenu = 3;
    
    if(keys[4]){
      lives = maxlives;
      menu = false;
      menusel.play();
      music.play();
      startime = millis();
    }
    
    return;
  }

  currentime = millis()-startime+1700;

  if(currentime > 10000 && phase == 0){
    phase = 1;
  }
  if(currentime > 38700 && phase == 1){
    phase = 2;
  }
  
  if(currentime > 50000 && phase == 5){
    phase = 6;
  }
  
  if(currentime > 59000 && phase == 7){
    phase = 8;
  }
  
  if(currentime > 77900 && phase == 8){
    phase = 9;
  }
  
  if(currentime > 87900 && phase == 11){
    phase = 12;
  }
  
  if(currentime > 88900 && phase == 11){
    phase = 12;
  }
  
  if(currentime > 97800 && phase == 12){
    phase = 13;
  }
  
  if(currentime > 107000 && phase == 13){
    phase = 15;
  }
  
  if(currentime > 116500 && phase == 16){
    phase = 17;
  }
  
  if(currentime > 125000 && phase == 17){
    phase = 18;
   }
   
  if(currentime > 135900 && phase == 18){
    phase = 19;
  }
  
  if(currentime > 142000 && phase == 19){
    phase = 20;
  }
  
  if(currentime > 155000 && phase == 20){
    phase = 21;
  }
  if(currentime > 193900 && phase == 23){
    rate = 10000;
    size = 0;
    try{
        for(int i = 0; i<objects.length; i++){
          if(objects[i] != null){
            objects[i].sizex = 0;
            objects[i].sizey = 0;
          }
          
        }
    }catch(NullPointerException e){
      println(e);
    }
  }
  if(currentime > 196000 && phase == 23){
    phase = 24;
  }
  
  if(currentime > 211500 && phase == 24){
    phase = 25;
  }
  
  if(currentime > 233500 && phase == 28){
    phase = 29;
  }
  
  
  /*if(currentime > 238000 && phase == 30){
    phase = 31;
  }*/
  
  if(currentime > 243000 && phase == 31){
    phase = 29;
  }
  
  
  
  //Progression LVL1
  if(currentime-lastime[1] > 50){
    lastime[1] = currentime;
    switch(phase){
      case 1:
      if(rate > 100) rate-=1;

       
       break;
       case 2:
      if(bgcolor < 199){
       bgcolor+=100;
      }else{
       phase = 3;
       
      }
      break;
      case 3:
      bgcolor-=20;
      if(bgcolor < 100)phase = 4;
      size = 15;
      break;
      case 4:
      if(bgcolor < 199){
       bgcolor+=50;
      }else{
       phase = 5;
       size = 10;
       rate = 40;
      }
      break;
      case 5:
      if(bgcolor > 0) bgcolor-=10;
      break;
      case 6:
      vel = 6;
      rate = 50;
      phase = 7;
      break;
      case 7:
      vel = 8;
      phase = 6;
      break;
      case 8:
      objects[objectnum] = new Pixel(barrierspos, -80, 20, size, size+80, color(255,255,255), true, 0, false);
      objectnum++;
      objects[objectnum] = new Pixel(width-barrierspos, -80, 20, size, size+80, color(255,255,255), true, 0, false);
      objectnum++;
      objects[objectnum] = new Pixel(-80, barrierspos, 20, size+80, size, color(255,255,255), true, 2, false);
      objectnum++;
      objects[objectnum] = new Pixel(-80, height-barrierspos, 20, size+80, size, color(255,255,255), true, 2, false);
      objectnum++;
      
      
      if(barrierspos < 200){
        barrierspos+=5;
        constrain[0]+=5;
        constrain[1]-=5;
        rate+=5;
        
      }else{
        if(currentime-lastime[7] > 200){
          lastime[7] = currentime;
          objects[objectnum] = new Pixel(-5, 0, 5, barrierspos+5, size-4, color(255,255,255), true, 0, false);
          objectnum++;
          objects[objectnum] = new Pixel(width-barrierspos, 0, 5, barrierspos+5, size-4, color(255,255,255), true, 0, false);
          objectnum++;
        }
        if(rate > 100){
         rate-=2;
        }
      }
      break;
      case 9:
        bgcolor=255;
        phase = 10;
      break;
      case 10:
      bgcolor=0;
      phase = 9;
      if(currentime > 78150){
        phase = 11;
      }
      break;
      case 11:
      rate = 50;
      size = 15;
      maindirection = 2;
      constrain[0]=0;
      constrain[1]=height;
      break;
      case 12:
      maindirection = 3;
      spawnpoint = width;
      constrain[0]=0;
      constrain[1]=height;
      break;
      case 13:
      maindirection = 1;
      spawnpoint = height;
      constrain[0]=0;
      constrain[1]=width;
      phase = 14;
      vel = 10;
      break;
      case 14:
      vel = 6;
      phase = 13;
      break;
      case 15:
      maindirection = 0;
      spawnpoint = 0;
      constrain[0]=0;
      constrain[1]=width;
      phase = 16;
      vel = 10;
      break;
      case 16:
      vel = 6;
      phase = 15;
      break;
      case 17:
      lives = maxlives;
      rate = 300;
      vel = 10;
      size = 20;
      rate-=1;
      println(rate);
      break;
      case 18:
      if(rate > 100) rate-=1;
      gradient = true;
      c2 = color(gradientBuffer[0], 0, 0);
      if(gradientBuffer[0] < 255) gradientBuffer[0]++;
      break;
      case 19:
      vel = 4;
      if(rate>60) rate-=2;
      if(pcolorR > 15) pcolorR-=5;
      if(pcolorG > 15) pcolorG-=5;
      if(pcolorB > 15) pcolorB-=5;
      c2 = color(gradientBuffer[0], 0, 0);
      if(gradientBuffer[0] > 2)gradientBuffer[0]-=2;
      break;
      case 20:
      c2 = color(gradientBuffer[0], 0, 0);
      gradientBuffer[0]+=2;
      break;
      case 21:
      if(pcolorR < 255) pcolorR+=15;
      if(pcolorG < 255) pcolorG+=15;
      if(pcolorB < 255) pcolorB+=15;
      size = 15;
      //rate = 120;
      c2 = color(gradientBuffer[0], 0, 0);
      if(gradientBuffer[0] > 2){
        gradientBuffer[0]-=2;
      }else{
        gradient = false;
        gradientBuffer[0] = 0;
      }
      maindirection = 3;
      spawnpoint = width;
      constrain[0]=0;
      constrain[1]=height;
      phase = 22;
      break;
      case 22:
      maindirection = 0;
      spawnpoint = 0;
      constrain[0]=0;
      constrain[1]=width;
      if(currentime >= 175000){
        phase = 23;
      }else{
       phase = 21; 
      }
      break;
      case 23:
      maindirection = 2;
      spawnpoint = 0;
      constrain[0]=0;
      constrain[1]=height;
      phase = 21;
      break;
      case 24:
      maindirection = 0;
      spawnpoint = 0;
      constrain[0]=0;
      constrain[1]=width;
      size = 30;
      rate = 100;
      vel = 12;
      if(currentime-lastime[7] > 200){
        lastime[7] = currentime;
        try{
          for(int i = 0; i<objects.length; i++){
            if(objects[i] != null){
               
                  objects[i].sizex = PApplet.parseInt(random(10, size));
                  objects[i].sizey = PApplet.parseInt(random(10, size));
                objects[i].pxcolor = color(PApplet.parseInt(random(0, 255)), PApplet.parseInt(random(0, 255)), PApplet.parseInt(random(0, 255)));
            }
            
          }
        }catch(NullPointerException e){
          println(e);
        }
      }
      
      break;
      case 25:
      if(bgcolor < 199){
       bgcolor+=100;
      }else{
       phase = 26;
       
      }
      break;
      case 26:
      bgcolor-=20;
      if(bgcolor < 100) phase = 27;
      size = 15;
      break;
      case 27:
      if(bgcolor < 199){
       bgcolor+=50;
      }else{
       phase = 28;
       size = 10;
       rate = 40;
      }
      break;
      case 28:
      if(bgcolor > 0){
        bgcolor-=10;
      }else{
        rgbsplit = true;
      }
      
      try{
          for(int i = 0; i<objects.length; i++){
            if(objects[i] != null){
              objects[i].sizex = PApplet.parseInt(random(size, size*2));
              objects[i].sizey = PApplet.parseInt(random(size, size*2));
            }
            
          }
        }catch(NullPointerException e){
          println(e);
        }
      break;
      
      case 29:
      lazer = true;
      rate = 100000;
      size = 0;
      if(warnlen < 47){
          warnpos[warnlen] = PApplet.parseInt(random(height));
          warnlen++;
        }else{
          if(lazerintensity == 255) lazerintensity = 0;
          phase = 30;

        }
       break;
       case 30:
       if(lazerintensity < 255)lazerintensity+=15;
       if(lazerintensity == 255) lazer=false;
       if(!lazer) phase = 31;
       break;
       case 31:
       lazerintensity = 255;
       warnpos[warnlen] = 0;
       if(warnlen > 0) warnlen--;
        
       rate = 100;
       size = 30;
       
      } 
  }
  

  
  if(currentime-lastime[0] > rate){
    lastime[0] = currentime;
    if(objectnum > 490){
       cleanbuffer = true;
       objectnum = 0;
       
     }
     if(maindirection < 2){
       objects[objectnum] = new Pixel(PApplet.parseInt(random(constrain[0], constrain[1])), spawnpoint, vel, size, size, color(pcolorR,pcolorB,pcolorG), true, maindirection, rgbsplit);
     }else{
       objects[objectnum] = new Pixel(spawnpoint, PApplet.parseInt(random(constrain[0], constrain[1])), vel, size, size, color(pcolorR,pcolorB,pcolorG), true, maindirection, rgbsplit); 
     }
     
     objectnum++;
  }
  
 
  
  
  if(parpadeocount > 11){
    parpadeo = 0;
    parpadeocount = 0;
  }
  if(parpadeo == 1){
    if(currentime-lastime[5] > 50){
     lastime[5] = currentime;
     p.pcolor = color(10, 92, 106);
     parpadeo = 2;
     parpadeocount++;
    }
  }
  if(parpadeo == 2){
    if(currentime-lastime[6] > 100){
     lastime[6] = currentime;
     p.pcolor = color(72, 229, 255);
     parpadeo = 1;
     parpadeocount++;
    }
  }
  
  
  p.update();
  try{
    for(int i = 0; i<objects.length; i++){
      if(objects[i] != null){
        objects[i].update(p.xpos, p.ypos);
        objects[i].physics();
      }
      
    }
  }catch(NullPointerException e){
    println(e);
  }
  
  
   

   if(lazer){
     for(int i = 0; i<warnpos.length; i++){
      if(warnpos[i] != 0){
        if(lazerintensity > 254) fill(200, 0, 0);
        rect(0, warnpos[i], width, 10);
        fill(52-lazerintensity,209-lazerintensity,235-lazerintensity);
        rect(2, warnpos[i]+2, width-4, 6);
        if(p.xpos+10 > 0 && p.xpos < width){
          if(p.ypos+10 > warnpos[i] && p.ypos < warnpos[i]+10){
            if(lazerintensity == 0) hitCheck();
          }
        }
        
      }
      
    }
   }
  

  fill(color(72, 229, 255));
  textFont(ufont);
  text("Time: "+((millis()-startime+1700)/1000), 10, 30);
  text("Hits: "+(lives), 10, 60);
  textFont(sfont);
  text("FPS: "+PApplet.parseInt(frameRate), width-40, 10);
  
}


class Player{
  int xpos, ypos, size, speed;
  int pcolor;
  
  Player (int x, int y, int s, int sp, int c) {  
    xpos = x;
    ypos = y;
    size = s;
    speed = sp;
    pcolor = c;
  }
  
  public void update(){
    fill(pcolor);
    rect(xpos, ypos, size, size);
    
    if (keys[0] && keys[1]) {
        return;
      }
      if (keys[0]) 
      {  
        if (ypos <= 0) {
          ypos=0;
        } else {
          ypos-=speed;
        }
      }
      if (keys[1]) 
      {
        if (ypos >= height-size) {
          ypos=height-size;
        } else {
          ypos+=speed;
        }
      }
      if (keys[2]) 
      {  
        if (xpos <= 0) {
          xpos=0;
        } else {
          xpos-=speed;
        }
      }
      if (keys[3]) 
      {
        if (xpos >= width-size) {
          xpos = width-size;
        } else {
          xpos+=speed;
        }
      }
  }
}

public void hitCheck(){
  
  if(currentime-lastime[2] > 1000){
    lastime[2] = currentime;
    lives--;
    if(lives > 0){
      parpadeo = 1;
      return;
    }
  }else{
    return;
  }
  music.stop();
  diesnd.play();
  dead = true;
  lastime[4] = currentime;
          
}

class Pixel { 
  int xpos, ypos, mass, sizex, sizey, fallpos;
  int pxcolor;
  boolean gravity;
  boolean rgbSplit;
  
  Pixel (int x, int y, int m, int sx, int sy, int c, boolean g, int f, boolean rgb) {  
    xpos = x;
    ypos = y;
    mass = m;
    pxcolor = c;
    gravity = g;
    sizex = sx;
    sizey = sy;
    fallpos = f;
    rgbSplit = rgb;
  }
  
  public void update(int pxpos, int pypos){
    
    
    if(rgbSplit){
      fill(random(255), 0, 0);
      rect(xpos+random(-8, 8), ypos+random(-8, 8), sizex, sizey);
      fill(0, random(255), 0);
      rect(xpos+random(-6, 6), ypos+random(-6,6), sizex, sizey);
      fill(0, 0, random(255));
      rect(xpos+random(-4,4), ypos+random(-4,4), sizex, sizey);
    }
    
    fill(pxcolor);
    rect(xpos, ypos, sizex, sizey);
    
    if(pxpos+10 > xpos && pxpos < xpos+sizex){
      if(pypos+10 > ypos && pypos < ypos+sizey){
        
          hitCheck();
        }
    }
    
  }
  
  public void physics(){
   //Gravity 
   if(gravity){
     switch(fallpos){
      case 0:
      ypos+=mass;
      break;
      
      case 1:
      ypos-=mass;
      break;
      
      case 2:
      xpos+=mass;
      break;
      
      case 3:
      xpos-=mass;
      break;
     }
     
   }
   
  }
  
}

public void keyPressed()
{
  if (keyCode==UP)
    keys[0]=true;
  if (keyCode==DOWN)
    keys[1]=true;
  if (keyCode==LEFT)
    keys[2]=true;
  if (keyCode==RIGHT)
    keys[3]=true;
  if(keyCode==ENTER)
    keys[4]=true;
}
public void keyReleased()
{
  if (keyCode==UP)
    keys[0]=false;
    released = true;
  if (keyCode==DOWN)
    keys[1]=false;
    released = true;
  if (keyCode==LEFT)
    keys[2]=false;
  released = true;
  if (keyCode==RIGHT)
    keys[3]=false;
  if(keyCode==ENTER)
    keys[4]=false;
  released = true;
}
  public void settings() {  size(640, 480);  noSmooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "pixelgamesecurity" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
