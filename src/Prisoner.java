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

    public int getScore() {
        return this.score;
    }

    public void addScore(int value) {
        this.score += value;
    }

    public Decision.Type getFirstDecision() {
        return firstDecision;
    }

    protected void setFirstDecision(Decision.Type firstDecision) {
        this.firstDecision = firstDecision;
    }
}
