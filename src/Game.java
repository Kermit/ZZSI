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

    /*
    Metoda generująca wszystkich więźniów.
     */
    private void generatePrisoners() {
        for (int i = 0; i < numberOfPrisoners; i++) {
            //brak randomowego wybierania pierwszej decyzji
            prisoners.add(new Prisoner(String.valueOf(i), Decision.generateRandomDecision()));
        }

        System.out.format("%-20s%-20s%-20s%-20s%-20s%-20s%-20s", "Nazwa", "Punkty", "Pierwsza decyzja", "Temptation %",
                "Reward %", "Suckers Payoff %", "Punishment %");
        System.out.println();
        for (Prisoner prisoner : prisoners) {
            System.out.format("%-20s%-20s%-20s%-20s%-20s%-20s%-20s", prisoner.getName(), prisoner.getScore(),
                    prisoner.getLastDecision(), prisoner.getProbabilities().getAfterTemptation(),
                    prisoner.getProbabilities().getAfterReward(), prisoner.getProbabilities().getAfterSuckersPayoff(),
                    prisoner.getProbabilities().getAfterPunishment());
            System.out.println();
        }
    }

    private void duel(Prisoner firstPrisoner, Prisoner secondPrisoner) {
        for (int i = 0; i < getNumberOfRounds(); ++i) {
            if (firstPrisoner.getLastDecision() == Decision.Type.Cooperate) {
                if (secondPrisoner.getLastDecision() == Decision.Type.Cooperate) {
                         //TODO ddoać generowanie decyzji i nagrody/kary
                }
                else {

                }
            }
            else {
                if (secondPrisoner.getLastDecision() == Decision.Type.Cooperate) {

                }
                else {

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
