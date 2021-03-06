package muzak.mycomp;

import muzak.UIUtils;
import muzakModel.DataModelObject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ViewModDelTools extends HBox
{
    private static final double DEF_TOOL_BUTTON_SIZE = 22.0;
    private static final double DEF_BUTTONS_SPACING = 5.0;
    
    private Button  ui_viewButton   = new Button();
    private Button  ui_modiButton   = new Button();
    private Button  ui_deleButton   = new Button();
    
    public ViewModDelTools(final ViewModDelObserver observer)
    {
        super(DEF_BUTTONS_SPACING);
        
        setupComponents();

        this.getChildren().addAll(ui_viewButton, ui_modiButton, ui_deleButton);
        
        ui_viewButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent arg0)
            {
                observer.handleViewRequest((DataModelObject)getUserData());
            }
        });
        
        ui_modiButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent arg0)
            {
                observer.handleModifyRequest((DataModelObject)getUserData());
            }
        });
        
        ui_deleButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent arg0)
            {
                observer.handleDeleteRequest((DataModelObject)getUserData());
            }
        });
    }
    
    private void setupComponents()
    {
        ui_viewButton.getStyleClass().addAll("tool-button");
        ui_viewButton.setGraphic(new ImageView(new Image("file:resources/icons/view16x16.png")));
        UIUtils.setFixedSize(ui_viewButton, DEF_TOOL_BUTTON_SIZE);
        
        ui_modiButton.getStyleClass().addAll("tool-button");
        ui_modiButton.setGraphic(new ImageView(new Image("file:resources/icons/tools16x16.png")));
        UIUtils.setFixedSize(ui_modiButton, DEF_TOOL_BUTTON_SIZE);
        
        ui_deleButton.getStyleClass().addAll("tool-button");
        ui_deleButton.setGraphic(new ImageView(new Image("file:resources/icons/delete16x16.png")));
        UIUtils.setFixedSize(ui_deleButton, DEF_TOOL_BUTTON_SIZE);
    }
}
