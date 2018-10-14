/**********�o�b�`�t�@�C���p**********/
/*<applet code="change.class" width="640" height="480">
</applet>*/
/**********�摜�֘A�����̏���**********/
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
/**********�{�̃N���X**********/
public class change extends Applet implements Runnable{
  /**********�t�B�[���h�ϐ�**********/
  int field;
    static int mx,my;             //�}�E�X���W
    static int pw,ph,pn;          //�v���C���[�s�N�`���̏��
    static int red,green,blue;    //�v���C���[�s�N�`���̐F��
    static int cred,cgreen,cblue; //�F����r�p
    static int enemyNumber;       //���ɏo������G�̔ԍ�
    static int enemyStart;        //��������n�߂�ԍ�
    static long miliSec,imgSec;   //�J�E���g�p
    static int mSecG,mSecF;       //�E�F�C�g����~���b
    static int countG;            //�Q�[���I�[�o�[�̃J�E���g�p
    static boolean gameover,con;  //�Q�[���I�[�o�[�����p
    static boolean start1,start2=false;  //�Q�[���X�^�[�g�p
    Thread th1,th2;               //�X���b�h�p
    Image img,anime,buf;          //�_�u���o�b�t�@�����O�A�^�C�g���p
    Graphics ct;                  //�I�t�X�N���[���`��p
    Dimension dim;                //��ʏ��擾�p
    /**********�G�N���X�I�u�W�F�N�g**********/
    int enemyLength=10;
    Enemy[] enemy;
  /**********�`�揈��**********/
  public void paint(Graphics g){
    /**********�I�t�X�N���[���`��**********/
    if(ct==null) ct = buf.getGraphics();
    if(start1 && !start2){
      /**********�����w�i�ݒ�**********/
      ct.setColor(new Color(255,255,255));
      ct.fillRect(0,0,dim.width,dim.height);
      /**********�J�n��ʕ`��**********/
      img = getImage(getDocumentBase(),"title.png");
      ct.drawImage(img,0,0,dim.width,dim.height,this);
      int number=(int)imgSec/20;
      anime = getImage(getDocumentBase(),"start"+number+".png");
      ct.drawImage(anime,213,150,this);
    }else if(!gameover){
      /**********�����w�i�ݒ�**********/
      ct.setColor(new Color(255,255,255));
      ct.fillRect(0,0,dim.width,dim.height);
      /**********�v���C���[��`��**********/
      ct.setColor(new Color(red,green,blue));
      ct.fillOval(mx-pw/2,my-ph/2,pw,ph);
      /**********�G��`��**********/
      for(int i=0;i<enemyLength;i++){
        /**********���݂��Ă��邩����**********/
        if(enemy[i].getExist()){
          /**********���W�𑪒�**********/
          double X=enemy[i].getStartX()+enemy[i].getSpeedX()*(miliSec-enemy[i].getSecond());
          double Y=enemy[i].getSpeedY()*(miliSec-enemy[i].getSecond())-enemy[i].getHeight();
          enemy[i].setXY(X,Y);
          /**********��ʊO�Ȃ�΁A���ݔ��������**********/
          if(X>dim.width || Y>dim.height){enemy[i].setExist(false);}
          /**********�F����ݒ�**********/
          switch(enemy[i].getColor()){
            case 0:ct.setColor(new Color(255,0,0));break;
            case 1:ct.setColor(new Color(0,255,0));break;
            case 2:ct.setColor(new Color(0,0,255));break;
          }
          /**********��ނ�ݒ�A�ړ�����(���łɂ����蔻��)**********/
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
                    //�����蔻��
                    if(lineJudge(X,Y,x0,y0,i)){
                      collapse();
                    }
                    break;
            case 1: double x1 = X+enemy[i].getWidth();
                    double y1 = Y+enemy[i].getHeight();
                    ct.fillRect((int)X,(int)Y,enemy[i].getWidth(),enemy[i].getHeight());
                    //�����蔻��(�l�{�̐������Ƃɔ���{��������)
                    if(lineJudge(X,Y,x1,Y,i) || lineJudge(X,Y,X,y1,i) || lineJudge(x1,Y,x1,y1,i) || lineJudge(X,y1,x1,y1,i) || innerJudge(X,x1,Y,y1,i)){
                      collapse();
                    }
                    break;
            case 2: ct.fillOval((int)X,(int)Y,enemy[i].getWidth(),enemy[i].getHeight());
                    //�����蔻��
                    double xl=mx-X-enemy[i].getWidth()/2;
                    double yl=my-Y-enemy[i].getHeight()/2;
                    double cr=pw/2+enemy[i].getWidth()/2;
                    if(xl*xl+yl*yl<cr*cr){
                      //�ڐG���Ă���̂ŁA�F������
                      switch(enemy[i].getColor()){
                        case 0:cred=255;cgreen=0;cblue=0;break;
                        case 1:cred=0;cgreen=255;cblue=0;break;
                        case 2:cred=0;cgreen=0;cblue=255;break;
                      }
                      if(!(cred==red && cgreen==green && cblue==blue)){
                        //�F���s��v�Ȃ�ΐ���
                        collapse();
                      }
                    }
                    break;
          }
        }
      }
    }else{
      /**********�Q�[���I�[�o�[���`��**********/
      if(countG<1500){
        countG+=20;
        ct.setColor(new Color(red,green,blue));
        ct.fillOval(mx-pw/2-countG/2,my-ph/2-countG/2,pw+countG,ph+countG);
      }else{
        String str1 = "Game Over";
        ct.setFont(new Font("Serif",Font.BOLD + Font.ITALIC,100));  //�t�H���g�ݒ�
        FontMetrics fo1 = ct.getFontMetrics();  //�t�H���g���擾�̏���
        ct.setColor(new Color(0,0,0));
        ct.drawString(str1,dim.width/2 - fo1.stringWidth(str1)/2,dim.height/2 - fo1.getHeight()/2);
        String str2 = "Click to restart";
        ct.setFont(new Font("Serif",Font.BOLD,50));  //�t�H���g�ݒ�
        FontMetrics fo2 = ct.getFontMetrics();  //�t�H���g���擾�̏���
        ct.drawString(str2,dim.width/2 - fo2.stringWidth(str2)/2,dim.height/2 - fo2.getHeight()/2+100);
        con=true;
      }
    }
    /**********�I�t�X�N���[������A���ۂɕ`��**********/
    g.drawImage(buf,0,0,this);
  }
  public void update(Graphics g){paint(g);} //�ĕ`���h��

  /**********�ŏ�������**********/
  public void init(){
    /**********�G���̏�����*********/
    enemy = new Enemy[enemyLength];
    for(byte i=0;i<enemyLength;i++){
      enemy[i]=new Enemy();
    }
    setFirst();
    /**********�X���b�h����*********/
    th1 = new Thread(this);
    th1.start();
    /**********�_�u���o�b�t�@�����O�p�̃I�t�X�N���[��������Ă���**********/
    dim = getSize();
    buf = createImage(dim.width,dim.height);
    /**********�N���b�N����̏���**********/
    click cli = new click();
    addMouseListener(cli);
    /**********�}�E�X�ǐՂ̏���**********/
    move mov = new move();
    addMouseMotionListener(mov);
  }
  /**********init()�I����A�A�v���b�g�ĊJ��**********/
  public void start(){}
  /**********�A�v���b�g���f��**********/
  public void stop(){}
  /**********�A�v���b�g�I����**********/
  public void destroy(){}

  /**********�����̓����蔻����s��**********/
  public boolean lineJudge(double ax,double ay,double bx,double by,int i){
    boolean judge=false;
    double L = Math.abs((bx-ax)*(my-ay)-(by-ay)*(mx-ax))/Math.sqrt((bx-ax)*(bx-ax)+(by-ay)*(by-ay));
    if(L<pw/2){
      double dotA=(mx-ax)*(bx-ax)+(my-ay)*(by-ay);
      double dotB=(mx-bx)*(bx-ax)+(my-by)*(by-ay);
      if(dotA*dotB<0 || ((mx-ax)*(mx-ax)+(my-ay)*(my-ay))<pw*pw/4 || ((mx-bx)*(mx-bx)+(my-by)*(my-by))<pw*pw/4){
        //�ڐG���Ă���̂ŁA�F������
        switch(enemy[i].getColor()){
          case 0:cred=255;cgreen=0;cblue=0;break;
          case 1:cred=0;cgreen=255;cblue=0;break;
          case 2:cred=0;cgreen=0;cblue=255;break;
        }
        if(!(cred==red && cgreen==green && cblue==blue)){
          //�F���s��v�Ȃ�ΐ���
          judge=true;
        }
      }
    }
    return judge;
  }
  /**********�l�p�`����������s��**********/
  public boolean innerJudge(double x1,double x2,double y1,double y2,int i){
    boolean judge=false;
    if(x1<mx && mx<x2 && y1<my && my<y2){
      //�ڐG���Ă���̂ŁA�F������
      switch(enemy[i].getColor()){
        case 0:cred=255;cgreen=0;cblue=0;break;
        case 1:cred=0;cgreen=255;cblue=0;break;
        case 2:cred=0;cgreen=0;cblue=255;break;
      }
      if(!(cred==red && cgreen==green && cblue==blue)){
        //�F���s��v�Ȃ�ΐ���
        judge=true;
      }
    }
    return judge;
  }
  /**********�j�󏈗����s��**********/
  public void collapse(){
    gameover=true;
  }

  /**********set���\�b�h**********/
  public void setFirst(){
    /**********�G���̏�����*********/
    //(�o���Z���`�b,�F��(0=�� 1=�� 2=��),���(0=�� 1=�l�p 2=�~),�����ʒu,����,�c��,����,X���x,Y���x)
    enemy[0].setEnemy(100,0,0,0,400,0,2,0,0.5);
    enemy[1].setEnemy(300,1,0,240,400,0,2,0,0.5);
    enemy[2].setEnemy(500,2,1,270,100,100,0,0,0.5);
    enemy[3].setEnemy(700,1,2,0,50,50,0,0.4,0.5);
    enemy[4].setEnemy(700,2,2,640,50,50,0,-0.4,0.5);
    enemy[5].setEnemy(1000,0,1,0,640,20,0,0,0.8);

//    enemy[0].setEnemy(100,0,0,0,40,40,2,0.5,0.5);
//    enemy[1].setEnemy(300,1,1,50,60,60,0,0.7,0.7);
//    enemy[2].setEnemy(700,2,2,640,20,20,0,-0.7,0.7);
    /**********���l�̏�����*********/
    pw=50;ph=50;pn=0;
    red=255;green=0;blue=0;
    enemyNumber=0;
    countG=0;
    mSecG=10;mSecF=10;
    miliSec=0;imgSec=0;
    gameover=false;con=false;
    start1=true;
  }

  /**********run�ő��点��**********/
  public void run(){
    try{
      while(true){
        repaint();
        Thread.sleep(mSecG);

        if(start2){miliSec++;}else if(imgSec<140){imgSec++;}
        boolean loop=true;
        /**********�o������**********/
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

/**********�N���b�N������s���A�F���ύX**********/
class click extends change implements MouseListener{
  /**********�}�E�X�֘A����**********/
  public void mouseClicked(MouseEvent e){}
  public void mouseEntered(MouseEvent e){}
  public void mouseExited(MouseEvent e){}
  public void mousePressed(MouseEvent e){
    if(start1){
      //�X�^�[�g��ʂ̏I��
      start2=true;
    }
    /**********�F���ύX�ԍ���ς���**********/
    if(!super.gameover){
      super.pn++;
      super.pn %= 3;
      colorChange();
    }else if(super.con){
      //�Q�[���I�[�o�[����
      /**********�G���̏�����*********/
      enemy = new Enemy[enemyLength];
      for(byte i=0;i<enemyLength;i++){
        enemy[i]=new Enemy();
      }
      super.setFirst();
    }
  }
  public void mouseReleased(MouseEvent e){}

  /**********�F���ύX**********/
  public void colorChange(){
    switch(pn){
      case 0:super.red=255;super.green=0;super.blue=0;break;
      case 1:super.red=0;super.green=255;super.blue=0;break;
      case 2:super.red=0;super.green=0;super.blue=255;break;
    }
  }

}
/**********�}�E�X�̓���������W���擾����**********/
class move extends change implements MouseMotionListener{
  /**********�}�E�X�֘A����**********/
  public void mouseDragged(MouseEvent e){
    super.mx = e.getX();
    super.my = e.getY();
  }
  public void mouseMoved(MouseEvent e){
    super.mx = e.getX();
    super.my = e.getY();

  }
}

/**********���̂̐݌v�}**********/
class Enemy{
  int second=0;   //�o���Z���`�b
  int color=0;    //�F���́A0=�� 1=�� 2=��
  int kind=0;     //�G�̎�ʂ́A0=�� 1=�l�p 2=�~
  int startX=0;   //�����o���ʒu
  int width=0;    //����
  int height=0;   //�c��
  int special=0;  //�����̑���
  double speedX=0;//�E�֐i��ł������x
  double speedY=0;//���֐i��ł������x
  double X=0;     //���̂�X�ʒu
  double Y=0;     //���̂�Y�ʒu
  boolean exist=false;//���̂̑��ݔ���
  /**********set���\�b�h**********/
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
  /**********get���\�b�h**********/
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
