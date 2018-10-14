/**********バッチファイル用**********/
/*<applet code="test.class" width="640" height="480">
</applet>*/
/**********画像関連処理の準備**********/
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
/**********本体クラス**********/
public class test extends Applet{
  public void paint(Graphics g){
    g.fillRect(100,-80,120,120);
    g.fillRect(100,100,100,100);
  }
  public void init(){
    repaint();
  }
}
