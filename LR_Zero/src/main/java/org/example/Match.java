package org.example;

import org.apache.commons.lang3.StringUtils;
import procedure.actionFunction;
import procedure.goToFunction;

import java.util.Set;
import java.util.Stack;

public class Match {
    private final grammarParser ps;
    public Match(grammarParser ps){
        this.ps = ps;
    }
    String dataPath = "src/main/resources/photo/InputString1.png";
    Set<String> inputString = new OCRGetText().getText(dataPath);
    String analyticalString = inputString.iterator().next() + "#";

    private int stepSequenceNumber = 0;//步骤序号
    private final Stack<Integer> status = new Stack<>();//状态栈
    private final Stack<Character> operation = new Stack<>();//符号栈

    public void analyticalProcess (){
        status.push(0);//初始状态
        operation.push('#');
        char[] analyticalArray = analyticalString.toCharArray();
        int analyticalArrayIndex = 0;
        System.out.printf("%-14s%-13s%-13s%-15s%-15s","步骤","状态栈","符号栈","输入串","动作说明");
        System.out.println();
        while(true){
            System.out.printf("%-15d",stepSequenceNumber++);
            System.out.printf("%-15s",StringUtils.join(status,""));
            System.out.printf("%-15s",StringUtils.join(operation,""));
            System.out.printf("%-15s",StringUtils.substring(String.valueOf(analyticalArray),analyticalArrayIndex));
            String invert = getInvert(status.peek(),analyticalArray[analyticalArrayIndex]);
            if(invert.equals("acc")){
                System.out.println("Analysis success");
                break;
            }
            else if(invert.equals("error")){
                System.out.println("fail !");
                break;
            }
            else if(invert.charAt(0) == 'S'){//移进项目
                System.out.println("ACTION[" + status.peek()+ ","
                        + analyticalArray[analyticalArrayIndex] + "] = " + invert);
                status.push(Integer.parseInt(String.valueOf(invert.charAt(1))));
                operation.push(analyticalArray[analyticalArrayIndex++]);
            }else{
                String protocolString = ps.getExtendedGrammar().get(Character.getNumericValue(invert.charAt(1)));
                String[] divide = protocolString.split("->");
                for(int i=0;i<divide[1].length();i++){//出栈
                    status.pop();
                    operation.pop();
                }
                Integer record = status.peek();
                operation.push(divide[0].charAt(0));
                String statusNum = getInvert(status.peek(),divide[0].charAt(0));
                status.push(Integer.parseInt(statusNum));
                System.out.println(invert + ":用" + protocolString
                        + "进行规约且GOTO(" + record + "," + operation.peek() + ") = " + statusNum);
            }
        }
    }

    public String getInvert(Integer integer , Character ch){
        for(actionFunction temp : ps.getActionTable()){//从action表中查找
            if(temp.getStartItem() == integer && temp.getConversionCh().equals(ch)){
                return temp.getEndItem();
            }
        }
        for(goToFunction temp : ps.getGoToTable()){
            if(temp.getStartItem() == integer && temp.getConversionCh().equals(ch)){
                return String.valueOf(temp.getEndItem());
            }
        }
        return "error";
    }

    public void work(){
        analyticalProcess();
    }

}
