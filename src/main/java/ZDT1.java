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
        double xMin = 0;
        double xMax = 1;
        double Vmax = xMax - xMin;
        double personalBest[][] = new double[number][dimension];
        double globalBest[] = new double[dimension];

        initial(velocity, position, personalBest, globalBest, xMin, xMax);

        pso(velocity, position, personalBest, globalBest, Vmax, xMax, xMin);
    }

    private static void pso(double[][] v, double[][] x, double[][] pbest, double[] gbest, double Vmax, double xMax, double xMin) {
        ArrayList<double[]> list = Lists.newArrayList(v);
        double w;
        for (int g = 0; g < G; g++) {
            w = w2 + (w1 - w2) * (G - g) / G;
            for (int i = 0; i < number; i++) {
                for (int j = 0; j < dimension; j++) {
                    v[i][j] = w * v[i][j] + c1 * rand() * (pbest[i][j] - x[i][j]) + c2 * rand() * (gbest[i] - x[i][j]);
                    if (v[i][j] > Vmax) {
                        v[i][j] = Vmax;
                    }
                    x[i][j] += v[i][j];
                    if (x[i][j] < xMin) x[i][j] = xMin;
                    if (x[i][j] > xMax) x[i][j] = xMax;
                }
            }
            double fitness[][] = calculateFitness(x);
//        for (i = 0; i < N; i++) {
//            pbest[i] = MAX(pbest[i], y[i]);
//            gbest = MAX(gbest, pbest[i]);
//        }
        }
    }

    private static double[][] calculateFitness(double[][] x) {
        double[][] fitness = new double[number][2];
        for (int i = 0; i < number; i++) {
            fitness[i][0] = getF1x(x[i][0]);
            fitness[i][1] = getF2x(x[i], dimension);
        }
        return fitness;
    }

    private static void initial(double[][] v, double[][] x, double[][] pBest, double[] gBest, double xMin, double xMax) {
        for (int i = 0; i < number; i++) {
            for (int j = 0; j < dimension; j++) {
                v[i][j] = -(xMax - xMin) + rand() * (xMax - xMin) * 2;
                x[i][j] = xMin + rand() * (xMax - xMin);
                pBest[i][j] = v[i][j];
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
