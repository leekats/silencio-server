package com.silencio.server.controller;

import com.silencio.server.bl.PermitsBL;
import com.silencio.server.models.pojos.Permit;
import com.silencio.server.models.pojos.PermitUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/permits")
@RestController
public class PermitsController {
    private final PermitsBL bl;

    @Autowired
    public PermitsController(PermitsBL bl) {
        this.bl = bl;
    }

    @GetMapping(path = "/{issuerId}")
    public List<Permit> getIssuersPermits(@PathVariable("issuerId") String issuerId) {
        return bl.getIssuersPermits(issuerId);
    }

    @GetMapping()
    public List<Permit> getAllPermits() {
        return bl.getAllPermits();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public boolean addPermit(@Valid @RequestBody Permit permit) {
        return bl.addPermit(permit);
    }

    @DeleteMapping(path ="/{personId}/{id}")
    public boolean deletePermit(@PathVariable("personId") String personId, @PathVariable("id") String id) {
        return bl.deletePermit(personId, id);
    }

    @PostMapping(path = "/state")
    public boolean changePermitState(@RequestBody PermitUpdateRequest pru) {
        return bl.changePermitState(pru);
    }
}