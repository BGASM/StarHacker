package starhacker.ui.intel.element;

import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.util.Pair;
import org.lwjgl.input.Keyboard;
import starhacker.campaign.SectorManager;
import starhacker.ui.extractor.*;
import starhacker.ui.intel.StarHackerBoard.DataTab;
import starhacker.ui.ui.*;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class DataViewFactory {

    private LinkedHashSet<Pair<SectorEntityToken, IntelInfoPlugin>> intelQueue;
    private IntelSelectionFactory intelSelectionFactory;

    public DataViewFactory(IntelSelectionFactory intelSelectionFactory) {
        this.intelQueue = SectorManager.getManager().getIntelQueue();
        this.intelSelectionFactory = intelSelectionFactory;
    }
    public void refreshIntel(IntelSelectionFactory intelSelectionFactory) {
        this.intelQueue = SectorManager.getManager().getIntelQueue();
        this.intelSelectionFactory = intelSelectionFactory;
    }

    public Renderable get(String dataId, DataTab activeTab, float width, float height) {
        float tabsHeight = 15f;
        float tableHeight = height - tabsHeight;
        TableContent tableContent = getTableContent(dataId, activeTab);
        Renderable tabs = getTabs(activeTab);
        Renderable table = new Table(dataId, width, tableHeight, tableContent);
        return new Stack(tabs, table);
    }

    private Renderable getTabs(DataTab activeTab) {
        Renderable buyButton = new TabButton(DataTab.BUY, activeTab, Keyboard.KEY_B);
        Renderable sellButton = new TabButton(DataTab.SELL, activeTab, Keyboard.KEY_S);
        return new Row(Arrays.asList(buyButton, sellButton));
    }

    private TableContent getTableContent(String commodityId, DataTab activeTab) {
        TableContent tableContent = null;
        if (activeTab == DataTab.BUY) {
            tableContent = getBuyTableContent(commodityId);
        } else if (activeTab == DataTab.SELL) {
            tableContent = getBuyTableContent(commodityId);
        }
        return tableContent;
    }

    /**
     * get the hashset from sector manager and use that to generate a list of avaialble mission types, locations we have
     * sniffers, and of course, the intel.
     */
    private TableContent getBuyTableContent(String dataId) {
        return (TableContent) new CommTableContent(dataId, getIntel(new CommIntelFactory(dataId, intelQueue)), intelQueue);
    }

    private List<IntelInfoPlugin> getIntel(IntelFactory factory) {
        List<IntelInfoPlugin> intel = factory.getIntel();
        intelSelectionFactory.setMarkets(intel);
        return intel;
    }
}
