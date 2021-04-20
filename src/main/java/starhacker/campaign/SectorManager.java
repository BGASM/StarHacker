package starhacker.campaign;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BaseCampaignEventListener;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin;
import com.fs.starfarer.api.util.Pair;
import org.apache.log4j.Logger;

import java.util.*;

public class SectorManager extends BaseCampaignEventListener implements EveryFrameScript {

    public static Logger log = Global.getLogger(SectorManager.class);
    protected static final String MANAGER_MAP_KEY = "starhacker_sectorManager";
    protected transient LinkedHashSet<Pair<SectorEntityToken, IntelInfoPlugin>> intelRX = new LinkedHashSet<Pair<SectorEntityToken, IntelInfoPlugin>>();
    public SectorManager(){super(true);}

    public void addIntel(Pair<SectorEntityToken, IntelInfoPlugin> intel) {
        if (intelRX == null)
            intelRX = new LinkedHashSet<Pair<SectorEntityToken, IntelInfoPlugin>>();
        Object info = ((BaseIntelPlugin)intel.two).getListInfoParam();
        Collection<String> mem = ((BaseIntelPlugin)intel.two).getPostingLocation().getMemoryWithoutUpdate().getKeys();
        log.info(info);
        log.info(mem);
        intelRX.add(intel);
        log.info(intelRX);
    }

    public LinkedHashSet<Pair<SectorEntityToken, IntelInfoPlugin>> getIntelQueue(){
        return intelRX;
    }

    public static SectorManager create(){
        SectorManager manager = getManager();
        if (manager != null)
            return manager;
        Map<String, Object> data = Global.getSector().getPersistentData();
        manager = new SectorManager();
        data.put(MANAGER_MAP_KEY, manager);
        return manager;
    }

    public static SectorManager getManager()
    {
        Map<String, Object> data = Global.getSector().getPersistentData();
        return (SectorManager)data.get(MANAGER_MAP_KEY);
    }

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
