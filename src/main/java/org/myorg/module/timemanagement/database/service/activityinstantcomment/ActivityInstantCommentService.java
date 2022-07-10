package org.myorg.module.timemanagement.database.service.activityinstantcomment;

import org.apache.commons.lang3.StringUtils;
import org.myorg.module.timemanagement.database.dao.ActivityInstantCommentDAO;
import org.myorg.module.timemanagement.database.dao.ActivityInstantDAO;
import org.myorg.module.timemanagement.database.domainobject.DbActivityInstantComment;
import org.myorg.modules.access.context.Context;
import org.myorg.modules.modules.database.service.DomainObjectService;
import org.myorg.modules.modules.exception.ModuleException;
import org.myorg.modules.modules.exception.ModuleExceptionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActivityInstantCommentService extends
        DomainObjectService<
                DbActivityInstantComment,
                ActivityInstantCommentDAO,
                ActivityInstantCommentBuilder,
                ActivityInstantCommentDto> {

    private final ActivityInstantDAO activityInstantDAO;

    @Autowired
    public ActivityInstantCommentService(ActivityInstantCommentDAO commentDAO, ActivityInstantDAO activityInstantDAO) {
        super(commentDAO, ActivityInstantCommentDto::from);
        this.activityInstantDAO = activityInstantDAO;
    }

    public Set<ActivityInstantCommentDto> findAllByActivityInstantId(long activityInstantId) {
        return dao.execNamedQuery(
                DbActivityInstantComment.QUERY_FIND_ALL_BY_FK_ACTIVITY_INSTANT, new HashMap<String, Object>() {{
                    put(DbActivityInstantComment.FIELD_FK_ACTIVITY_INSTANT, activityInstantId);
                }})
                .map(dtoBuilder)
                .collect(Collectors.toSet());
    }

    @Override
    public ActivityInstantCommentDto create(ActivityInstantCommentBuilder builder,
                                            Context<?> context) throws ModuleException {
        if (!builder.isContainText() || StringUtils.isEmpty(builder.getText())) {
            throw ModuleExceptionBuilder.buildEmptyValueException(
                    DbActivityInstantComment.class, DbActivityInstantComment.FIELD_TEXT);
        }

        if (!builder.isContainActivityInstantId() || builder.getActivityInstantId() == null) {
            throw ModuleExceptionBuilder.buildEmptyValueException(
                    DbActivityInstantComment.class, DbActivityInstantComment.FIELD_FK_ACTIVITY_INSTANT);
        }

        DbActivityInstantComment dbComment = new DbActivityInstantComment();
        setFields(dbComment, builder);

        return dtoBuilder.apply(dao.makePersistent(dbComment));
    }

    @Override
    public ActivityInstantCommentDto update(long id,
                                            ActivityInstantCommentBuilder builder,
                                            Context<?> context) throws ModuleException {
        DbActivityInstantComment dbComment = dao.checkExistenceAndGet(id);

        if (builder.isContainText() && StringUtils.isEmpty(builder.getText())) {
            throw ModuleExceptionBuilder.buildEmptyValueException(
                    DbActivityInstantComment.class, DbActivityInstantComment.FIELD_TEXT);
        }

        if (builder.isContainActivityInstantId() && builder.getActivityInstantId() == null) {
            throw ModuleExceptionBuilder.buildEmptyValueException(
                    DbActivityInstantComment.class, DbActivityInstantComment.FIELD_FK_ACTIVITY_INSTANT);
        }

        setFields(dbComment, builder);

        return dtoBuilder.apply(dao.makePersistent(dbComment));
    }

    private void setFields(
            DbActivityInstantComment dbComment,
            ActivityInstantCommentBuilder builder
    ) throws ModuleException {
        if (builder.isContainText()) {
            dbComment.setText(builder.getText());
        }

        if (builder.isContainActivityInstantId()) {
            dbComment.setActivityInstant(activityInstantDAO.checkExistenceAndGet(builder.getActivityInstantId()));
        }
    }

}
