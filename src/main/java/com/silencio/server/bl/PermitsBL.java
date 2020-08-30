package com.silencio.server.bl;

import com.silencio.server.dal.PersonsDAL;
import com.silencio.server.models.pojos.Permit;
import com.silencio.server.models.pojos.PermitUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.silencio.server.models.enums.Indication.GREEN;
import static com.silencio.server.models.enums.State.OPEN;

@Service
public class PermitsBL {
    private final PersonsDAL dal;

    @Autowired
    public PermitsBL(PersonsDAL dal) {
        this.dal = dal;
    }

    public boolean addPermit(Permit permit) {
        permit.setId(UUID.randomUUID().toString());
        permit.setIndication(GREEN);
        permit.setState(OPEN);
        permit.setTimestamp(System.currentTimeMillis());
        return dal.addPermit(permit);
    }

    public boolean deletePermit(String personId, String id) {
        return dal.deletePermit(personId, id);
    }

    public List<Permit> getIssuersPermits(String issuerId) {
        return getAllPermits().stream()
                .filter(p->p.getIssuerId().equals(issuerId))
                .collect(Collectors.toList());
    }

    public boolean changePermitState(PermitUpdateRequest pru) {
        return dal.savePermit(pru.getPersonId(), pru.getPermitId(), pru.getState(), pru.getAccepterId());
    }

    public List<Permit> getAllPermits() {
        return dal.getAllPermits();
    }
}