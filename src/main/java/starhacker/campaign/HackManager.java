package starhacker.campaign;


import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BaseCampaignEventListener;
import org.apache.log4j.Logger;

import java.util.Map;

public class HackManager extends BaseCampaignEventListener implements EveryFrameScript {
    public HackManager()  {
        super(true);
    }

    public static Logger log = Global.getLogger(HackManager.class);

    public static final String PERSISTENT_KEY = "sh_hackManager";

    public static HackManager create(){
        Map<String, Object> data = Global.getSector().getPersistentData();
        HackManager manager = (HackManager)data.get(PERSISTENT_KEY);
        if (manager != null)
            return manager;
        manager = new HackManager();
        data.put(PERSISTENT_KEY, manager);
        return manager;
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
