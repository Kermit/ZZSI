import ZZSI.Game;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int prisonersCount = -1;
        int populationCount = -1;
        int roundCount = -1;
        double crossProb = -1;
        double mutationProb = -1;
        String showScoresAlways = "A";
        boolean showScoreAfterGeneration = false;

        while (prisonersCount == -1) {
            System.out.println("Podaj liczbę osobników w populacji (x > 0): ");
            prisonersCount = getIntNumber(0, -1);
        }

        while (populationCount == -1) {
            System.out.println("Podaj liczbę populacji (x > 0): ");
            populationCount = getIntNumber(0, -1);
        }

        while (roundCount == -1) {
            System.out.println("Podaj liczbę rund (x > 0): ");
            roundCount = getIntNumber(0, -1);
        }

        while (mutationProb == -1) {
            System.out.println("Podaj prawdopodobieństwo mutacji (0 - 1): ");
            mutationProb = getDoubleNumber(0, 1);
        }

        while (crossProb == -1) {
            System.out.println("Podaj prawdopodobieństwo krzyżowania (0 - 1): ");
            crossProb = getDoubleNumber(0, 1);
        }

        while ("A".equals(showScoresAlways)) {
            System.out.println("Pokazywać wynki co generację (T/N)?");
            showScoresAlways = getString();

            if ("T".equals(showScoresAlways)) {
                showScoreAfterGeneration = true;
            } else if ("N".equals(showScoresAlways)) {
                showScoreAfterGeneration = false;
            }
        }

        Game game = new Game(populationCount, prisonersCount, roundCount, mutationProb, crossProb, showScoreAfterGeneration);
        game.runGame();
    }

    private static int getIntNumber(final int min, final int max) {
        Scanner scanner = new Scanner(System.in);
        int value;

        try {
            value = scanner.nextInt();

            if (value <= min) {
                return -1;
            }

            if (max != -1 && value >= max) {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }

        return value;
    }

    private static double getDoubleNumber(final double min, final double max) {
        Scanner scanner = new Scanner(System.in);
        double value;

        try {
            value = scanner.nextDouble();

            if (value < min) {
                return -1;
            }

            if (max != -1 && value > max) {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }

        return value;
    }

    private static String getString() {
        Scanner scanner = new Scanner(System.in);
        String value;

        try {
            value = scanner.next();

            if (!"T".equals(value) && !"N".equals(value)) {
                return "A";
            }
        } catch (Exception e) {
            return "A";
        }

        return value;
    }
}
