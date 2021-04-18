package starhacker.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomCampaignEntityPlugin;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.util.Pair;
import org.apache.log4j.Logger;
import org.lazywizard.lazylib.CollectionUtils;
import org.lazywizard.lazylib.MathUtils;
import starhacker.helper.CampaignHelper;

import starhacker.impl.campaign.SH_HackablePlugin;
import starhacker.impl.campaign.intel.misc.BackdoorIntel;

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
            if (plugin instanceof SH_HackablePlugin) {
                SH_HackablePlugin o = (SH_HackablePlugin) plugin;
                log.info("Hacking " + o);
                o.setBackdoor(true);
            }
        }
    }

    public static void removeVirus(SectorEntityToken token) {
        getBackdoorIntel(token);
    }

    public static void getBackdoorIntel(SectorEntityToken token) {
        BackdoorIntel intel = BackdoorIntel.getBackdoorIntelForRelay(token);
        if (intel != null) {
            intel.uninstall();
        } else {
            CustomCampaignEntityPlugin plugin = token.getCustomPlugin();
            if (plugin instanceof SH_HackablePlugin) {
                SH_HackablePlugin o = (SH_HackablePlugin) plugin;
                o.setBackdoor(false);
            }
        }
    }
}
