import processing.sound.*;

SoundFile music;
SoundFile menusnd;
SoundFile menusel;
SoundFile diesnd;

Pixel[] objects = new Pixel[600];

Player p;
PFont ufont;
PFont sfont;
int objectnum=0;

color c1;
color c2;

int[] gradientBuffer = new int[10];

//Define key array to detect key press
boolean[] keys;
boolean cleanbuffer = false;
boolean released = true;
long[] lastime = new long[10];

long currentime = 0;

int phase = 0;

int rate = 300;
int vel = 10;
int size = 20;

int[] constrain = new int[2];

int bgcolor = 0;

int barrierspos = 0;

int maindirection = 0;

int spawnpoint = 0;

int lives = 0;

boolean dead = false;;

boolean menu = true;

boolean gradient = false;

int parpadeo = 0;
int parpadeocount = 0;

int cursorpos = -95;

int selmenu = 0;

long startime = 0;

void setup(){
  size(640, 480);
  noStroke();
  noSmooth();
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

void draw(){
  background(bgcolor);
  if(gradient){
   for(int y= 0; y < height; y++){
     float n = map(y, 0, height, 0, 1);
     color newc = lerpColor(c1, c2, n);
     stroke(newc);
     line(0, y, width, y);
    } 
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
    text("Easy - 10 Hits", width/2-140, height/2-80);
    fill(0, 255, 255);
    text("Normal - 5 Hits", width/2-140, height/2-30);
    fill(255, 0, 0);
    text("Hard - 2 Hits", width/2-140, height/2+30);
    fill(200, 0, 0);
    text("No hit - 0 Hits", width/2-140, height/2+80);
    
    switch(selmenu){
     case 0:
     cursorpos = -95;
     lives = 10;
     break;
     
     case 1:
     cursorpos = -45;
     lives = 5;
     break;
     
     case 2:
     cursorpos = 12;
     lives = 2;
     break;
     
     case 3:
     cursorpos = 65;
     lives = 0;
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
   
  if(currentime > 136000 && phase == 18){
    phase = 19;
  }
  
  if(currentime > 142000 && phase == 19){
    phase = 20;
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
      objects[objectnum] = new Pixel(barrierspos, -80, 20, size, size+80, color(255,255,255), true, 0);
      objectnum++;
      objects[objectnum] = new Pixel(width-barrierspos, -80, 20, size, size+80, color(255,255,255), true, 0);
      objectnum++;
      objects[objectnum] = new Pixel(-80, barrierspos, 20, size+80, size, color(255,255,255), true, 2);
      objectnum++;
      objects[objectnum] = new Pixel(-80, height-barrierspos, 20, size+80, size, color(255,255,255), true, 2);
      objectnum++;
      if(barrierspos < 200){
        barrierspos+=5;
        constrain[0]+=5;
        constrain[1]-=5;
        rate+=5;
      }else{
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
      rate = 300;
      vel = 10;
      size = 20;
      println(rate);
      break;
      case 18:
      if(vel < 15) vel++;
      gradient = true;
      c2 = color(gradientBuffer[0], 0, 0);
      if(gradientBuffer[0] < 255) gradientBuffer[0]++;
      break;
      case 19:
      vel = 5;
      if(rate>1) rate-=2;
      c2 = color(gradientBuffer[0], 0, 0);
      if(gradientBuffer[0] > 2)gradientBuffer[0]-=2;
      break;
      case 20:
      c2 = color(gradientBuffer[0], 0, 0);
      gradientBuffer[0]+=2;
      break;
    } 
  }
  
  if(currentime-lastime[0] > rate){
    lastime[0] = currentime;
    if(objectnum > 490){
       cleanbuffer = true;
       objectnum = 0;
       
     }
     if(maindirection < 2){
       objects[objectnum] = new Pixel(int(random(constrain[0], constrain[1])), spawnpoint, vel, size, size, color(255,255,255), true, maindirection);
     }else{
       objects[objectnum] = new Pixel(spawnpoint, int(random(constrain[0], constrain[1])), vel, size, size, color(255,255,255), true, maindirection); 
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
  
  
  fill(color(72, 229, 255));
  textFont(ufont);
  text("Time: "+((millis()-startime+1700)/1000), 10, 30);
  text("Hits: "+(lives), 10, 60);
  textFont(sfont);
  text("FPS: "+int(frameRate), width-40, 10);
  
}


class Player{
  int xpos, ypos, size, speed;
  color pcolor;
  
  Player (int x, int y, int s, int sp, color c) {  
    xpos = x;
    ypos = y;
    size = s;
    speed = sp;
    pcolor = c;
  }
  
  void update(){
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

class Pixel { 
  int xpos, ypos, mass, sizex, sizey, fallpos;
  color pxcolor;
  boolean gravity;

  
  Pixel (int x, int y, int m, int sx, int sy, color c, boolean g, int f) {  
    xpos = x;
    ypos = y;
    mass = m;
    pxcolor = c;
    gravity = g;
    sizex = sx;
    sizey = sy;
    fallpos = f;
  }
  
  void update(int pxpos, int pypos){
    fill(pxcolor);
    rect(xpos, ypos, sizex, sizey);
    
    if(pxpos > xpos-size && pxpos < xpos+size){
      if(pypos > ypos-size && pypos < ypos+size){
        //TODO: DIE
        
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
    }
    
  }
  
  void physics(){
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

void keyPressed()
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
void keyReleased()
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
