package ui.editor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Slot {
    public enum Direction {
        INPUT, OUTPUT
    }

    @Getter
    private final NodeUI node;

    @Getter
    private final Direction direction;

    @Getter
    private final int index;

    @Getter
    private final int position;
}
