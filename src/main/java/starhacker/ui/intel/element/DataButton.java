package starhacker.ui.intel.element;

import com.fs.starfarer.api.campaign.econ.CommoditySpecAPI;
import com.fs.starfarer.api.util.Misc;
import starhacker.ui.intel.StarHackerBoard;
import starhacker.ui.ui.Callable;
import starhacker.ui.ui.Size;
import starhacker.ui.ui.ToggleButton;

public class DataButton extends ToggleButton {

    public DataButton(final String intel, boolean isOn) {
        super(new Size(200, 24), intel, intel, true, Misc.getHighlightColor(),
                Misc.getGrayColor(), isOn);

        setCallback(new Callable() {

            @Override
            public void callback() {
                StarHackerBoard board = StarHackerBoard.getInstance();
                board.setActiveId(intel);
            }
        });
    }
}
