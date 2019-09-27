package fr.cocoraid.prodigyparticle.utils;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.LinkedList;
import java.util.Random;


public class UtilMath {

    public static final Random random = new Random(System.nanoTime());


    public static double offset(Location a, Location b) {
        return offset(a.toVector(), b.toVector());
    }

    public static double offset(Vector a, Vector b) {
        return a.subtract(b).length();
    }

    public static float randomRange(float min, float max) {
        return min + (float) Math.random() * (max - min);
    }

    public static int randomRange(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public static double randomRange(double min, double max) {
        return Math.random() < 0.5 ? ((1 - Math.random()) * (max - min) + min) : (Math.random() * (max - min) + min);
    }

    public static double arrondi(double A, int B) {
        return (double) ((int) (A * Math.pow(10, B) + .5)) / Math.pow(10, B);
    }

    public static int getRandomWithExclusion(int start, int end, int... exclude) {
        int rand = start + random.nextInt(end - start + 1 - exclude.length);
        for (int ex : exclude) {
            if (rand < ex) {
                break;
            }
            rand++;
        }
        return rand;
    }



    /**
     * Vecteur qui s'update autour de l'axe X avec un angle
     *
     * @param v
     * @param angle
     * @return
     */
    public static final Vector rotateAroundAxisX(Vector v, double angle) {
        double y, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        y = v.getY() * cos - v.getZ() * sin;
        z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    /**
     * Vecteur qui s'update autour de l'axe Y avec un angle
     *
     * @param v
     * @param angle
     * @return
     */
    public static final Vector rotateAroundAxisY(Vector v, double angle) {
        double x, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = v.getX() * cos + v.getZ() * sin;
        z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    /**
     * Vecteur qui s'update autour de l'axe Z avec un angle
     *
     * @param v
     * @param angle
     * @return
     */
    public static final Vector rotateAroundAxisZ(Vector v, double angle) {
        double x, y, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = v.getX() * cos - v.getY() * sin;
        y = v.getX() * sin + v.getY() * cos;
        return v.setX(x).setY(y);
    }

    /**
     * @param v
     * @param angleX
     * @param angleY
     * @param angleZ
     * @return
     */
    public static final Vector rotateVector(Vector v, double angleX, double angleY, double angleZ) {
        rotateAroundAxisX(v, angleX);
        rotateAroundAxisY(v, angleY);
        rotateAroundAxisZ(v, angleZ);
        return v;
    }

    public static Vector rotate(Vector v, Location l) {
        double yaw = l.getYaw() / 180 * Math.PI;
        double pitch = l.getPitch() / 180 * Math.PI;

        v = rotateAroundAxisX(v, pitch);
        v = rotateAroundAxisY(v, -yaw);
        return v;
    }



    public static Vector getRandomVector() {
        double x, y, z;
        x = random.nextDouble() * 2 - 1;
        y = random.nextDouble() * 2 - 1;
        z = random.nextDouble() * 2 - 1;

        return new Vector(x, y, z).normalize();
    }

    public static Vector getRandom2DVector() {
        double x, y, z;
        x = random.nextDouble() * 2 - 1;
        y = random.nextDouble() * 2 - 1;
        z = random.nextDouble() * 2 - 1;

        return new Vector(x, 0, z).normalize();
    }

    public static Vector getRandomCircleVector() {
        double rnd, x, z, y;
        rnd = random.nextDouble() * 2 * Math.PI;
        x = Math.cos(rnd);
        z = Math.sin(rnd);
        y = Math.sin(rnd);

        return new Vector(x, y, z);
    }

    public static Vector getRandomVectorline() {

        int min = -5;
        int max = 5;
        int rz = (int) (Math.random() * (max - min) + min);
        int rx = (int) (Math.random() * (max - min) + min);

        double miny = -5;
        double maxy = -1;
        double ry = (Math.random() * (maxy - miny) + miny);

        return new Vector(rx, ry, rz).normalize();
    }


    public static LinkedList<Vector> createCircle(double radius, double particleamount) {
        double amount = radius * particleamount;
        double inc = (2 * Math.PI) / amount;
        LinkedList<Vector> vecs = new LinkedList<>();
        for (int i = 0; i < amount; i++) {
            double angle = i * inc;
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            Vector v = new Vector(x, 0, z);
            vecs.add(v);
        }
        return vecs;
    }

}
