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
                g.setColor(new Color((float) (1-alpha), 0, (float)alpha));
                g.fillRect(j, i, 1, 1);
            }
        }

        JFrame frame = new JFrame("Image");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel() {
        	private static final long serialVersionUID = 1L;
			@Override
	        protected void paintComponent(Graphics g) {
	            Graphics2D g2d = (Graphics2D)g;
	            g2d.clearRect(0, 0, getWidth(), getHeight());
	            g2d.setRenderingHint(
	                    RenderingHints.KEY_INTERPOLATION,
	                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	                    // Or _BICUBIC
	            g2d.scale(10, 10);
	            g2d.drawImage(img, 0, 0, this);
	        }
        };
        panel.setPreferredSize(new Dimension(width*10, height*10));
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}