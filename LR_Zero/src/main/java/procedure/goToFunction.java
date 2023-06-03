package procedure;

public class goToFunction {
    private int startItem;//起始项目
    private Character ConversionCh;//状态
    private int endItem;//对应产生式编号

    public goToFunction(int startItem , Character conversionCh , int endItem){
        this.startItem = startItem;
        this.ConversionCh = conversionCh;
        this.endItem = endItem;
    }


    public int getStartItem() {
        return startItem;
    }

    public void setStartItem(int startItem) {
        this.startItem = startItem;
    }

    public Character getConversionCh() {
        return ConversionCh;
    }

    public void setConversionCh(Character conversionCh) {
        ConversionCh = conversionCh;
    }

    public int getEndItem() {
        return endItem;
    }

    public void setEndItem(int endItem) {
        this.endItem = endItem;
    }

    @Override
    public String toString() {
        return "goToFunction{" +
                "startItem=" + startItem +
                ", ConversionCh=" + ConversionCh +
                ", endItem=" + endItem +
                '}' + "\n";
    }
}
