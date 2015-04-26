package con;

/**
 * Created by lxq on 15-4-26.
 */
public class Config {
    /*
用粒子群算法求函数最值测试
测试函数为 y=x*sin(10*pi*x)+2
x取值范围[-1, 2]
*/

    private double MAX(double a, double b) {
        return ((a) > (b) ? (a) : (b));
    }

    public final double PI = 3.14159265;

    public static final int N = 30;
    public static final int G = 100;
    public static final int c1 = 2;
    public static final int c2 = 2;
    public static final double w1 = 0.9;
    public static final double w2 = 0.4;
    public static final double Vmax = 0.01;

//    double x[N],y[N],v[N],pbest[N],gbest;

//    double randd() {
//        return (double) rand() / RAND_MAX;
//    }

//    int randi(int k) {
//        return (int) (randd() * k + 0.5);
//    }

//    void cal_fitness() {
//        for (int i = 0; i < N; i++) y[i] = x[i] * sin(10 * PI * x[i]) + 2;
//    }
//
//    void init() {
//        for (int i = 0; i < N; i++) {
//            x[i] = -1 + 3 * randd();
//            v[i] = randd() * Vmax;
//        }
//        cal_fitness();
//
//        gbest = y[0];
//        for (int i = 0; i < N; i++) {
//            pbest[i] = y[i];
//            gbest = MAX(gbest, y[i]);
//        }
//    }
//
//    void pso() {
//        int i, g;
//        double w;
//        for (g = 0; g < G; g++) {
//            w = w2 + (w1 - w2) * (G - g) / G;
//            for (i = 0; i < N; i++) {
//                v[i] = w * v[i] + c1 * randd() * (pbest[i] - x[i]) + c2 * randd() * (gbest - x[i]);
//                if (v[i] > Vmax) v[i] = Vmax;
//                x[i] += v[i];
//                if (x[i] < -1) x[i] = -1;
//                if (x[i] > 2) x[i] = 2;
//            }
//            cal_fitness();
//            for (i = 0; i < N; i++) {
//                pbest[i] = MAX(pbest[i], y[i]);
//                gbest = MAX(gbest, pbest[i]);
//            }
//        }
//        printf("%.6lf\n", gbest);
//    }
//
//    int main() {
//        srand((unsigned) time(NULL));
//        init();
//        pso();
//
//        system("pause");
//        return 0;
//    }
}
