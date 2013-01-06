import java.util.Random;

public class Decision {
    public enum Type {
        Cooperate,
        Defect
    }

    public static Type generateRandomDecision() {
        Random rand = new Random();

        if (rand.nextInt(1) == 1) {
            return Type.Defect;
        }
        else {
            return Type.Cooperate;
        }
    }

    public static Type generateDecision(int cooperationPropability) {

        if (cooperationPropability == 0) {
            return Type.Defect;
        }

        Random rand = new Random();
        if (rand.nextInt(100) < cooperationPropability) {
            return Type.Cooperate;
        }
        else {
            return Type.Defect;
        }
    }
}
