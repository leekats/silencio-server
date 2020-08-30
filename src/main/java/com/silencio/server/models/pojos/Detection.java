package com.silencio.server.models.pojos;

import com.amazonaws.services.rekognition.model.FaceMatch;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
public class Detection {
    private List<FaceMatch> suspects;
    private String imageUrl;
    private long timestamp;
    private String cameraId;
}