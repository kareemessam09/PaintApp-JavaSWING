import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.text.DecimalFormat;
import java.util.*;

public class PaintApp extends JFrame
{

		JButton brushBut, lineBut, ellipseBut, rectBut, fillBut,clearBUT,clearAllBUT , RED , GREEN , BLACK;
		JSlider transSlider;
		JLabel transLabel;
		JLabel colorLabel;
		JPanel buttonPanel;
		JPanel DrawPanel;
		DecimalFormat dec = new DecimalFormat("#.##");
		
		Graphics2D graphSettings;

		int currentAction = 1;

		float transparentVal = 1.0f;
		float strokeVal = 4.0f;
		
		Box theBox = Box.createHorizontalBox();
		Box BOX2 = Box.createHorizontalBox();
		Color fillColor=Color.BLACK;
	
	
	
        public static void main(String [] args)
        {
                new PaintApp();
        }
		
		
		
        public PaintApp()
        {
        	
            setSize(1000, 800);
            setTitle("PaintApp using SWING");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            JPanel buttonPanel = new JPanel();
            
            JPanel DrawPanel = new JPanel();
            DrawPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.insets = new Insets(10, 10, 10, 10);
			
			RED = new JButton("");
			GREEN = new JButton("");
			BLACK = new JButton("");
			RED.setBackground(Color.RED);
			RED.setMargin(new Insets(25, 25,10,30));
			RED.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fillColor = Color.RED;
				}
            });
			GREEN.setBackground(Color.GREEN);
			GREEN.setMargin(new Insets(25, 25,10,30));
			GREEN.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fillColor = Color.GREEN;
				}
            });
			BLACK.setBackground(Color.BLACK);
			BLACK.setMargin(new Insets(25, 25,10,30));
			BLACK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fillColor = Color.BLACK;
				}
            });
			
			BOX2.add(RED,c);
			BOX2.add(GREEN,c);
			BOX2.add(BLACK,c);
			DrawPanel.add(BOX2);
			
			//colorLabel = new JLabel("PICK Color:");
			
            this.add(DrawPanel, BorderLayout.NORTH);
            brushBut = makeMeButtons("Brush", 1);
            lineBut = makeMeButtons("Line", 2);
            ellipseBut = makeMeButtons("Ellipse", 3);
            rectBut = makeMeButtons("Rectangle", 4);
			clearAllBUT = makeMeButtons("ClearAll", 5);
			clearBUT = makeMeButtons("Clear", 8);
            
            fillBut = makeMeColorButton("fill", 6);
            

            
            theBox.add(brushBut);
            theBox.add(lineBut);
            theBox.add(ellipseBut);
            theBox.add(rectBut);
            theBox.add(fillBut);
			theBox.add(clearBUT);
			
			theBox.add(clearAllBUT);
            
            
            transLabel = new JLabel("Transparent: 1");
            
            transSlider = new JSlider(1, 99, 99);
            
            
            ListenForSlider lForSlider = new ListenForSlider();

            transSlider.addChangeListener(lForSlider);

            theBox.add(transLabel);
            theBox.add(transSlider);
           

            buttonPanel.add(theBox);
			
			this.getContentPane().setBackground(Color.WHITE);
            
            this.add(buttonPanel, BorderLayout.SOUTH);
            
            
            this.add(new DrawingBoard(), BorderLayout.CENTER);

            
            this.setVisible(true);
        }
        
        
        public JButton makeMeButtons(String name, final int actionNum){
        	JButton theBut = new JButton(name);
			theBut.setFocusable(false);
            theBut.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					currentAction = actionNum;
				}
            });
            
            return theBut;  
        }
        

        
        public JButton makeMeColorButton(String Name, final int actionNum){
        	JButton theBut = new JButton(Name);
			theBut.setFocusable(false);

            theBut.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					
						fillColor = JColorChooser.showDialog(null,  "Pick a Fill", Color.BLACK);  
					
				}
            });
            
            return theBut;  
        }
		
        private  class DrawingBoard extends JComponent
        {

                ArrayList<Shape> shapes = new ArrayList<Shape>();
                ArrayList<Color> shapeFill = new ArrayList<Color>();
                ArrayList<Float> transPercent = new ArrayList<Float>();
                
                Point drawStart, drawEnd;
				
                public DrawingBoard()
                {
                	
                        this.addMouseListener(new MouseAdapter()
                          {
                        	
                            public void mousePressed(MouseEvent e)
                            {
                            	
                            	if(currentAction != 1 && currentAction != 8){
                            	
                            	drawStart = new Point(e.getX(), e.getY());
                            	drawEnd = drawStart;
                                repaint();
                                
                            	}
								
                                }

                            public void mouseReleased(MouseEvent e)
                                {
                            	
                            	if(currentAction != 1 && currentAction != 8){
                            	
                            	Shape aShape = null;
                            	
                            	if (currentAction == 2){
                            		aShape = drawLine(drawStart.x, drawStart.y,
                            				e.getX(), e.getY());
                            	} else if (currentAction == 3){
                            		aShape = drawEllipse(drawStart.x, drawStart.y,
                            				e.getX(), e.getY());
                            	} else if (currentAction == 4) {
                                    aShape = drawRectangle(drawStart.x, drawStart.y,
                                    		e.getX(), e.getY());
                            	}
								
								
                            	
                                  shapes.add(aShape);
                                  shapeFill.add(fillColor);

                                  
                                  transPercent.add(transparentVal);
                                  
                                  drawStart = null;
                                  drawEnd = null;
                                  
                                  repaint();
                                  
                            	}
                                  
                                }
                          } );

                        this.addMouseMotionListener(new MouseMotionAdapter()
                        {
                        	
                          public void mouseDragged(MouseEvent e)
                          {
                        	  
                        	  if(currentAction == 1){
                      			
                      			 int x = e.getX();
                      			 int y = e.getY();
                      			
                      			Shape aShape = null;
                      			
                      			aShape = drawBrush(x,y,20,20);
                      			
                      			shapes.add(aShape);
                                  shapeFill.add(fillColor);
                                  
                                  transPercent.add(transparentVal);
                      		}
							 if(currentAction == 8){
                      			
                      			 int x = e.getX();
                      			 int y = e.getY();
                      			
                      			Shape aShape = null;
                      			
                      			aShape = drawBrush(x,y,20,20);
                      			
                      			shapes.add(aShape);
                                  shapeFill.add(Color.WHITE);
                                  
                                  transPercent.add(transparentVal);
                      		}

                        	drawEnd = new Point(e.getX(), e.getY());
                            repaint();
                          }
                        } );
                }
                

                public void paint(Graphics g)
                {
                	
                        graphSettings = (Graphics2D)g;

                        graphSettings.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
                       
                        
                        graphSettings.setStroke(new BasicStroke(10)); //////////////////////

                        Iterator<Color> fillCounter = shapeFill.iterator();
                       
                        
                        Iterator<Float> transCounter = transPercent.iterator();
                        
                        for (Shape s : shapes)
                        {
                            
                            graphSettings.setComposite(AlphaComposite.getInstance(
                                    AlphaComposite.SRC_OVER, transCounter.next()));
                        	
                        	graphSettings.draw(s);
                     
                        	graphSettings.setPaint(fillCounter.next());
                        	
                        	graphSettings.fill(s);
                        }

                        if (drawStart != null && drawEnd != null)
                        {
                            
                        	
                            
                        	graphSettings.setPaint(Color.LIGHT_GRAY);
                        	
                        	Shape aShape = null;
                        	
                        	if (currentAction == 2){
                        		aShape = drawLine(drawStart.x, drawStart.y,
                                		drawEnd.x, drawEnd.y);
                        	} else if (currentAction == 3){
                        		aShape = drawEllipse(drawStart.x, drawStart.y,
                                		drawEnd.x, drawEnd.y);
                        	} else if (currentAction == 4) {                       		
                                aShape = drawRectangle(drawStart.x, drawStart.y,
                                		drawEnd.x, drawEnd.y);
                        	}else if (currentAction == 5) {  
								dispose ();                     		
                                PaintApp.main(null); 
                        	}																			
                                
                                
                                graphSettings.draw(aShape);
                        }	
                }

                private Rectangle2D.Float drawRectangle(
                        int x1, int y1, int x2, int y2)
                {

                	
                        int x = Math.min(x1, x2);
                        int y = Math.min(y1, y2);
                        
                        int width = Math.abs(x1 - x2);
                        int height = Math.abs(y1 - y2);

                        return new Rectangle2D.Float(
                                x, y, width, height);
                }
                
                private Ellipse2D.Float drawEllipse(
                        int x1, int y1, int x2, int y2)
                {
                        int x = Math.min(x1, x2);
                        int y = Math.min(y1, y2);
                        int width = Math.abs(x1 - x2);
                        int height = Math.abs(y1 - y2);

                        return new Ellipse2D.Float(
                                x, y, width, height);
                }
                
                private Line2D.Float drawLine(
                        int x1, int y1, int x2, int y2)
                {

                        return new Line2D.Float(
                                x1, y1, x2, y2);
                }
                
                private Ellipse2D.Float drawBrush(
                        int x1, int y1, int brushStrokeWidth, int brushStrokeHeight)
                {
                	
                	return new Ellipse2D.Float(
                            x1, y1, brushStrokeWidth, brushStrokeHeight);
                	
                }
        }	
		
        private class ListenForSlider implements ChangeListener{
        	
        	
        	public void stateChanged(ChangeEvent e) {
        	
        	
        		if(e.getSource() == transSlider){
        	
        			transLabel.setText("Transparent: " + dec.format(transSlider.getValue() * .01) );
        			
        			
        			transparentVal = (float) (transSlider.getValue() * .01);
					strokeVal = (float) (transSlider.getValue() * 2);
        			
        		}
        	
        	}
        	
        }
}