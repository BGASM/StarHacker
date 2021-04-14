package starhacker.impl.campaign.abilities;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.impl.campaign.abilities.BaseDurationAbility;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import org.apache.log4j.Logger;
import org.lazywizard.lazylib.campaign.CampaignUtils;
import starhacker.plugins.StarHackInteractionDialogPlugin;

import java.util.List;

/**
 * Ability class.
 */
public class StarHackAbility extends BaseDurationAbility {
    public static Logger log = Global.getLogger(StarHackAbility.class);
    protected static String TAG = "sh_hackable";
    protected List<SectorEntityToken> nearby;

    public StarHackAbility() {
    }

    /**
     * When the Hack ability is used generate a list of nearby entities with the sh_hackable tag from my worldgen.
     * List of entities gets sent over to the StarHackInteractionDialog. Right now I only am tagging Campaign Objectives
     * because they already have built in 'hack()' functionality. It will expand to ships and markets when I learn
     * wtf I am doing.
     *
     * I initially was using a list of SectorEntityTokens, but when I realized that entities were being lost,
     * I switched to using their IDs to avoid having instances in Collections that may be nullified
     * ... they are still getting lost.*/
    protected void activateImpl()
    {
        if (this.entity.isPlayerFleet()) {
            Global.getSector().addPing(entity, "interdict"); // How do I delay the dialog box so we can see ping?
            log.info(this.entity.getSensorStrength());
            CampaignFleetAPI fleet = this.getFleet();
            nearby = CampaignUtils.getNearbyEntitiesWithTag(fleet, 3.4028235E38F, TAG); // wtf is Range in? LY?
            Global.getSector().getCampaignUI().showInteractionDialog(
                    new StarHackInteractionDialogPlugin(fleet, nearby), fleet);
        }
    }

    protected void applyEffect(float arg0, float arg1) {
    }

    protected void cleanupImpl() {
    }

    protected void deactivateImpl() {
    }

    public void createTooltip(TooltipMakerAPI tooltip, boolean expanded) {
        tooltip.addTitle(this.spec.getName());
        float pad = 10.0F;
        tooltip.addPara("Scan for unsecure networks.", pad);
        this.addIncompatibleToTooltip(tooltip, expanded);
    }

    public boolean hasTooltip() {
        return true;
    }
}
