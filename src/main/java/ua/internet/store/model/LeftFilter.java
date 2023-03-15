package ua.internet.store.model;
import java.util.ArrayList;
public class LeftFilter {
    private ArrayList<String> colorList;
    private ArrayList<String> typeList;
    public LeftFilter(){}

    public LeftFilter(ArrayList<String> colorList, ArrayList<String> typeList) {
        this.colorList = colorList;
        this.typeList = typeList;
    }

    public ArrayList<String> getColorList() {
        return colorList;
    }

    public void setColorList(ArrayList<String> colorList) {
        this.colorList = colorList;
    }

    public ArrayList<String> getTypeList() {
        return typeList;
    }

    public void setTypeList(ArrayList<String> typeList) {
        this.typeList = typeList;
    }
}
