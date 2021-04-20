package starhacker.ui.ui;

import com.fs.starfarer.api.ui.CustomPanelAPI;

import java.util.List;

public class Row extends Group {

    public Row(List<Renderable> elements) {
        super(elements);
    }

    @Override
    public void render(CustomPanelAPI panel, float x, float y) {
        for (Renderable renderable : getElements()) {
            Size size = renderable.getSize();
            renderable.render(panel, x, y);
            x += size.getWidth();
        }
    }
}
