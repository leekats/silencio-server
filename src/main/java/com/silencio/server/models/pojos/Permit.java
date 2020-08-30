package com.silencio.server.models.pojos;

import com.silencio.server.models.enums.Indication;
import com.silencio.server.models.enums.State;
import lombok.Data;

@Data
public class Permit {
    private String id;
    private String personId;
    private String issuerId;
    private String accepterId;
    private long startAccess;
    private long endAccess;
    private Indication indication;
    private String reason;
    private String info;
    private State state;
    private long timestamp;
}