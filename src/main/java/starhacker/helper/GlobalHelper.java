package starhacker.helper;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignClockAPI;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.comm.IntelManagerAPI;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.ListenerManagerAPI;

import java.util.List;

public class GlobalHelper {

    public static CampaignClockAPI getCurrentClock() {
        return Global.getSector().getClock();
    }

    public static EconomyAPI getEconomy() {
        return Global.getSector().getEconomy();
    }

    public static IntelManagerAPI getIntelManager() {return Global.getSector().getIntelManager();}

    public static ListenerManagerAPI getListenerManager() {
        return Global.getSector().getListenerManager();
    }

    public static String getSpriteName(String sprite) {
        return Global.getSettings().getSpriteName("starhacker", sprite);
    }

    public static List<MarketAPI> getMarkets() {
        return Global.getSector().getEconomy().getMarketsCopy();
    }

    public static CampaignFleetAPI getPlayerFleet() {
        return Global.getSector().getPlayerFleet();
    }

    public static FactionAPI getPlayerFaction() {
        return Global.getSector().getPlayerFaction();
    }
}
