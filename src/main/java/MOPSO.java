import com.google.common.base.Preconditions;
import con.TestFunction;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by lxq on 15-4-20.
 */
public class MOPSO {
    static TestFunction function = TestFunction.ZDT3;
    static final int c1 = 2;
    static final int c2 = 2;
    static final double w1 = 0.9;
    static final double w2 = 0.4;
    static final int G = 200;
    public static final int WINDOWS_WIDTH = 600;
    public static final int WINDOWS_HEIGHT = 600;
    public static final int X_BASE_VALUE = 400;
    public static final int Y_BASE_VALUE = 200;
    public static final int STATE_UPDATE = 1;
    public static final int STATE_NOTCHANGE = 2;
    static int number = 12;
    static int times = 200;
    static int dimension = 30;
    static int numOfCondition = 2;
    private static JPanel jPanel;
    private static double[][] fitness;
    private static double w;
    private static int[] parotFont;

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        jPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.blue);
                g.drawLine(0, WINDOWS_HEIGHT, X_BASE_VALUE * 1, WINDOWS_HEIGHT);
                g.drawLine(0, WINDOWS_HEIGHT, 0, WINDOWS_HEIGHT - Y_BASE_VALUE * 1);
                for (int i = 0; i < number; i++) {
//                    if (parotFont[i] == STATE_NOTCHANGE)
                    {
                        g.fillOval((int) (X_BASE_VALUE * fitness[i][0]), (int) (WINDOWS_HEIGHT - Y_BASE_VALUE * fitness[i][1]), 5, 5);
                    }
                }
            }
        };
        frame.setContentPane(jPanel);
        frame.setSize(WINDOWS_WIDTH, WINDOWS_HEIGHT + 50);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        double velocity[][] = new double[number][dimension];//[-1,1]
        double position[][] = new double[number][dimension];//[0,1]
        double positionPre[][] = new double[number][dimension];
        double xMin = 0;
        double xMax = 1;
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
        double[][] fitnessPre = calculateFitness(x);
        double[] dgBest = new double[dimension];
        double[][] dpBest = new double[number][dimension];
        double[][] gBest = new double[numOfCondition][dimension];
        //gBest初始化
        for (int i = 0; i < dimension; i++) {
            gBest[0][i] = x[0][i];
            gBest[1][i] = x[0][i];
        }
        //pBest初始化
        double pBest[][][] = new double[numOfCondition][number][dimension];
        for (int i = 0; i < number; i++) {
            for (int j = 0; j < dimension; j++) {
                pBest[0][i][j] = xPre[i][j];
                pBest[1][i][j] = xPre[i][j];
            }
        }
        //迭代G代
        for (int g = 0; g < G; g++) {

            fitness = calculateFitness(x);
            parotFont = checkParotFont(fitness);
            repaint();
            for (int i = 0; i < number; i++) {
                System.out.print(fitness[i][0] + " " + fitness[i][1] + ";  ");
            }


            System.out.println("\n" + Arrays.toString(parotFont));
            for (int i = 0; i < number; i++) {
                if (parotFont[i] == STATE_NOTCHANGE) {
                    continue;
                }
                for (int j = 0; j < dimension; j++) {
                    v[i][j] = w * v[i][j] + c1 * rand() * (personalBest[i][j] - x[i][j]) + c2 * rand() * (globalBest[j] - x[i][j]);
                    if (v[i][j] > vMax) {
                        v[i][j] = vMax;
                    }
                    if (v[i][j] < -vMax) {
                        v[i][j] = -vMax;
                    }
                    x[i][j] += v[i][j];
                    if (x[i][j] < xMin) x[i][j] = xMin;
                    if (x[i][j] > xMax) x[i][j] = xMax;
                }
            }
            System.out.println("\n");

            //pBest更新
            for (int i = 0; i < number; i++) {
                if (fitness[i][0] < fitnessPre[i][0]) {
                    fitnessPre[i][0] = fitness[i][0];
                    for (int j = 0; j < dimension; j++) {
                        pBest[0][i][j] = xPre[i][j];
                    }
                }
                if (fitness[i][1] < fitnessPre[i][1]) {
                    fitnessPre[i][1] = fitness[i][1];
                    for (int j = 0; j < dimension; j++) {
                        pBest[1][i][j] = xPre[i][j];
                    }
                }
            }

            //gBest更新
            int[] index = calculateMinIndex(fitness, numOfCondition);
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

            //更新globalBest，dgBest
            for (int j = 0; j < dimension; j++) {
                globalBest[j] = average(gBest[0][j], gBest[1][j]);
                dgBest[j] = distance(gBest[0][j], gBest[1][j]);
            }
            //更新dpBest
            for (int i = 0; i < number; i++) {
                for (int j = 0; j < dimension; j++) {
                    dpBest[i][j] = distance(pBest[0][i][j], pBest[1][i][j]);
                }
            }

            //更新personalBest
            for (int i = 0; i < number; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (dpBest[i][j] < dgBest[j]) {
                        personalBest[i][j] = randSelect(pBest[0][i][j], pBest[1][i][j]);
                    } else {
                        personalBest[i][j] = average(pBest[0][i][j], pBest[1][i][j]);
                    }
                }
            }

            //更新v、x
            w = w2 + (w1 - w2) * (G - g) / G;
            for (int i = 0; i < number; i++) {
                for (int j = 0; j < dimension; j++) {
                    v[i][j] = w * v[i][j] + c1 * rand() * (personalBest[i][j] - x[i][j]) + c2 * rand() * (globalBest[j] - x[i][j]);
                    if (v[i][j] > vMax) {
                        v[i][j] = vMax;
                    }
                    if (v[i][j] < -vMax) {
                        v[i][j] = -vMax;
                    }
                    x[i][j] += v[i][j];
                    if (x[i][j] < xMin) x[i][j] = xMin;
                    if (x[i][j] > xMax) x[i][j] = xMax;
                }
            }
        }
    }

    private static int[] checkParotFont(double[][] fitness) {
        int[] isUpdate = new int[number];
        int x0 = calculateMinIndexInX(fitness);
        isUpdate[x0] = STATE_NOTCHANGE;
        int x1 = 0;
        for (int i = 0; i < number; i++) {
            double k = 0.0;
            boolean isFinish = true;

            for (int j = 0; j < number; j++) {
                if (isUpdate[j] == STATE_NOTCHANGE || isUpdate[j] == STATE_UPDATE) {
                    continue;
                }
                double tanA = (fitness[x0][1] - fitness[j][1]) / (fitness[x0][0] - fitness[j][0]);
                if (tanA >= 0) {
                    isUpdate[j] = STATE_UPDATE;
                } else {
                    if (tanA < k) {
                        isFinish = false;
                        x1 = j;
                        k = tanA;
                    }
                }
            }
            if (isFinish) {
                break;
            }
            isUpdate[x1] = STATE_NOTCHANGE;
            x0 = x1;
        }
        return isUpdate;
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

    //分别找出每个粒子在各dim上fitness最小的点的索引
    private static int[] calculateMinIndex(double[][] fitness, int dim) {
        Preconditions.checkArgument(dim > 0);
        int[] index = new int[dim];
        for (int i = 0; i < dim; i++) {
            index[i] = 0;
        }
        for (int i = 1; i < number; i++) {
            for (int j = 0; j < dim; j++) {
                index[j] = (fitness[i][j] < fitness[index[j]][j]) ? i : index[j];
            }
        }
        return index;
    }

    //求x最小的最左下方的点
    private static int calculateMinIndexInX(double[][] fitness) {
        int index = 0;
        for (int i = 0; i < number; i++) {
            if (fitness[i][0] < fitness[index][0]) {
                index = i;
            } else if (fitness[i][0] == fitness[index][0]) {
                if (fitness[i][1] < fitness[i][1]) {
                    index = i;
                }
            }
        }
        return index;
    }

    private static int calculateMinIndex(double[] value) {
        int index = 0;
        for (int i = 1; i < number; i++) {
            index = (value[i] < value[index]) ? i : index;
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
        switch (function) {
            case ZDT1:
            case ZDT2:
            case ZDT3:
                for (int i = 1; i < dim; i++) {
                    result = +x[i];
                }
                return 1 + result * 9 / (dim - 1);
            case ZDT4:
                for (int i = 1; i < dim; i++) {
                    result = +(x[i] * x[i] - 10 * Math.cos(4 * Math.PI * x[i]));
                }
                return 1 + 10 * (number - 1) + result;
        }

        return 1;
    }

    private static final double getF1x(double x) {
        return x;
    }

    private static final double getF2x(double xx[], int dim) {
        double gx = getGx(xx, dim);
        double f1 = getF1x(xx[0]);
        switch (function) {
            case ZDT1:
            case ZDT4:
                return gx * (1 - Math.sqrt(f1 / gx));
            case ZDT2:
                return gx * (1 - (f1 / gx) * (f1 / gx));
            case ZDT3:
                return gx * (1 - Math.sqrt(f1 / gx) - f1 / gx * Math.sin(10 * Math.PI * f1));
        }
        return 1;
//        return (xx[0] - 2) * (xx[0] - 2);
    }

    //[0,1]
    private static double rand() {
        Random random = new Random();
        return random.nextDouble();
    }

}
