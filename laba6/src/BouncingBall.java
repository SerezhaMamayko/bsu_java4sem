import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.stream.StreamSupport;

public class BouncingBall implements Runnable {
    private static final int MAX_RADIUS = 30;
    private static final int MIN_RADIUS = 25;
    private static final int MAX_SPEED = 2;
    private Field field;
    private int radius;
    private Color color;
    private double x;
    private double y;
    private int speed;
    private double speedX;
    private double speedY;
    public BouncingBall(Field field) {
        this.field = field;
        radius = new Double(Math.random()*(MAX_RADIUS -
                MIN_RADIUS)).intValue() + MIN_RADIUS;
        speed = new Double(Math.round(5 * MAX_SPEED / radius)).intValue();
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        double angle = Math.random() * 2 * Math.PI;
        speedX = 3*Math.cos(angle);
        speedY = 3*Math.sin(angle);
        color = new Color((float)Math.random(), (float)Math.random(),
                (float)Math.random());
        x = Math.random()*(field.getSize().getWidth()-2*radius) + radius;
        y = Math.random()*(field.getSize().getHeight()-2*radius) + radius;
        Thread thisThread = new Thread(this);
        thisThread.start();
    }
    public void run() {
        try {
            while(true) {
                field.canMove(this);
                if (x + speedX <= radius) {
                    speedX = -speedX;
                    x = radius;
                } else
                if (x + speedX >= field.getWidth() - radius) {
                    speedX = -speedX;
                    x=new Double(field.getWidth()-radius).intValue();
                } else
                if (y + speedY <= radius) {
                    speedY = -speedY;
                    y = radius;
                } else
                if (y + speedY >= field.getHeight() - radius) {
                    speedY = -speedY;
                    y = new Double(field.getHeight()-radius).intValue();
                }
                else {
                    Integer[][] matrix3D = field.getMatrix3D();
                    Integer[][] tmpDurability = field.getDurabilityMatrix();
                    for(int i = 0; i < 14; i++){
//                        Boolean x_Condition = (Math.abs((x + speedX - 49 * i)) >= radius && Math.abs((x + speedX - 49 * (i + 1))) <= radius) ||
//                                (Math.abs((x + speedX - 49 * (i + 1))) >= radius && Math.abs((x + speedX - 49 * (i))) <= radius);

                        for(int j = 0; j < 10; j++) {
//                            Boolean y_Condition = (Math.abs((y + speedY - 44 * j)) >= radius && Math.abs((y + speedY - 44 * (j + 1))) <= radius) ||
//                                    (Math.abs((y + speedY - 44 * (j + 1))) >= radius && Math.abs((y + speedY - 44 * (j))) <= radius);
                            if (matrix3D[i][j] == 1 && tmpDurability[i][j] > 0) {
                                if(x + speedX >= i * 49 && x + speedX <= 49 + i * 49 && y + speedY <= j * 44){
                                    if(y + speedY >= j * 44 - radius){
                                        speedY = -speedY;
                                        y = j * 44 - radius;
                                        tmpDurability[i][j]--;
                                        field.setDurabilityMatrix(tmpDurability);
                                    }
                                }
                                if(y + speedY >= 44 * j && y + speedY <= 44 + j * 44 && x + speedX <= 49 * i){
                                    if(x + speedX >= 49 * i - radius){
                                        speedX = -speedX;
                                        x = 49 * i - radius;
                                        tmpDurability[i][j]--;
                                        field.setDurabilityMatrix(tmpDurability);
                                    }

                                }
                                if(x + speedX >= i * 49 && x + speedX <= 49 + 49 * i && y + speedY >= 44 * j + 44){
                                    if(y + speedY <= 44 * j + 44 + radius){
                                        speedY = -speedY;
                                        y = 44 * j + 44 + radius;
                                        tmpDurability[i][j]--;
                                        field.setDurabilityMatrix(tmpDurability);
                                    }
                                }
                                if(y + speedY >= 44 * j && y + speedY <= 44 + 44 * j && x + speedX >= 49 * i + 49){
                                    if(x + speedX <= 49 * i + 49 + radius){
                                        speedX = -speedX;
                                        x = 49 * i + 49 + radius;
                                        tmpDurability[i][j]--;
                                        field.setDurabilityMatrix(tmpDurability);
                                    }
                                }

//

                            }
                        }
                    }
                    x += speedX;
                    y += speedY;
                }
                Thread.sleep(16-speed);
            }
        } catch (InterruptedException ex) {
        }
    }
    public void paint(Graphics2D canvas) {
        canvas.setColor(color);
        canvas.setPaint(color);
        Ellipse2D.Double ball = new Ellipse2D.Double(x-radius, y-radius,
                2*radius, 2*radius);
        canvas.draw(ball);
        canvas.fill(ball);
    }
}
