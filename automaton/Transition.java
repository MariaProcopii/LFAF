package automaton;

public class Transition {
    private char currentState;
    private char transitionLabel;
    private char nextState;
    public Transition(char currentState, char transitionLabel, char nextState){
        this.currentState = currentState;
        this.transitionLabel = transitionLabel;
        this.nextState = nextState;
    }

    public char getCurrentState() {
        return currentState;
    }
    public char getTransitionLabel() {
        return transitionLabel;
    }
    public char getNextState() {
        return nextState;
    }

    @Override
    public String toString(){
        return "currentState = " + currentState + "\n" +
                "transitionLabel = " + transitionLabel + "\n" +
                "nextState = " + nextState + "\n";
    }
}
