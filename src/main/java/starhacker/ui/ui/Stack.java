package starhacker.ui.ui;

import com.fs.starfarer.api.ui.CustomPanelAPI;

import java.util.Arrays;
import java.util.List;

public class Stack extends Group {

    public Stack(Renderable... elements) {
        super(Arrays.asList(elements));
    }

    public Stack(List<Renderable> elements) {
        super(elements);
    }

    @Override
    public void render(CustomPanelAPI panel, float x, float y) {
        for (Renderable renderable : getElements()) {
            Size size = renderable.getSize();
            renderable.render(panel, x, y);
            y += size.getHeigth();
        }
    }
}
