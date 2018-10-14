/**********バッチファイル用**********/
/*<applet code="change.class" width="640" height="480">
</applet>*/
/**********画像関連処理の準備**********/
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
/**********本体クラス**********/
public class change extends Applet implements Runnable{
  /**********フィールド変数**********/
  int field;
    static int mx,my;             //マウス座標
    static int pw,ph,pn;          //プレイヤーピクチャの情報
    static int red,green,blue;    //プレイヤーピクチャの色調
    static int cred,cgreen,cblue; //色調比較用
    static int enemyNumber;       //次に出現する敵の番号
    static int enemyStart;        //判定をし始める番号
    static long miliSec,imgSec;   //カウント用
    static int mSecG,mSecF;       //ウェイトするミリ秒
    static int countG;            //ゲームオーバーのカウント用
    static boolean gameover,con;  //ゲームオーバー処理用
    static boolean start1,start2=false;  //ゲームスタート用
    Thread th1,th2;               //スレッド用
    Image img,anime,buf;          //ダブルバッファリング、タイトル用
    Graphics ct;                  //オフスクリーン描画用
    Dimension dim;                //画面情報取得用
    /**********敵クラスオブジェクト**********/
    int enemyLength=10;
    Enemy[] enemy;
  /**********描画処理**********/
  public void paint(Graphics g){
    /**********オフスクリーン描画**********/
    if(ct==null) ct = buf.getGraphics();
    if(start1 && !start2){
      /**********実質背景設定**********/
      ct.setColor(new Color(255,255,255));
      ct.fillRect(0,0,dim.width,dim.height);
      /**********開始画面描画**********/
      img = getImage(getDocumentBase(),"title.png");
      ct.drawImage(img,0,0,dim.width,dim.height,this);
      int number=(int)imgSec/20;
      anime = getImage(getDocumentBase(),"start"+number+".png");
      ct.drawImage(anime,213,150,this);
    }else if(!gameover){
      /**********実質背景設定**********/
      ct.setColor(new Color(255,255,255));
      ct.fillRect(0,0,dim.width,dim.height);
      /**********プレイヤーを描画**********/
      ct.setColor(new Color(red,green,blue));
      ct.fillOval(mx-pw/2,my-ph/2,pw,ph);
      /**********敵を描画**********/
      for(int i=0;i<enemyLength;i++){
        /**********存在しているか判定**********/
        if(enemy[i].getExist()){
          /**********座標を測定**********/
          double X=enemy[i].getStartX()+enemy[i].getSpeedX()*(miliSec-enemy[i].getSecond());
          double Y=enemy[i].getSpeedY()*(miliSec-enemy[i].getSecond())-enemy[i].getHeight();
          enemy[i].setXY(X,Y);
          /**********画面外ならば、存在判定を消去**********/
          if(X>dim.width || Y>dim.height){enemy[i].setExist(false);}
          /**********色調を設定**********/
          switch(enemy[i].getColor()){
            case 0:ct.setColor(new Color(255,0,0));break;
            case 1:ct.setColor(new Color(0,255,0));break;
            case 2:ct.setColor(new Color(0,0,255));break;
          }
          /**********種類を設定、移動する(ついでにあたり判定)**********/
          switch(enemy[i].getKind()){
            case 0: double x0 = X+enemy[i].getWidth();
                    double y0 = Y+enemy[i].getHeight();
                    ct.drawLine((int)X,(int)Y,(int)x0,(int)y0);
                    for(byte r=0;r<enemy[i].getSpecial();r++){
                      ct.drawLine((int)X-r,(int)Y,(int)x0-r,(int)y0);
                      ct.drawLine((int)X,(int)Y-r,(int)x0,(int)y0-r);
                      ct.drawLine((int)X+r,(int)Y,(int)x0+r,(int)y0);
                      ct.drawLine((int)X,(int)Y+r,(int)x0,(int)y0+r);
                    }
                    //当たり判定
                    if(lineJudge(X,Y,x0,y0,i)){
                      collapse();
                    }
                    break;
            case 1: double x1 = X+enemy[i].getWidth();
                    double y1 = Y+enemy[i].getHeight();
                    ct.fillRect((int)X,(int)Y,enemy[i].getWidth(),enemy[i].getHeight());
                    //当たり判定(四本の線分ごとに判定＋内部判定)
                    if(lineJudge(X,Y,x1,Y,i) || lineJudge(X,Y,X,y1,i) || lineJudge(x1,Y,x1,y1,i) || lineJudge(X,y1,x1,y1,i) || innerJudge(X,x1,Y,y1,i)){
                      collapse();
                    }
                    break;
            case 2: ct.fillOval((int)X,(int)Y,enemy[i].getWidth(),enemy[i].getHeight());
                    //当たり判定
                    double xl=mx-X-enemy[i].getWidth()/2;
                    double yl=my-Y-enemy[i].getHeight()/2;
                    double cr=pw/2+enemy[i].getWidth()/2;
                    if(xl*xl+yl*yl<cr*cr){
                      //接触しているので、色調判定
                      switch(enemy[i].getColor()){
                        case 0:cred=255;cgreen=0;cblue=0;break;
                        case 1:cred=0;cgreen=255;cblue=0;break;
                        case 2:cred=0;cgreen=0;cblue=255;break;
                      }
                      if(!(cred==red && cgreen==green && cblue==blue)){
                        //色調不一致ならば成功
                        collapse();
                      }
                    }
                    break;
          }
        }
      }
    }else{
      /**********ゲームオーバー時描画**********/
      if(countG<1500){
        countG+=20;
        ct.setColor(new Color(red,green,blue));
        ct.fillOval(mx-pw/2-countG/2,my-ph/2-countG/2,pw+countG,ph+countG);
      }else{
        String str1 = "Game Over";
        ct.setFont(new Font("Serif",Font.BOLD + Font.ITALIC,100));  //フォント設定
        FontMetrics fo1 = ct.getFontMetrics();  //フォント情報取得の準備
        ct.setColor(new Color(0,0,0));
        ct.drawString(str1,dim.width/2 - fo1.stringWidth(str1)/2,dim.height/2 - fo1.getHeight()/2);
        String str2 = "Click to restart";
        ct.setFont(new Font("Serif",Font.BOLD,50));  //フォント設定
        FontMetrics fo2 = ct.getFontMetrics();  //フォント情報取得の準備
        ct.drawString(str2,dim.width/2 - fo2.stringWidth(str2)/2,dim.height/2 - fo2.getHeight()/2+100);
        con=true;
      }
    }
    /**********オフスクリーンから、実際に描画**********/
    g.drawImage(buf,0,0,this);
  }
  public void update(Graphics g){paint(g);} //再描画を防ぐ

