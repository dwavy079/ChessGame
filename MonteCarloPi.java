import java.util.Random;

public class MonteCarloPi {

    public static void main(String[] args) {
        int numPoints = 1000000;
        Random rand = new Random();

        int inside = 0;

        for (int i = 0; i < numPoints; i++) {
            double x = rand.nextDouble();
            double y = rand.nextDouble();

            if (x * x + y * y <= 1.0) {
                inside++;
            }
        }

        // Estimate of p and pi
        double p = (double) inside / numPoints; //p=estimate of the area
        double pi = 4.0 * p;
 
        System.out.println("Estimated π = " + pi);
    }
}
