package com.silencio.server.models.pojos;

import com.silencio.server.models.enums.State;
import lombok.Data;

@Data
public class PermitUpdateRequest {
    String personId;
    String permitId;
    String accepterId;
    State state;
}
