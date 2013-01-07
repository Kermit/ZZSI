package ZZSI;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int value = -1;
        int prisonersCount = 0;
        int populationCount = 0;
        int roundCount = 0;

        while (value == -1) {
            System.out.println("Podaj liczbę osobników w populacji (x > 0): ");
            value = getIntNumber();
            prisonersCount = value;
        }

        value = -1;
        while (value == -1) {
            System.out.println("Podaj liczbę populacji (x > 0): ");
            value = getIntNumber();
            populationCount = value;
        }

        value = -1;
        while (value == -1) {
            System.out.println("Podaj liczbę rund (x > 0): ");
            value = getIntNumber();
            roundCount = value;
        }

        Game game = new Game(populationCount, prisonersCount, roundCount);
        game.runGame();
    }

    private static int getIntNumber() {
        Scanner scanner = new Scanner(System.in);
        int value;

        try {
            value = scanner.nextInt();

            if (value <= 0) {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }

        return value;
    }
}
