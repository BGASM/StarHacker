package starhacker.impl.campaign;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.combat.MutableStat;
import com.fs.starfarer.api.impl.campaign.BaseCampaignObjectivePlugin;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import starhacker.plugins.BackdoorPlugin;

import java.util.Iterator;

public class SH_NavBuoyEntityPlugin extends SH_HackablePlugin implements BackdoorPlugin {
    public static float NAV_BONUS = 3.0F;
    public static float NAV_BONUS_MAKESHIFT = 2.0F;

    public SH_NavBuoyEntityPlugin() {
    }

    public void init(SectorEntityToken entity, Object pluginParams) {super.init(entity, pluginParams);}

    public void advance(float amount) {
        if (this.entity.getContainingLocation() != null && !this.entity.isInHyperspace()) {

        }
    }

    public void printEffect(TooltipMakerAPI text, float pad) {
        int bonus = (int)NAV_BONUS;
        if (this.isMakeshift()) {
            bonus = (int)NAV_BONUS_MAKESHIFT;
        }

        text.addPara("      %s burn level for all same-faction fleets in system", pad, Misc.getHighlightColor(), new String[]{"+" + bonus});
    }

    public void printNonFunctionalAndHackDescription(TextPanelAPI text) {
        if (this.entity.getMemoryWithoutUpdate().getBoolean("$objectiveNonFunctional")) {
            text.addPara("This one, however, does not appear to be transmitting a navigation telemetry broadcast. The cause of its lack of function is unknown.");
        }

        if (this.isHacked()) {
            text.addPara("You have a hack running on this sensor array.");
        }

    }

    public void addHackStatusToTooltip(TooltipMakerAPI text, float pad) {
        int bonus = (int)NAV_BONUS;
        if (this.isMakeshift()) {
            bonus = (int)NAV_BONUS_MAKESHIFT;
        }

        text.addPara("%s burn level for in-system fleets", pad, Misc.getHighlightColor(), new String[]{"+" + bonus});
        super.addHackStatusToTooltip(text, pad);
    }

    @Override
    public void vanillaBonus(){
        String id = this.getModId();
        Iterator var4 = this.entity.getContainingLocation().getFleets().iterator();

        while(true) {
            CampaignFleetAPI fleet;
            String desc;
            float bonus;
            MutableStat.StatMod curr;
            do {
                do {
                    do {
                        if (!var4.hasNext()) {
                            return;
                        }

                        fleet = (CampaignFleetAPI)var4.next();
                    } while(fleet.isInHyperspaceTransition());
                } while(fleet.getFaction() != this.entity.getFaction() && (!this.isHacked() || !fleet.getFaction().isPlayerFaction()));

                desc = "Nav buoy";
                bonus = NAV_BONUS;
                if (this.isMakeshift()) {
                    desc = "Makeshift nav buoy";
                    bonus = NAV_BONUS_MAKESHIFT;
                }

                curr = fleet.getStats().getFleetwideMaxBurnMod().getFlatBonus(id);
            } while(curr != null && !(curr.value <= bonus));

            fleet.getStats().addTemporaryModFlat(0.1F, id, desc, bonus, fleet.getStats().getFleetwideMaxBurnMod());
        }
    }

    protected String getModId() {
        return "sh_nav_buoy";
    }
}