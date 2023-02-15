package automaton;

import java.util.ArrayList;
import java.util.HashSet;

public class FiniteAutomaton
{
    private HashSet<Character> possibleStates;
    private HashSet<Character> alphabet;
    private ArrayList<Transition> transitions;
    private char initialState;
    private char finalState;

    public void setPossibleStates(HashSet<Character> vn) {
        possibleStates = new HashSet<>(vn);
        possibleStates.add('F');
    }
    public HashSet<Character> getPossibleStates() {
        return possibleStates;
    }
    public void setAlphabet(HashSet<Character> vt) {
        alphabet = new HashSet<>(vt);
    }
    public HashSet<Character> getAlphabet() {
        return alphabet;
    }
    public void setTransitions(Transition transition) {
        transitions.add(transition);
    }
    public void setInitialState(char initialState) {
        this.initialState = initialState;
    }
    public char getInitialState() {
        return initialState;
    }
    public void setFinalState(char finalState) {
        this.finalState = finalState;
    }
    public char getFinalState() {
        return finalState;
    }
    //    public abstract boolean wordIsValid();
}
