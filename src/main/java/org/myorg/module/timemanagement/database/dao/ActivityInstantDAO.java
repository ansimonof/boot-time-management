package org.myorg.module.timemanagement.database.dao;

import org.myorg.module.timemanagement.database.domainobject.DbActivityInstant;
import org.myorg.modules.modules.database.dao.GenericDAOImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActivityInstantDAO extends GenericDAOImpl<DbActivityInstant> {

    public ActivityInstantDAO() {
        super(DbActivityInstant.class);
    }

    public Set<DbActivityInstant> findAllByActivityId(long activityId) {
        return execNamedQuery(DbActivityInstant.QUERY_FIND_ALL_BY_FK_ACTIVITY, new HashMap<String, Object>() {{
            put(DbActivityInstant.FIELD_FK_ACTIVITY, activityId);
        }}).collect(Collectors.toSet());
    }
}
