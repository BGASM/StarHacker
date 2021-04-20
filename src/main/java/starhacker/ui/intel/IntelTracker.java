package starhacker.ui.intel;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.campaign.econ.CommoditySpecAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import starhacker.ui.extractor.Price;
import starhacker.ui.extractor.PriceFactory;

import java.util.HashMap;

@Deprecated public class IntelTracker extends HashMap<String, StarHackerIntel> {
/*
    private static final long serialVersionUID = 1L;
    private IntelManager manager;
    private PriceFactory priceFactory;

    public IntelTracker() {
        super();
        manager = new IntelManager();
        priceFactory = new PriceFactory();
    }

    public void remove(StarHackerIntel intel) {
        String key = getKey(intel.getAction(), intel.getCommodityId(), intel.getMarket());
        manager.remove(intel);
        remove(key);
    }

    public boolean has(String action, String commodityId, IntelInfoPlugin intel) {
        String key = getKey(action, commodityId, intel);
        StarHackerIntel intel = get(key);
        return intel != null;
    }

    public void toggle(String dataId, StarHackerBoard.DataTab dataTab, IntelInfoPlugin intel) {
        String action = dataTab.title;
        String key = getKey(action, dataId, intel);
        StarHackerIntel intel = get(key);
        if (intel == null) {
            CommoditySpecAPI commodity = Global.getSector().getEconomy().getCommoditySpec(dataId);
            Price price = priceFactory.get(dataId, dataTab);
            intel = new StarHackerIntel(action, commodity, intel, this, price);
            manager.add(intel);
            put(key, intel);
        } else {
            manager.remove(intel);
            remove(key);
        }
    }

    private String getKey(String action, String commodityId, MarketAPI market) {
        return action + ":" + commodityId + ":" + market.getName();
    }*/
}
