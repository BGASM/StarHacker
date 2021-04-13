package starhacker;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import org.apache.log4j.Logger;
import starhacker.world.StarHackerNewGameSetup;

/**
 * This Plugin code was shamelessly stolen from Histidine's Nexerelin mod.
 */
@SuppressWarnings("ConstantConditions")
public class StarHackerPlugin extends BaseModPlugin {
    // call order: onNewGame -> onNewGameAfterProcGen -> onNewGameAfterEconomyLoad -> onEnabled -> onNewGameAfterTimePass -> onGameLoad
    // public static final boolean HAVE_STELLAR = Global.getSettings().getModManager().isModEnabled("eli95");

    public static Logger log = Global.getLogger(StarHackerPlugin.class);
    protected static boolean isNewGame = false;

    public StarHackerPlugin(){
    }

    /**
     * On load check if player has hack ability. If not, it loads it on the player.
    */
    @Override
    public void onGameLoad(boolean newGame){
        log.info("Game load - Checking if Player has hack ability.");
        isNewGame = newGame;
        if (!Global.getSector().getPlayerFleet().hasAbility("starhack")) {
            Global.getSector().getCharacterData().addAbility("starhack");
        }
        Global.getSector().getPlayerFleet().addAbility("starhack");

    }

    /**
     * If the save did not have StarHack it will run the script to add required tags to entities.
     * */
    protected void applyToExistingSave(){
        log.info("Applying StarHacker to existing game");
        new StarHackerNewGameSetup().addTags();
        if (!Global.getSector().getPlayerFleet().hasAbility("starhack")) {
            Global.getSector().getCharacterData().addAbility("starhack");
        }
        Global.getSector().getPlayerFleet().addAbility("starhack");

    }

    @Override
    public void onEnabled(boolean wasEnabledBefore){
        log.info("On enabled; " + wasEnabledBefore);
        if (!isNewGame && !wasEnabledBefore) {
            log.info(!isNewGame + ", " + !wasEnabledBefore);
            this.applyToExistingSave();
        }
    }

    @Override
    public void beforeGameSave(){
        log.info("Before game save - Removing Starhack");
        toggleOff();

    }

    @Override
    public void afterGameSave(){
        log.info("After game save - Returning Starhack");
        toggleOn();
    }

    @Override
    public void onNewGame(){
        isNewGame = true;
    }

    @Override
    public void onNewGameAfterProcGen() {
        log.info("New game after proc gen; " + isNewGame);
    }

    @Override
    public void onNewGameAfterEconomyLoad() {
        log.info("New game after economy load; " + isNewGame);

    }

    /**
     * Delayed the tag-adding script to try to make sure it runs after custom entities are loaded by other mods
     * that may precede me in load order
     * */
    @Override
    public void onNewGameAfterTimePass(){
        new StarHackerNewGameSetup().addTags();
    }

    /**
     * Script removes abilities before save
     * */
    public void toggleOn(){
        Global.getSector().getCharacterData().addAbility("starhack");
        Global.getSector().getPlayerFleet().addAbility("starhack");
    }

    /**
     * And then adds them back after save.
     * */
    public void toggleOff(){
        Global.getSector().getCharacterData().removeAbility("starhack");
        Global.getSector().getPlayerFleet().removeAbility("starhack");
    }
}
