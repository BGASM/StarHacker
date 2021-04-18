package starhacker.impl.campaign.intel.misc;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomCampaignEntityPlugin;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.input.Keyboard;
import starhacker.impl.campaign.SH_HackablePlugin;

import java.awt.*;
import java.util.Set;

/**
 * BackdoorIntel determines two things: if the backdoor access has been discovered/exists, and if intel discovered can be
 * pushed.
 */
public class BackdoorIntel extends BaseIntelPlugin {
    public static final String UNINSTALL = "uninstall";

    protected SectorEntityToken relay;
    protected IntervalUtil check = new IntervalUtil(30f * 0.5f, 30f * 1.5f);
    protected Boolean uninstalled = null;


    public BackdoorIntel(SectorEntityToken relay) {
        this.relay = relay;
        Global.getSector().getIntelManager().addIntel(this, true);
        Global.getSector().addScript(this);
    }

    public static BackdoorIntel getBackdoorIntelForRelay(SectorEntityToken relay) {
        for (IntelInfoPlugin p : Global.getSector().getIntelManager().getIntel(BackdoorIntel.class)) {
            BackdoorIntel intel = (BackdoorIntel) p;
            if (intel.getRelay() == relay) {
                return intel;
            }
        }
        return null;
    }

    @Override
    protected void notifyEnded() {
        super.notifyEnded();
        Global.getSector().removeScript(this);

        CustomCampaignEntityPlugin plugin = relay.getCustomPlugin();
        if (plugin instanceof SH_HackablePlugin) {
            SH_HackablePlugin o = (SH_HackablePlugin) plugin;
            o.setBackdoor(false);
        }
    }


    @Override
    protected void advanceImpl(float amount) {
        super.advanceImpl(amount);

        float days = Misc.getDays(amount);
        check.advance(days);

        if (check.intervalElapsed()) {
            float p = getCurrLoseProb();
            if ((float) Math.random() < p) {
                endAfterDelay();
                sendUpdateIfPlayerHasIntel(new Object(), false);
                return;
            }
        }

        if (!relay.isAlive() || !((SH_HackablePlugin)relay.getCustomPlugin()).hasBackdoor()) {
            endAfterDelay();
            sendUpdateIfPlayerHasIntel(new Object(), false);
        }
    }

    public SectorEntityToken getRelay() {
        return relay;
    }

    protected void addBulletPoints(TooltipMakerAPI info, ListInfoMode mode) {

        Color h = Misc.getHighlightColor();
        Color g = Misc.getGrayColor();
        float pad = 3f;
        float opad = 10f;

        float initPad = pad;
        if (mode == ListInfoMode.IN_DESC) initPad = opad;

        Color tc = getBulletColorForMode(mode);

        bullet(info);
        boolean isUpdate = getListInfoParam() != null;

        unindent(info);
    }

    protected float getMax() {
        return Global.getSettings().getInt("maxCommSniffersBeforeChanceToLose");
    }
    protected float getBaseLoseProb() {
        return Global.getSettings().getFloat("probToLoseSnifferPerMonthPerExtra");
    }

    protected float getCurrLoseProb() {
        float curr = 0f;
        for (IntelInfoPlugin p : Global.getSector().getIntelManager().getIntel(BackdoorIntel.class)) {
            BackdoorIntel intel = (BackdoorIntel) p;
            if (!intel.isEnding()) curr++;
        }

        float max = getMax();
        if (curr <= max) return 0f;

        float p = (curr - max) * getBaseLoseProb();
        if (p > 1) p = 1;
        return p;
    }

    @Override
    public void createIntelInfo(TooltipMakerAPI info, ListInfoMode mode) {
        Color c = getTitleColor(mode);
        info.addPara(getName(), c, 0f);
        addBulletPoints(info, mode);
    }

