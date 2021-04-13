package starhacker.data.campaign.rulecmd;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.CampaignObjective;
import com.fs.starfarer.api.impl.campaign.intel.misc.CommSnifferIntel;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.util.Misc;
import org.lazywizard.lazylib.MathUtils;
import starhacker.StarHackerPlugin;
import starhacker.helper.CampaignHelper;

import java.util.*;
/*  Rulescmd was not giving me quite what I wanted for this project.
    I'm keeping the source up in case I decide to come back to it.
    Instead I'm using custom tags, abilities, and dialog interactions.
 */
@Deprecated class SH_Virus extends BaseCommandPlugin {
    protected CampaignFleetAPI playerFleet;
    protected SectorEntityToken entity;
    protected FactionAPI playerFaction;
    protected FactionAPI entityFaction;
    protected TextPanelAPI text;
    protected OptionPanelAPI options;
    protected CargoAPI playerCargo;
    protected MemoryAPI memory;
    protected InteractionDialogAPI dialog;
    protected Map<String, MemoryAPI> memoryMap;
    protected FactionAPI faction;
    protected Collection<String> tags;
    protected List<String> filters = Arrays.asList("comm_relay", "sensor_array", "nav_buoy");
    protected List<SectorEntityToken> nearby;

    @Deprecated public SH_Virus() {
    }

    @Deprecated public SH_Virus(SectorEntityToken entity) {
        init(entity);
    }

    @Deprecated protected void init(SectorEntityToken entity) {
        memory = entity.getMemoryWithoutUpdate();
        this.entity = entity;
        entityFaction = entity.getFaction();
        faction = entity.getFaction();
        playerFleet = Global.getSector().getPlayerFleet();
        playerCargo = playerFleet.getCargo();
        playerFaction = Global.getSector().getPlayerFaction();

        tags = entity.getTags();
        tags.retainAll(filters);

        nearby = CampaignHelper.getAllEntitiesFromFactionHyper(entity, tags.iterator().next(), entityFaction);

        List<SectorEntityToken> installed = Global.getSector().getIntel().getCommSnifferLocations();
        for (SectorEntityToken x : installed) {
            Global.getLogger(StarHackerPlugin.class).info(x.getName());
        }
        for (SectorEntityToken s : nearby) {
            float d = MathUtils.getDistance(entity.getLocationInHyperspace(), s.getLocationInHyperspace());
            String name = s.getName();
            String fac = s.getFaction().getDisplayName();
            String star = s.getStarSystem().getBaseName();
            String concat = name +" - "+ star + " - " + " - " + fac +":"+d;
            Global.getLogger(StarHackerPlugin.class).info(concat);
        }
    }

    @Deprecated public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {

        this.dialog = dialog;
        this.memoryMap = memoryMap;

        String command = params.get(0).getString(memoryMap);
        if (command == null) return false;

        entity = dialog.getInteractionTarget();
        init(entity);

        memory = getEntityMemory(memoryMap);

        text = dialog.getTextPanel();
        options = dialog.getOptionPanel();

        if (command.equals("isHacked")) {
            return isHacked();
        } else if (command.equals("doAction")) {
            String action = params.get(1).getString(memoryMap);
            if (action.equals("uploadVirus")) {
                uploadVirus();
            } else if (action.equals("removeVirus")) {
                removeVirus();
            }
        }
        return true;
    }

    @Deprecated public void removeVirus() {
        CommSnifferIntel intel = CommSnifferIntel.getExistingSnifferIntelForRelay(entity);
        if (intel != null) {
            intel.uninstall();
        } else {
            CustomCampaignEntityPlugin plugin = entity.getCustomPlugin();
            if (plugin instanceof CampaignObjective) {
                CampaignObjective o = (CampaignObjective) plugin;
                o.setHacked(false);
            }
        }
        updateMemory();
    }

    @Deprecated public void uploadVirus() {
        List<SectorEntityToken> tmp = new ArrayList<>(nearby);
        tmp.add(entity);
        for (SectorEntityToken s : tmp) {
            CustomCampaignEntityPlugin plugin = s.getCustomPlugin();
            if (plugin instanceof CampaignObjective) {
                CampaignObjective o = (CampaignObjective) plugin;
                Global.getLogger(StarHackerPlugin.class).info(o.toString());
                o.setHacked(true);
            }
        }
        updateMemory();
    }

    @Deprecated public boolean isHacked() {
        CustomCampaignEntityPlugin plugin = entity.getCustomPlugin();
        if (plugin instanceof CampaignObjective) {
            CampaignObjective o = (CampaignObjective) plugin;
            return o.isHacked();
        }
        return false;
    }

    @Deprecated public void updateMemory() {
        //memory.set("$cob_hacked", isHacked(), 0f);
        //memory.set(BaseCampaignObjectivePlugin.HACKED, isHacked(), 0f);
    }



}

