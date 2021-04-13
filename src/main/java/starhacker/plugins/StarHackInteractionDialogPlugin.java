package starhacker.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.combat.EngagementResultAPI;
import com.fs.starfarer.api.impl.campaign.CampaignObjective;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import org.apache.log4j.Logger;
import starhacker.helper.StringHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
*    Dialog code shameless stolen from Kentington's Capture Officers and Crew mod.
*/
public class StarHackInteractionDialogPlugin implements InteractionDialogPlugin {
    public static final int ENTRIES_PER_PAGE = 6;
    public static Logger log = Global.getLogger(StarHackInteractionDialogPlugin.class);
    protected static String STRING = "sh_hackdialogue";
    protected static String RELAY = "comm_relay";
    protected static String SENSOR = "sensor_array";
    protected static String NAV ="nav_buoy";
    protected InteractionDialogAPI dialog;
    protected HacksPlugin hacks = new HacksPlugin();
    protected TextPanelAPI text;
    protected OptionPanelAPI options;
    protected String token_id;
    protected ArrayList<Menu> currentMenu = new ArrayList<>();

    protected List<Pair<String, String>> optionsList = new ArrayList<>();
    protected int currentPage = 1;
    protected CampaignFleetAPI fleet;
    protected List<String> nearby;
    protected List<String> net_nearby = new ArrayList<>();
    public StarHackInteractionDialogPlugin(CampaignFleetAPI theFleet, List<String> nearbyTargets)
    {
        this.fleet = theFleet;
        this.nearby = nearbyTargets;
        log.info(nearby);
    }

    public static String getString(String id) {
        return getString(id, false);
    }

    public static String getString(String id, boolean ucFirst) {
        return StringHelper.getString(STRING, id, ucFirst);
    }

    protected void populateOptions() {
        this.options.clearOptions();
        if(currentMenu.get(currentMenu.size() -1) != null) {
            log.info(currentMenu.get(currentMenu.size() -1));
            switch (currentMenu.get(currentMenu.size() -1)) {
                case ACCESS:
                    populateHackingOptions();
                    break;
                case INIT:
                    populateMainMenuOptions();
                    break;
                case EXIT:
                    populateExitOptions();
                    break;
                default:
                    populateConfirmCancel();
            }
        }else {populateConfirmCancel();}
    }


    protected void populateHackingOptions() {
        log.info("Populate Hacks");
        Pair<String, List<String>> trace_data = new Pair<>();
        /* SectorEntityToken token = Global.getSector().getEntityById(token_id);
         * CustomCampaignEntityPlugin plugin = token.getCustomPlugin();
         * Collection<String> obj_tags = token.getTags(); */
        Collection<String> obj_tags = Global.getSector().getEntityById(token_id).getTags();
        /* if (plugin instanceof CampaignObjective) {
            if(((CampaignObjective) plugin).isHacked()){
                options.addOption("Remove the uploaded virus", Menu.REMOVE);
            }*/
                /**Removing this check as well just to test if I get any closer to figuring out where the entities
                are getting nullified. It was to check if entities where base campaign plugins, which I would
                have different types of hacks for vs Fleets or Markets. The Remove Virus depended on having an instance
                 of CampaignObjective. Once I figure out what's going on I'll replace it.*/
        /**
         * This block of code checks if the targets have tags that determine what hacks are available. Right now, I have
         * them all just calling the default setHacked(true) function, to test this all out. This also originially passed
         * The SectorEntityToken, but now sends a string of its ID instead. runTrace looks for similar entities belonging
         * to the local one's Faction. It searches across Hyperspace and returns their names, distance, and entityId.
          */
        if(obj_tags.contains(RELAY)){
                trace_data = hacks.runTrace(token_id);
                options.addOption("Upload a virus", Menu.UPLOAD);
            }else if (obj_tags.contains(NAV)){
                trace_data = hacks.runTrace(token_id);
                options.addOption("Upload a virus <Nothing fun implemented yet.>", Menu.UPLOAD);
            }else if (obj_tags.contains(SENSOR)){
                trace_data = hacks.runTrace(token_id);
                options.addOption("Upload a virus <Nothing fun implemented yet.>", Menu.UPLOAD);
            }
            if (trace_data != null){
                String str = trace_data.one;
                net_nearby = trace_data.two;
                text.addParagraph(str);
            }
        //}
        addBackOption();
    }

    protected void populateConfirmCancel() {
        this.options.clearOptions();
        this.options.addOption("Confirm", Menu.CONFIRM);
        this.addBackOption();
    }

    /**
     * Initial dialog menu that pops up. It takes the list of local entities and parses their strings to create
     * selection options.
     */
    protected void populateMainMenuOptions() {
        this.optionsList.clear();
        text.addParagraph( getString("hack_prompt"));
        dialog.setPromptText(Misc.ucFirst("Options"));
        for (String s : this.nearby) {
            SectorEntityToken token = Global.getSector().getEntityById(s);
            String name_fac = token.getName() + " - " + token.getFaction().getDisplayName();
            optionsList.add(new Pair<>(name_fac, s));
        }
        this.showPaginatedMenu();
    }

    /**
     * Creates the option for JUST back. I was getting annoyed at seeing confirm stuck on the menu after completing
     * the dialog. So it clears the options and then specifically adds a back option.
     */
    protected void populateExitOptions(){
        optionsList.clear();
        addBackOption();
    }

