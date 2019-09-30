import javax.swing.*;
import java.awt.event.*;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;
import java.lang.Math;
import java.awt.Color;
import java.awt.geom.Point2D;

public class Fourier extends JPanel implements ActionListener {

  public static JFrame frame;

  private static final int WIDTH = 1400;
  private static final int HEIGHT = 800;
  private static final int RIGHT_SHIFT = 700;
  double TRANSLATE_X = 0;
  double TRANSLATE_Y = 0;
  int N = 1500;
  double X[];
  double Y[];
  double re[];
  double im[];
  Timer timer = new Timer(5, this);
  double angle = 0;
  int step = 1;
  ArrayList<Integer> xValues = new ArrayList<Integer>();
  ArrayList<Integer> yValues = new ArrayList<Integer>();
  ArrayList<DFT> dft = new ArrayList<DFT>();


  public Fourier() {

    X = new double[N];
    Y = new double[N];

    re = new double[N];
    im = new double[N];

    double h = -Math.PI;

    for(int i = 0,j = 0;i < N;i++,j = j + step) {

      X[i] = Math.sin(3.2 * h) * 200;
      Y[i] = Math.sin(2.5 * h) * 200;

      h += (2 * Math.PI)/N;
    }

    for(int k = 0;k < N;k++) {
      re[k] = 0.0;
      im[k] = 0.0;
      for(int n = 0;n < N;n++) {
      
        re[k] += (X[n] * (Math.cos(2 * Math.PI * k * n / N))) + (Y[n] * (Math.sin(2 * Math.PI * k * n / N)));
        im[k] += (-X[n] * (Math.sin(2 * Math.PI * k * n / N))) + (Y[n] * (Math.cos(2 * Math.PI * k * n / N)));

      }
      re[k] /= N;
      im[k] /= N;
    }



    for(int i = 0;i < re.length;i++) {
      dft.add(new DFT(re[i], im[i], i));
    }

    Comparator<DFT> c = new DFTComparator(); 
    Collections.sort(dft, c);

  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g;

    g.setColor(Color.BLACK);
    g.fillRect(0, 0, WIDTH, HEIGHT);
    g.setColor(new Color(175, 175, 175));

    double x = 0,y = 0;

    goTo(500, 500);

    int n = 100,p = 4;
    double radius = 0;
    double phase = 0;

    for(int i = 0;i < im.length;i++) {

        radius = Math.sqrt((dft.get(i).re * dft.get(i).re) + (dft.get(i).im * dft.get(i).im));
        phase = Math.atan2(dft.get(i).im, dft.get(i).re);

        g.drawOval((int) (TRANSLATE_X - (Math.abs(radius))), (int) (TRANSLATE_Y - (Math.abs(radius))), (int) (Math.abs(2*radius)), (int) (Math.abs(2*radius)));
        x = radius * Math.cos(dft.get(i).freq * angle + phase);
        y = radius * Math.sin(dft.get(i).freq * angle + phase);
        g.drawLine((int)TRANSLATE_X, (int)TRANSLATE_Y, (int)(x + TRANSLATE_X), (int)(y + TRANSLATE_Y));
        g.fillOval((int)(x - 1.5 + TRANSLATE_X), (int)(y - 1.5 + TRANSLATE_Y), 3, 3); 

        goTo(x + TRANSLATE_X, y + TRANSLATE_Y);

    }
    
    yValues.add(0, (int)TRANSLATE_Y);
    xValues.add(0, (int)TRANSLATE_X);
    if(yValues.size() > N) {
      yValues.remove(N);
    }
    if(xValues.size() > N) {
      xValues.remove(N);
    }

    g.setColor(Color.RED);

    for(int i = 0;i < xValues.size() - 1;i++) {
      g.drawLine((int) xValues.get(i), (int) yValues.get(i), (int) xValues.get(i + 1), (int) yValues.get(i + 1));   
    }

    timer.start();
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    angle += (2 * Math.PI / Y.length);

   repaint(); 
  }

  /*@Override
  public void mouseClicked(MouseEvent e) {}
  @Override
  public void mouseEntered(MouseEvent e) {}
  @Override
  public void mouseExited(MouseEvent e) {}
  @Override
  public void mousePressed(MouseEvent e) {}
  @Override
  public void mouseReleased(MouseEvent e) {}*/

  public void goTo(double x, double y) {
    TRANSLATE_X = x;
    TRANSLATE_Y = y;
  }

  public static double map(double x, double x0, double y0, double x1, double y1) {

    if(x > y0 || x < x0) {
      throw new RuntimeException();
    }

    return ((x - x0)/(y0 - x0) * (y1 - x1)) + x1;
  }

  public static void main(String[] args) {

    Fourier t = new Fourier();

    frame = new JFrame();
    frame.setSize(WIDTH, HEIGHT);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(t);
  }
}