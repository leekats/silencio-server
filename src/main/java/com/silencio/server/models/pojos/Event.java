package com.silencio.server.models.pojos;

import com.silencio.server.models.enums.Indication;
import com.silencio.server.models.pojos.Detection;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@SuperBuilder
@NoArgsConstructor
@Document(collection = "events")
public class Event extends Detection {
    private ObjectId _id;
    private Indication indication;
    private String personId;
}
