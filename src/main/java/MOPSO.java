import com.google.common.collect.Maps;
import con.TestFunction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by lxq on 15-4-20.
 */
public class MOPSO {
    public static final int VALUE_POSITIVE = 1;
    public static final int VALUE_NEGATIVE = -1;
    public static final int VALUE_EITHER = 0;
    public static final int used = 1;
    public static final int USED = used;
    static TestFunction function = TestFunction.ZDT2;
    static double c1 = 2;
    static double c2 = 1;
    static double w1 = 0.8;
    static double w2 = 0.3;
    static int gen = 5;
    static int number = 10;
    static int dimension = 30;
    static final int numOfCondition = 2;
    public static final int WINDOWS_WIDTH = 800;
    public static final int WINDOWS_HEIGHT = 400;
    public static final int X_BASE_VALUE = 400;
    public static final int Y_BASE_VALUE = 200;
    public static final int X_OFFSET = 50;
    public static final int Y_OFFSET = 100;
    public static final int STATE_BEGOVERN = 1;
    public static final int STATE_NOTBEGOVERN = 2;
    private static JPanel jPanel;
    private static double[][] fitness = new double[number][dimension];
    private static double w;
    private static int[] state;
    private static boolean isShowParot = true;
    private static Random random = new Random();
    private static double[][] v;
    private static double[][] x;
    private static int numOfParot;
    private static JFrame jFrame;
    private static int[] state2;


