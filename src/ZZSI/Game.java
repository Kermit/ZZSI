package ZZSI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
Główna klasa gry. Tutaj są przechowywane wszystkie potrzebne informacje.
 */
public class Game {
    private int numberOfPopulations;
    private int numberOfPrisoners;
    private int numberOfRounds;
    private int currentPopulation = 1;
    private int currentRound = 1;
    private double mutationProbability;
    private double crossProbability;
    private ArrayList<Prisoner> prisoners = new ArrayList<Prisoner>();

    public Game(final int populations, final int prisoners, final int rounds, final double mutation, final double cross) {
        this.numberOfPopulations = populations;
        this.numberOfPrisoners = prisoners;
        this.numberOfRounds = rounds;
        this.mutationProbability = mutation;
        this.crossProbability = cross;

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
        double populationAvg = 0;
        for (Prisoner prisoner : prisoners) {
            populationAvg += prisoner.getScore();
        }
        populationAvg /= numberOfPrisoners;

        ArrayList<Prisoner> bestPrisoners = new ArrayList<Prisoner>();
        for (Prisoner prisoner : prisoners) {
            if (prisoner.getScore() > populationAvg) {
                bestPrisoners.add(prisoner);
            }
        }
        prisoners.removeAll(bestPrisoners);

        while (bestPrisoners.size() > 0) {
            Prisoner parent1 = Prisoner.getRandomPrisoner(bestPrisoners);
            bestPrisoners.remove(parent1);
            Prisoner parent2 = Prisoner.getRandomPrisoner(bestPrisoners);

            if (parent2 != null) {
                bestPrisoners.remove(parent2);

                if (Probabilities.getRandom((int)(crossProbability * 100))) {
                    Prisoner child1 = Prisoner.getCrossedPrisoner(parent1, parent2);
                    Prisoner child2 = Prisoner.getCrossedPrisoner(parent2, parent1);

                    child1.setName(parent1.getName());
                    child2.setName(parent2.getName());

                    prisoners.add(child1);
                    prisoners.add(child2);
                }
                else {
                    prisoners.add(parent1);
                    prisoners.add(parent2);
                }
            }
            else {
                //nieparzysta ilośc rodziców - do puli wraca ostatni rodzic
                prisoners.add(parent1);
            }
        }
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
