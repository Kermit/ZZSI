import java.util.ArrayList;

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
            prisoners.add(new Prisoner(String.valueOf(i), Decision.Type.Cooperate));
        }

        for (Prisoner prisoner : prisoners) {
            System.out.println(prisoner.getName() + ", " + prisoner.getScore() + ", " + prisoner.getFirstDecision());
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
