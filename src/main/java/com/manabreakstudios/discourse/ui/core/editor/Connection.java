package com.manabreakstudios.discourse.ui.core.editor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class Connection {

    @Getter
    private final SlotBinding from;
    @Getter
    private final SlotBinding to;
}
