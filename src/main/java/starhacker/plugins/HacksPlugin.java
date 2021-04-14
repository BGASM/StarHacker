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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * As this mod expands, 'hack' functionality may move into their own classes as needed.
 */
public class HacksPlugin {
    public static Logger log = Global.getLogger(HacksPlugin.class);


    public static Pair<String, Collection<SectorEntityToken>> runTrace(SectorEntityToken token, String tag){
        log.info("Run Trace");
        FactionAPI entityFaction = token.getFaction();
        Collection<SectorEntityToken> nearby = CampaignHelper.getAllEntitiesFromFactionHyper(token, tag, entityFaction);
        Collection<String> trace_data = new ArrayList<>();
        for (SectorEntityToken s : nearby) {
            float d = MathUtils.getDistance(token.getLocationInHyperspace(), s.getLocationInHyperspace());
            HashMap<String, String> tmp = CampaignHelper.strEntity(s, d);
            String str = tmp.get("name") + " - " + tmp.get("system") + " - " + tmp.get("faction") + " - " + tmp.get("distance");
            trace_data.add(str);
        }
        log.info(trace_data);
        String trace = CollectionUtils.implode(trace_data, "\n");
        return new Pair<>(trace, nearby);
    }

    public static void uploadVirus(Collection<SectorEntityToken> tokens) {
        for (SectorEntityToken s : tokens) {
            CustomCampaignEntityPlugin plugin = s.getCustomPlugin();
            if (plugin instanceof CampaignObjective) {
                CampaignObjective o = (CampaignObjective) plugin;
                log.info("Hacking " + o);
                o.setHacked(true);
            }
        }
    }

    public static void removeVirus(SectorEntityToken token) {
        getCommSnifferIntel(token);
    }

    public static void getCommSnifferIntel(SectorEntityToken token) {
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
