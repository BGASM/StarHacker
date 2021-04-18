package starhacker.impl.campaign;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.CommRelayEntityPlugin;
import com.fs.starfarer.api.impl.campaign.econ.CommRelayCondition;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.apache.log4j.Logger;
import starhacker.impl.campaign.intel.misc.CommRelayBackdoorIntel;
import starhacker.plugins.BackdoorPlugin;

import java.util.Iterator;

public class SH_CommRelayEntityPlugin extends SH_HackablePlugin implements BackdoorPlugin {
    public static Logger log = Global.getLogger(SH_CommRelayEntityPlugin.class);
    public SH_CommRelayEntityPlugin() {
    }

    public void init(SectorEntityToken entity, Object pluginParams) {super.init(entity, pluginParams);}

    public void advance(float amount) {
        if (this.entity.getContainingLocation() != null && !this.entity.isInHyperspace()) {
            if (!this.entity.getMemoryWithoutUpdate().getBoolean("$objectiveNonFunctional")) {
                vanillaBonus();
                this.checkBackdoor();
                this.checkIntelFromBackdoor();
            }
        }
    }

    public void printNonFunctionalAndHackDescription(TextPanelAPI text) {
        if (this.entity.getMemoryWithoutUpdate().getBoolean("$objectiveNonFunctional")) {
            text.addPara("This one, however, is not connected to the Sector-wide network and is not emitting the hyperwave radiation typically indicative of relay operation. The cause of its lack of function is unknown.");
        }

        if (this.isHacked()) {
            text.addPara("You have a comm sniffer running on this relay.");
        }

    }

    public void printEffect(TooltipMakerAPI text, float pad) {
        int bonus = Math.abs(Math.round(CommRelayCondition.COMM_RELAY_BONUS));
        if (this.isMakeshift()) {
            bonus = Math.abs(Math.round(CommRelayCondition.MAKESHIFT_COMM_RELAY_BONUS));
        }

        text.addPara("      %s stability for same-faction colonies in system", pad, Misc.getHighlightColor(), new String[]{"+" + bonus});
    }

    public void addHackStatusToTooltip(TooltipMakerAPI text, float pad) {
        int bonus = Math.abs(Math.round(CommRelayCondition.COMM_RELAY_BONUS));
        if (this.isMakeshift()) {
            bonus = Math.abs(Math.round(CommRelayCondition.MAKESHIFT_COMM_RELAY_BONUS));
        }

        if (this.isHacked()) {
            text.addPara("%s stability for in-system colonies", pad, Misc.getHighlightColor(), new String[]{"+" + bonus});
            text.addPara("Comm sniffer installed", Misc.getTextColor(), pad);
        } else {
            text.addPara("%s stability for same-faction colonies in-system", pad, Misc.getHighlightColor(), new String[]{"+" + bonus});
        }

    }

    @Override
    public void vanillaBonus() {
        Iterator var3 = Misc.getMarketsInLocation(this.entity.getContainingLocation()).iterator();

        while(var3.hasNext()) {
            MarketAPI market = (MarketAPI)var3.next();
            CommRelayCondition mc = CommRelayCondition.get(market);
            if (mc == null) {
                market.addCondition("comm_relay");
                mc = CommRelayCondition.get(market);
            }

            if (mc != null) {
                mc.getRelays().add(this.entity);
            }
        }
    }

    @Override
    public void checkIntelFromBackdoor(){
        if (!hasBackdoor()) return;
        for (IntelInfoPlugin intel : Global.getSector().getIntelManager().getCommQueue()) {
            if (CommRelayBackdoorIntel.checkIntel(intel) && intel instanceof CommRelayEntityPlugin.CommSnifferReadableIntel) {
                CommRelayEntityPlugin.CommSnifferReadableIntel csi = (CommRelayEntityPlugin.CommSnifferReadableIntel) intel;
                if (csi.canMakeVisibleToCommSniffer(isConnected(), entity)) {
                    intel.setForceAddNextFrame(true);
                }
            }
        }
    }
}

