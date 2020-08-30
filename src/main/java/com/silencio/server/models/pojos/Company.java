package com.silencio.server.models.pojos;

import lombok.Data;

@Data
public class Company {
    private String companyId;
    private String name;
    private int managerId;
}