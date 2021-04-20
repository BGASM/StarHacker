package starhacker.ui.intel;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin;
import com.fs.starfarer.api.ui.CustomPanelAPI;
import com.fs.starfarer.api.ui.IntelUIAPI;
import com.fs.starfarer.api.ui.SectorMapAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import starhacker.ui.intel.element.ButtonViewFactory;
import starhacker.ui.intel.element.DataViewFactory;
import starhacker.ui.intel.element.IntelSelectionFactory;
import starhacker.ui.ui.Callable;
import starhacker.ui.ui.GridRenderer;
import starhacker.ui.ui.IntelConstants;
import starhacker.ui.ui.Size;

import java.util.Set;

public class StarHackerBoard extends BaseIntelPlugin {

    public enum DataTab {
        BUY("COMM"), SELL("SENSOR");

        public String title;

        private DataTab(String title) {
            this.title = title;
        }
    }

    private String activeId;
    private DataTab activeTab;
    private ButtonViewFactory buttonViewFactory;
    private DataViewFactory dataViewFactory;
    private IntelSelectionFactory intelSelectionFactory;

    public static StarHackerBoard getInstance() {
        IntelManager intelManager = new IntelManager();
        IntelInfoPlugin intel = intelManager.get(StarHackerBoard.class);
        if (intel == null) {
            intel = new StarHackerBoard();
            intelManager.add(intel);
        }
        return (StarHackerBoard) intel;
    }

    public StarHackerBoard() {
        readResolve();
    }

    @Override
    public void buttonPressConfirmed(Object buttonId, IntelUIAPI ui) {
        Callable callable = (Callable) buttonId;
        callable.callback();
        ui.updateUIForItem(this);
    }

    @Override
    public void createIntelInfo(TooltipMakerAPI info, ListInfoMode mode) {
        info.addPara("StarHacker Board", getTitleColor(mode), 0);
        info.addPara("Track current backdoor access and gateways.", getBulletColorForMode(mode), 1f);
        info.addPara("", 1f);
    }

    @Override
    public void createLargeDescription(CustomPanelAPI panel, float width, float height) {
        float intelSourceViewWidth = width - 210;
        float intelSourceViewHeight = height - 35;
        GridRenderer renderer = new GridRenderer(new Size(width, height));
        dataViewFactory.refreshIntel(intelSelectionFactory);
        renderer.setTopLeft(dataViewFactory.get(activeId, activeTab, intelSourceViewWidth, intelSourceViewHeight));
        renderer.setTopRight(buttonViewFactory.get(activeId, IntelConstants.Source.valueOf(activeTab.BUY.title)));
        renderer.setBottomLeft(intelSelectionFactory.get(activeId, activeTab, intelSourceViewWidth));
        renderer.render(panel);
    }

    @Override
    public String getIcon() {
        return Global.getSettings().getSpriteName("galmart", "board");
    }

    @Override
    public Set<String> getIntelTags(SectorMapAPI map) {
        Set<String> tags = super.getIntelTags(map);
        tags.add("StarHacker");
        return tags;
    }

    @Override
    public IntelSortTier getSortTier() {
        return IntelSortTier.TIER_0;
    }

    @Override
    public boolean hasLargeDescription() {
        return true;
    }

    @Override
    public boolean hasSmallDescription() {
        return false;
    }

    public void setActiveId(String activeId) {
        this.activeId = activeId;
    }

    public void setActiveTab(DataTab activeTab) {
        this.activeTab = activeTab;
    }

    protected Object readResolve() {
        if (activeId == null) {
            activeId = "Analyze";
        }
        if (activeTab == null) {
            activeTab = DataTab.BUY;
        }
        if (buttonViewFactory == null) {
            buttonViewFactory = new ButtonViewFactory();
        }
        if (intelSelectionFactory == null) {
            intelSelectionFactory = new IntelSelectionFactory();
        }
        if (dataViewFactory == null) {
            dataViewFactory = new DataViewFactory(intelSelectionFactory);
        }
        return this;
    }
}
