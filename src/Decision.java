import java.util.Random;

public class Decision {
    public enum Type {
        Cooperate,
        Defect
    }

    public static Type GenerateDecision(int cooperationPropability) {

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
