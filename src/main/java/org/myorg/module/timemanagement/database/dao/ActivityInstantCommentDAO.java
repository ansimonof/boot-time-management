package org.myorg.module.timemanagement.database.dao;

import org.myorg.module.timemanagement.database.domainobject.DbActivityInstantComment;
import org.myorg.modules.modules.database.dao.GenericDAOImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActivityInstantCommentDAO extends GenericDAOImpl<DbActivityInstantComment> {

    public ActivityInstantCommentDAO() {
        super(DbActivityInstantComment.class);
    }

    public Set<DbActivityInstantComment> findAllByActivityInstantId(long activityInstantId) {
        return execNamedQuery(DbActivityInstantComment.FIELD_FK_ACTIVITY_INSTANT, new HashMap<String, Object>() {{
            put(DbActivityInstantComment.FIELD_FK_ACTIVITY_INSTANT, activityInstantId);
        }}).collect(Collectors.toSet());
    }
}
