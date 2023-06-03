package org.example;
import procedure.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        grammarParser ps = new grammarParser();//构造词法分析器

        OCRGetText ocrGetText = new OCRGetText();//获取文本
        String dataPath = "src/main/resources/photo/grammar0.png";//Grammar picture path
        ps.setPrimitiveGrammar(ocrGetText.getText(dataPath));//设置原始文法

        Init initData = new Init(ps);//构建初始对象
        initData.work();//设置相关数据

        Match match = new Match(ps);
        match.work();

        stateTransitionDiagram std = new stateTransitionDiagram(ps);

        std.transGraph();//生成状态转换图
    }
}