    @Override
    public void createSmallDescription(TooltipMakerAPI info, float width, float height) {
        Color h = Misc.getHighlightColor();
        Color g = Misc.getGrayColor();
        Color tc = Misc.getTextColor();
        float pad = 3f;
        float opad = 10f;

        info.addImage(Global.getSector().getPlayerFaction().getLogo(), width, 128, opad);

        String name = relay.getName();
        if (name.equals(relay.getCustomEntitySpec().getDefaultName())) {
            name = name.toLowerCase();
        }


        if (isEnding()) {
            if (uninstalled != null) {
                info.addPara("You've uninstalled the comm sniffer at " + name + ".", opad);
            } else {
                info.addPara("The comm sniffer installed at " + name + " is no longer responding to status queries.", opad);
            }
            addBulletPoints(info, ListInfoMode.IN_DESC);
        } else {
            info.addPara("You have installed a comm sniffer at " + name + ".", opad);
            if (!relay.isInHyperspace()) {
                info.addPara("It is providing you with up-to-date local intel for the " +
                        relay.getContainingLocation().getNameWithLowercaseType() + ".", opad);
            }

            addBulletPoints(info, ListInfoMode.IN_DESC);

            float p = getCurrLoseProb();

            String danger = null;
            Color dangerColor = null;
            if (p <= 0) {
            } else if (p <= 0.33f) {
                danger = "low";
                dangerColor = Misc.getPositiveHighlightColor();
            } else if (p <= 0.67f) {
                danger = "medium";
                dangerColor = h;
            } else {
                danger = "high";
                dangerColor = Misc.getNegativeHighlightColor();
            }

            //int num = Global.getSector().getIntelManager().getIntelCount(CommSnifferIntel.class, false);
            int num = 0;
            for (IntelInfoPlugin i : Global.getSector().getIntelManager().getIntel(BackdoorIntel.class)) {
                BackdoorIntel intel = (BackdoorIntel) i;
                if (!intel.isEnding()) num++;
            }

            String sniffers = "sniffers";
            String any = "any of them";
            if (num == 1) {
                sniffers = "sniffer";
                any = "it";
            }
            if (danger == null) {
                info.addPara("You have %s comm " + sniffers + " installed in the comm network. " +
                                "There is no danger of " + any + " being detected and cleared out.", opad,
                        h, "" + num);
            } else {
                LabelAPI label = info.addPara("You have %s comm " + sniffers + " installed in the comm network. The probability " +
                                "of " + any + " being detected and cleared out is %s.", opad,
                        h, "" + num, danger);
                label.setHighlight("" + num, danger);
                label.setHighlightColors(h, dangerColor);
            }


            ButtonAPI button = addGenericButton(info, width, "Uninstall comm sniffer", UNINSTALL);
            button.setShortcut(Keyboard.KEY_U, true);
        }
    }


    public void uninstall() {
        buttonPressConfirmed(UNINSTALL, null);
    }


    @Override
    public void buttonPressConfirmed(Object buttonId, IntelUIAPI ui) {
        if (buttonId == UNINSTALL) {
            //endAfterDelay();
            endImmediately();
            uninstalled = true;
            if (ui != null) {
                //ui.updateUIForItem(this);
                ui.recreateIntelUI();
            }
        }
    }

    @Override
    public void createConfirmationPrompt(Object buttonId, TooltipMakerAPI prompt) {
        prompt.addPara("Uninstalling this comm sniffer will reduce the chance " +
                "that the remaining comm sniffers are detected.", 0f);
        //prompt.addPara("Continue?", 10f);
    }

    @Override
    public boolean doesButtonHaveConfirmDialog(Object buttonId) {
        return true;
    }


    @Override
    public String getIcon() {
        return Global.getSettings().getSpriteName("intel", "comm_sniffer");
    }

    @Override
    public Set<String> getIntelTags(SectorMapAPI map) {
        Set<String> tags = super.getIntelTags(map);
        tags.add(Tags.INTEL_COMM_SNIFFERS);
        return tags;
    }

    public String getSortString() {
        return "Backdoor Access";
    }

    public String getName() {
        String base = "Backdoor Access";
        if (isEnding()) {
            if (uninstalled != null) {
                return base + " - Uninstalled";
            }
            return base + " - Lost";
        }
        if (isNew()) {
            return base + " Installed";
        } else {
            return base;
        }
    }



    public String getSmallDescriptionTitle() {
        return getName();
    }

    @Override
    public SectorEntityToken getMapLocation(SectorMapAPI map) {
        return relay;
    }

    @Override
    public boolean shouldRemoveIntel() {
        return isEnded();
    }

    @Override
    public String getCommMessageSound() {
        return getSoundMinorMessage();
    }


    public boolean canMakeVisibleToBackdoor(boolean playerInRelayRange, SectorEntityToken relay) {
        return canMakeVisible(playerInRelayRange, relay.getContainingLocation(),
                relay.getLocationInHyperspace(), true);
    }

}
