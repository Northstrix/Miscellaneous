import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException; 
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
/*
Image convertor for the Arduino-compatible displays
You can find more information here https://github.com/Northstrix/Miscellaneous
Distributed under the MIT License
© Copyright Maxim Bortnikov 2022
 */
public class Stuff extends JFrame 
{
    JButton openImgButton = new JButton("Open");
    PictureBox panel1 = new PictureBox();
    JLabel lb = new JLabel(" ");
    JButton ColorsW = new JButton("B/W");
    JButton InvB = new JButton("Invert clr");
    JButton send = new JButton("Convert image");
    JMenuBar mb = new JMenuBar();
    Stuff()
      {   
    	  setTitle("Image convertor");
    	  setJMenuBar(mb);  
          setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          mb.add(openImgButton);
          mb.add(ColorsW);
          mb.add(InvB);
          mb.add(send);
          add(panel1);
          add(lb);
          setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
          getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
          setSize(new Dimension(356,343));
    	  Color bclr = new Color(2, 2, 6);
	      setBackground(bclr);
          AddActionListeners();
          setVisible(true);
      }
      
      void AddActionListeners() {
          openImgButton.addActionListener(e -> {
              try {
                  JFileChooser open = new JFileChooser();
                  open.showOpenDialog(this);
                  panel1.setPicture(ImageIO.read(open.getSelectedFile()));
                  lb.setText("Width: " + panel1.origImg.getWidth() + "px Heigth: " + panel1.origImg.getHeight() + "px");
              }
              catch (Exception ignored) {}
          });
          ColorsW.addActionListener(e -> {
              if(panel1.origImg != null) {
                  for (int x = 0; x < panel1.origImg.getWidth(); x++) {
                      for (int y = 0; y < panel1.origImg.getHeight(); y++) {
                                      Color clr = new Color((panel1.origImg.getRGB(x, y)));
                                      int r = clr.getRed();
                                      int g = clr.getGreen();
                                      int b = clr.getBlue();
                                      int aV = (int)(r * 0.3) + (int)(b * 0.59) + (int)(g * 0.11); // get the average for black and white
                          Color clr1 = new Color(aV, aV, aV);
                          panel1.origImg.setRGB(x, y, clr1.getRGB());
                      }
                  }
              }
             panel1.setPicture(panel1.origImg);     
          });
          InvB.addActionListener(e -> {
              if(panel1.origImg != null) {
                  for (int x = 0; x < panel1.origImg.getWidth(); x++) {
                      for (int y = 0; y < panel1.origImg.getHeight(); y++) {
                                      Color clr = new Color((panel1.origImg.getRGB(x, y)));
                                      int r = 255 - clr.getRed();
                                      int g = 255 - clr.getGreen();
                                      int b = 255 - clr.getBlue();
                          Color clr1 = new Color(r, g, b);
                          panel1.origImg.setRGB(x, y, clr1.getRGB());
                      }
                  }
              }
             panel1.setPicture(panel1.origImg);     
          });
          send.addActionListener(e -> {
              if(panel1.origImg != null) {
                  try {
                	 FileWriter Wr = new FileWriter("r.txt");
                	 FileWriter Wg = new FileWriter("g.txt");
                	 FileWriter Wb = new FileWriter("b.txt");
                	 FileWriter fsf = new FileWriter("565_color.txt");
                	 FileWriter BaW = new FileWriter("Black_and_White.txt");
                	 int h = panel1.origImg.getHeight();
                	 int w = panel1.origImg.getWidth();
                	 Wr.write("const uint8_t red_col PROGMEM [" + w + "][" + h + "] = {\n");
                	 Wg.write("const uint8_t blue_col PROGMEM [" + w + "][" + h + "] = {\n");
                	 Wb.write("const uint8_t green_col PROGMEM [" + w + "][" + h + "] = {\n");
                	 fsf.write("const uint16_t conv_to_565_img PROGMEM [" + w + "][" + h + "] = {\n");
                	 BaW.write("const uint8_t black_and_white PROGMEM [" + w + "][" + h + "] = {\n");
                	 for (int x = 0; x < panel1.origImg.getWidth(); x++) {
                		 Wr.write("{");
                		 Wg.write("{");
                		 Wb.write("{");
                		 fsf.write("{");
                		 BaW.write("{");
                      for (int y = 0; y < panel1.origImg.getHeight(); y++) {
                        Color clr = new Color((panel1.origImg.getRGB(x, y)));
                        int r = clr.getRed();
                        int g = clr.getGreen();
                        int b = clr.getBlue();
                        int bw = (int)(r * 0.3) + (int)(b * 0.59) + (int)(g * 0.11);
                        	if(y < h - 1) {
                        		Wr.write(r + ",");
                        		Wg.write(g + ",");
                        		Wb.write(b + ",");
                        		fsf.write((((r & 0b11111000) << 8) | ((g & 0b11111100) << 3) | (b >> 3)) + ",");
                        		BaW.write(bw + ",");
                        	}
                        	else {
                        		Wr.write(r + "}");
                            	if(x < w-1)
                            		Wr.write(",");
                            	
                        		Wg.write(r + "}");
                            	if(x < w-1)
                            		Wg.write(",");
                            	
                        		Wb.write(r + "}");
                            	if(x < w-1)
                            		Wb.write(",");
                            	
                        		fsf.write(r + "}");
                            	if(x < w-1)
                            		fsf.write(",");
                            	
                        		BaW.write(r + "}");
                            	if(x < w-1)
                            		BaW.write(",");
                        	}
                      }
                      Wr.write("\n");
                      Wg.write("\n");
                      Wb.write("\n");
                      fsf.write("\n");
                      BaW.write("\n");
                	 }
                	 Wr.write("};");
                     Wr.close();
                	 Wg.write("};");
                     Wg.close();
                	 Wb.write("};");
                     Wb.close();
                	 fsf.write("};");
                     fsf.close();
                	 BaW.write("};");
                     BaW.close();
                  }
                  	catch (IOException b1) {
                      System.out.println("An error occurred.");
                      b1.printStackTrace();
                  }
              }    
          });
      }
      
      class PictureBox extends JPanel {
    	    BufferedImage origImg = null;
    	    BufferedImage dImg = null;

    	    PictureBox(){
    	    	Color bclr = new Color(20, 20, 20);
    	        setBackground(bclr);
    	    }
    	    public void setPicture(BufferedImage img) {
    	        origImg = img;
    	        repaint();
    	    }
    	    @Override
    	    protected void paintComponent(Graphics g) {
    	        super.paintComponent(g);
    	        if(origImg != null){
    	        	g.drawImage(origImg, 0, 0, origImg.getWidth(), origImg.getHeight(), null);
    	        }
    	        
    	    }
    	}

       public static void main(String[] args)
       {
    	   new Stuff();
       }
}
