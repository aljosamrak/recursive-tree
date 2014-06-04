import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author Aljoša
 * @version 1.0
 *
 */
public class TreePanel extends JPanel{

    private Random random;

    // Default values
    private int branches, thickness, levels;
    private double angle;                         // in radians
    private boolean isRandom;
    private Color color;

    public TreePanel() {
        this(4, 100, 0.52, false, 3, Color.BLACK);
    }

    /**
     *
     * @param branches      number of branches that splits from one branch
     * @param thickness     the thickness of the trunk
     * @param angle         the angle between branches is radians
     * @param isRandom      if all parameters are randomly distorted in ech recursion
     * @param levels        number of brunch levels
     * @param color         color of tree
     */
    public TreePanel(int branches, int thickness, double angle, boolean isRandom, int levels, Color color) {
        setParams(branches, thickness, angle, isRandom, levels);
        setColor(color);
        random = new Random();
    }

    /**
     * This function sets parameters of the tree
     *
     * @param branches      number of branches that splits from one branch
     * @param thickness     the thickness of the trunk
     * @param angle         the angle between branches is radians
     * @param isRandom      if all parameters are randomly distorted in ech recursion
     * @param levels        number of brunch levels
     */
    public void setParams(int branches, int thickness, double angle, boolean isRandom, int levels) {
        this.branches = branches;
        this.thickness = thickness;
        this.angle = angle;
        this.isRandom = isRandom;
        this.levels = levels;
        
        this.repaint();
    }

    /**
     * Sets the color of the tree
     *
     * @param color     color to be set
     */
    public void setColor(Color color) {
        this.color = color;
        this.repaint();
    }
    
    @Override
     public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.white);
        g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        g.setColor(color);

        // calculate the length of teh first line -> total height / sum of geometric series
        double length = (int) (this.getHeight() / ((1 - Math.pow(5.0/6.0, levels+1)) / (1.0 - 5.0/6.0)));
        if (isRandom)    // This is because total height of thee is random if isRandom=True
            length *= 0.7;

        // draw a tree
        recursion(this.getWidth() / 2, this.getHeight(), thickness, length, -Math.PI / 2, levels, (Graphics2D) g);
    }

    /**
     * This is the main function that actually draws the tree one line at a time.
     * This method draws the tree recursively
     *
     * @param posX          coordinate x
     * @param posY          coordinate y
     * @param thickness     thickness of the line
     * @param length        length of the line
     * @param angle         angle at which the line is
     * @param n             at which level we are
     * @param g             Graphics2D object
     */
    public void recursion(int posX, int posY, double thickness, double length, double angle, int n, Graphics2D g) {
        // end condition
        if (n < 0)
            return;

        // calculate coordinate of the end of the line
        int endPosX = (int)(posX + Math.cos(angle) * length);
        int endPosY = (int)(posY + Math.sin(angle) * length);

        g.setStroke(new BasicStroke((float) thickness));        // Set the line thickness
        g.drawLine(posX , posY, endPosX, endPosY);              //draw one line

        // at which angle will the first line go
        double angleFirst = angle - (this.angle * (branches - 1)) / 2;

        // recursively call this function for each branch
        for(int i = 0; i < branches; i ++) {
            if(isRandom)
                recursion(endPosX, endPosY, random(thickness * 1 / 2), random(length * 5 / 6),
                        angleFirst + random(this.angle * i), (int) Math.round(random(n) - 1), g);
            else
                recursion(endPosX, endPosY, thickness * 1 / 2, length * 5 / 6, angleFirst + this.angle * i, n - 1, g);
        }
    }


    /**
     * Randomly distorts a value with gaussian distribution
     *
     * @param data                  witch value you want to distort
     * @param standardDeviation     standard deviation od random
     * @return                      data distorted by gauss with standardDeviation
     */
    private double random(double data, double standardDeviation) {
        return data * (random.nextGaussian() * standardDeviation + 1);
    }
    private double random(double data) {
        return random(data, 0.1);
    }


    /**
     * This method saves what is shown on the panel - saves the three to  file
     *
     * @param file  file where the image will be saved
     */
    public void save(File file) {
        file = new File(file.getName().endsWith(".jpg") ? file.getAbsolutePath() : file.getAbsoluteFile() + ".jpg");

        BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();  
        g2.translate(-this.getX() - 1, -this.getY() - 1);  
        this.paint(g2);  
        g2.dispose();          

        try  {
            ImageIO.write(image, "jpg", file);
        } catch(IOException e) {
            e.printStackTrace();
        }  
    }
}
