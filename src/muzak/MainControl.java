
package muzak;

import java.util.ArrayList;
import java.util.TreeSet;

import muzak.mycomp.ViewModDelObserver;
import muzakModel.Artist;
import muzakModel.DataModelObject;
import muzakModel.MuzakDataModel;
import muzakModel.MuzakDataModel.Order;
import muzakModel.MuzakDataModel.Tables;
import muzakModel.Release;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import muzakModel.NotUniqueSignatureException;

public class MainControl implements DialogObserver, ViewModDelObserver
{
    private MuzakDataModel m_model;
    private MuzakConfig m_config;
    /* Initialize 'default' locale. */
    //private Locale m_locale = new Locale("fi");
    private Stage mainWindow;
    private Muzak muzak;
    
    public MainControl()
    {
        super();
        m_model = new MuzakDataModel();
        m_config = new MuzakConfig();
    }
    
    public void setMainWindow(final Stage win)
    {
        mainWindow = win;
    }
    
    public void setMuzak(Muzak m)
    {
        muzak = m;
    }
    
    public Stage getMainWindow()
    {
        return mainWindow;
    }
    
    public Configurations getConfigurations()
    {
        return m_config;
    }
    
    public boolean changeToFinnish()
    {
        return m_config.changeLangToFI();
    }
    
    public boolean changeToEnglish()
    {
        return m_config.changeLangToEN();
    }
    
    public void quit()
    {
        System.out.println("Quitting...");
        Platform.exit();
    }
    
    @Override
    public void handleViewRequest(DataModelObject dmo)
    {
        System.out.println("View Request from ID: " + dmo.getID() + " of " + dmo.getClass().getSimpleName());
    }

    @Override
    public void handleModifyRequest(DataModelObject dmo)
    {
        System.out.println("Modify Request from ID: " + dmo.getID() + " of " + dmo.getClass().getSimpleName());
    }

    @Override
    public void handleDeleteRequest(DataModelObject dmo)
    {
        System.out.println("Delete Request from ID: " + dmo.getID() + " of " + dmo.getClass().getSimpleName());
    }
    
    @Override
    public TreeSet<DataModelObject> getArtists()
    {
        @SuppressWarnings("unchecked")
        TreeSet<DataModelObject> set = (TreeSet<DataModelObject>)m_model.selectAll(Tables.ARTISTS, Order.ALPHABETICAL, false);
        
        return set;
    }
    
    @Override
    public void createArtist(DialogCallback callback)
    {
        showArtistDialog(callback.getOwningStage());
        callback.update();
    }
    
    public void handleSearchAction(String searchString, String filter)
    {
        System.out.println("Search " + searchString + " from " + filter);
    }
    
    public void handleMenuAction(ActionEvent event)
    {
        if(!(event.getSource() instanceof MenuItem)) return;
        
        switch( ((MenuItem)event.getSource()).getId() )
        {
        case "ExitRequest":
            System.out.println(((MenuItem)event.getSource()).getId());
            quit();
            break;
        case "AddArtistRequest":
            System.out.println(((MenuItem)event.getSource()).getId());
            showArtistDialog(mainWindow);
            break;
        case "AddReleaseRequest":
            System.out.println(((MenuItem)event.getSource()).getId());
            showReleaseDialog();
            break;
        case "AddTracksRequest":
            System.out.println(((MenuItem)event.getSource()).getId());
            break;
        case "ModifyArtistRequest":
            System.out.println(((MenuItem)event.getSource()).getId());
            break;
        case "ModifyReleaseRequest":
            System.out.println(((MenuItem)event.getSource()).getId());
            break;
        case "ModifyTracksRequest":
            System.out.println(((MenuItem)event.getSource()).getId());
            break;
        case "DeleteArtistRequest":
            System.out.println(((MenuItem)event.getSource()).getId());
            break;
        case "DeleteReleaseRequest":
            System.out.println(((MenuItem)event.getSource()).getId());
            break;
        case "DeleteTracksRequest":
            System.out.println(((MenuItem)event.getSource()).getId());
            break;
        case "AboutRequest":
            System.out.println(((MenuItem)event.getSource()).getId());
            break;
        default:
            /* Shouldn't really happen... */
            break;
        }
    }
    
    public void handleButtonAction(ActionEvent event)
    {
        if(!(event.getSource() instanceof Node)) return;
        
        switch(((Node)event.getSource()).getId())
        {
            case "SearchRequest":
                System.out.println("Search requested from Main Window.");
                break;

            case "AddArtistRequest":
                System.out.println("Add Artist requested from Main Window.");
                break;

            case "AddReleaseRequest":
                System.out.println("Add Release requested from Main Window.");
                break;

            case "AddTracksRequest":
                System.out.println("Add Tracks requested from Main Window.");
                break;

            default:
            /* Shouldn't really happen... */
            break;
        } 
    }
    
    private void showReleaseDialog()
    {
        ReleaseDialog dialog = new ReleaseDialog(m_config, this);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(mainWindow);
        
        if(dialog.execute())
        {
            Release release = MuzakDataModel.createRelease();
            release.setTitle(dialog.getReleaseTitle());
            release.setTechTitle(dialog.getTechTitle());
            release.setAltTitle(dialog.getAlternateTitle());
            release.setCatalogNumber(dialog.getCatalogNumber());
            release.setBarCode(dialog.getBarcode());
            release.addType(dialog.getTypeKeys());
            release.addMedia(dialog.getMediaKeys());
            release.setOriginalRelease(dialog.getIsOriginal());
            release.setExtendedEdition(dialog.getIsExtended());
            release.setCurYear(dialog.getCurrentYear());
            release.setOrgYear(dialog.getOriginalYear());
            release.setDiscs(dialog.getDiscCount());
            release.setStyleKey(dialog.getStyleKey());
            release.setRating(dialog.getRating());
            release.setComment(dialog.getComment());
            
            // TODO: Ehk�p� jokin hivenen nerokkaampi poikkeustenk�sittely lienee paikallaan. Vai h�h?
            try
            {
                m_model.insert(release);
            }
            catch(IllegalArgumentException e)
            {
                e.printStackTrace();
            }
            catch(NotUniqueSignatureException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            
        }
        
        dialog.output();
    }
    
    private void showArtistDialog(Stage owner)
    {
        ArtistDialog dialog = new ArtistDialog(m_config);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(owner);
        
        if(dialog.execute())
        {
            Artist artist = MuzakDataModel.createArtist();
            artist.setType(dialog.getType());
            artist.setName(dialog.getName());
            artist.setTechName(dialog.getTechName());
            artist.addAlias(dialog.getAliases());
            artist.setCountryCode(dialog.getOriginCode());
            artist.setFounded(dialog.getFounded());
            artist.setComment(dialog.getComment());
            
            // TODO: Ehk�p� jokin hivenen nerokkaampi poikkeustenk�sittely lienee paikallaan. Vai h�h?
            try
            {
                m_model.insert(artist);
            }
            catch(IllegalArgumentException e)
            {
                e.printStackTrace();
            }
            catch(NotUniqueSignatureException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            
        }
    }
}
