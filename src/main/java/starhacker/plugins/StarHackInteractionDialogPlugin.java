package starhacker.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.combat.EngagementResultAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import org.apache.log4j.Logger;
import org.lazywizard.lazylib.CollectionUtils;
import starhacker.helper.StringHelper;

import starhacker.impl.campaign.SH_HackablePlugin;

import java.util.*;

/**
*    Dialog code shameless stolen from Kentington's Capture Officers and Crew mod.
*/
public class StarHackInteractionDialogPlugin implements InteractionDialogPlugin, Cloneable {
    public static final int ENTRIES_PER_PAGE = 6;
    public static Logger log = Global.getLogger(StarHackInteractionDialogPlugin.class);
    protected static Collection<String> filter = Arrays.asList("sh_comm_relay", "sh_sensor_array", "sh_nav_buoy");
    protected static String STRING = "sh_hackdialogue";

    protected InteractionDialogAPI dialog;

    protected TextPanelAPI text;
    protected OptionPanelAPI options;
    protected SectorEntityToken token;
    protected ArrayList<Menu> currentMenu = new ArrayList<>();

    protected List<Pair<String, Object>> optionsList = new ArrayList<>();
    protected int currentPage = 1;
    protected CampaignFleetAPI fleet;
    protected Collection<SectorEntityToken> nearby;
    protected List<SectorEntityToken> target_group;
    public StarHackInteractionDialogPlugin(CampaignFleetAPI theFleet, List<SectorEntityToken> nearbyTargets)
    {
        fleet = theFleet;
        nearby = nearbyTargets;
    }

    public static String getString(String id) {
        return getString(id, false);
    }

    public static String getString(String id, boolean ucFirst) {
        return StringHelper.getString(STRING, id, ucFirst);
    }

    protected void populateOptions() {
        options.clearOptions();
        if(currentMenuIndex() != null) {            
            switch (currentMenuIndex()) {
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
        }else {dialog.dismiss();}
    }

    protected void populateHackingOptions() {
        CustomCampaignEntityPlugin plugin = token.getCustomPlugin();
        if (plugin instanceof SH_HackablePlugin) {
            if (((SH_HackablePlugin) plugin).hasBackdoor())
                options.addOption("Remove the uploaded virus", Menu.REMOVE);

            Collection<String> filters = filter;
            Pair<String, Collection<SectorEntityToken>> trace_data = null;
            String type = token.getCustomEntityType();
            if (filters.contains(type)) {
                trace_data = HacksPlugin.runTrace(token, type);
                options.addOption("Upload a virus.", Menu.UPLOAD);
            }
            if (trace_data != null) {
                String str = trace_data.one;
                target_group = CollectionUtils.combinedList(trace_data.two, Collections.singletonList(token));
                text.addParagraph(str);
            } else
                dialog.dismissAsCancel();
        }
        addBackOption();
    }

    protected void populateConfirmCancel() {
        options.clearOptions();
        options.addOption("Confirm", Menu.CONFIRM);
        addBackOption();        
    }

    /**
     * Initial dialog menu that pops up. It takes the list of local entities and parses their strings to create
     * selection options.
     */
    protected void populateMainMenuOptions() {
        optionsList.clear();
        text.addParagraph( getString("hack_prompt"));
        
        for (SectorEntityToken token : this.nearby) {
            String name_fac = token.getName() + " - " + token.getFaction().getDisplayName();
            optionsList.add(new Pair<String, Object> (name_fac, (SectorEntityToken)token));
        }
        showPaginatedMenu();
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
     * @param dialog_in who knows....?
     */
    public void init(InteractionDialogAPI dialog_in) {        
        dialog = dialog_in;
        options = dialog.getOptionPanel();
        text = dialog.getTextPanel();
        dialog.getVisualPanel().setVisualFade(0.25F, 0.25F);
        dialog.setPromptText(Misc.ucFirst("Options"));
        // Initialize the Dialog API and then call our first page.
        currentMenu.add(Menu.INIT);
        populateOptions();        
    }

    /**
     * Hacks.
     */
    public void optionSelected(String optionText, Object optionData) {
        int oldPage = currentPage;
        currentPage = 1;
        if (optionText != null)
            text.addParagraph(optionText, Global.getSettings().getColor("buttonText"));

        if (optionData instanceof Menu) {
            Menu option = (Menu) optionData;
            switch (option) {
                case NEXT:
                    currentPage = oldPage +1;
                    showPaginatedMenu();
                    break;
                case PREVIOUS:
                    currentPage = oldPage - 1;
                    showPaginatedMenu();
                    break;
                case BACK:
                    if (currentMenuIndex() == Menu.EXIT || currentMenuIndex() == Menu.INIT)
                        dialog.dismissAsCancel();
                    else
                        currentMenu.remove(currentMenu.size() -1);
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
                    switch (currentMenuIndex()){
                        case UPLOAD:
                            HacksPlugin.uploadVirus(target_group);
                            break;
                        case REMOVE:
                            HacksPlugin.removeVirus(token);
                            break;
                        default:
                            text.addParagraph("You realize you don't have the right scripts.");
                    }
                    text.addParagraph("You hit return, and a few moments later your hear a hum of confirmation.");
                    currentMenu.add(Menu.EXIT);
            }
        } else if (optionData instanceof SectorEntityToken) {
            currentMenu.add(Menu.ACCESS);
            token = (SectorEntityToken)optionData;
            text.addParagraph(StringHelper.substituteToken(getString("access_network"),
                    "$target", (token.getName())));
            text.addParagraph(getString("access_network2"));
        } else
            log.info("Called unexpected option: " + optionData.toString() + ".");

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
                Pair<String, Object> entry = optionsList.get(x);
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
    
    protected Menu currentMenuIndex(){
        return currentMenu.get(currentMenu.size() -1);
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
