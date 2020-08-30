package com.silencio.server.bl;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;
import com.silencio.server.dal.repositories.FacesRepository;
import com.silencio.server.models.pojos.Detection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RekognitionBL {

    private FacesRepository faceDB;
    private GateBL gateBL;
    private AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();
    private final String collectionId = "faces";

    @Autowired
    public RekognitionBL(FacesRepository faceDB, GateBL gateBL) {
        this.gateBL = gateBL;
        this.faceDB = faceDB;
    }

    public boolean addFace(String bucket, String photo) {
        Image image = new Image()
                .withS3Object(new S3Object()
                        .withBucket(bucket)
                        .withName(photo));

        IndexFacesRequest indexFacesRequest = new IndexFacesRequest()
                .withImage(image)
                .withQualityFilter(QualityFilter.AUTO)
                .withMaxFaces(1)
                .withCollectionId(collectionId)
                .withExternalImageId(photo)
                .withDetectionAttributes("DEFAULT");

        IndexFacesResult indexFacesResult = rekognitionClient.indexFaces(indexFacesRequest);

        List<FaceRecord> faceRecords = indexFacesResult.getFaceRecords();
        List<UnindexedFace> unindexedFaces = indexFacesResult.getUnindexedFaces();

        if (faceRecords.size() > 0) {
            faceDB.save(faceRecords.get(0).getFace());
        }

        return faceRecords.size() > unindexedFaces.size();
    }

    public SearchFacesByImageResult searchFace(String bucket, String photo) {
        SearchFacesByImageRequest request = new SearchFacesByImageRequest()
                .withCollectionId(collectionId)
                .withImage(new Image().withS3Object(new S3Object().withBucket(bucket).withName(photo)))
                .withMaxFaces(5)
                .withFaceMatchThreshold(85f);

        SearchFacesByImageResult res = rekognitionClient.searchFacesByImage(request);
        final Detection detection = Detection.builder()
                .suspects(res.getFaceMatches())
                .imageUrl(photo)
                .cameraId("default-camera")
                .timestamp(System.currentTimeMillis())
                .build();

        gateBL.newDetection(detection);
        return res;
    }

    public boolean deleteFace(String personId) {
        return Optional.ofNullable(faceDB.findFaceByExternalImageId(personId + ".jpg"))
                .map(face -> {
                    DeleteFacesRequest deleteFacesRequest = new DeleteFacesRequest()
                            .withCollectionId(collectionId)
                            .withFaceIds(face.getFaceId());

                    DeleteFacesResult deleteFacesResult = rekognitionClient.deleteFaces(deleteFacesRequest);
                    List<String> faceRecords = deleteFacesResult.getDeletedFaces();

                    faceDB.deleteFaceByExternalImageId(face.getExternalImageId());
                    return faceRecords.size() > 0;
                })
                .orElse(false);
    }
}