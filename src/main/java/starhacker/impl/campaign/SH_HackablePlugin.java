package starhacker.impl.campaign;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.impl.campaign.BaseCampaignObjectivePlugin;
import starhacker.impl.campaign.intel.misc.BackdoorIntel;
import starhacker.plugins.BackdoorPlugin;

public class SH_HackablePlugin extends BaseCampaignObjectivePlugin implements BackdoorPlugin {
    public SH_HackablePlugin() {
    }

    public void init(SectorEntityToken entity, Object pluginParams) {
        super.init(entity, pluginParams);
        this.readResolve();
    }

    Object readResolve() {
        return this;
    }

    protected boolean isMakeshift() {return this.entity.hasTag("makeshift");}

    @Override
    public void setBackdoor(boolean backdoor, float days) {
        if (backdoor){
            this.entity.getMemoryWithoutUpdate().set("$sh_backdoor", backdoor, days);
        } else {
            this.entity.getMemoryWithoutUpdate().unset("$sh_backdoor");
        }
    }


    @Override
    public void setBackdoor(boolean backdoor){
        if (backdoor) {
            this.setBackdoor(backdoor, -1.0F);
            boolean found = BackdoorIntel.getBackdoorIntelForRelay(this.entity) != null;
            if (!found) {
                BackdoorIntel intel = new BackdoorIntel(this.entity);
                InteractionDialogAPI dialog = Global.getSector().getCampaignUI().getCurrentInteractionDialog();
                if (dialog != null) {
                    Global.getSector().getIntelManager().addIntelToTextPanel(intel, dialog.getTextPanel());
                }
            }
        } else {
            this.setBackdoor(backdoor, -1.0F);
        }
    }

    @Override
    public boolean isConnected() {
        return Global.getSector().getIntelManager().isPlayerInRangeOfCommRelay();
    }

    @Override
    public void checkBackdoor() {
        if (!hasBackdoor()) return;
        if (this.isConnected()) {
            for (IntelInfoPlugin intel : Global.getSector().getIntelManager().getCommQueue()) {
                if (intel instanceof BackdoorIntel) {
                    BackdoorIntel csi = (BackdoorIntel) intel;
                    if (csi.canMakeVisibleToBackdoor(this.isConnected(), this.entity)) {
                        intel.setForceAddNextFrame(true);
                    }
                }
            }

        }
    }

    @Override
    public boolean hasBackdoor(){
        return this.entity != null && this.entity.getMemoryWithoutUpdate().getBoolean("$sh_backdoor");
    }

    @Override
    public void vanillaBonus() {

    }

    @Override
    public void checkIntelFromBackdoor() {

    }

}
