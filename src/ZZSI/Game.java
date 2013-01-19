package ZZSI;

import java.util.*;

/*
Główna klasa gry. Tutaj są przechowywane wszystkie potrzebne informacje.
 */
public class Game {
    private int numberOfPopulations;
    private int numberOfPrisoners;
    private int numberOfRounds;
    private boolean showScoreAfterGeneration;
    private boolean zeroScores;
    private int currentPopulation = 1;
    private int currentRound = 1;
    private double mutationProbability;
    private double crossProbability;
    private Map<String, Prisoner> prisoners = new LinkedHashMap<String, Prisoner>();

    public Game(final int populations, final int prisoners, final int rounds, final double mutation, final double cross,
                final boolean showScoreAfterGeneration, final boolean zeroScores) {
        this.numberOfPopulations = populations;
        this.numberOfPrisoners = prisoners;
        this.numberOfRounds = rounds;
        this.mutationProbability = mutation;
        this.crossProbability = cross;
        this.showScoreAfterGeneration = showScoreAfterGeneration;
        this.zeroScores = zeroScores;

        generatePrisoners();
    }

    /**
     * Metoda generująca wszystkich więźniów.
     */
    private void generatePrisoners() {
        for (int i = 0; i < numberOfPrisoners; i++) {
            Prisoner prisoner = new Prisoner(currentPopulation + "-" + String.valueOf(i), Decision.generateRandomDecision());
            prisoners.put(prisoner.getName(), prisoner);
        }

        printScroes(false);
    }

    public void runGame() {
        while (currentPopulation != numberOfPopulations) {
            calculateGeneration();
            cross();

            if (currentPopulation != numberOfPopulations - 1) {  //mutowanie do przedostatniej rundy, aby wyświetlić prawidłowe wyniki
                mutate();
                if (zeroScores) {
                    for (Prisoner prisoner : prisoners.values()) {
                        prisoner.zeroScore();
                    }
                }
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
        for (Prisoner prisoner : prisoners.values()) {
            if (prisoner.getScore() > populationMax) {
                populationMax = prisoner.getScore();
            }
        }

        Map<String, Prisoner> winners = new LinkedHashMap<String, Prisoner>();

        while (winners.size() < numberOfPrisoners) {
            Prisoner parent1 = Prisoner.getRandomPrisoner(prisoners.values(), populationMax);
            Prisoner parent2 = Prisoner.getRandomPrisoner(prisoners.values(), populationMax);

            if (Probabilities.getRandom((int) (crossProbability * 100))) {
                Map<String, Prisoner> children = Prisoner.getCrossedPrisoner(parent1, parent2, currentPopulation);

                Prisoner child1 = children.entrySet().iterator().next().getValue();
                Prisoner child2 = children.entrySet().iterator().next().getValue();

                winners.put(child1.getName(), child1);
                if (winners.size() < numberOfPrisoners) {
                    winners.put(child2.getName(), child2);
                }
            } else {
                if (!winners.containsKey(parent1.getName())) {
                    winners.put(parent1.getName(), parent1);
                }
                if (!winners.containsKey(parent2.getName()) && winners.size() < numberOfPrisoners) {
                    winners.put(parent2.getName(), parent2);
                }
            }
        }

        prisoners = winners;
    }

    /**
     * Wykonaj mutację
     */
    private void mutate() {
        for (Prisoner prisoner : prisoners.values()) {
            if (Probabilities.getRandom((int) (mutationProbability * 100))) {
                prisoner.mutate();
            }
        }
    }

    /**
     * Rozgrywa wszystkie gry w tej generacji.
     */
    private void calculateGeneration() {
        ArrayList<Prisoner> list = new ArrayList<Prisoner>(prisoners.values());
        for (int i = 0; i < list.size(); ++i) {
            for (int y = i + 1; y < list.size(); ++y) {
                duel(list.get(i), list.get(y));
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
            List<Map.Entry<String, Prisoner>> entries = new ArrayList<Map.Entry<String, Prisoner>>(prisoners.entrySet());
            Collections.sort(entries, new Comparator<Map.Entry<String, Prisoner>>() {
                @Override
                public int compare(Map.Entry<String, Prisoner> o1, Map.Entry<String, Prisoner> o2) {
                    return Double.compare(o2.getValue().getScore(), o1.getValue().getScore());
                }
            });

            prisoners = new LinkedHashMap<String, Prisoner>();
            for (Map.Entry<String, Prisoner> entry : entries) {
                prisoners.put(entry.getKey(), entry.getValue());
            }
        }
        System.out.format("%-20s%-20s%-20s%-20s%-20s%-20s%-20s", "Nazwa", "Punkty", "Ostatnia decyzja", "Temptation %",
                "Reward %", "Suckers Payoff %", "Punishment %");
        System.out.println();
        for (Prisoner prisoner : prisoners.values()) {
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
}
