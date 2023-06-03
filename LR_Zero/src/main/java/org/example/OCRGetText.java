package org.example;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class OCRGetText {
    public Set<String> getText(String dataPath) {
        Tesseract tesseract = new Tesseract();
        Set<String> text = new LinkedHashSet<>();//保持顺序
        // 设置OCR引擎的数据文件路径（需要提前下载训练数据）
        tesseract.setDatapath("D:/OCR/Tessdata"); // 替换为Tesseract训练数据的路径
        try {
            // 识别图片并获取文本内容
            String result = tesseract.doOCR(new File(dataPath));//识别图片的内容
            String[] strings = result.split("\n");//按行分开
            text.addAll(List.of(strings));
        } catch (TesseractException e) {
            System.err.println("识别出错：" + e.getMessage());
        }
        System.out.println(text);
        return text;
    }
}
