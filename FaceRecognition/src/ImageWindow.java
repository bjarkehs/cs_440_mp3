import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ImageWindow {
    public void printImage(double[][] matrix) {
    	int height = matrix.length;
    	int width = matrix[0].length;
    	
    	double max = Double.MIN_VALUE;
    	double min = Double.MAX_VALUE;
    	for (int i = 0; i < height; i++) {
    		for (int j = 0; j < width; j++) {
    			max = Math.max(max, matrix[i][j]);
    			min = Math.min(min, matrix[i][j]);
    		}
    	}

        final BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D)img.getGraphics();
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                double tmp = matrix[i][j];
                double alpha = (max - tmp) / (max - min);

//                g.setColor(Color.getHSBColor((float) (1-alpha), 0.8f, 0.8f));
                g.setColor(new Color((float) (1-alpha), 0, (float)alpha));
//                if (alpha <= 0.33) {
//                	g.setColor(new Color(v, 0, 0));
//                }
//                else if (c > 0.33 && c <= 0.66){
//                	g.setColor(new Color(0, v, 0));
//                }
//                else {
//                	g.setColor(new Color(0, 0, v));
//                }
                g.fillRect(j, i, 1, 1);
            }
        }

        JFrame frame = new JFrame("Image test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D)g;
                g2d.clearRect(0, 0, getWidth(), getHeight());
                g2d.setRenderingHint(
                        RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        // Or _BICUBIC
                g2d.scale(5, 5);
                g2d.drawImage(img, 0, 0, this);
            }
        };
        panel.setPreferredSize(new Dimension(width*5, height*5));
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}