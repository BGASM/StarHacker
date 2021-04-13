package starhacker.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomCampaignEntityPlugin;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.impl.campaign.CampaignObjective;
import com.fs.starfarer.api.impl.campaign.intel.misc.CommSnifferIntel;
import com.fs.starfarer.api.util.Pair;
import org.apache.log4j.Logger;
import org.lazywizard.lazylib.CollectionUtils;
import org.lazywizard.lazylib.MathUtils;
import starhacker.helper.CampaignHelper;

import java.util.*;

/**
 * As this mod expands, 'hack' functionality may move into their own classes as needed.
 */
public class HacksPlugin {
    public static Logger log = Global.getLogger(HacksPlugin.class);
    protected List<String> filters = Arrays.asList("comm_relay", "sensor_array", "nav_buoy");
    /*  protected SectorEntityToken entity;
        protected FactionAPI entityFaction;
        protected Collection<String> tags;
        protected List<SectorEntityToken> nearby;
        Removed these to see if having them instantiated this way was causing them to get lost. I also figured this
        class would not actually need to hold any of these as instance variables.
    */

    /**
     * Like in the other scripts, I initially was passing */
    public Pair<String, List<String>> runTrace(String token_id){
        log.info("Run Trace");
        SectorEntityToken entity = Global.getSector().getEntityById(token_id);
        FactionAPI entityFaction = entity.getFaction();
        Collection<String> tags = entity.getTags();
        tags.retainAll(filters);
        log.info(tags);
        List<SectorEntityToken> nearby = CampaignHelper.getAllEntitiesFromFactionHyper(entity, tags.iterator().next(), entityFaction);
        log.info(nearby);
        List<String> trace_data = new ArrayList<>();
        List<String> trace_id = new ArrayList<>();
        for (SectorEntityToken s : nearby) {
            float d = MathUtils.getDistance(entity.getLocationInHyperspace(), s.getLocationInHyperspace());
            HashMap<String, String> tmp = CampaignHelper.strEntity(s, d);
            String str = tmp.get("name") + " - " + tmp.get("system") + " - " + tmp.get("faction") + " - " + tmp.get("distance");
            trace_data.add(str);
            trace_id.add(s.getId());
        }
        log.info(trace_data);
        String trace = CollectionUtils.implode(trace_data, "\n");
        return new Pair<>(trace, trace_id);
    }

    public void uploadVirus(List<String> trace_id) {
        ArrayList<SectorEntityToken> token_list = new ArrayList<>();
        for (String s : trace_id){
            token_list.add(Global.getSector().getEntityById(s));
        }
        for (SectorEntityToken s : token_list) {
            CustomCampaignEntityPlugin plugin = s.getCustomPlugin();
            if (plugin instanceof CampaignObjective) {
                CampaignObjective o = (CampaignObjective) plugin;
                log.info("Hacking " + o);
                o.setHacked(true);
            }
        }
    }

    public void removeVirus(String token_id) {
        SectorEntityToken token = Global.getSector().getEntityById(token_id);
        CommSnifferIntel intel = CommSnifferIntel.getExistingSnifferIntelForRelay(token);
        if (intel != null) {
            intel.uninstall();
        } else {
            CustomCampaignEntityPlugin plugin = token.getCustomPlugin();
            if (plugin instanceof CampaignObjective) {
                CampaignObjective o = (CampaignObjective) plugin;
                o.setHacked(false);
            }
        }
    }
}
