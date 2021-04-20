package starhacker.ui.extractor;

import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.util.Pair;

import java.util.LinkedHashSet;
import java.util.List;

@Deprecated public class SellTableContent extends IntelTableContent {
    protected SellTableContent(String commodityId, List<IntelInfoPlugin> intel, LinkedHashSet<Pair<SectorEntityToken, IntelInfoPlugin>> intelQueue) {
        super(commodityId, intel, intelQueue);
    }
/*
    public SellTableContent(String commodityId, List<MarketAPI> markets, EconomyAPI economy) {
        super(commodityId, markets, new DemandPrice(commodityId, economy));
    }

    @Override
    public Object[] getHeaders(float width) {
        return getHeader(width);
    }

    @Override
    protected Object[] getRow(int i, MarketAPI market) {
        CommodityOnMarketAPI commodity = market.getCommodityData(commodityId);
        float price = getPrice(market);
        int demand = helper.getDemand(market, commodity);
        int deficit = -commodity.getDeficitQuantity();
        return getRow(i, market, commodity, price, demand, deficit);
    }*/
}
