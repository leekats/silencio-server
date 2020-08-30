package com.silencio.server.models.pojos;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Image {
    private byte[] payload;
    private long timestamp;
}
