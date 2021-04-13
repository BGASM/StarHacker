package starhacker.world;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import org.apache.log4j.Logger;

import java.util.List;

/**
* This script will iterate through all entities in the game and add sh_hackable tags to the ones
* this mod plans to operate on.
* */
public class StarHackerNewGameSetup {
    public static Logger log = Global.getLogger(StarHackerNewGameSetup.class);
    protected List<StarSystemAPI> populatedSystems;

    public void addTags() {
        log.info("Adding StarHacker Tags to Entities");
        populatedSystems = Global.getSector().getStarSystems();
        log.info(populatedSystems);
        tagObjectives();
    }

    /**
     * Add sh_hackable tag to Objectives
     */
    protected void tagObjectives() {
        for (StarSystemAPI system : populatedSystems) {
            // see if there are existing relays or other objectives we can co-opt
            for (SectorEntityToken objective : system.getEntitiesWithTag(Tags.OBJECTIVE)) {
                if (objective.hasTag(Tags.COMM_RELAY) || objective.hasTag(Tags.SENSOR_ARRAY)
                        || objective.hasTag(Tags.NAV_BUOY)) {
                    objective.addTag("sh_hackable");
                    log.info(objective.getTags());
                }
            }
            log.info("Finished adding hack tag.");
        }
        log.info("Exiting tagObjectives.");
    }
}
