package com.silencio.server.controller;

import com.silencio.server.bl.PersonsBL;
import com.silencio.server.models.pojos.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/persons")
@RestController
public class PersonController {
    private final PersonsBL bl;

    @Autowired
    public PersonController(PersonsBL bl) {
        this.bl = bl;
    }

    @GetMapping(path = "/{personId}")
    public Person getPerson(@PathVariable("personId") String personId) {
        return bl.getPerson(personId);
    }

    @GetMapping
    public List<Person> getAllPersons() {
        return bl.getAllPersons();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public boolean addPerson(@RequestPart MultipartFile file, @RequestPart String firstName,
                             @RequestPart String lastName, @RequestPart String companyId,
                             @RequestPart String phone, @RequestPart String personId) {
        final Person person = Person.builder()
                .personId(personId)
                .firstName(firstName)
                .lastName(lastName)
                .companyId(companyId)
                .phone(phone)
                .build();

        return bl.addPerson(person, file);
    }

    @DeleteMapping(path = "/{personId}")
    public boolean deletePerson(@PathVariable("personId") String personId) {
        return bl.deletePerson(personId);
    }
}