  /**********最初期処理**********/
  public void init(){
    /**********敵情報の初期化*********/
    enemy = new Enemy[enemyLength];
    for(byte i=0;i<enemyLength;i++){
      enemy[i]=new Enemy();
    }
    setFirst();
    /**********スレッド処理*********/
    th1 = new Thread(this);
    th1.start();
    /**********ダブルバッファリング用のオフスクリーンを作っておく**********/
    dim = getSize();
    buf = createImage(dim.width,dim.height);
    /**********クリック判定の処理**********/
    click cli = new click();
    addMouseListener(cli);
    /**********マウス追跡の処理**********/
    move mov = new move();
    addMouseMotionListener(mov);
  }
  /**********init()終了後、アプレット再開時**********/
  public void start(){}
  /**********アプレット中断時**********/
  public void stop(){}
  /**********アプレット終了時**********/
  public void destroy(){}

  /**********線分の当たり判定を行う**********/
  public boolean lineJudge(double ax,double ay,double bx,double by,int i){
    boolean judge=false;
    double L = Math.abs((bx-ax)*(my-ay)-(by-ay)*(mx-ax))/Math.sqrt((bx-ax)*(bx-ax)+(by-ay)*(by-ay));
    if(L<pw/2){
      double dotA=(mx-ax)*(bx-ax)+(my-ay)*(by-ay);
      double dotB=(mx-bx)*(bx-ax)+(my-by)*(by-ay);
      if(dotA*dotB<0 || ((mx-ax)*(mx-ax)+(my-ay)*(my-ay))<pw*pw/4 || ((mx-bx)*(mx-bx)+(my-by)*(my-by))<pw*pw/4){
        //接触しているので、色調判定
        switch(enemy[i].getColor()){
          case 0:cred=255;cgreen=0;cblue=0;break;
          case 1:cred=0;cgreen=255;cblue=0;break;
          case 2:cred=0;cgreen=0;cblue=255;break;
        }
        if(!(cred==red && cgreen==green && cblue==blue)){
          //色調不一致ならば成功
          judge=true;
        }
      }
    }
    return judge;
  }
  /**********四角形内部判定を行う**********/
  public boolean innerJudge(double x1,double x2,double y1,double y2,int i){
    boolean judge=false;
    if(x1<mx && mx<x2 && y1<my && my<y2){
      //接触しているので、色調判定
      switch(enemy[i].getColor()){
        case 0:cred=255;cgreen=0;cblue=0;break;
        case 1:cred=0;cgreen=255;cblue=0;break;
        case 2:cred=0;cgreen=0;cblue=255;break;
      }
      if(!(cred==red && cgreen==green && cblue==blue)){
        //色調不一致ならば成功
        judge=true;
      }
    }
    return judge;
  }
  /**********破壊処理を行う**********/
  public void collapse(){
    gameover=true;
  }

