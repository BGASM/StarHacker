package starhacker.world;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.OrbitAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.CommRelayEntityPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.List;

/**
* This script will iterate through all entities in the game and add sh_hackable tags to the ones
* this mod plans to operate on.
* */
public class StarHackerNewGameSetup {
    public static Logger log = Global.getLogger(StarHackerNewGameSetup.class);
    private static CommRelayEntityPlugin comm;
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
                    swapObj(system, objective);
                }
            }
            log.info("Finished adding hack tag.");
        }
        log.info("Exiting tagObjectives.");
    }

    public static void swapObj(StarSystemAPI system, SectorEntityToken obj){
        OrbitAPI orbit = obj.getOrbit();
        String name = obj.getName();
        FactionAPI owner = obj.getFaction();
        String type = "sh_"+obj.getCustomEntityType();
        Collection<String> tags = obj.getTags();
        String id = obj.getId();
        MemoryAPI mem = obj.getMemoryWithoutUpdate();

        tags.add(type);
        SectorEntityToken relay = system.addCustomEntity(id, name, type, owner.getId());
        relay.setOrbit(orbit);
        for (String t : tags){
            relay.addTag(t);}

        system.removeEntity(obj);
    }
}
