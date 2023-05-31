public class ImageStats {
    public double averageRed = 0;
    public double averageGreen = 0;
    public double averageBlue = 0;

    public double redVariance = 0;
    public double greenVariance = 0;
    public double blueVariance = 0;

    public ImageStats(
            double averageRed,
            double averageGreen,
            double averageBlue,
            double redVariance,
            double greenVariance,
            double blueVariance
    ) {
        this.averageRed = averageRed;
        this.averageGreen = averageGreen;
        this.averageBlue = averageBlue;
        this.redVariance = redVariance;
        this.greenVariance = greenVariance;
        this.blueVariance = blueVariance;

        System.out.println(averageRed);
        System.out.println(averageGreen);
        System.out.println(averageBlue);

        System.out.println(redVariance);
        System.out.println(greenVariance);
        System.out.println(blueVariance);

        System.out.println();
    }
}
