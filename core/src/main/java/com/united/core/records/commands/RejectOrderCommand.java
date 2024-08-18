package com.united.core.records.commands;

import java.util.UUID;

public record RejectOrderCommand(
        UUID orderId) {

    public RejectOrderCommand() {
        this(null);
    }
}
