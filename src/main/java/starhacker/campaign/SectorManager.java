package starhacker.campaign;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BaseCampaignEventListener;
import org.apache.log4j.Logger;

public class SectorManager extends BaseCampaignEventListener implements EveryFrameScript {
    public SectorManager() {super(true);}
    public static Logger log = Global.getLogger(SectorManager.class);
    protected static final String MANAGER_MAP_KEY = "starhacker_sectorManager";


    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean runWhilePaused() {
        return false;
    }

    @Override
    public void advance(float v) {

    }
}
