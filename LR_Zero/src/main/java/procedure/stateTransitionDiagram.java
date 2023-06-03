package procedure;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import org.example.Item;
import org.example.grammarParser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class stateTransitionDiagram {
    private final grammarParser ps;
    public stateTransitionDiagram(grammarParser ps){
        this.ps = ps;
    }
    // 用于跟踪已添加的节点及其对应的 MutableNode 对象
    Map<String, MutableNode> addedNodes = new HashMap<>();

    public void transGraph() throws IOException {
        // 创建图
        MutableGraph graph = Factory.mutGraph("状态转换图").setDirected(true)
                .graphAttrs().add(Rank.dir(Rank.RankDir.LEFT_TO_RIGHT));


        //goTo集合，即对于非终结符而言
        for (goToFunction temp1 : ps.getGoToTable()) {
            String startNodeName = String.valueOf(temp1.getStartItem());
            String endNodeName = String.valueOf(temp1.getEndItem());
            String ConversionCh = temp1.getConversionCh().toString();

            // 获取起始节点和终止节点对应的 MutableNode 对象，如果不存在则创建新的节点
            generatingPoint(graph,startNodeName,endNodeName,ConversionCh);
        }

        //action集合，即对于非终结符而言
        for(actionFunction temp1 : ps.getActionTable()){
            // 获取起始节点和终止节点对应的 MutableNode 对象，如果不存在则创建新的节点
            if(temp1.getEndItem().charAt(0) == 'S'){
                String startNodeName = String.valueOf(temp1.getStartItem());
                String endNodeName = String.valueOf(temp1.getEndItem().charAt(1));
                String ConversionCh = temp1.getConversionCh().toString();

                generatingPoint(graph,startNodeName,endNodeName,ConversionCh);
            }
        }

        // 生成DOT文件
        File dotFile = new File("D:\\idea\\graphviz\\temp", "state_transition_graph.dot");
        Graphviz.fromGraph(graph).render(Format.DOT).toFile(dotFile);

        // 使用Graphviz工具将DOT文件转换为图像
        File outputFile = new File("D:\\idea\\graphviz\\temp", "state_transition_graph.png");
        Graphviz.fromGraph(graph).render(Format.PNG).toFile(outputFile);

        System.out.println("State transition graph generated successfully.");
    }

    private void generatingPoint(MutableGraph graph ,String startNodeName,String endNodeName, String ConversionCh ){
        MutableNode temp2 = addedNodes.computeIfAbsent(startNodeName, k -> Factory.mutNode
                (("I"+ startNodeName +":\n" + getItem(Integer.valueOf(startNodeName)))
                        .replace(",","\n")).add(Shape.RECTANGLE));
        MutableNode temp3 = addedNodes.computeIfAbsent(endNodeName, k -> Factory.mutNode
                (("I"+ endNodeName +":\n"+ getItem(Integer.valueOf(endNodeName)))
                        .replace(",","\n")).add(Shape.RECTANGLE));

        graph.add(temp2);
        graph.add(temp3);
        temp2.addLink(Factory.to(temp3).with(Label.of(ConversionCh)));
    }

    private Set<String> getItem(Integer integer){
        for(Item item : ps.getItemSet()){
            if(integer.equals(item.getGroupID()))
                return item.getItemNormSet();
        }
        return null;
    }


}

/*
MutableGraph graph = Factory.mutGraph("状态转换图").setDirected(true);

    // 用于跟踪已添加的节点及其对应的 MutableNode 对象
    Map<String, MutableNode> addedNodes = new HashMap<>();

    for (goToFunction temp1 : ps.getGoToTable()) {
        String startNodeName = String.valueOf(temp1.getStartItem());
        String endNodeName = String.valueOf(temp1.getEndItem());

        // 获取起始节点和终止节点对应的 MutableNode 对象，如果不存在则创建新的节点
        MutableNode temp2 = addedNodes.computeIfAbsent(startNodeName, k -> Factory.mutNode(k).add(Shape.CIRCLE));
        MutableNode temp3 = addedNodes.computeIfAbsent(endNodeName, k -> Factory.mutNode(k).add(Shape.CIRCLE));

        graph.add(temp2);
        graph.add(temp3);
        temp2.addLink(Factory.to(temp3).with(Label.of(temp1.getConversionCh().toString())));

        System.out.println(temp1);
    }

    // 生成DOT文件
    File dotFile = new File("D:\\idea\\graphviz\\temp", "state_transition_graph.dot");
    Graphviz.fromGraph(graph).render(Format.DOT).toFile(dotFile);

    // 使用Graphviz工具将DOT文件转换为图像
    File outputFile = new File("D:\\idea\\graphviz\\temp", "state_transition_graph.png");
    Graphviz.fromGraph(graph).render(Format.PNG).toFile(outputFile);

    System.out.println("State transition graph generated successfully.");
*/
