package com.silencio.server.controller;

import com.amazonaws.services.rekognition.model.SearchFacesByImageResult;
import com.silencio.server.bl.RekognitionBL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/faces")
@RestController
public class AWSController {
    @Value("{s3.addface:silencio-faces}")
    private String addFaceBucket;

    @Value("{s3.searchface:silencio-persons}")
    private String searchFaceBucket;

    private final RekognitionBL connection;

    @Autowired
    public AWSController(RekognitionBL connection) {
        this.connection = connection;
    }

    @PostMapping(path = "/add", consumes = "application/json")
    public boolean addFace(@RequestBody String image) {
        return connection.addFace(addFaceBucket, image);
    }

    @PostMapping(path = "/search", consumes = "application/json")
    public SearchFacesByImageResult searchFace(@RequestBody String image) {
        System.out.println(image);
        return connection.searchFace(searchFaceBucket, image);
    }
}