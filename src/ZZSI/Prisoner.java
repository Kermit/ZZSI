package ZZSI;

import java.util.ArrayList;
import java.util.Random;

public class Prisoner {

    /*
     Określamy pierwszą decyzję danego więźnia.
     Losowane na starcie aplikacji.
      */
    private Decision.Type lastDecision;

    /*
    Punkty więźnia.
     */
    private double score;

    /*
    Nazwa więźnia.
     */
    private String name;

    /*
    Prawdopodobieństwa reakcji.
     */
    private Probabilities probabilities;

    public Prisoner(final String name, final Decision.Type lastDecision) {
        this.name = name;
        this.lastDecision = lastDecision;
        this.score = 0;

        generateProbabilities();
    }

    public static Prisoner getRandomPrisoner(ArrayList<Prisoner> prisonersToChoose, double maxScore) {
        Random rand = new Random();

        if (prisonersToChoose.size() > 0) {

            while (true) {
                Prisoner prisoner = prisonersToChoose.get(rand.nextInt(prisonersToChoose.size()));
                double prisonerPercetage = prisoner.getScore() * 80 / maxScore + 10;

                if (Probabilities.getRandom((int)prisonerPercetage)) {
                    return prisoner;
                }
            }
        }
        else {
            return null;
        }
    }

    public static Prisoner getCrossedPrisoner(Prisoner father, Prisoner mother) {
        double attribA = 0.75;
        double attribB = 0.25;

        Probabilities probabilities = new Probabilities(
                (int)(father.getProbabilities().getAfterReward() * attribA + mother.getProbabilities().getAfterReward() * attribB),
                (int)(father.getProbabilities().getAfterTemptation() * attribA + mother.getProbabilities().getAfterTemptation() * attribB),
                (int)(father.getProbabilities().getAfterSuckersPayoff() * attribA + mother.getProbabilities().getAfterSuckersPayoff() * attribB),
                (int)(father.getProbabilities().getAfterPunishment() * attribA + mother.getProbabilities().getAfterPunishment() * attribB)
        );

        Prisoner child = new Prisoner("-1", Decision.generateRandomDecision());
        child.setProbabilities(probabilities);

        return child;
    }

    public void mutate() {
        Random rand = new Random();
        int randResult = rand.nextInt(4);

        switch (randResult) {
            case 0:
                probabilities.setAfterReward((int)(probabilities.getAfterReward() * rand.nextDouble()) + rand.nextInt(30));
                break;
            case 1:
                probabilities.setAfterTemptation((int)(probabilities.getAfterTemptation() * rand.nextDouble()) + rand.nextInt(30));
                break;
            case 2:
                probabilities.setAfterSuckersPayoff((int)(probabilities.getAfterSuckersPayoff() * rand.nextDouble()) + rand.nextInt(30));
                break;
            case 3:
                probabilities.setAfterPunishment((int)(probabilities.getAfterPunishment() * rand.nextDouble()) + rand.nextInt(30));
                break;
        }
    }

    public double getScore() {
        return this.score;
    }

    public void addScore(double value) {
        this.score += value;
    }

    public Decision.Type getLastDecision() {
        return lastDecision;
    }

    protected void setLastDecision(Decision.Type lastDecision) {
        this.lastDecision = lastDecision;
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
