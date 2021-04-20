package starhacker.ui.extractor;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import starhacker.ui.intel.StarHackerBoard.DataTab;
public class PriceFactory {

    private EconomyAPI economy;

    public PriceFactory() {
        this.economy = Global.getSector().getEconomy();
    }

    public Price get(String commodityId, DataTab dataTab) {
        Price price = new DummyPrice();
        switch (dataTab) {
            case BUY:
                price = new SupplyPrice(commodityId, economy);
                break;

            case SELL:
                price = new DemandPrice(commodityId, economy);
                break;
        }
        return price;
    }
}
