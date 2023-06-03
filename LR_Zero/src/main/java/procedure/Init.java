package procedure;
import org.example.Item;
import org.example.grammarParser;

import java.util.*;

public class Init {
    private final grammarParser ps;
    public Init(grammarParser ps){
        this.ps = ps;
    }
    int countVN = 1;
    int countVT = 1;

    private final Map<Integer,String> extendedGrammar = new HashMap<>();
    private Map<Integer, Character> VT = new HashMap<>();//终结符号集
    private Map<Integer, Character> VN = new HashMap<>();//非终结符号
    Set<Item> itemSet = new TreeSet<>();//项目规范集，使用TreeSet便于排序
    Integer groupIDOnly = 0;
    Set<Set<String>> hasContains = new HashSet<>();//标记

    private final Set<goToFunction> goToTable =new LinkedHashSet<>();

    private final Set<actionFunction> actionTable = new LinkedHashSet<>();

    //将原始文法转换为拓广文法
    public void convertToExtendedGrammar(){
        int count = 0;
        for(String str : ps.getPrimitiveGrammar()){
            String[] divide1 = str.split("->");
            String[] divide2 = divide1[1].split("\\|");
            if(count==0) {
                String addStart = ps.getGrammarStart() + "->" + divide1[0];
                extendedGrammar.put(count++,addStart);
            }
            for(String str1 : divide2){
                String stringBuilder = divide1[0] + "->" + str1;
                extendedGrammar.put(count++, stringBuilder);
            }
        }
        ps.setExtendedGrammar(extendedGrammar);
    }
    //通过原始产生式获取非终结符
    public Map<Integer, Character> getVN(){
        for(String temp : ps.getPrimitiveGrammar()){
            if(!VN.containsValue(temp.charAt(0))){
                VN.put(countVN++,temp.charAt(0));
            }
        }
        return VN;
    }

    //通过原始产生式获取终结符
    public Map<Integer, Character> getVT(){
        for(String str : ps.getPrimitiveGrammar()){
            String[] divide1 = str.split("->");
            String[] divide2 = divide1[1].split("\\|");
            for(String s : divide2){
                for(char ch : s.toCharArray()){
                    if(!(VN.containsValue(ch))&&!(VT.containsValue(ch))){
                        VT.put(countVT++,ch);
                    }
                }
            }
        }
        if(!VT.containsValue('#'))
            VT.put(countVT++,'#');
        return VT;
    }
    //获取所有项目
    public void allItems(){
        int count = 1;
        for(String str : ps.getExtendedGrammar().values()){
            char insertCh = '•';
            for(int i=3;i<=str.length();i++){
                StringBuilder stringBuilder = new StringBuilder(str);
                stringBuilder.insert(i,insertCh);
                ps.getAllItems().put(count++,stringBuilder.toString());
            }
        }
    }
    //项目规范族
    public Set<Item> constructItemSet(){
        Set<Character> operation = new HashSet<>();//文法符号，添加所有
        operation.addAll(ps.getVN().values());
        operation.addAll(ps.getVT().values());

        Set<String> strings = new HashSet<>(Collections.singleton(ps.getAllItems().get(1)));//从第一个产生式开始

        Item item = new Item(groupIDOnly++,"null",closure(strings));//创建item对象

        hasContains.add(item.getItemNormSet());//标记已经求过
        itemSet.add(item);//添加到项目集规范族里

        Stack<Set<String>> itemStack = new Stack<>();//创建stack
        itemStack.add(item.getItemNormSet());//初始只有第一个产生式的closure（I）闭包

        while(!itemStack.isEmpty()) {//不为空时循环
            Set<String> stringSet = itemStack.pop();//出栈
            Integer startState = getItemGroupID(stringSet);
            for (Character ch : operation) {//对于所有文法符号
                Set<String> nextSet = goTo(stringSet, ch);//求其closure（J）中的J
                if (!hasContains.contains(nextSet) && !nextSet.isEmpty()){//如果J已经求过，或者J为空（表示不含该分支）则跳过
                    hasContains.add(nextSet);//标记
                    Item temp = new Item(groupIDOnly++,"null",closure(nextSet));//创建新的项目集
                    itemSet.add(temp);
                    itemStack.push(closure(nextSet));//入栈
                }
                //待约项目
                if(ps.getVN().containsValue(ch) && getKeyFromValue(ch)!=null && !nextSet.isEmpty()){
                    goToFunction goToTemp = new goToFunction(startState,ch,groupIDOnly-1);
                    goToTable.add(goToTemp);
                }
                //移进项目
                if(ps.getVT().containsValue(ch) && getKeyFromValue(ch)!=null && !nextSet.isEmpty()){
                    actionFunction actionTemp = new actionFunction(startState,ch,"S" + (getItemGroupID(closure(nextSet))));
                    actionTable.add(actionTemp);
                }
            }
        }
        return itemSet;
    }

