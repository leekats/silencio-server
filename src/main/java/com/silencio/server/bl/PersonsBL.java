package com.silencio.server.bl;

import com.silencio.server.dal.PersonsDAL;
import com.silencio.server.models.enums.Indication;
import com.silencio.server.models.pojos.Permit;
import com.silencio.server.models.pojos.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.silencio.server.models.enums.Indication.RED;
import static com.silencio.server.models.enums.State.APPROVED;
import static java.util.Objects.isNull;

@Service
public class PersonsBL {
    private final PersonsDAL dal;
    private final S3BL s3;
    private final RekognitionBL rekognition;

    @Autowired
    public PersonsBL(PersonsDAL dal, S3BL s3, RekognitionBL rekognition) {
        this.dal = dal;
        this.s3 = s3;
        this.rekognition = rekognition;
    }

    public Person getPerson(String personId) {
        return dal.getPerson(personId);
    }

    public boolean addPerson(Person person, MultipartFile file) {
        final Optional<Person> maybePerson = Optional.ofNullable(dal.getPerson(person.getPersonId()));
        if (!maybePerson.isPresent()) {
            dal.addPerson(person);
            String fileName = person.getPersonId() + ".jpg";
            if (s3.saveImageToS3(fileName, file)) {
                return rekognition.addFace("silencio-faces", fileName);
            }
        }

        return false;
    }

    public boolean deletePerson(String personId) {
        dal.deletePerson(personId);
        s3.removeImage(personId);
        return rekognition.deleteFace(personId);
    }

    public List<Person> getAllPersons() {
        return dal.getAllPersons();
    }
}