package grammarConversion;
import grammar.Grammar;

import java.lang.reflect.Array;
import java.util.*;
import java.lang.*;

public class ToCNF{

    private static Grammar grammar;

    //for not being able to make the instance of the ToCNF class
    private ToCNF(){}

    private static void rmEmptyProd(){
        HashSet<String> emptyProd = new HashSet<>(); //set of empty productions

        for(String key : grammar.getProductions().keySet()){
            ArrayList<String> prodList = grammar.getProductions().get(key);
            if(prodList.contains("")){
                prodList.remove("");
                emptyProd.add(key);
            }
        }

        for(String emptyP: emptyProd){
            for(String prodLeft : grammar.getProductions().keySet()){
                List<String> newProductions = new ArrayList<>(); //list with new production with all combination of found occurrences
                ArrayList<String> prodList = grammar.getProductions().get(prodLeft);
                for (int i = 0; i < prodList.size(); i++){
                    String s = prodList.get(i);
                    if(s.contains(emptyP)){
                        prodList.remove(s);
                        newProductions.addAll(newProdList(s, emptyP));
                        i--;
                    }
                }
                prodList.addAll(newProductions);
            }
        }
    }

    // creates list with new production with all combination of found occurrences
    private static ArrayList<String> newProdList(String s, String emptyP){
        ArrayList<String> newProductions = new ArrayList<>();
        ArrayList<Integer> occurrences = new ArrayList<>();
        for(int i = 0; i < s.length(); i++){
            if(emptyP.charAt(0) == s.charAt(i)){
                occurrences.add(i);
            }
        }
        List<List<Integer>> combinations = generateCombinations(occurrences);
        for(List<Integer> positions : combinations){
            newProductions.add(removeCharsAtPositions(s, positions));
        }
        newProductions.add(s); //add the initial production without removed symbols
        return newProductions;
    }

    // creates combination of all occurrences of emptyProduction in the given production
    private static List<List<Integer>> generateCombinations(ArrayList<Integer> nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(result, new ArrayList<>(), nums, 0);
        return result;
    }

    //backtracking is used to create the combinations
    private static void backtrack(List<List<Integer>> result, List<Integer> tempList, ArrayList<Integer> nums, int start) {
        if (tempList.size() > 0) {
            result.add(new ArrayList<>(tempList));
        }
        for (int i = start; i < nums.size(); i++) {
            tempList.add(nums.get(i));
            backtrack(result, tempList, nums, i + 1);
            tempList.remove(tempList.size() - 1);
        }
    }

    //removers the emptyTransaction symbol found at a given position
    private static String removeCharsAtPositions(String inputString, List<Integer> positions) {
        StringBuilder stringBuilder = new StringBuilder(inputString);

        // Sort the positions in descending order so that we don't have to adjust
        // the positions when we remove characters from the string.
        Collections.sort(positions, Collections.reverseOrder());
        for (int position : positions) {
            stringBuilder.deleteCharAt(position);
        }

        return stringBuilder.toString();
    }

    private static void rmUnitProd(){
        for(String key : grammar.getProductions().keySet()){
            ArrayList<String> prodList = grammar.getProductions().get(key);
            for(int i = 0; i < prodList.size(); i ++){
                String prod = prodList.get(i);
                if(prod.length() == 1 && Character.isUpperCase(prod.charAt(0))){ //chase when we have unit A -> B
                    prodList.remove(prod); // remove the found production Ex: B
                    prodList.addAll(grammar.getProductions().get(prod)); // add the set of productions corresponding to deleted production B -> ...
                }
            }
        }
    }

    private static void rmInaccessibleProd(){ //when the production doesn't appear in any deviation from the starting symbol
        ArrayList<String> prodFromStartS = new ArrayList<>();
        Stack<String> verifiedProd = new Stack<>();
        String start = grammar.getStartSymbol();
        prodFromStartS.add(start);
        verifiedProd.push(start);

        while(!verifiedProd.isEmpty()){ //collect all the reachable productions from the starting one
            String symbol = verifiedProd.pop();
            ArrayList<String> prodList = grammar.getProductions().get(symbol);

            for (int i = 0; i < prodList.size(); i++) {
                String prod = prodList.get(i);
                for (int j = 0; j < prod.length(); j++) {
                    char ch = prod.charAt(j);
                    String leftProd = Character.toString(ch);
                    if (Character.isUpperCase(ch) && !prodFromStartS.contains(leftProd)) {
//                        System.out.println(grammar.getProductions().get(leftProd) + leftProd);
                        System.out.println(grammar.getProductions() + "Hereeeeeeeeeeeeeeeeeeee" + leftProd + " " + prod);
                        boolean isNonProd = rmNonProdSymbols(leftProd, prod, prodList); //check for non-prod symbols
//                        boolean isNonProd = false;
                        if(!isNonProd){
                            prodFromStartS.add(leftProd);
                            verifiedProd.push(leftProd);
                        }
                    }
                }
            }
        }
        for (String symbol : grammar.getNonTerminalVariables()) { //remove inaccessible productions
            if (!prodFromStartS.contains(symbol)) {
                grammar.getNonTerminalVariables().remove(symbol);
                grammar.getProductions().remove(symbol);
            }
        }
    }

    private static boolean rmNonProdSymbols(String leftProd, String prod, ArrayList<String> prodList) {
        if (grammar.getProductions().get(leftProd) == null) { //symbol is non-productive because it's not present in production set
            System.out.println("symbol is non-productive" + grammar.getProductions());
            prodList.remove(prod);
            return true;
        }
        boolean cannotTerminate = true;  //production can't be terminated. Ex: C -> aC
        boolean hasTerminal = false; // we need it in the chase we have Ex: C -> aC | a
        boolean hasAnotherProd = false; // we need it to check the chase Ex: C -> aC | AB
        HashSet<String> anotherProd = new HashSet<>();

        for(String symbol : grammar.getProductions().get(leftProd)){
            System.out.println(grammar.getProductions() + "Heree22222" + leftProd + " " + symbol + cannotTerminate);

            if(symbol.length() == 1 && Character.isLowerCase(symbol.charAt(0))){
                cannotTerminate = false;
                hasTerminal = true;
            }
            for(int i = 0; i < symbol.length(); i++){
                char ch = symbol.charAt(i);
                if(!Character.toString(ch).equals(leftProd)){
                    hasAnotherProd = true;
                    anotherProd.add(Character.toString(ch));
                }
            }
        }
        if(!hasTerminal && hasAnotherProd){
            cannotTerminate = false;
        }
        if(cannotTerminate){
            prodList.remove(prod);
            grammar.getProductions().remove(leftProd);
            return true;
        }
        return false;
    }

    //used to work with a copy for the given grammar
    public static Grammar getCopyModGrammar(Grammar gr){
        grammar = new Grammar(gr);
        rmEmptyProd();
        rmUnitProd();
        rmInaccessibleProd();
        return grammar;
    }
    //used to work directly with the given grammar
    public static void modifyGrammar(Grammar gr){
        grammar = gr;
        rmEmptyProd();
        rmUnitProd();
//        rmInaccessibleProd();
    }
}
