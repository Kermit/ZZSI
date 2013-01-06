/*
Klasa określająca nagrody dla więźniów.
 */
public enum Rewards {
    Temptation(5), Reward(3), SuckersPayoff(0), Punishment(1);

    public int value;
    private Rewards(final int value) {
        this.value = value;
    }
}
