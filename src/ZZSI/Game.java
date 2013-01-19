package ZZSI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
Główna klasa gry. Tutaj są przechowywane wszystkie potrzebne informacje.
 */
public class Game {
    private int numberOfPopulations;
    private int numberOfPrisoners;
    private int numberOfRounds;
    private boolean showScoreAfterGeneration;
    private int currentPopulation = 1;
    private int currentRound = 1;
    private double mutationProbability;
    private double crossProbability;
    private ArrayList<Prisoner> prisoners = new ArrayList<Prisoner>();

    public Game(final int populations, final int prisoners, final int rounds, final double mutation, final double cross,
                final boolean showScoreAfterGeneration) {
        this.numberOfPopulations = populations;
        this.numberOfPrisoners = prisoners;
        this.numberOfRounds = rounds;
        this.mutationProbability = mutation;
        this.crossProbability = cross;
        this.showScoreAfterGeneration = showScoreAfterGeneration;

        generatePrisoners();
    }

    /**
     * Metoda generująca wszystkich więźniów.
     */
    private void generatePrisoners() {
        for (int i = 0; i < numberOfPrisoners; i++) {
            prisoners.add(new Prisoner(currentPopulation + "-" + String.valueOf(i), Decision.generateRandomDecision()));
        }

        printScroes(false);
    }

    public void runGame() {
        while (currentPopulation != numberOfPopulations) {
            calculateGeneration();
            cross();

            if (currentPopulation != numberOfPopulations - 1) {  //mutowanie do przedostatniej rundy, aby wyświetlić prawidłowe wyniki
                mutate();
            }

            ++currentPopulation;
            printProgress();
        }

        printScroes(true);
    }

    /**
     * Wykonaj krzyżowanie.
     */
    private void cross() {

        double populationMax = 0;
        for (Prisoner prisoner : prisoners) {
            if (prisoner.getScore() > populationMax) {
                populationMax = prisoner.getScore();
            }
        }

        ArrayList<Prisoner> winners = new ArrayList<Prisoner>();

        while (winners.size() < numberOfPrisoners) {
            Prisoner parent1 = Prisoner.getRandomPrisoner(prisoners, populationMax);
            Prisoner parent2 = Prisoner.getRandomPrisoner(prisoners, populationMax);

            if (Probabilities.getRandom((int)(crossProbability * 100))) {
                List<Prisoner> childs = Prisoner.getCrossedPrisoner(parent1, parent2, currentPopulation);
                winners.addAll(childs);
            }
            else {
                if (!winners.contains(parent1)) {
                    winners.add(parent1);
                }
                if (!winners.contains(parent2) && winners.size() < numberOfPrisoners) {
                    winners.add(parent2);
                }
            }
        }

        prisoners = winners;
    }

    /**
     * Wykonaj mutację
     */
    private void mutate() {
        for (Prisoner prisoner : prisoners) {
            if (Probabilities.getRandom((int)(mutationProbability * 100))) {
                 prisoner.mutate();
            }
        }
    }

    /**
     * Rozgrywa wszystkie gry w tej generacji.
     */
    private void calculateGeneration() {
        for (int i = 0; i < prisoners.size(); ++i) {
            for (int y = i + 1; y < prisoners.size(); ++y) {
                duel(prisoners.get(i), prisoners.get(y));
            }
        }
    }

    /**
     * Metoda odpowiadający za odbycie pojedynków.
     *
     * @param firstPrisoner  pierwszy gracz
     * @param secondPrisoner drugi gracz
     */
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

    /**
     * Wydruk wyników.
     *
     * @param sort jeśli true to sortuj wyniki, inaczej pozostaw kolejność listy
     */
    private void printScroes(boolean sort) {
        if (sort) {
            Collections.sort(prisoners, new Comparator<Prisoner>() {
                @Override
                public int compare(Prisoner o1, Prisoner o2) {
                    return Double.compare(o2.getScore(), o1.getScore());
                }
            });
        }
        System.out.format("%-20s%-20s%-20s%-20s%-20s%-20s%-20s", "Nazwa", "Punkty", "Ostatnia decyzja", "Temptation %",
                "Reward %", "Suckers Payoff %", "Punishment %");
        System.out.println();
        for (Prisoner prisoner : prisoners) {
            System.out.format("%-20s%-20s%-20s%-20s%-20s%-20s%-20s", prisoner.getName(),
                    prisoner.getScore() / numberOfPrisoners, prisoner.getLastDecision(),
                    prisoner.getProbabilities().getAfterTemptation(), prisoner.getProbabilities().getAfterReward(),
                    prisoner.getProbabilities().getAfterSuckersPayoff(), prisoner.getProbabilities().getAfterPunishment());
            System.out.println();
        }
    }

    /**
     * Wydrukuj informację o przetwarzanej generacji.
     */
    private void printProgress() {
        System.out.println("Generacja " + currentPopulation + " z " + numberOfPopulations);
        if (showScoreAfterGeneration) {
            printScroes(false);
        }
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
