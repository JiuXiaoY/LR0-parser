import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.MutableGraph;

public class test {
    public void draw(){
        MutableGraph graph = Factory.mutGraph("表格").setDirected(true)
                .graphAttrs().add(Rank.dir(Rank.RankDir.LEFT_TO_RIGHT));
        大学时期的相关编程作业以及成果
    }

}