  /**********setメソッド**********/
  public void setFirst(){
    /**********敵情報の初期化*********/
    //(出現センチ秒,色調(0=赤 1=緑 2=青),種別(0=線 1=四角 2=円),初期位置,横幅,縦幅,太さ,X速度,Y速度)
    enemy[0].setEnemy(100,0,0,0,400,0,2,0,0.5);
    enemy[1].setEnemy(300,1,0,240,400,0,2,0,0.5);
    enemy[2].setEnemy(500,2,1,270,100,100,0,0,0.5);
    enemy[3].setEnemy(700,1,2,0,50,50,0,0.4,0.5);
    enemy[4].setEnemy(700,2,2,640,50,50,0,-0.4,0.5);
    enemy[5].setEnemy(1000,0,1,0,640,20,0,0,0.8);

//    enemy[0].setEnemy(100,0,0,0,40,40,2,0.5,0.5);
//    enemy[1].setEnemy(300,1,1,50,60,60,0,0.7,0.7);
//    enemy[2].setEnemy(700,2,2,640,20,20,0,-0.7,0.7);
    /**********数値の初期化*********/
    pw=50;ph=50;pn=0;
    red=255;green=0;blue=0;
    enemyNumber=0;
    countG=0;
    mSecG=10;mSecF=10;
    miliSec=0;imgSec=0;
    gameover=false;con=false;
    start1=true;
  }

  /**********runで走らせる**********/
  public void run(){
    try{
      while(true){
        repaint();
        Thread.sleep(mSecG);

        if(start2){miliSec++;}else if(imgSec<140){imgSec++;}
        boolean loop=true;
        /**********出現判定**********/
        while(loop){
          if(miliSec==enemy[enemyNumber].getSecond()){
            enemy[enemyNumber].setExist(true);
            enemyNumber++;
          }else loop=false;
        }
      }
    }catch(Exception e){}
  }
}

/**********クリック判定を行い、色調変更**********/
class click extends change implements MouseListener{
  /**********マウス関連処理**********/
  public void mouseClicked(MouseEvent e){}
  public void mouseEntered(MouseEvent e){}
  public void mouseExited(MouseEvent e){}
  public void mousePressed(MouseEvent e){
    if(start1){
      //スタート画面の終了
      start2=true;
    }
    /**********色調変更番号を変える**********/
    if(!super.gameover){
      super.pn++;
      super.pn %= 3;
      colorChange();
    }else if(super.con){
      //ゲームオーバー処理
      /**********敵情報の初期化*********/
      enemy = new Enemy[enemyLength];
      for(byte i=0;i<enemyLength;i++){
        enemy[i]=new Enemy();
      }
      super.setFirst();
    }
  }
  public void mouseReleased(MouseEvent e){}

  /**********色調変更**********/
  public void colorChange(){
    switch(pn){
      case 0:super.red=255;super.green=0;super.blue=0;break;
      case 1:super.red=0;super.green=255;super.blue=0;break;
      case 2:super.red=0;super.green=0;super.blue=255;break;
    }
  }

}
/**********マウスの動きから座標を取得する**********/
class move extends change implements MouseMotionListener{
  /**********マウス関連処理**********/
  public void mouseDragged(MouseEvent e){
    super.mx = e.getX();
    super.my = e.getY();
  }
  public void mouseMoved(MouseEvent e){
    super.mx = e.getX();
    super.my = e.getY();

  }
}

/**********物体の設計図**********/
class Enemy{
  int second=0;   //出現センチ秒
  int color=0;    //色調は、0=赤 1=緑 2=青
  int kind=0;     //敵の種別は、0=線 1=四角 2=円
  int startX=0;   //初期出現位置
  int width=0;    //横幅
  int height=0;   //縦幅
  int special=0;  //直線の太さ
  double speedX=0;//右へ進んでいく速度
  double speedY=0;//下へ進んでいく速度
  double X=0;     //物体のX位置
  double Y=0;     //物体のY位置
  boolean exist=false;//物体の存在判定
  /**********setメソッド**********/
  public void setEnemy(int s,int c,int k,int sx,int w,int h,int sp,double sX,double sY){
    second=s;
    color=c;
    kind=k;
    startX=sx;
    width=w;
    height=h;
    special=sp;
    speedX=sX;
    speedY=sY;
  }
  public void setXY(double x,double y){
    X=x;
    Y=y;
  }
  public void setExist(boolean e){
    exist=e;
  }
  /**********getメソッド**********/
  public int getSecond(){
    return second;
  }
  public int getColor(){
    return color;
  }
  public int getKind(){
    return kind;
  }
  public int getStartX(){
    return startX;
  }
  public int getWidth(){
    return width;
  }
  public int getHeight(){
    return height;
  }
  public int getSpecial(){
    return special;
  }
  public double getSpeedX(){
    return speedX;
  }
  public double getSpeedY(){
    return speedY;
  }
  public double getX(){
    return X;
  }
  public double getY(){
    return Y;
  }
  public boolean getExist(){
    return exist;
  }
}
