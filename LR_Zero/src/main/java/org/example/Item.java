package org.example;

import java.util.HashSet;
import java.util.Set;

public class Item implements Comparable<Item> {
    private Integer groupID;//唯一ID
    private String itemSymbol;//设置一个标志
    private Set<String> itemNormSet = new HashSet<>();//包含的项目

    public Item(){}
    public Item(Integer groupID,String itemSymbol,Set<String> itemNormSet){
        this.groupID=groupID;
        this.itemSymbol=itemSymbol;
        this.itemNormSet=itemNormSet;
    }

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public String getItemSymbol() {
        return itemSymbol;
    }

    public void setItemSymbol(String itemSymbol) {
        this.itemSymbol = itemSymbol;
    }

    public Set<String> getItemNormSet() {
        return itemNormSet;
    }

    public void setItemNormSet(Set<String> itemNormSet) {
        this.itemNormSet = itemNormSet;
    }

    @Override
    public String toString() {
        return "Item{" +
                "groupID=" + groupID +
                ", itemSymbol='" + itemSymbol + '\'' +
                ", itemSet=" + itemNormSet +
                '}' + "\n";
    }

    @Override
    public int compareTo(Item other) {
        return this.groupID.compareTo(other.groupID);
    }
}