    public static void main(String[] args) {
        jFrame = new JFrame();
        jPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.blue);
                g.drawLine(X_OFFSET, WINDOWS_HEIGHT - Y_OFFSET, X_OFFSET + X_BASE_VALUE * 1, WINDOWS_HEIGHT - Y_OFFSET);
                g.drawLine(X_OFFSET, WINDOWS_HEIGHT - Y_OFFSET + Y_BASE_VALUE / 2, X_OFFSET, WINDOWS_HEIGHT - Y_BASE_VALUE * 1 - Y_OFFSET);
                for (int i = 0; i < 7; i++) {
                    g.drawString(String.valueOf(-1 + i * 0.5), X_OFFSET - 23, WINDOWS_HEIGHT - Y_OFFSET + Y_BASE_VALUE / 2 - i * Y_BASE_VALUE / 4);
                }
                for (int i = 0; i < 6; i++) {
                    g.drawString(String.format("%.1f", i * 0.2), X_OFFSET + X_BASE_VALUE / 5 * i, WINDOWS_HEIGHT - Y_OFFSET + 20);
                }
                for (int i = 0; i < number; i++) {
                    if (isShowParot || state2[i] == 0) {
                        g.fillOval((int) (X_BASE_VALUE * fitness[i][0]) + X_OFFSET, (int) (WINDOWS_HEIGHT - Y_BASE_VALUE * fitness[i][1]) - Y_OFFSET, 5, 5);
//                        g.drawString(String.valueOf(i), (int) (X_BASE_VALUE * fitness[i][0]) + X_OFFSET, (int) (WINDOWS_HEIGHT - Y_BASE_VALUE * fitness[i][1]) - Y_OFFSET);
                    }
                }
            }
        };

        JButton jb_w1 = new JButton("w1");
        JButton jb_w2 = new JButton("w2");
        JButton jb_c1 = new JButton("c1");
        JButton jb_c2 = new JButton("c2");
        JButton jb_num = new JButton("粒子个数");
        JButton jb_gen = new JButton("迭代次数");
        JButton jb_isShowParot = new JButton("仅显示parotfont");
        final Checkbox checkbox = new Checkbox();
        checkbox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                isShowParot = !checkbox.getState();
                repaint();
            }
        });
        final JComboBox comboBox = new JComboBox();
        comboBox.addItem(TestFunction.ZDT1.getName());
        comboBox.addItem(TestFunction.ZDT2.getName());
        comboBox.addItem(TestFunction.ZDT3.getName());
        comboBox.addItem(TestFunction.ZDT4.getName());
        final HashMap<String, Enum> map = Maps.newHashMap();
        map.put(TestFunction.ZDT1.getName(), TestFunction.ZDT1);
        map.put(TestFunction.ZDT2.getName(), TestFunction.ZDT2);
        map.put(TestFunction.ZDT3.getName(), TestFunction.ZDT3);
        map.put(TestFunction.ZDT4.getName(), TestFunction.ZDT4);
        JButton jb_ok = new JButton("确定");

        final JTextField jt_w1 = new JTextField(String.valueOf(w1));
        final JTextField jt_w2 = new JTextField(String.valueOf(w2));
        final JTextField jt_c1 = new JTextField(String.valueOf(c1));
        final JTextField jt_c2 = new JTextField(String.valueOf(c2));
        final JTextField jt_num = new JTextField(String.valueOf(number));
        final JTextField jt_gen = new JTextField(String.valueOf(gen));
        GridLayout layout = new GridLayout(8, 2, 9, 9);
        JPanel jPanel1 = new JPanel();
        jPanel1.setLayout(layout);
        jPanel1.add(jb_w1);
        jPanel1.add(jt_w1);
        jPanel1.add(jb_w2);
        jPanel1.add(jt_w2);
        jPanel1.add(jb_c1);
        jPanel1.add(jt_c1);
        jPanel1.add(jb_c2);
        jPanel1.add(jt_c2);
        jPanel1.add(jb_num);
        jPanel1.add(jt_num);
        jPanel1.add(jb_gen);
        jPanel1.add(jt_gen);
        jPanel1.add(jb_isShowParot);
        jPanel1.add(checkbox);
        jPanel1.add(comboBox);
        jPanel1.add(jb_ok);

        jFrame.add(jPanel, BorderLayout.CENTER);

        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add(jPanel1, BorderLayout.NORTH);
        jFrame.add(jPanel2, BorderLayout.EAST);
        jFrame.setSize(WINDOWS_WIDTH, WINDOWS_HEIGHT + 50);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jb_ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                function = (TestFunction) map.get(comboBox.getSelectedItem());
                w1 = Double.valueOf(jt_w1.getText());
                w2 = Double.valueOf(jt_w2.getText());
                c1 = Double.valueOf(jt_c1.getText());
                c2 = Double.valueOf(jt_c2.getText());
                gen = Integer.valueOf(jt_gen.getText());
                number = Integer.valueOf(jt_num.getText());
                fitness = new double[number][dimension];
                v = new double[number][dimension];
                x = new double[number][dimension];


                new Thread(new Runnable() {
                    public void run() {
                        double xMin = 0;
                        double xMax = 1;
                        double x1Min = 0;
                        double x1Max = 1;
                        double personalBest[][] = new double[number][dimension];
                        double globalBest[][] = new double[number][dimension];
                        if (function == TestFunction.ZDT4) {
                            xMax = 5;
                            xMin = -5;
                        }
                        initial(xMin, xMax, x1Max, x1Min);
                        pso(personalBest, globalBest, xMax, xMin, x1Max, x1Min);
                    }
                }).start();

            }
        });
    }


    private static void repaint() {
        jPanel.repaint();
    }

    private static void pso(double[][] personalBest, double[][] globalBest, double xMax, double xMin, double x1Max, double x1Min) {
        double[][] fitnessPLast = calculateFitness(x);
        int[] controlPNumLast = new int[number];

        //迭代G代
        for (int g = 0; g < gen; g++) {

            fitness = calculateFitness(x);
            state2 = calculateControlArray(fitness);
            int[] indexNotBeControlOrderDesc = getIndexNotBeControlOrderDesc(fitness);//checkParotFont(fitness);

            repaint();
//            for (int i = 0; i < number; i++) {
//                System.out.print(fitness[i][0] + " " + fitness[i][1] + "; ");
//                System.out.print(state[i] + " ");
//            }
//            System.out.println("\n");
//            System.out.println(Arrays.toString(indexNotBeControlOrderDesc));
//            System.out.println("\n");

            int[] array = calculateControlNum(fitness);

            double[] distanceBusy = calculateDistanceBusy(indexNotBeControlOrderDesc);
            for (int i = 0; i < number; i++) {
                int k = roulette(distanceBusy);
                for (int j = 0; j < dimension; j++) {
                    globalBest[i][j] = x[indexNotBeControlOrderDesc[k]][j];
                }
            }


            //更新personalBest
            for (int i = 0; i < number; i++) {
                switch (valueFitness(fitness[i], fitnessPLast[i])) {
                    case VALUE_EITHER:
                        if (controlPNumLast[i] >= array[i]) {
                            break;
                        }
                    case VALUE_POSITIVE:
                        for (int j = 0; j < dimension; j++) {
                            personalBest[i][j] = x[i][j];
                        }
                        for (int j = 0; j < numOfCondition; j++) {
                            fitnessPLast[i][j] = fitness[i][j];
                        }
                        controlPNumLast[i] = array[i];
                        break;
                    case VALUE_NEGATIVE:
                        break;
                }
            }

            //更新v、x
            w = w2 + (w1 - w2) * (gen - g) / gen;
            for (int i = 0; i < number; i++) {
                v[i][0] = updateV(v[i][0], x[i][0], personalBest[i][0], globalBest[i][0], x1Max, x1Min);
                x[i][0] = updateX(v[i][0], x[i][0], x1Max, x1Min);
                for (int j = 1; j < dimension; j++) {
                    v[i][j] = updateV(v[i][j], x[i][j], personalBest[i][j], globalBest[i][j], xMax, xMin);
                    x[i][j] = updateX(v[i][j], x[i][j], xMax, xMin);
                }
            }
        }

    }

    private static int[] getIndexNotBeControlOrderDesc(double[][] fitness) {
        int[] array = new int[number];
        int[] flag = new int[number];
        int n = 0;
        for (int i = 0; i < number; i++) {
            if (state2[i] == STATE_BEGOVERN) {
                continue;
            }
            int m = -1;
            for (int j = 0; j < number; j++) {
                if (state2[j] != STATE_BEGOVERN && flag[j] != USED && m != j) {
                    if (m == -1) {
                        m = j;
                        continue;
                    }
                    m = (fitness[j][0] < fitness[m][0]) ? j : m;
                }
            }
            array[n++] = m;
            flag[m] = USED;
        }
        numOfParot = n;
        return array;
    }

    private static double[] calculateDistanceBusy(int[] index) {
        double[] array = new double[numOfParot];
        double sum = 0;
        for (int i = 1; i < numOfParot - 1; i++) {
            array[i] = Math.abs(fitness[index[i - 1]][0] - fitness[index[i + 1]][0]) +
                    Math.abs(fitness[index[i - 1]][1] - fitness[index[i + 1]][1]);
            sum += array[i];
        }
        for (int i = 1; i < numOfParot - 1; i++) {
            array[i] /= sum;
        }
        return array;
    }

    private static double updateV(double v, double x, double pBest, double gbest, double xMax, double xMin) {
        v = w * v + c1 * rand() * (pBest - x) + c2 * rand() * (gbest - x);
        double vMax = xMax - xMin;
        if (v > vMax) {
            v = vMax;
        }
        if (v < -vMax) {
            v = -vMax;
        }
        return v;
    }

    private static double updateX(double v, double x, double xMax, double xMin) {
        x += v;
        if (x < xMin) {
            x = xMin;
        }
        if (x > xMax) {
            x = xMax;
        }
        return x;
    }

    private static int[] checkParotFont(double[][] fitness) {
        int[] array = new int[number];
        state = new int[number];
        int x0 = calculateMinIndexInX(fitness);
        state[x0] = STATE_NOTBEGOVERN;
        int x1 = 0, t = 0;
        for (int i = 0; i < number; i++) {
            double k = 0.0;
            for (int j = 0; j < number; j++) {
                if (state[j] == STATE_BEGOVERN || state[j] == STATE_NOTBEGOVERN || x0 == j) {
                    continue;
                }
                if (fitness[x0][0] == fitness[j][0]) {
                    state[j] = STATE_BEGOVERN;
                    continue;
                }
                double tanA = (fitness[x0][1] - fitness[j][1]) / (fitness[x0][0] - fitness[j][0]);
                if (tanA >= 0) {
                    state[j] = STATE_BEGOVERN;
                } else {
                    if (tanA < k) {
                        x1 = j;
                        k = tanA;
                    }
                }
            }
            array[t++] = x0;
            if (k == 0) {
                break;
            }
            state[x1] = STATE_NOTBEGOVERN;
            x0 = x1;
        }
        numOfParot = t;
        return array;
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

    private static int calculateMaxIndex(double[] value) {
        int index = 0;
        for (int i = 1; i < number; i++) {
            index = (value[i] > value[index]) ? i : index;
        }
        return index;
    }


    private static int[] calculateControlNum(double[][] fitness) {
        int[] array = new int[number];
        for (int i = 0; i < number; i++) {
            for (int j = i + 1; j < number; j++) {
                switch (valueFitness(fitness[i], fitness[j])) {
                    case VALUE_POSITIVE:
                        array[i]++;
                        break;
                    case VALUE_NEGATIVE:
                        array[j]++;
                        break;
                }
            }
        }
        return array;
    }

    private static int[] calculateControlArray(double[][] fitness) {
        int[] array = new int[number];
        for (int i = 0; i < number; i++) {
            for (int j = i + 1; j < number; j++) {
                switch (valueFitness(fitness[i], fitness[j])) {
                    case VALUE_POSITIVE:
                        array[j] = STATE_BEGOVERN;
                        break;
                    case VALUE_NEGATIVE:
                        array[i] = STATE_BEGOVERN;
                        break;
                }
            }
        }
        return array;
    }

    private static int valueFitness(double[] fitnes, double[] fitnes1) {
        if (fitnes[0] <= fitnes1[0] && fitnes[1] <= fitnes1[1]) {
            return VALUE_POSITIVE;
        } else if (fitnes[0] > fitnes1[0] && fitnes[1] > fitnes1[1]) {
            return VALUE_NEGATIVE;
        }
        return VALUE_EITHER;
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

    private static void initial(double xMin, double xMax, double x1Max, double x1Min) {
        for (int i = 0; i < number; i++) {
            v[i][0] = -(x1Max - x1Min) + rand() * (x1Max - x1Min) * 2;
            x[i][0] = x1Min + rand() * (x1Max - x1Min);
            for (int j = 1; j < dimension; j++) {
                v[i][j] = -(xMax - xMin) + rand() * (xMax - xMin) * 2;
                x[i][j] = xMin + rand() * (xMax - xMin);
            }
        }
    }


    private static double getGx(double x[], int dim) {
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
                return 1 + 10 * (dim - 1) + result;
        }

        return 1;
    }

    private static double getF1x(double x) {
        return x;
    }

    private static double getF2x(double xx[], int dim) {
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
        return random.nextDouble();
    }

    private static int roulette(double[] distance) {
        double sum = 0;
        double r = rand();
        for (int i = 1; i < numOfParot - 1; i++) {
            sum += distance[i];
            if (sum >= r) {
                return i;
            }
        }

        return 0;
    }

}
