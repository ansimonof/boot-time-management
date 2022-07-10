package org.myorg.module.timemanagement.database.service.activityinstant;

import org.myorg.module.timemanagement.database.dao.ActivityDAO;
import org.myorg.module.timemanagement.database.dao.ActivityInstantDAO;
import org.myorg.module.timemanagement.database.domainobject.DbActivityInstant;
import org.myorg.modules.access.context.Context;
import org.myorg.modules.modules.database.service.DomainObjectService;
import org.myorg.modules.modules.exception.ModuleException;
import org.myorg.modules.modules.exception.ModuleExceptionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityInstantService extends DomainObjectService<
        DbActivityInstant,
        ActivityInstantDAO,
        ActivityInstantBuilder,
        ActivityInstantDto> {

    private final ActivityDAO activityDAO;

    @Autowired
    public ActivityInstantService(ActivityInstantDAO activityInstantDAO, ActivityDAO activityDAO) {
        super(activityInstantDAO, ActivityInstantDto::from);
        this.activityDAO = activityDAO;
    }

    @Override
    public ActivityInstantDto create(ActivityInstantBuilder builder, Context<?> context) throws ModuleException {
        if (!builder.isContainStartDatetime() || builder.getStartDatetime() == null) {
            throw ModuleExceptionBuilder.buildEmptyValueException(
                    DbActivityInstant.class, DbActivityInstant.FIELD_START_DATETIME);
        }

        if (!builder.isContainEndDatetime() || builder.getEndDatetime() == null) {
            throw ModuleExceptionBuilder.buildEmptyValueException(
                    DbActivityInstant.class, DbActivityInstant.FIELD_END_DATETIME);
        }

        if (!builder.isContainActivityId() || builder.getActivityId() == null) {
            throw ModuleExceptionBuilder.buildEmptyValueException(
                    DbActivityInstant.class, DbActivityInstant.FIELD_FK_ACTIVITY);
        }

        DbActivityInstant dbActivityInstant = new DbActivityInstant();
        setFields(dbActivityInstant, builder);

        return dtoBuilder.apply(dao.makePersistent(dbActivityInstant));
    }

    @Override
    public ActivityInstantDto update(long id, ActivityInstantBuilder builder, Context<?> context) throws ModuleException {
        DbActivityInstant dbActivityInstant = dao.checkExistenceAndGet(id);

        if (builder.isContainStartDatetime() && builder.getStartDatetime() == null) {
            throw ModuleExceptionBuilder.buildEmptyValueException(
                    DbActivityInstant.class, DbActivityInstant.FIELD_START_DATETIME);
        }

        if (builder.isContainEndDatetime() && builder.getEndDatetime() == null) {
            throw ModuleExceptionBuilder.buildEmptyValueException(
                    DbActivityInstant.class, DbActivityInstant.FIELD_END_DATETIME);
        }

        if (builder.isContainActivityId() && builder.getActivityId() == null) {
            throw ModuleExceptionBuilder.buildEmptyValueException(
                    DbActivityInstant.class, DbActivityInstant.FIELD_FK_ACTIVITY);
        }

        setFields(dbActivityInstant, builder);

        return dtoBuilder.apply(dao.makePersistent(dbActivityInstant));
    }

    private void setFields(
            DbActivityInstant dbActivityInstant,
            ActivityInstantBuilder builder
    ) throws ModuleException {
        if (builder.isContainStartDatetime()) {
            dbActivityInstant.setStartDatetime(builder.getStartDatetime());
        }

        if (builder.isContainEndDatetime()) {
            dbActivityInstant.setEndDatetime(builder.getEndDatetime());
        }

        if (builder.isContainActivityId()) {
            dbActivityInstant.setActivity(activityDAO.checkExistenceAndGet(builder.getActivityId()));
        }
    }
}
