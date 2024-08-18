package com.united.core.records.commands;

import java.util.UUID;

public record ApproveOrderCommand(
        UUID orderId) {
}