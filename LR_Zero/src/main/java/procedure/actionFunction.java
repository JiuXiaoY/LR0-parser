package procedure;

public class actionFunction {
    private int startItem;//起始项目
    private Character ConversionCh;//匹配符号
    private String endItem;//对应动作

    public actionFunction(){}

    public actionFunction(int startItem , Character conversionCh , String endItem){
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

    public String getEndItem() {
        return endItem;
    }

    public void setEndItem(String endItem) {
        this.endItem = endItem;
    }

    @Override
    public String toString() {
        return "actionFunction{" +
                "startItem=" + startItem +
                ", ConversionCh=" + ConversionCh +
                ", endItem='" + endItem + '\'' +
                '}' + "\n";
    }
}
