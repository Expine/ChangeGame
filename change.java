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
  static final int field=1;
    static int keep;              //�L�[�v
    static int mx,my;             //�}�E�X���W
    static int pw,ph,pn;          //�v���C���[�s�N�`���̏��
    static int red,green,blue;    //�v���C���[�s�N�`���̐F��
    static int cred,cgreen,cblue; //�F����r�p
    static int enemyNumber;       //���ɏo������G�̔ԍ�
    static int enemyStart;        //����p�ԍ�
    static long miliSec,imgSec;   //�J�E���g�p
    static int mSecG;       //�E�F�C�g����~���b
    static int level;             //���݂̃��x��
    static int countG;            //�Q�[���I�[�o�[�̃J�E���g�p
    static boolean gameover,con;  //�Q�[���I�[�o�[�����p
    static boolean start1,start2; //�Q�[���X�^�[�g�p
    static boolean goal,next;     //�S�[���㏈���p
    Thread th1,th2;               //�X���b�h�p
    Image img,anime,buf;          //�_�u���o�b�t�@�����O�A�^�C�g���p
    Graphics ct;                  //�I�t�X�N���[���`��p
    Dimension dim;                //��ʏ��擾�p

    static int debug=0;             //�f�o�b�O�p
    /**********�G�N���X�I�u�W�F�N�g**********/
    int enemyLength=127;
    Enemy[] enemy;
  /**********�`�揈��**********/
  public void paint(Graphics g){
    /**********�I�t�X�N���[���`��**********/
    if(ct==null) ct = buf.getGraphics();
    if(start1 && !start2 && !next){
      /**********�����w�i�ݒ�**********/
      ct.setColor(new Color(255,255,255));
      ct.fillRect(0,0,dim.width,dim.height);
      /**********�J�n��ʕ`��**********/
      img = getImage(getDocumentBase(),"title.png");
      ct.drawImage(img,0,0,dim.width,dim.height,this);
      int number=(int)imgSec/20;
      anime = getImage(getDocumentBase(),"start"+number+".png");
      int x=dim.width/2 - 219/2/field;
      int y=dim.height/2 - 29/2/field -60/field;
      ct.drawImage(anime,x,y,219/field,29/field,this);
    }else if(miliSec>10800 && !gameover){
      if(next){
        if(imgSec<140){
          ct.setColor(new Color(255,255,255));
          ct.fillRect(0,0,dim.width,dim.height);
          String str1 = "N E X T";
          ct.setFont(new Font("Serif",Font.BOLD + Font.ITALIC,100/field));  //�t�H���g�ݒ�
          FontMetrics fo1 = ct.getFontMetrics();  //�t�H���g���擾�̏���
          ct.setColor(new Color(0,0,0));
          ct.drawString(str1,dim.width/2 - fo1.stringWidth(str1)/2,dim.height/2 - fo1.getHeight()/2);
          String str2 = "L E V E L !!";
          ct.setFont(new Font("Serif",Font.BOLD + Font.ITALIC,100/field));  //�t�H���g�ݒ�
          FontMetrics fo2 = ct.getFontMetrics();  //�t�H���g���擾�̏���
          ct.drawString(str2,dim.width/2 - fo2.stringWidth(str2)/2,dim.height/2 - fo2.getHeight()/2+150/field);
        }else{
          start2=true;
          goal=false;next=false;
          setFirst();
        }
      }else{
        /**********�S�[������**********/
        ct.setColor(new Color(255,255,255));
        ct.fillRect(0,0,dim.width,dim.height);
        String str1 = "G O A L !!";
        ct.setFont(new Font("Serif",Font.BOLD + Font.ITALIC,100/field));  //�t�H���g�ݒ�
        FontMetrics fo1 = ct.getFontMetrics();  //�t�H���g���擾�̏���
        ct.setColor(new Color(0,0,0));
        ct.drawString(str1,dim.width/2 - fo1.stringWidth(str1)/2,dim.height/2 - fo1.getHeight()/2);
        String str2 = "Click to next level";
        ct.setFont(new Font("Serif",Font.BOLD,50/field));  //�t�H���g�ݒ�
        FontMetrics fo2 = ct.getFontMetrics();  //�t�H���g���擾�̏���
        ct.drawString(str2,dim.width/2 - fo2.stringWidth(str2)/2,dim.height/2 - fo2.getHeight()/2+100/field);
        goal=true;
        imgSec=0;
      }
    }else if(!gameover){
      /**********�����w�i�ݒ�**********/
      ct.setColor(new Color(255,255,255));
      ct.fillRect(0,0,dim.width,dim.height);
      /**********�v���C���[��`��**********/
      ct.setColor(new Color(red,green,blue));
      ct.fillOval(mx-pw/2,my-ph/2,pw,ph);
      /**********�G��`��**********/
      for(int i=enemyStart;i<enemyNumber;i++){
        /**********���݂��Ă��邩����**********/
        l1:if(enemy[i].exist){
          /**********���W�𑪒�**********/
          double X=enemy[i].startX+enemy[i].speedX*(miliSec-enemy[i].second);
          double Y=enemy[i].speedY*(miliSec-enemy[i].second)-Math.abs(enemy[i].height);
          enemy[i].setXY(X,Y);
          /**********��ʊO�Ȃ�΁A���ݔ��������**********/
          if(X>dim.width || Y>dim.height){enemy[i].setExist(false);if(keep<=i)keep=i;if(enemyStart==i)enemyStart=keep;}
          /**********�F����ݒ�**********/
          switch(enemy[i].color){
            case 0:cred=255;cgreen=0;cblue=0;break;
            case 1:cred=0;cgreen=255;cblue=0;break;
            case 2:cred=0;cgreen=0;cblue=255;break;
            case 3:cred=0;cgreen=0;cblue=0;break;
          }
          ct.setColor(new Color(cred,cgreen,cblue));
          /**********��ނ�ݒ�A�ړ�����(���łɂ����蔻��)**********/
          switch(enemy[i].kind){
            case 0: double x0 = X+enemy[i].width;
                    double y0 = Y+enemy[i].height;
                    ct.drawLine((int)X,(int)Y,(int)x0,(int)y0);
                    for(byte r=0;r<enemy[i].special;r++){
                      ct.drawLine((int)X-r,(int)Y,(int)x0-r,(int)y0);
                      ct.drawLine((int)X,(int)Y-r,(int)x0,(int)y0-r);
                      ct.drawLine((int)X+r,(int)Y,(int)x0+r,(int)y0);
                      ct.drawLine((int)X,(int)Y+r,(int)x0,(int)y0+r);
                    }
                    //�����蔻��
                    //�F������
                    if(!(cred==red && cgreen==green && cblue==blue)){
                      //�F���s��v�Ȃ�ΐ���
                      if(lineJudge(X,Y,x0,y0,i)){
                        collapse();
                      }
                    }
                    break;
            case 1: ct.fillRect((int)X,(int)Y,enemy[i].width,enemy[i].height);
                    //�����蔻��(�l�{�̐������Ƃɔ���{��������)
                    //�F������
                    if(!(cred==red && cgreen==green && cblue==blue)){
                      //�F���s��v�Ȃ�ΐ���
                      double x1 = X+enemy[i].width;
                      double y1 = Y+enemy[i].height;
                      if(lineJudge(X,Y,x1,Y,i) || lineJudge(X,Y,X,y1,i) || lineJudge(x1,Y,x1,y1,i) || lineJudge(X,y1,x1,y1,i) || innerJudge(X,x1,Y,y1,i)){
                        collapse();
                      }
                    }
                    break;
            case 2: ct.fillOval((int)X,(int)Y,enemy[i].width,enemy[i].height);
                    //�����蔻��
                    //�F������
                    if(!(cred==red && cgreen==green && cblue==blue)){
                      //�F���s��v�Ȃ�ΐ���
                      double xl=mx-X-enemy[i].width/2;
                      double yl=my-Y-enemy[i].height/2;
                      double cr=pw/2+enemy[i].width/2;
                      if(xl*xl+yl*yl<cr*cr){
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
        ct.setFont(new Font("Serif",Font.BOLD + Font.ITALIC,100/field));  //�t�H���g�ݒ�
        FontMetrics fo1 = ct.getFontMetrics();  //�t�H���g���擾�̏���
        ct.setColor(new Color(0,0,0));
        ct.drawString(str1,dim.width/2 - fo1.stringWidth(str1)/2,dim.height/2 - fo1.getHeight()/2);
        String str2 = "Click to restart";
        ct.setFont(new Font("Serif",Font.BOLD,50/field));  //�t�H���g�ݒ�
        FontMetrics fo2 = ct.getFontMetrics();  //�t�H���g���擾�̏���
        ct.drawString(str2,dim.width/2 - fo2.stringWidth(str2)/2,dim.height/2 - fo2.getHeight()/2+100/field);
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
    /**********���l���̏�����*********/
    level=1;
    goal=false;next=false;
    start2=false;
    mSecG=10;
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
        judge=true;
      }
    }
    return judge;
  }
  /**********�l�p�`����������s��**********/
  public boolean innerJudge(double x1,double x2,double y1,double y2,int i){
    boolean judge=false;
    if(x1<mx && mx<x2 && y1<my && my<y2){
      judge=true;
    }
    return judge;
  }
  /**********�j�󏈗����s��**********/
  public void collapse(){
    if(debug==0)gameover=true;
  }

  /**********set���\�b�h**********/
  public void setFirst(){
    /**********�G���̏�����*********/
    //(�o���Z���`�b,�F��(0=�� 1=�� 2=�� 3=��),���(0=�� 1=�l�p 2=�~),�����ʒu,����,�c��,����,X���x,Y���x)
    //�Ȃ��A�l�p�`���o���ƁA�����l�{���̏����̏d����������B
    int i=0;
    enemy[i].setEnemy(100,0,0,0,400,0,2,0,0.5);i++;
    enemy[i].setEnemy(300,1,0,240,400,0,2,0,0.5);i++;
    enemy[i].setEnemy(500,2,0,0,400,0,2,0,0.5);i++;
    enemy[i].setEnemy(700,1,2,0,50,50,0,0.4,0.5);i++;
    enemy[i].setEnemy(700,2,2,590,50,50,0,-0.4,0.5);i++;
    enemy[i].setEnemy(1000,0,0,0,640,0,2,0,0.8);i++;
    enemy[i].setEnemy(1200,0,2,0,60,60,0,0.3,0.4);i++;
    enemy[i].setEnemy(1200,1,2,580,60,60,0,-0.3,0.4);i++;
    enemy[i].setEnemy(1300,3,1,300,40,480,0,0,0.5);i++;
    enemy[i].setEnemy(1800,0,2,130,80,80,0,0,1.0);i++;
    enemy[i].setEnemy(1800,0,0,430,0,240,2,0,1.0);i++;
    enemy[i].setEnemy(2000,1,2,210,80,80,0,0,1.0);i++;
    enemy[i].setEnemy(2000,1,0,510,0,240,2,0,1.0);i++;
    enemy[i].setEnemy(2200,2,2,60,80,80,0,0,1.0);i++;
    enemy[i].setEnemy(2200,2,0,590,0,240,2,0,1.0);i++;
    enemy[i].setEnemy(2500,1,1,60,520,60,0,0,0.2);i++;
    enemy[i].setEnemy(2700,0,0,0,120,120,2,0.8,0.8);i++;
    enemy[i].setEnemy(2700,2,0,640,120,-120,2,-0.8,0.8);i++;
    enemy[i].setEnemy(2900,2,0,0,200,100,2,1.2,0.8);i++;
    enemy[i].setEnemy(2900,0,0,640,200,-100,2,-1.2,0.8);i++;
    enemy[i].setEnemy(2900,2,0,120,100,100,2,0.7,1.0);i++;
    enemy[i].setEnemy(2900,0,0,360,100,-100,2,-0.7,1.0);i++;
    enemy[i].setEnemy(2900,2,0,320,0,300,2,0,2.0);i++;
    enemy[i].setEnemy(3500,3,1,0,100,600,0,0,0.5);i++;
    enemy[i].setEnemy(3500,3,1,540,100,600,0,0,0.5);i++;
    enemy[i].setEnemy(3700,2,0,0,640,0,2,0,0.8);i++;
    enemy[i].setEnemy(4000,0,0,0,640,0,2,0,1.4);i++;
    enemy[i].setEnemy(4300,1,0,0,640,0,2,0,0.8);i++;
    enemy[i].setEnemy(4300,0,0,120,0,300,2,0,2.0);i++;
    enemy[i].setEnemy(4300,0,0,180,0,300,2,0,2.0);i++;
    enemy[i].setEnemy(4400,0,0,320,0,300,2,0,2.0);i++;
    enemy[i].setEnemy(4400,2,0,400,0,300,2,0,2.0);i++;
    enemy[i].setEnemy(4500,2,0,140,0,300,2,0,2.0);i++;
    enemy[i].setEnemy(4500,0,0,500,0,300,2,0,2.0);i++;
    enemy[i].setEnemy(4600,2,0,290,0,300,2,0,2.0);i++;
    enemy[i].setEnemy(4600,2,0,360,0,300,2,0,2.0);i++;
    enemy[i].setEnemy(4700,2,0,290,0,300,2,0,2.0);i++;
    enemy[i].setEnemy(5000,3,1,0,500,50,0,0,0.8);i++;
    enemy[i].setEnemy(5100,2,0,180,0,300,2,0,2.0);i++;
    enemy[i].setEnemy(5100,0,0,300,0,300,2,0,2.0);i++;
    enemy[i].setEnemy(5150,1,0,200,0,300,2,0,2.0);i++;
    enemy[i].setEnemy(5150,1,0,320,0,300,2,0,2.0);i++;
    enemy[i].setEnemy(5200,0,0,220,0,300,2,0,2.0);i++;
    enemy[i].setEnemy(5200,2,0,340,0,300,2,0,2.0);i++;
    enemy[i].setEnemy(5400,2,0,360,0,300,2,0,2.0);i++;
    enemy[i].setEnemy(5400,0,0,440,0,300,2,0,2.0);i++;
    enemy[i].setEnemy(5450,1,0,380,0,300,2,0,2.0);i++;
    enemy[i].setEnemy(5450,1,0,460,0,300,2,0,2.0);i++;
    enemy[i].setEnemy(5500,0,0,400,0,300,2,0,2.0);i++;
    enemy[i].setEnemy(5500,2,0,480,0,300,2,0,2.0);i++;
    enemy[i].setEnemy(5500,3,1,140,500,50,0,0,0.8);i++;
    enemy[i].setEnemy(5600,0,2,80,80,80,0,0.3,1.5);i++;
    enemy[i].setEnemy(5620,1,2,140,80,80,0,0.3,1.7);i++;
    enemy[i].setEnemy(5640,2,2,400,80,80,0,-0.2,1.2);i++;
    enemy[i].setEnemy(5660,2,2,340,80,80,0,-0.5,1.8);i++;
    enemy[i].setEnemy(5680,0,2,220,80,80,0,0.4,1.1);i++;
    enemy[i].setEnemy(5700,1,2,550,80,80,0,-0.9,1.0);i++;
    enemy[i].setEnemy(5720,2,2,0,80,80,0,1.8,1.2);i++;
    enemy[i].setEnemy(5740,0,2,120,80,80,0,0.3,1.2);i++;
    enemy[i].setEnemy(5760,1,2,300,80,80,0,0,1.9);i++;
    enemy[i].setEnemy(5780,0,2,410,80,80,0,-1.8,0.9);i++;
    enemy[i].setEnemy(5800,2,2,30,80,80,0,0.9,1.0);i++;
    enemy[i].setEnemy(5820,2,2,220,80,80,0,0,0.5);i++;
    enemy[i].setEnemy(5840,0,2,100,80,80,0,2.0,1.0);i++;
    enemy[i].setEnemy(5860,1,2,640,80,80,0,-2.0,1.4);i++;
    enemy[i].setEnemy(5880,1,2,10,80,80,0,1.1,1.8);i++;
    enemy[i].setEnemy(5900,2,2,320,80,80,0,0.2,1.3);i++;
    enemy[i].setEnemy(5920,0,2,540,80,80,0,-1.3,1.5);i++;
    enemy[i].setEnemy(5940,2,2,430,80,80,0,-0.2,1.0);i++;
    enemy[i].setEnemy(5960,1,2,260,80,80,0,0.2,1.3);i++;
    enemy[i].setEnemy(5980,0,2,180,80,80,0,2.0,1.0);i++;
    enemy[i].setEnemy(6000,3,1,0,500,50,0,0,1.0);i++;
    enemy[i].setEnemy(6200,0,0,0,640,100,0,0,0.7);i++;
    enemy[i].setEnemy(6320,2,0,0,640,-100,0,0,0.7);i++;
    enemy[i].setEnemy(6440,1,0,0,640,100,0,0,0.7);i++;
    enemy[i].setEnemy(6530,0,0,0,640,-100,0,0,0.7);i++;
    enemy[i].setEnemy(6630,2,0,0,640,100,0,0,0.7);i++;
    enemy[i].setEnemy(6700,0,0,0,640,-100,0,0,0.7);i++;
    enemy[i].setEnemy(6790,1,0,0,640,100,0,0,0.7);i++;
    enemy[i].setEnemy(6890,0,0,0,640,-100,0,0,0.7);i++;
    enemy[i].setEnemy(6980,1,0,0,640,100,0,0,0.7);i++;
    enemy[i].setEnemy(7080,2,0,0,640,-100,0,0,0.7);i++;
    enemy[i].setEnemy(7400,0,1,120,120,120,0,0,1.3);i++;
    enemy[i].setEnemy(7430,1,1,530,120,120,0,0,1.4);i++;
    enemy[i].setEnemy(7460,3,1,340,120,120,0,0,1.5);i++;
    enemy[i].setEnemy(7490,2,1,280,120,120,0,0,1.1);i++;
    enemy[i].setEnemy(7520,3,1,400,120,120,0,0,1.2);i++;
    enemy[i].setEnemy(7550,2,1,500,120,120,0,0,1.6);i++;
    enemy[i].setEnemy(7580,2,1,50,120,120,0,0,1.3);i++;
    enemy[i].setEnemy(7610,3,1,240,120,120,0,0,1.5);i++;
    enemy[i].setEnemy(7640,0,1,390,120,120,0,0,1.2);i++;
    enemy[i].setEnemy(7670,1,1,80,120,120,0,0,1.1);i++;
    enemy[i].setEnemy(7700,3,1,530,120,120,0,0,1.7);i++;
    enemy[i].setEnemy(7730,3,1,240,120,120,0,0,1.2);i++;
    enemy[i].setEnemy(7760,0,1,460,120,120,0,0,1.6);i++;
    enemy[i].setEnemy(7790,2,1,100,120,120,0,0,1.2);i++;
    enemy[i].setEnemy(7820,3,1,340,120,120,0,0,1.8);i++;
    enemy[i].setEnemy(7850,0,1,220,120,120,0,0,1.9);i++;
    enemy[i].setEnemy(7880,1,1,60,120,120,0,0,1.2);i++;
    enemy[i].setEnemy(7910,2,1,540,120,120,0,0,0.4);i++;
    enemy[i].setEnemy(7940,3,1,0,120,120,0,0,1.5);i++;
    enemy[i].setEnemy(7970,0,1,400,120,120,0,0,1.7);i++;
    enemy[i].setEnemy(8000,1,1,320,120,120,0,0,1.1);i++;
    enemy[i].setEnemy(8200,3,1,0,100,1500,0,0,1.2);i++;
    enemy[i].setEnemy(8200,3,1,540,100,1500,0,0,1.2);i++;
    enemy[i].setEnemy(8200,0,1,270,100,1500,0,0,1.2);i++;
    enemy[i].setEnemy(8250,3,1,100,170,170,0,0,2.0);i++;
    enemy[i].setEnemy(8500,2,0,0,640,0,0,0,1.0);i++;
    enemy[i].setEnemy(8600,3,1,370,170,170,0,0,2.0);i++;
    enemy[i].setEnemy(8700,1,0,0,640,0,0,0,1.0);i++;
    enemy[i].setEnemy(8800,3,1,100,170,170,0,0,2.0);i++;
    enemy[i].setEnemy(8900,0,0,0,640,0,0,0,1.0);i++;
    enemy[i].setEnemy(9000,3,1,100,170,170,0,0,2.0);i++;
    enemy[i].setEnemy(9000,3,1,370,170,170,0,0,2.0);i++;
    enemy[i].setEnemy(9100,2,0,0,640,0,0,0,1.0);i++;
    enemy[i].setEnemy(10000,3,0,50,100,0,0,0,1.0);i++;
    enemy[i].setEnemy(10000,3,0,50,0,100,0,0,1.0);i++;
    enemy[i].setEnemy(10000,3,0,150,0,40,0,0,1.0);i++;
    enemy[i].setEnemy(10000,3,1,200,100,100,0,0,1.0);i++;
    enemy[i].setEnemy(10000,3,0,400,50,100,0,0,1.0);i++;
    enemy[i].setEnemy(10000,3,0,400,-50,100,0,0,1.0);i++;
    enemy[i].setEnemy(10000,3,0,500,100,0,0,0,1.0);i++;
    enemy[i].setEnemy(10000,3,0,500,0,100,0,0,1.0);i++;
    enemy[i].setEnemy(10040,3,0,130,40,0,0,0,1.0);i++;
    enemy[i].setEnemy(10050,3,0,375,50,0,0,0,1.0);i++;
    enemy[i].setEnemy(10100,3,0,50,100,0,0,0,1.0);i++;
    /**********���l�̏�����*********/
    pw=50/field;ph=50/field;pn=0;
    red=255;green=0;blue=0;
    enemyNumber=0;enemyStart=0;
    countG=0;
    miliSec=0;imgSec=0;
    gameover=false;con=false;
    start1=true;
    keep=0;
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
          if(miliSec==enemy[enemyNumber].second){
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
    if(super.goal){
      //���̃��x���ֈڍs
      switch(super.level){
        case 1:super.mSecG=5;break;
        case 2:super.mSecG=3;break;
      }
      super.start2=false;
      super.imgSec=0;
      super.next=true;
      super.level++;
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
class Enemy extends change{
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
    startX=sx/field;
    width=w/field;
    height=h/field;
    special=sp/field;
    speedX=sX/field;
    speedY=sY/field;
  }
  public void setXY(double x,double y){
    X=x;
    Y=y;
  }
  public void setExist(boolean e){
    exist=e;
  }
}
