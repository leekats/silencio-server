package com.silencio.server.dal.repositories;

import com.amazonaws.services.rekognition.model.Face;
import com.silencio.server.models.pojos.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FacesRepository extends MongoRepository<Face, String> {
    Face findFaceByExternalImageId(String personId);
    Face deleteFaceByExternalImageId(String personId);
}
