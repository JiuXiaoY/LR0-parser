package org.example;

import procedure.actionFunction;
import procedure.goToFunction;

import java.util.*;

public class grammarParser {
    private Set<String> primitiveGrammar = new HashSet<>();//原始文法
    private Map<Integer,String> extendedGrammar = new HashMap<>();//拓广文法
    private Map<Integer, Character> VT = new HashMap<>();//终结符号集
    private Map<Integer, Character> VN = new HashMap<>();//非终结符号集
    private Map<Integer,String> allItems = new HashMap<>();//所有项目
    private Set<Item> itemSet = new HashSet<>();//项目规范集
    private Character grammarStart = 'S';

    private Set<goToFunction> goToTable =new LinkedHashSet<>();
    private Set<actionFunction> actionTable = new LinkedHashSet<>();

    public Set<String> getPrimitiveGrammar() {
        return primitiveGrammar;
    }

    public void setPrimitiveGrammar(Set<String> primitiveGrammar) {
        this.primitiveGrammar = primitiveGrammar;
    }

    public Map<Integer, String> getExtendedGrammar() {
        return extendedGrammar;
    }

    public void setExtendedGrammar(Map<Integer, String> extendedGrammar) {
        this.extendedGrammar = extendedGrammar;
    }

    public Map<Integer, Character> getVT() {
        return VT;
    }

    public void setVT(Map<Integer, Character> VT) {
        this.VT = VT;
    }

    public Map<Integer, Character> getVN() {
        return VN;
    }

    public void setVN(Map<Integer, Character> VN) {
        this.VN = VN;
    }

    public Character getGrammarStart() {
        return grammarStart;
    }

    public void setGrammarStart(Character grammarStart) {
        this.grammarStart = grammarStart;
    }

    public Map<Integer, String> getAllItems() {
        return allItems;
    }

    public void setAllItems(Map<Integer, String> allItems) {
        this.allItems = allItems;
    }

    public Set<Item> getItemSet() {
        return itemSet;
    }

    public void setItemSet(Set<Item> itemSet) {
        this.itemSet = itemSet;
    }

    public Set<goToFunction> getGoToTable() {
        return goToTable;
    }

    public void setGoToTable(Set<goToFunction> goToTable) {
        this.goToTable = goToTable;
    }

    public Set<actionFunction> getActionTable() {
        return actionTable;
    }

    public void setActionTable(Set<actionFunction> actionTable) {
        this.actionTable = actionTable;
    }
}
