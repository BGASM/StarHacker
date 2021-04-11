package starhacker;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import org.apache.log4j.Logger;


public class StarHackerPlugin extends BaseModPlugin {
    // call order: onNewGame -> onNewGameAfterProcGen -> onNewGameAfterEconomyLoad -> onEnabled -> onNewGameAfterTimePass -> onGameLoad
    // public static final boolean HAVE_STELLAR = Global.getSettings().getModManager().isModEnabled("eli95");

    public static Logger log = Global.getLogger(StarHackerPlugin.class);
    protected static boolean isNewGame = false;

    protected void applyToExistingSave()
    {
        log.info("Applying StarHacker to existing game");
    }

    @Override
    public void onGameLoad(boolean newGame) {
        log.info("Game load");
        isNewGame = newGame;
    }

    @Override
    public void beforeGameSave()
    {
        log.info("Before game save");
    }

    @Override
    public void afterGameSave() {
        log.info("After game save");
    }

      @Override
    public void onNewGame() {
        log.info("New game");
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

    @Override
    public void onNewGameAfterTimePass() {
        log.info("New game after time pass; " + isNewGame);
    }
}
