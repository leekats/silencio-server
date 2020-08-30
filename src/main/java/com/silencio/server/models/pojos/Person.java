package com.silencio.server.models.pojos;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "persons")
public class Person {
    private ObjectId _id;
    @NonNull
    private String personId;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String companyId;
    @NonNull
    private String phone;
    private List<Permit> permits;
}