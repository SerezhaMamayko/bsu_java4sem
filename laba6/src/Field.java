import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;
@SuppressWarnings("serial")
public class Field extends JPanel {
    private Integer[][] matrix3D;
    private Integer[][] DurabilityMatrix;
    public Integer[][] getMatrix3D() {
        return matrix3D;
    }
    private boolean paused;
    private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);
    private Timer repaintTimer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            repaint();
        }
    });

    public Integer[][] getDurabilityMatrix() {
        return DurabilityMatrix;
    }
    public Field() {
        setBackground(Color.WHITE);
        repaintTimer.start();
        matrix3D = new Integer[14][10];
        DurabilityMatrix = new Integer[14][10];
        //x scale - 49
        //y scale - 44
        for(int i = 0; i < 14; i++){
            for(int j = 0; j < 10; j++){
                if(Math.random() < 0.95){
                    matrix3D[i][j] = 0;
                    DurabilityMatrix[i][j] = 0;
                } else {
                    matrix3D[i][j] = 1;
                    DurabilityMatrix[i][j] = Integer.valueOf((int) (Math.random() * 10 + 1));
                }
            }
        }

    }

    public void setDurabilityMatrix(Integer[][] durabilityMatrix) {
        DurabilityMatrix = durabilityMatrix;
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
        for (BouncingBall ball: balls) {
            ball.paint(canvas);
        }

        canvas.setColor(Color.RED);
        canvas.setFont(new Font("TimesRoman", Font.BOLD, 14));
        boolean sign = true;
        for(int i = 0; i < 14; i++){
            for(int j = 0; j < 10; j++){
                if(DurabilityMatrix[i][j] != 0){
                    sign = false;
                }
                if(matrix3D[i][j].equals(1) && DurabilityMatrix[i][j] != 0){
                    canvas.drawRect(i * 49, j * 44, 49, 44);
                    canvas.drawString(String.valueOf(DurabilityMatrix[i][j]), (int) (i * 49 + 24.5), j * 44 + 22);
                }
            }
        }
        if(sign){
            paused = true;
            canvas.setFont(new Font("TimesRoman", Font.BOLD, 50));
            canvas.drawString("Mission complete!", (int) getHeight() / 3, (int) getWidth() / 3);
        }
    }
    public void addBall() {
        balls.add(new BouncingBall(this));
    }
    public synchronized void pause() {
        paused = true;
    }
    public synchronized void resume() {
        paused = false;
        notifyAll();
    }
    public synchronized void canMove(BouncingBall ball) throws
            InterruptedException {
        if (paused) {
            wait();
        }
    }
}

