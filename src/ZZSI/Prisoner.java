package ZZSI;

import java.util.ArrayList;
import java.util.List;
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

    public static List<Prisoner> getCrossedPrisoner(Prisoner father, Prisoner mother, int currentPopulation) {
        Random random = new Random();

        List<Prisoner> result = new ArrayList<Prisoner>();

        double afterRewardA = random.nextDouble();
        double afterRewardB = 1.0 - afterRewardA;
        double afterTemptationA = random.nextDouble();
        double afterTemptationB = 1.0 - afterTemptationA;
        double afterSuckerPayoffA = random.nextDouble();
        double afterSuckerPayoffB = 1.0 - afterSuckerPayoffA;
        double afterPunishmentA = random.nextDouble();
        double afterPunishmentB = 1.0 - afterPunishmentA;

        Probabilities firstChildProb = new Probabilities(
                (int)(father.getProbabilities().getAfterReward() * afterRewardA + mother.getProbabilities().getAfterReward() * afterRewardB),
                (int)(father.getProbabilities().getAfterTemptation() * afterTemptationA + mother.getProbabilities().getAfterTemptation() * afterTemptationB),
                (int)(father.getProbabilities().getAfterSuckersPayoff() * afterSuckerPayoffA + mother.getProbabilities().getAfterSuckersPayoff() * afterSuckerPayoffB),
                (int)(father.getProbabilities().getAfterPunishment() * afterPunishmentA + mother.getProbabilities().getAfterPunishment() * afterPunishmentB)
        );

        Probabilities secondChildProb = new Probabilities(
                (int)(father.getProbabilities().getAfterReward() * afterRewardB + mother.getProbabilities().getAfterReward() * afterRewardA),
                (int)(father.getProbabilities().getAfterTemptation() * afterTemptationB + mother.getProbabilities().getAfterTemptation() * afterTemptationA),
                (int)(father.getProbabilities().getAfterSuckersPayoff() * afterSuckerPayoffB + mother.getProbabilities().getAfterSuckersPayoff() * afterSuckerPayoffA),
                (int)(father.getProbabilities().getAfterPunishment() * afterPunishmentB + mother.getProbabilities().getAfterPunishment() * afterPunishmentA)
        );

        Prisoner firstChild = new Prisoner(currentPopulation + father.getName().substring(father.getName().indexOf("-")), Decision.generateRandomDecision());
        firstChild.setProbabilities(firstChildProb);

        Prisoner secondChild = new Prisoner(currentPopulation + mother.getName().substring(mother.getName().indexOf("-")), Decision.generateRandomDecision());
        secondChild.setProbabilities(secondChildProb);

        result.add(firstChild);
        result.add(secondChild);
        return result;
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
