/**********�o�b�`�t�@�C���p**********/
/*<applet code="test.class" width="640" height="480">
</applet>*/
/**********�摜�֘A�����̏���**********/
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
/**********�{�̃N���X**********/
public class test extends Applet{
  public void paint(Graphics g){
    g.fillRect(100,-80,120,120);
    g.fillRect(100,100,100,100);
  }
  public void init(){
    repaint();
  }
}
