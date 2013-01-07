package ZZSI;

/*
Klasa określająca nagrody dla więźniów.
 */
public enum Rewards {
    /**
     * Nieuczciwy osobnik oszukał uczciwego
     */
    Temptation(5),
    /**
     * Oba osobnicy współpracowali
     */
    Reward(3),
    /**
     * Uczciwy osobnik został oszukany
     */
    SuckersPayoff(0),
    /**
     * Wzajemne oszukanie
     */
    Punishment(1);

    public int value;
    private Rewards(final int value) {
        this.value = value;
    }
}
