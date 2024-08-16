package com.unitedairlines.core.records.events;

import java.util.UUID;

public record OrderApprovedEvent(
   UUID orderId) {
}

