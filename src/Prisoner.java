/**
 * Created with IntelliJ IDEA.
 * User: ArkadiuszN
 * Date: 06.01.13
 * Time: 13:34
 * To change this template use File | Settings | File Templates.
 */
public class Prisoner {

    private Decision.Type firstDecision;

    public Decision.Type getFirstDecision() {
        return firstDecision;
    }

    protected void setFirstDecision(Decision.Type firstDecision) {
        this.firstDecision = firstDecision;
    }
}
