package ZZSI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
Główna klasa gry. Tutaj są przechowywane wszystkie potrzebne informacje.
 */
public class Game {
    private int numberOfPopulations;
    private int numberOfPrisoners;
    private int numberOfRounds;
    private int currentPopulation = 1;
    private int currentRounds = 1;
    private ArrayList<Prisoner> prisoners = new ArrayList<Prisoner>();

    public Game(final int populations, final int prisoners, final int rounds) {
        this.numberOfPopulations = populations;
        this.numberOfPrisoners = prisoners;
        this.numberOfRounds = rounds;

        generatePrisoners();
    }

    /**
     * Metoda generująca wszystkich więźniów.
     */
    private void generatePrisoners() {
        for (int i = 0; i < numberOfPrisoners; i++) {
            prisoners.add(new Prisoner(String.valueOf(i), Decision.generateRandomDecision()));
        }

        printScroes(false);
    }

    public void runGame() {
        while (currentPopulation != numberOfPopulations) {
            calculateGeneration();
            ++currentPopulation;
            printProgress();
        }

        printScroes(true);
    }

    /**
     * Rozgrywa wszystkie gra w tej generacji.
     */
    private void calculateGeneration() {
        for (int i = 0; i < prisoners.size(); ++i) {
            for (int y = i + 1; y < prisoners.size(); ++y) {
                duel(prisoners.get(i), prisoners.get(y));
            }
        }
    }

    private void duel(Prisoner firstPrisoner, Prisoner secondPrisoner) {
        for (int i = 0; i < getNumberOfRounds(); ++i) {
            if (Decision.Type.Cooperate == firstPrisoner.getLastDecision()) {
                if (Decision.Type.Cooperate == secondPrisoner.getLastDecision()) {
                    firstPrisoner.setLastDecision(Decision.generateDecision(firstPrisoner.getProbabilities().getAfterReward()));
                    secondPrisoner.setLastDecision(Decision.generateDecision(secondPrisoner.getProbabilities().getAfterReward()));

                    firstPrisoner.addScore(Rewards.Reward.value);
                    secondPrisoner.addScore(Rewards.Reward.value);
                } else {
                    firstPrisoner.setLastDecision(Decision.generateDecision(firstPrisoner.getProbabilities().getAfterSuckersPayoff()));
                    secondPrisoner.setLastDecision(Decision.generateDecision(secondPrisoner.getProbabilities().getAfterTemptation()));

                    firstPrisoner.addScore(Rewards.SuckersPayoff.value);
                    secondPrisoner.addScore(Rewards.Temptation.value);
                }
            } else {
                if (Decision.Type.Cooperate == secondPrisoner.getLastDecision()) {
                    firstPrisoner.setLastDecision(Decision.generateDecision(firstPrisoner.getProbabilities().getAfterTemptation()));
                    secondPrisoner.setLastDecision(Decision.generateDecision(secondPrisoner.getProbabilities().getAfterSuckersPayoff()));

                    firstPrisoner.addScore(Rewards.Temptation.value);
                    secondPrisoner.addScore(Rewards.SuckersPayoff.value);
                } else {
                    firstPrisoner.setLastDecision(Decision.generateDecision(firstPrisoner.getProbabilities().getAfterPunishment()));
                    secondPrisoner.setLastDecision(Decision.generateDecision(secondPrisoner.getProbabilities().getAfterPunishment()));

                    firstPrisoner.addScore(Rewards.Punishment.value);
                    secondPrisoner.addScore(Rewards.Punishment.value);
                }
            }
        }
    }

    private Map<Prisoner, Prisoner> generatePairs(ArrayList<Prisoner> prisoners) {

        Map<Prisoner, Prisoner> pairs = new HashMap<Prisoner, Prisoner>();

        for (int i = 0; i < prisoners.size(); ++i) {
            for (int y = i + 1; y < prisoners.size(); ++y) {
                pairs.put(prisoners.get(i), prisoners.get(y));
            }
        }

        return pairs;
    }

    private void printScroes(boolean sort) {
        System.out.format("%-20s%-20s%-20s%-20s%-20s%-20s%-20s", "Nazwa", "Punkty", "Ostatnia decyzja", "Temptation %",
                "Reward %", "Suckers Payoff %", "Punishment %");
        System.out.println();
        for (Prisoner prisoner : prisoners) {
            System.out.format("%-20s%-20s%-20s%-20s%-20s%-20s%-20s", prisoner.getName(), prisoner.getScore() / numberOfPopulations,
                    prisoner.getLastDecision(), prisoner.getProbabilities().getAfterTemptation(),
                    prisoner.getProbabilities().getAfterReward(), prisoner.getProbabilities().getAfterSuckersPayoff(),
                    prisoner.getProbabilities().getAfterPunishment());
            System.out.println();
        }
    }

    private void printProgress() {
        try {
            String OS = System.getProperty("os.name");
            if (OS.startsWith("Windows")) {
                Runtime.getRuntime().exec("cls");
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException e) {

        }

        System.out.println("Progress " + currentPopulation);
    }

    public int getNumberOfPopulations() {
        return numberOfPopulations;
    }

    public int getNumberOfPrisoners() {
        return numberOfPrisoners;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public ArrayList<Prisoner> getPrisoners() {
        return prisoners;
    }
}