    //规约项目
    public void protocolItem (){
        for(String str : ps.getAllItems().values()){
            int pointIndex = str.indexOf('•');
            if(pointIndex == str.length()-1){
                StringBuilder stringBuilder = new StringBuilder(str);
                stringBuilder.deleteCharAt(str.length()-1);
                for(Character ch : ps.getVT().values()){
                    if(str.charAt(0) == ps.getGrammarStart()){
                        actionFunction action = new actionFunction(getItemGroupID
                                (new HashSet<>(Collections.singleton(str))),'#',"acc");
                        actionTable.add(action);
                        break;
                    }
                    actionFunction actionTemp = new actionFunction(getItemGroupID(
                            new HashSet<>(Collections.singleton(str))),ch,
                            "r" + (getKeyFromExternGra(stringBuilder.toString())));
                    actionTable.add(actionTemp);
                }
            }
        }
    }

    public Set<String> closure(Set<String> stringSet){//求某一项目集的closure闭包
        Set<String> resultSet = new HashSet<>(stringSet);

        Stack<String> itemSetStack = new Stack<>();
        itemSetStack.addAll(stringSet);

        while(!(itemSetStack.isEmpty())){
            String item = itemSetStack.pop();
            int pointIndex = item.indexOf('•');
            if(pointIndex<item.length()-1) {
                Character ch = item.charAt(pointIndex + 1);
                if (ps.getVN().containsValue(ch)) {//说明圆点位置后面为非终结符
                    for (String str : getVNItem(ch)) {
                        if (!resultSet.contains(str)) {
                            resultSet.add(str);
                            itemSetStack.push(str);//对于新项目，需要继续找与其等价的项目
                        }
                    }
                }
            }
        }
        return resultSet;
    }

    public Set<String> getVNItem(Character ch){//对于非终结符ch，求他的所有项目
        Set<String> strings = new HashSet<>();
        for(String temp : ps.getExtendedGrammar().values()){//遍历拓广文法
            if(temp.charAt(0) == ch){//找到产生式右部为ch的产生式
                StringBuilder stringBuilder = new StringBuilder(temp);
                stringBuilder.insert(3,'•');//插入point
                strings.add(stringBuilder.toString());
            }
        }
        return strings;
    }

    public Set<String> goTo(Set<String>strings , Character ch){//某一项目集，经过ch分支后到达的项目集合
        Set<String> nextSet = new HashSet<>();
        for(String str : strings){
            int pointIndex = str.indexOf('•');//寻找point的位置
            if(pointIndex<str.length()-1 && str.charAt(pointIndex+1)==ch){//不是规约项目
                StringBuilder stringBuilder = new StringBuilder(str);
                stringBuilder.deleteCharAt(pointIndex);//后移
                stringBuilder.insert(pointIndex+1,'•');
                nextSet.add(stringBuilder.toString());
            }
        }
        return nextSet;
    }

    public Integer getKeyFromValue(Character ch){
        for(Integer keyTemp : ps.getVN().keySet()){
            if(ps.getVN().get(keyTemp) == ch)
                return keyTemp;
        }
        for(Integer keyTemp : ps.getVT().keySet()){
            if(ps.getVT().get(keyTemp) == ch){
                return keyTemp;
            }
        }
        return null;
    }

    public Integer getItemGroupID(Set<String> stringSet){
        for(Item temp : itemSet){
            if(temp.getItemNormSet().equals(stringSet))
                return temp.getGroupID();
        }
        return -1;
    }

    public Integer getKeyFromExternGra(String str){
        for(Integer temp : ps.getExtendedGrammar().keySet()){
            if(ps.getExtendedGrammar().get(temp).equals(str)){
                return temp;
            }
        }
        return -1;
    }


    public void work(){
        convertToExtendedGrammar();
        VN = getVN();
        VT = getVT();
        ps.setVN(VN);
        ps.setVT(VT);
        allItems();
        ps.setItemSet(constructItemSet());
        protocolItem();
        ps.setGoToTable(goToTable);
        ps.setActionTable(actionTable);


//        System.out.println(ps.getPrimitiveGrammar());
//        System.out.println(ps.getExtendedGrammar());
//        System.out.println(ps.getVT());
//        System.out.println(ps.getVN());
//        System.out.println(ps.getAllItems());
//        System.out.println(goToTable);
//       System.out.println(actionTable);
//        System.out.println(ps.getItemSet());
    }
}
