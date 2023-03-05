package automaton;

public class Transition {
    private final String currentState;
    private final String transitionLabel;
    private final String nextState;
    public Transition(String currentState, String transitionLabel, String nextState){
        this.currentState = currentState;
        this.transitionLabel = transitionLabel;
        this.nextState = nextState;
    }

    public String getCurrentState() {
        return currentState;
    }
    public String getTransitionLabel() {
        return transitionLabel;
    }
    public String getNextState() {
        return nextState;
    }

    @Override
    public String toString(){
        return "currentState = " + currentState + "\n" +
                "transitionLabel = " + transitionLabel + "\n" +
                "nextState = " + nextState + "\n";
    }
}
