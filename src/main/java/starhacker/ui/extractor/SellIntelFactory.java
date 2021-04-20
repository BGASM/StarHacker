package starhacker.ui.extractor;

import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.util.Pair;
import starhacker.ui.CollectionsHelper;
import starhacker.ui.filter.intel.CommodityDemandFilter;


import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;

@Deprecated public class SellIntelFactory extends IntelFactory {
    public SellIntelFactory(String dataId, LinkedHashSet<Pair<SectorEntityToken, IntelInfoPlugin>> intelMap) {
        super(dataId, intelMap);
    }
/*
    private DemandPrice price;

    public SellIntelFactory(String commodityId, EconomyAPI economy) {
        super(commodityId, economy);
        this.price = new DemandPrice(commodityId, economy);
    }

    @Override
    protected void filterMarkets(List<MarketAPI> markets) {
        CollectionsHelper.reduce(markets, new CommodityDemandFilter(commodityId, economy));
    }

    @Override
    protected void sortMarkets(List<MarketAPI> markets) {
        Collections.sort(markets, new Comparator<MarketAPI>() {

            @Override
            public int compare(MarketAPI marketA, MarketAPI marketB) {
                float priceA = getPrice(marketA);
                float priceB = getPrice(marketB);
                return (int) Math.signum(priceB - priceA);
            }
        });
    }

    private float getPrice(MarketAPI market) {
        return price.getPrice(market);
    }*/
}
