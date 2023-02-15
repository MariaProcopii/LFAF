import java.lang.reflect.Array;
import java.util.*;
public class Grammar {
    private HashSet<Character> nonTerminalVariables = new HashSet<>();
    private HashSet<Character> terminalVariables = new HashSet<>();
    private HashMap<Character, ArrayList<String>> productions = new HashMap<>();
    private Character startSymbol;
    private int count = 1;
    public void setNonTerminalVariables(Character s){
        nonTerminalVariables.add(s);
    }
    public HashSet<Character> getNonTerminalVariables() {
        return nonTerminalVariables;
    }
    public void setTerminalVariables(Character s){
        terminalVariables.add(s);
    }
    public HashSet<Character> getTerminalVariables() {
        return terminalVariables;
    }
    public void setProductions(Character key, String val) {
        if(!productions.containsKey(key)){
            productions.put(key, new ArrayList<>());
        }
        productions.get(key).add(val);

    }
    public HashMap<Character, ArrayList<String>> getProductions() {
        return productions;
    }
    public void setStartSymbol(Character startSymbol) {
        this.startSymbol = startSymbol;
    }
    public Character getStartSymbol() {
        return startSymbol;
    }

    public ArrayList<String> generateWords(){
        ArrayList<String> result = new ArrayList<>();
        Random random = new Random();

        while(result.size()  < count){
            Stack<Character> queue = new Stack<>();
            StringBuilder stringBuilder = new StringBuilder();
            queue.add(startSymbol);
            System.out.print(startSymbol + " ---> ");
            while(!queue.isEmpty()){
                Character term = queue.pop();
                if(nonTerminalVariables.contains(term)){
                    ArrayList<String> tempArrayRes = productions.get(term);
                    String tempRes = tempArrayRes.get(random.nextInt(tempArrayRes.size()));
                    System.out.print(stringBuilder + tempRes + " ---> ");

                    for(int i = tempRes.length() - 1; i >= 0; i--){
                        queue.add(tempRes.charAt(i));
                    }
                }
                else{
                    stringBuilder.append(term);
                }
            }
            result.add(stringBuilder.toString());
        }
        return result;
    }
}
