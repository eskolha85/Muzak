package muzak;

import java.util.Locale;
import java.util.ResourceBundle;

public interface Configurations
{
    public enum Resources
    {
        MAIN_WINDOW,
        ABSTRACT_PHASED_DIALOG,
        ARTIST_DIALOG,
        RELEASE_DIALOG,
        TRACKS_DIALOG,
        DISCOGS_RESULTS_DIALOG,
        LIST_OF_COUNTRIES,
        LIST_OF_STYLES,
        LIST_OF_RELEASE_TYPES,
        LIST_OF_RELEASE_MEDIA,
        LIST_OF_RATINGS;
    }
    
    /* Starting value for year of foundation list. */
    public int getFoundedStartValue();
    /* Ending value for year of foundation list. */
    public int getFoundedEndValue();
    
    public int getReleasedStartValue();
    public int getReleasedEndValue();
    
    public int getMinRatingValue();
    public int getMaxRatingValue();
    
    public ResourceBundle getResources(Resources resource);
    
    public String mapCodeToCountry(String code);
    public String mapKeyToStyle(String code);
    
    public boolean changeLangToEN();
    public boolean changeLangToFI();
    
    public Locale getCurrentLocale();
}
