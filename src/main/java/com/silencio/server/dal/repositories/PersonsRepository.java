package com.silencio.server.dal.repositories;

import com.silencio.server.models.pojos.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonsRepository extends MongoRepository<Person, String> {
    Person findPersonByPersonId(String personId);
    void deletePersonByPersonId(String personId);
}
