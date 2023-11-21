import java.util.*;

public class symboltable {
    private static Map<String, String> symbolTable = new HashMap<>();
    public Map<String, String> getSymbolTable(){
        return symbolTable;
    }
    private static Set<String> symbolTableSymbols = new HashSet<>();
    private ArrayList<String> Addresses = new ArrayList<>();
    private int maxAddress = 0x0000;
    private int minAddress = 0xFFFF;
    private int numberOfNewSymbols =1;

    static {
        symbolTableSymbols.add("Earth");
        symbolTableSymbols.add("Venus");
        symbolTableSymbols.add("Uranus");
        symbolTableSymbols.add("Saturn");
        symbolTableSymbols.add("Neptune");
        symbolTableSymbols.add("Mars");
        symbolTableSymbols.add("Luna");
        symbolTableSymbols.add("Io");
        symbolTableSymbols.add("Ceres");
        symbolTableSymbols.add("Ares");
        symbolTableSymbols.add("Ymir");
        symbolTableSymbols.add("Rhea");
        symbolTableSymbols.add("Quaoar");
        symbolTableSymbols.add("Oberon");
        symbolTableSymbols.add("Kepler");
        symbolTableSymbols.add("Janssen");
        symbolTableSymbols.add("Hoth");
        symbolTableSymbols.add("Galileo");
        symbolTableSymbols.add("Draugr");
        symbolTableSymbols.add("Jabbah");
        symbolTableSymbols.add("Nunki");
        symbolTableSymbols.add("Rigel");
        symbolTableSymbols.add("Sirius");
        symbolTableSymbols.add("Dubhe");
    }
    public symboltable(){
        symbolTable.put("Earth","1000");
    }
    public String checkSYMTAB(String pc){
        System.out.println();
        for(String symbol : symbolTable.keySet()) {
            if(symbolTable.get(symbol) == pc)
                return symbol;
        }
        return " ";
    }
    public String getSymbol(String address){
        for (String symbol : symbolTableSymbols) // if symbol matches the address, return symbol
            if(symbolTable.containsKey(symbol) && symbolTable.get(symbol).equals(address))
                return symbol;
        for (String symbol : symbolTableSymbols) // if symbol doesn't match with the address, return symbol
            if (!symbolTable.containsKey(symbol))
                return symbol;
        while(true){ //Creating a new symbol to symbol table with its own address
            String newSymbol = "Sym" + numberOfNewSymbols;
            numberOfNewSymbols++;
            if (!symbolTable.containsKey(newSymbol)) {
                symbolTable.put(newSymbol, address);
                return newSymbol;
            }
        }
    }

    public void addSymbol(String address){
        int addressDecimal = Integer.parseInt(address,16);
        for (String symbol : symbolTable.keySet()) //checking if symbol is already in the table
            if (symbolTable.get(symbol).equals(address))
                return;
        symbolTable.put(getSymbol(address),address);
        Addresses.add(address);
        if(addressDecimal > maxAddress){
            maxAddress= Integer.parseInt(Integer.toHexString(addressDecimal).substring(0, 2));
        } else{
            minAddress = Integer.parseInt(Integer.toHexString(addressDecimal).substring(0, 2));
        }
    }

}
