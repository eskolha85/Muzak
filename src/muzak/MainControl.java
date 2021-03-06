
package muzak;

import discogs.DiscogsWorker;
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.TreeSet;

import muzak.mycomp.ViewModDelObserver;
import muzakModel.Artist;
import muzakModel.DataModelObject;
import muzakModel.MuzakDataModel;
import muzakModel.TrackInfoElement;
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
    private Stage mainWindow;
    private Muzak muzak;
    private DiscogsWorker m_discogs;
    
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
        ArtistViewDialog dialog = new ArtistViewDialog((Artist)dmo, m_config, this);
        
        Release rel = MuzakDataModel.createRelease();
        rel.setTitle("Blizzard Beasts");
        rel.setCatalogNumber("OPCD051");
        rel.setBarCode("8347658374");
        rel.setExtendedEdition(true);
        rel.setOriginalRelease(true);
        rel.setCurYear(1993);
        rel.setStyleKey("ST15");
        rel.setID(123123123L);
        TreeSet<Release> ts = new TreeSet<>();
        ts.add(rel);
        
        dialog.setReleases(ts);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(mainWindow);
        dialog.execute();
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
    public ArrayList<KeyValueElement> getReleasesByArtist(KeyValueElement artist)
    {
        ArrayList<KeyValueElement> releases = new ArrayList<>();
        
        for(Release rel : m_model.selectReleasesByArtist(Long.parseLong(artist.getKey())))
            releases.add(new KeyValueElement(rel.getIDString(), rel.getListString()));
        
        return releases;
    }
    
    @Override
    public ArrayList<KeyValueElement> getDiscogsResults()
    {
//        if(m_discogs == null)
//            return new ArrayList<KeyValueElement>();
//            
//        return m_discogs.getReleases();
        
        return DiscogsWorker.getReleases();
    }
    
    @Override
    public void createArtist(DialogCallback callback)
    {
        showArtistDialog(callback.getOwningStage());
        callback.update();
    }
    
    @Override
    public void discogsRequest(DialogCallback callback)
    {
        DiscogsWorker discogs = new DiscogsWorker();
        discogs.searchReleases(callback.getQueryTitle(), callback.getQueryCatNumber(), callback.getQueryBarcode());
        //System.out.println("MainControl / Discogs request");
        showDiscogsResultsDialog(callback, discogs);
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
            showTracksDialog();
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
    
    private void showDiscogsResultsDialog(DialogCallback owner, DiscogsWorker worker)
    {
        DiscogsResultsDialog dialog = new DiscogsResultsDialog(m_config, this);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(owner.getOwningStage());
        
        worker.setOnFinished(dialog);
        worker.start();
        
        if(dialog.execute())
        {
            String user = dialog.getUserSelection();
            
            if(!user.isEmpty())
            {
                DiscogsWorker dw = new DiscogsWorker();
                dw.setRequestMode();
                dw.requestRelease(user);
                dw.setOnFinished(owner);
                dw.start();
                
                System.out.println("VALINTA " + dialog.getUserSelection());
            }
        }
        else
        {
        }
        
        /* Interrupt Discogs search if still ongoing. */
        if(worker.getState() != State.TERMINATED)
            worker.interrupt();
    }
    
    private void showTracksDialog()
    {
        TracksDialog dialog = new TracksDialog(m_config, this);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(mainWindow);
        
        if(dialog.execute())
        {
            try
            {
                m_model.insertTracklist(new TreeSet<>(dialog.getTracklist()), dialog.getArtistID(), dialog.getReleaseID());
            }
            catch(IllegalArgumentException e)
            {
                e.printStackTrace();
            }
            catch(NotUniqueSignatureException e)
            {
                e.printStackTrace();
            }
            
            m_model.outputArtists();
            m_model.outputBTAssociations();
            m_model.outputTracks();
            m_model.outputATAssociations();
            m_model.outputReleases();
        }
        else
        {
            
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
            release.setComment(dialog.getComment());
            
            ArrayList<TrackInfoElement> tracklist = dialog.getTracklist();
            ArrayList<KeyValueCombo> artists = dialog.getArtists();
            
            // TODO: Better exception handling would be in order.
            try
            {
                m_model.insert(release);
                
                if(tracklist.isEmpty())
                {
                    for(KeyValueCombo kvc : artists)
                        m_model.associateArtistAndRelease(Long.parseLong(kvc.getKey()), release);
                }
                else
                {
                    long aid = (artists.isEmpty() ? 0L : Long.parseLong(artists.get(0).getKey()));
                    m_model.insertTracklist(new TreeSet<TrackInfoElement>(tracklist), aid, release.getID());
                }
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
    
    private void showArtistDialog(Stage owner)
    {
        ArtistDialog dialog = new ArtistDialog(m_config, this);
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
            
            // TODO: Better exception handling would be in order. 
            try
            {
                m_model.insert(artist);
                muzak.addContent(UIUtils.getListInfoElement(artist, this));
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
