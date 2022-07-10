package org.myorg.module.timemanagement.database.dao;

import org.myorg.module.timemanagement.database.domainobject.DbActivity;
import org.myorg.modules.modules.database.dao.GenericDAOImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActivityDAO extends GenericDAOImpl<DbActivity> {

    public ActivityDAO() {
        super(DbActivity.class);
    }

    public Set<DbActivity> findAllByUserId(long userId) {
        return execNamedQuery(DbActivity.QUERY_FIND_ALL_BY_FK_USER, new HashMap<String, Object>() {{
            put(DbActivity.FIELD_FK_USER, userId);
        }}).collect(Collectors.toSet());
    }

    public Set<DbActivity> findAllByNameAndUserId(String name, long userId) {
        return execNamedQuery(DbActivity.QUERY_FIND_BY_NAME_AND_FK_USER, new HashMap<String, Object>() {{
            put(DbActivity.FIELD_NAME, name);
            put(DbActivity.FIELD_FK_USER, userId);
        }}).collect(Collectors.toSet());
    }

}
