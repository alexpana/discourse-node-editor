package com.manabreakstudios.discourse.ui.core.editor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class SlotBinding {
    @Getter
    private final NodeUI node;
    @Getter
    private final Slot slot;
}
