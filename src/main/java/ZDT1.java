import com.google.common.collect.Lists;

import java.util.ArrayList;
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
    static int number = 120;
    static int times = 200;
    static int dimension = 30;
    static int numOfCondition = 2;

    public static void main(String[] args) {

//        JFrame frame = new JFrame();
//        JPanel panel = new JPanel();
//        JTextArea textArea = new JTextArea();
//
//        panel.setLayout(new GridLayout());
//        textArea.setText("test");
//        //当TextArea里的内容过长时生成滚动条
//        panel.add(new JScrollPane(textArea));
//        frame.add(panel);
//
//        frame.setSize(200, 200);
//        frame.setVisible(true);


        double velocity[][] = new double[number][dimension];//[-1,1]
        double position[][] = new double[number][dimension];//[0,1]
        double positionPre[][] = new double[number][dimension];
        double xMin = 0;
        double xMax = 1;
        double vMax = xMax - xMin;
        double personalBest[][] = new double[number][dimension];
        double pBest[][][] = new double[number][dimension][numOfCondition];
        double globalBest[] = new double[dimension];
        double gBest[][] = new double[dimension][numOfCondition];

        initial(velocity, position, positionPre, personalBest, globalBest, xMin, xMax);

        pso(velocity, position, positionPre, personalBest, globalBest, pBest, gBest, vMax, xMax, xMin);
    }

    private static void pso(double[][] v, double[][] x, double[][] xPre, double[][] personalBest, double[] globalBest, double[][][] pBest, double[][] gBest, double vMax, double xMax, double xMin) {
        double w;
        double[][] fitnessPre = calculateFitness(x);
        int[] gfIndex = calculateGlobalFitness(fitnessPre);
        double[] dgBest = new double[dimension];
        double[][] dpBest = new double[number][dimension];

        for (int g = 0; g < G; g++) {

            double fitness[][] = calculateFitness(x);

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
            if (fitness[index[0]][0] < fitnessPre[gfIndex[0]][0]) {
                for (int j = 0; j < dimension; j++) {
                    gBest[j][0] = x[index[0]][j];
                }
            }
            if (fitness[index[1]][1] < fitnessPre[gfIndex[1]][1]) {
                for (int j = 0; j < dimension; j++) {
                    gBest[j][1] = x[index[1]][j];
                }
            }

            for (int i = 0; i < number; i++) {
                for (int j = 0; j < numOfCondition; j++) {
                    fitnessPre[i][j] = fitness[i][j];
                }
            }

            for (int j = 0; j < dimension; j++) {
                globalBest[j] = average(gBest[j][0], gBest[j][1]);
                dgBest[j] = distance(gBest[j][0], gBest[j][1]);
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
                    v[i][j] = w * v[i][j] + c1 * rand() * (personalBest[i][j] - x[i][j]) + c2 * rand() * (globalBest[i] - x[i][j]);
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
        return x;
    }

    private static final double getF2x(double xx[], int dim) {
        double gx = getGx(xx, dim);
        return gx * (1 - Math.sqrt(getF1x(xx[0]) / gx));
    }

    //[0,1]
    private static double rand() {
        Random random = new Random();
        return random.nextDouble();
    }

}
