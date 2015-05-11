import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by lxq on 15-4-20.
 */
public class ZDT1 {
    static final int c1 = 2;
    static final int c2 = 2;
    static final double w1 = 0.9;
    static final double w2 = 0.4;
    static final int G = 100;
    public static final int WINDOWS_WIDTH = 400;
    public static final int WINDOWS_HEIGHT = 400;
    public static final int X_BASE_VALUE = 50;
    public static final int Y_BASE_VALUE = 50;
    static int number = 100;
    static int times = 200;
    static int dimension = 1;
    static int numOfCondition = 2;
    private static JPanel jPanel;
    private static double[][] fitness;

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        jPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.blue);
                g.drawLine(0, WINDOWS_HEIGHT, WINDOWS_WIDTH, WINDOWS_HEIGHT);
                g.drawLine(0, WINDOWS_HEIGHT, 0, 0);
                for (int i = 0; i < number; i++) {
                    g.fillOval((int) (X_BASE_VALUE * fitness[i][0]), (int) (WINDOWS_HEIGHT - Y_BASE_VALUE * fitness[i][1]), 5, 5);
                }
            }
        };
        frame.setContentPane(jPanel);
        frame.setSize(WINDOWS_WIDTH, WINDOWS_HEIGHT);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        double velocity[][] = new double[number][dimension];//[-1,1]
        double position[][] = new double[number][dimension];//[0,1]
        double positionPre[][] = new double[number][dimension];
        double xMin = -5;
        double xMax = 7;
        double vMax = xMax - xMin;
        double personalBest[][] = new double[number][dimension];
        double globalBest[] = new double[dimension];

        initial(velocity, position, positionPre, personalBest, globalBest, xMin, xMax);

        pso(velocity, position, positionPre, personalBest, globalBest, vMax, xMax, xMin);
    }

    private static void repaint() {
        jPanel.repaint();
    }

    private static void pso(double[][] v, double[][] x, double[][] xPre, double[][] personalBest, double[] globalBest, double vMax, double xMax, double xMin) {
        double w;
        double[][] fitnessPre = calculateFitness(x);
        double[] dgBest = new double[dimension];
        double[][] dpBest = new double[number][dimension];
        double[][] gBest = new double[numOfCondition][dimension];
        for (int i = 0; i < dimension; i++) {
            gBest[0][i] = x[0][i];
            gBest[1][i] = x[0][i];
        }
        double pBest[][][] = new double[number][dimension][numOfCondition];
        for (int i = 0; i < number; i++) {
            for (int j = 0; j < dimension; j++) {
                pBest[i][j][0] = xPre[i][j];
                pBest[i][j][1] = xPre[i][j];
            }
        }
        for (int g = 0; g < G; g++) {

            fitness = calculateFitness(x);

            repaint();
            for (int i = 0; i < number; i++) {
                System.out.print(fitness[i][0] + " " + fitness[i][1] + ";  ");
            }
            System.out.println("\n");

            for (int i = 0; i < number; i++) {
                if (fitness[i][0] < fitnessPre[i][0]) {
                    fitnessPre[i][0] = fitness[i][0];
                    for (int j = 0; j < dimension; j++) {
                        pBest[i][j][0] = xPre[i][j];
                    }
                }
                if (fitness[i][1] < fitnessPre[i][1]) {
                    fitnessPre[i][1] = fitness[i][1];
                    for (int j = 0; j < dimension; j++) {
                        pBest[i][j][1] = xPre[i][j];
                    }
                }
            }

            int[] index = calculateGlobalFitness(fitness);

            if (fitness[index[0]][0] < calculateFitness(gBest[0])[0]) {
                for (int j = 0; j < dimension; j++) {
                    gBest[0][j] = x[index[0]][j];
                }
            }
            if (fitness[index[1]][1] < calculateFitness(gBest[1])[1]) {
                for (int j = 0; j < dimension; j++) {
                    gBest[1][j] = x[index[1]][j];
                }
            }

            for (int i = 0; i < number; i++) {
                for (int j = 0; j < numOfCondition; j++) {
                    fitnessPre[i][j] = fitness[i][j];
                }
            }


            for (int j = 0; j < dimension; j++) {
                globalBest[j] = average(gBest[0][j], gBest[1][j]);
                dgBest[j] = distance(gBest[0][j], gBest[1][j]);
            }

            for (int i = 0; i < number; i++) {
                for (int j = 0; j < dimension; j++) {
                    dpBest[i][j] = distance(pBest[i][j][0], pBest[i][j][1]);
                }
            }

            for (int i = 0; i < number; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (dpBest[i][j] < dgBest[j]) {
                        personalBest[i][j] = randSelect(pBest[i][j][0], pBest[i][j][1]);
                    } else {
                        personalBest[i][j] = average(pBest[i][j][0], pBest[i][j][1]);
                    }
                }
            }

            w = w2 + (w1 - w2) * (G - g) / G;
            for (int i = 0; i < number; i++) {
                for (int j = 0; j < dimension; j++) {
                    v[i][j] = w * v[i][j] + c1 * rand() * (personalBest[i][j] - x[i][j]) + c2 * rand() * (globalBest[j] - x[i][j]);
                    if (v[i][j] > vMax) {
                        v[i][j] = vMax;
                    }
                    x[i][j] += v[i][j];
                    if (x[i][j] < xMin) x[i][j] = xMin;
                    if (x[i][j] > xMax) x[i][j] = xMax;
                }
            }
        }
    }

    private static double randSelect(double v, double v1) {
        return (rand() > 0.5) ? v : v1;
    }

    private static double distance(double v, double v1) {
        return Math.abs(v - v1);
    }

    private static double average(double v, double v1) {
        return (v + v1) / 2.0;
    }

    private static int[] calculateGlobalFitness(double[][] fitness) {
        int[] index = new int[numOfCondition];
        for (int i = 0; i < numOfCondition; i++) {
            index[i] = 0;
        }
        for (int i = 1; i < number; i++) {
            for (int j = 0; j < numOfCondition; j++) {
                index[j] = (fitness[i][j] < fitness[index[j]][j]) ? i : index[j];
            }
        }
        return index;
    }


    private static int valueFitness(double[] srcFitness, double[] destFitnes) {
        return 0;
    }


    private static double[][] calculateFitness(double[][] x) {
        double[][] fitness = new double[number][numOfCondition];
        for (int i = 0; i < number; i++) {
            fitness[i][0] = getF1x(x[i][0]);
            fitness[i][1] = getF2x(x[i], dimension);
        }
        return fitness;
    }

    private static double[] calculateFitness(double[] x) {
        double[] fitness = new double[numOfCondition];
        fitness[0] = getF1x(x[0]);
        fitness[1] = getF2x(x, dimension);
        return fitness;
    }

    private static void initial(double[][] v, double[][] x, double[][] xPre, double[][] pBest, double[] gBest, double xMin, double xMax) {
        for (int i = 0; i < number; i++) {
            for (int j = 0; j < dimension; j++) {
                v[i][j] = -(xMax - xMin) + rand() * (xMax - xMin) * 2;
                x[i][j] = xMin + rand() * (xMax - xMin);
                xPre[i][j] = x[i][j];
            }
        }
    }


    private static final double getGx(double x[], int dim) {
        double result = 0.0;
        for (int i = 1; i < dim; i++) {
            result = +x[i];
        }
        return 1 + result * 9 / (dim - 1);
    }

    private static final double getF1x(double x) {
        return x * x;
    }

    private static final double getF2x(double xx[], int dim) {
//        double gx = getGx(xx, dim);
//        return gx * (1 - Math.sqrt(getF1x(xx[0]) / gx));
//        return gx * (1 - (getF1x(xx[0]) / gx) * (getF1x(xx[0]) / gx));
        return (xx[0] - 2) * (xx[0] - 2);
    }

    //[0,1]
    private static double rand() {
        Random random = new Random();
        return random.nextDouble();
    }

}
