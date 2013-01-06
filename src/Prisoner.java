import java.util.Random;

public class Prisoner {

    /*
     Określamy pierwszą decyzję danego więźnia.
     Losowane na starcie aplikacji.
      */
    private Decision.Type firstDecision;

    /*
    Punkty więźnia.
     */
    private int score;

    /*
    Nazwa więźnia.
     */
    private String name;

    /*
    Prawdopodobieństwa reakcji.
     */
    private Probabilities probabilities;

    public Prisoner(final String name, final Decision.Type firstDecision) {
        this.name = name;
        this.firstDecision = firstDecision;
        this.score = 0;

        generateProbabilities();
    }

    public int getScore() {
        return this.score;
    }

    public void addScore(int value) {
        this.score += value;
    }

    public Decision.Type getLastDecision() {
        return firstDecision;
    }

    protected void setFirstDecision(Decision.Type firstDecision) {
        this.firstDecision = firstDecision;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Probabilities getProbabilities() {
        return probabilities;
    }

    public void setProbabilities(Probabilities probabilities) {
        this.probabilities = probabilities;
    }

    public void generateProbabilities() {
        Random random = new Random();

        setProbabilities(new Probabilities(random.nextInt(100), random.nextInt(100),
                random.nextInt(100), random.nextInt(100)));
    }
}
