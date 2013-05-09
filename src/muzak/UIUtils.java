package muzak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import muzak.mycomp.ViewModDelTools;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UIUtils
{
    public static String trimArticles(String str)
    {
        String trimmed = "";
        
        String[] bits = str.split(" ");
        
        if(bits[0].equalsIgnoreCase("the"))
            trimmed = str.substring(4) + ", the";
        else if(bits[0].equalsIgnoreCase("an"))
            trimmed = str.substring(2) + ", an";
        else if(bits[0].equalsIgnoreCase("a"))
            trimmed = str.substring(1) + ", a";
        else
            trimmed = str;
        
        return trimmed;
    }
    
    public static Pane getInfoElement(String title, String subTitle)
    {
        Text mTitle = new Text(title);
        mTitle.getStyleClass().addAll("main-title");
        
        Text sTitle = new Text(subTitle);
        sTitle.getStyleClass().addAll("subtitle");
        
        VBox box = vLayout(5.0, mTitle, sTitle, hLayoutRight(new ViewModDelTools()));
        box.getStyleClass().addAll("glass-pane", "simple-display-entry");
        
        box.setUserData("iidee");
        
        return box;
    }
    
    public static void setFixedSize(Control component, double value)
    {
        component.setMinSize(value, value);
        component.setMaxSize(component.getMinWidth(), component.getMinHeight());
    }
    
    public static void populate(ComboBox<KeyValueCombo> cbox, ResourceBundle res)
    {
        ArrayList<KeyValueCombo> values = new ArrayList<>();
        for(String key : res.keySet())
            values.add(new KeyValueCombo(key, res.getString(key)));
        
        Collections.sort(values);
        
        cbox.setItems(FXCollections.observableArrayList(values));
    }
    
    public static void populate(ComboBox<String> cbox, int begin, int end)
    {
        ArrayList<String> list = new ArrayList<>();
        for(int i = end; i >= begin; --i)
            list.add(Integer.toString(i));
        
        cbox.setItems(FXCollections.observableList(list));
    }
    
    public static Region getHStretcher()
    {
        Region stretcher = new Region();
        HBox.setHgrow(stretcher, Priority.ALWAYS);
        
        return stretcher;
    }
    
    public static HBox hLayout(double spacing, Node... nodes)
    {
        HBox box = new HBox(spacing);
        box.getChildren().addAll(nodes);
        
        return box;
    }
    
    public static HBox hLayoutRight(Node node)
    {
        return hLayout(0, getHStretcher(), node);
    }
    
    public static HBox hLayoutLeft(Node node)
    {
        return hLayout(0, node, getHStretcher());
    }
    
    public static HBox hLayoutCentered(Node node)
    {
        return hLayout(0, UIUtils.getHStretcher(), node, UIUtils.getHStretcher());
    }
    
    public static VBox vLayout(double spacing, Node... nodes)
    {
        VBox box = new VBox(spacing);
        box.getChildren().addAll(nodes);
        
        return box;
    }
    
    public static ColumnConstraints getColumnConstraint(double relativeWidth)
    {
        ColumnConstraints c = new ColumnConstraints();
        c.setPercentWidth(relativeWidth);
        
        return c;
    }
    
    public static ArrayList<ColumnConstraints> getColumnConstraints(int cnt)
    {
        double w = 100.0 / cnt;
        ColumnConstraints c;
        ArrayList<ColumnConstraints> constraints = new ArrayList<>();
        for(int i = 0; i < cnt; ++i)
        {
            c = new ColumnConstraints();
            c.setPercentWidth(w);
            
            constraints.add(c);
        }
        
        return constraints;
    }
}