    /**
     * Generates the dialog on initialization. No idea what any of this does, I copied this lol. I do know that I set
     * the currentMenu to INIT which was the enum I used to point to loading my 'first' page.
     *
     * @param dialog who knows....?
     */
    public void init(InteractionDialogAPI dialog) {
        log.info("In Dialog Init.");
        this.dialog = dialog;
        options = this.dialog.getOptionPanel();
        text = this.dialog.getTextPanel();
        this.dialog.getVisualPanel().setVisualFade(0.25F, 0.25F);
        currentMenu.add(Menu.INIT);
        populateOptions();
    }

    /**
     * This was when things started to get weird. It is possible that the original version was also having the issue
     * of deleting the selected entity when dialog ended - but I was only focusing on getting through all the options.
     * I feel like I remember seeing my remove virus option come up, which would mean that nothing was deleted, since
     * it checks the entities isHacked boolean.
     *
     * So the big change I made here when I noticed the issue was changing how I track steps through the dialog tree.
     * Initially I was only tracking lastselected menu. Issue was that you could not backtrack out of a multi-step tree.
     * I ended up creating an array, and then when we step through the tree each Menu enum was added to the tree. If
     * the player selected BACK from options, it would pop the current dialog frame from the end of the array, and then
     * point at the preceding one when reaching populateOptions(). This worked really well!
     *
     * My worry though is that somehow, a reference that is pointing to the entity instance is either getting nullified,
     * or otherwise hung up. I've tried remove all hard references to the entity by using IDs instead. But no luck.
     *
     * So now, if you end up here with the option BACK selected, and the currentMenu is set to BACK, the dialog close
     * function is called. Otherwise, it loads whatever menu was on the index.
     */
    public void optionSelected(String optionText, Object optionData) {
        if (optionText != null) {
            text.addParagraph(optionText, Global.getSettings().getColor("buttonText"));
        }

        if (optionData instanceof Menu) {
            Menu option = (Menu) optionData;
            switch (option) {
                case NEXT:
                    ++currentPage;
                    showPaginatedMenu();
                    return;
                case PREVIOUS:
                    --currentPage;
                    showPaginatedMenu();
                    return;
                default:
                    currentPage = 1;
                }
            switch (option) {
                case BACK:
                    if (currentMenu.get(currentMenu.size() -1) == Menu.EXIT || currentMenu.get(currentMenu.size() -1) == Menu.INIT) {
                        this.dialog.dismiss();
                    } else {
                        currentMenu.remove(currentMenu.size() -1);
                    }
                    break;
                case UPLOAD:
                    text.addParagraph("You select a virus and enter the commands to upload it.");
                    currentMenu.add(Menu.UPLOAD);
                    break;
                case REMOVE:
                    text.addParagraph("You enter the commands to delete the virus you found.");
                    currentMenu.add(Menu.REMOVE);
                    break;
                case CONFIRM:
                    switch (currentMenu.get(currentMenu.size() -1)){
                        case UPLOAD:
                            text.addParagraph("You hit return, and a few moments later your hear a hum of confirmation.");
                            hacks.uploadVirus(net_nearby);
                            currentMenu.add(Menu.EXIT);
                            break;
                        case REMOVE:
                            text.addParagraph("You hit return, and a few moments later your hear a hum of confirmation.");
                            hacks.removeVirus(token_id);
                            currentMenu.add(Menu.EXIT);
                            break;
                        default:
                            currentMenu.add(Menu.EXIT);
                    }

            }
        } else if (optionData instanceof String) {  // This also was originally checking instanceof SectorEntityToken
            String str;                             // which was initially passed as an object.
            currentPage = 1;
            currentMenu.add(Menu.ACCESS);
            token_id = (String)optionData;
            str = getString("access_network");
            str = StringHelper.substituteToken(str, "$target", Global.getSector().getEntityById(token_id).getName());
            text.addParagraph(str);
            str = getString("access_network2");
            text.addParagraph(str);
        }
        populateOptions();
    }

    protected void showPaginatedMenu() {
        if (!optionsList.isEmpty()) {

            options.clearOptions();
            int offset = (currentPage - 1) * ENTRIES_PER_PAGE;
            int max = Math.min(offset + ENTRIES_PER_PAGE, optionsList.size());
            int numPages = 1 + (optionsList.size() - 1) / ENTRIES_PER_PAGE;
            if (currentPage > numPages) {
                currentPage = numPages;
                offset = (currentPage - 1) * ENTRIES_PER_PAGE;
            }

            for (int x = offset; x < max; ++x) {
                Pair<String, String> entry = optionsList.get(x);
                options.addOption(entry.one, entry.two);
            }

            if (currentPage > 1) {
                options.addOption("Previous", Menu.PREVIOUS);
                options.setShortcut(Menu.PREVIOUS, 203, false, false, false, true);
            }

            if (currentPage < numPages) {
                options.addOption("Next", Menu.NEXT);
                options.setShortcut(Menu.NEXT, 205, false, false, false, true);
            }
        }
        addBackOption();
    }

    protected void addBackOption() {
        options.addOption("Back", Menu.BACK);
        options.setShortcut(Menu.BACK, 1, false, false, false, true);
    }

    /**
     * No clue what the rest of these are for...
     */
    public void optionMousedOver(String arg0, Object arg1) {
    }

    public void advance(float arg0) {
    }

    public void backFromEngagement(EngagementResultAPI arg0) {
    }

    public Object getContext() {
        return null;
    }

    public Map<String, MemoryAPI> getMemoryMap() {
        return null;
    }

    /**
     * Menu Enums.
     */
    protected enum Menu {
        REMOVE,
        UPLOAD,
        ACCESS,
        INIT,
        BACK,
        NEXT,
        PREVIOUS,
        CONFIRM,
        EXIT
    }


}
