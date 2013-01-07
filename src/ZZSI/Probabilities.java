package ZZSI;

import java.util.Random;

/*
Przetrzymuje wartości prawdopodobieństwa daleszj współpracy po konkretnej nagrodzie.
 */
public class Probabilities {
    public static final int MAX_VALUE = 100;
    public static final int MIN_VALUE = 0;

    private int afterReward;
    private int afterTemptation;
    private int afterSuckersPayoff;
    private int afterPunishment;

    public Probabilities() {}

    public Probabilities(int afterReward, int afterTemptation, int afterSuckersPayoff, int afterPunishment) {
        this.afterReward = afterReward;
        this.afterTemptation = afterTemptation;
        this.afterSuckersPayoff = afterSuckersPayoff;
        this.afterPunishment = afterPunishment;
    }

    /**
     * Losuje prawdopodobieństwo z podanego przedzialu (0 - 100%)
     * @param propability
     * @return True jeśli wylosowano
     */
    public static boolean getRandom(int propability) {
        if (new Random().nextInt(100) < propability) {
            return true;
        }

        return false;
    }

    public int getAfterReward() {
        return afterReward;
    }

    public void setAfterReward(int afterReward) {
        this.afterReward = checkValue(afterReward);
    }

    public int getAfterTemptation() {
        return afterTemptation;
    }

    public void setAfterTemptation(int afterTemptation) {
        this.afterTemptation = checkValue(afterTemptation);
    }

    public int getAfterSuckersPayoff() {
        return afterSuckersPayoff;
    }

    public void setAfterSuckersPayoff(int afterSuckersPayoff) {
        this.afterSuckersPayoff = checkValue(afterSuckersPayoff);
    }

    public int getAfterPunishment() {
        return afterPunishment;
    }

    public void setAfterPunishment(int afterPunishment) {
        this.afterPunishment = checkValue(afterPunishment);
    }

    private int checkValue(int value) {
        if (value < MIN_VALUE) {
            return MIN_VALUE;
        }

        if (value > MAX_VALUE) {
            return MAX_VALUE;
        }

        return value;
    }
}
