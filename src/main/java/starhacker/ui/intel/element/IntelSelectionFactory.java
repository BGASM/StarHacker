package starhacker.ui.intel.element;

import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import starhacker.ui.intel.IntelTracker;
import starhacker.ui.intel.StarHackerBoard;
import starhacker.ui.intel.StarHackerBoard.DataTab;
import starhacker.ui.ui.Renderable;
import starhacker.ui.ui.Row;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class IntelSelectionFactory {

    private List<IntelInfoPlugin> intel;

    public IntelSelectionFactory() {
        intel = Collections.<IntelInfoPlugin>emptyList();
    }

    public Renderable get(String dataId, DataTab actionTab, float width) {
        int buttonsOnScreen = (int) Math.floor(width / 28f);
        int maxButtons = this.intel.size();
        int numberOfButtons = Math.min(buttonsOnScreen, maxButtons);
        List<Renderable> buttons = new LinkedList<>();
        for (int i = 0; i < numberOfButtons; i++) {
            IntelInfoPlugin intel = this.intel.get(i);
            buttons.add((Renderable) new IntelButton(i + 1, actionTab, dataId, intel));
        }
        return new Row(buttons);
    }

    public void setMarkets(List<IntelInfoPlugin> intel) {
        this.intel = intel;
    }
}
