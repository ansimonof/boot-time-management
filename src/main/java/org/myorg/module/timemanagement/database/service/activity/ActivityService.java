package org.myorg.module.timemanagement.database.service.activity;

import org.apache.commons.lang3.StringUtils;
import org.myorg.module.core.database.dao.UserDAO;
import org.myorg.module.timemanagement.database.dao.ActivityDAO;
import org.myorg.module.timemanagement.database.dao.ActivityInstantDAO;
import org.myorg.module.timemanagement.database.domainobject.DbActivity;
import org.myorg.module.timemanagement.database.domainobject.DbActivityInstant;
import org.myorg.module.timemanagement.database.service.activityinstant.ActivityInstantDto;
import org.myorg.module.timemanagement.exception.TimeManagementExceptionBuilder;
import org.myorg.modules.access.context.Context;
import org.myorg.modules.modules.database.service.DomainObjectService;
import org.myorg.modules.modules.exception.ModuleException;
import org.myorg.modules.modules.exception.ModuleExceptionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActivityService extends DomainObjectService<DbActivity, ActivityDAO, ActivityBuilder, ActivityDto> {

    private final ActivityInstantDAO activityInstantDAO;
    private final UserDAO userDAO;

    @Autowired
    public ActivityService(ActivityDAO activityDAO, ActivityInstantDAO activityInstantDAO, UserDAO userDAO) {
        super(activityDAO, ActivityDto::from);
        this.activityInstantDAO = activityInstantDAO;
        this.userDAO = userDAO;
    }

    @Override
    public ActivityDto create(ActivityBuilder builder, Context<?> context) throws ModuleException {
        if (!builder.isContainName() || StringUtils.isEmpty(builder.getName())) {
            throw ModuleExceptionBuilder.buildEmptyValueException(DbActivity.FIELD_NAME);
        }

        if (!builder.isContainUserId() || builder.getUserId() == null) {
            throw ModuleExceptionBuilder.buildEmptyValueException(DbActivity.FIELD_FK_USER);
        }

        DbActivity dbActivity = new DbActivity();
        setFields(dbActivity, builder);

        return dtoBuilder.apply(dao.makePersistent(dbActivity));
    }

    @Override
    public ActivityDto update(long id, ActivityBuilder builder, Context<?> context) throws ModuleException {
        DbActivity dbActivity = dao.checkExistenceAndGet(id);

        if (builder.isContainName() && StringUtils.isEmpty(builder.getName())) {
            throw ModuleExceptionBuilder.buildEmptyValueException(DbActivity.FIELD_NAME);
        }

        if (builder.isContainUserId() && builder.getUserId() == null) {
            throw ModuleExceptionBuilder.buildEmptyValueException(DbActivity.FIELD_FK_USER);
        }

        setFields(dbActivity, builder);

        return dtoBuilder.apply(dao.makePersistent(dbActivity));
    }

    public Set<ActivityDto> findAllByUserId(long userId) {
        return dao.findAllByUserId(userId).stream()
                .map(dtoBuilder)
                .collect(Collectors.toSet());
    }

    public Set<ActivityInstantDto> findAllInstants(long activityId) {
        return activityInstantDAO.findAllByActivityId(activityId).stream()
                .map(ActivityInstantDto::from)
                .collect(Collectors.toSet());
    }

    public ActivityInstantDto setTime(long activityId, @NotNull Date date) throws ModuleException {
        List<DbActivityInstant> instantsSorted = activityInstantDAO.findAllByActivityId(activityId).stream()
                .sorted(Comparator.comparing(DbActivityInstant::getStartDatetime))
                .collect(Collectors.toList());

        checkDate(instantsSorted, date);

        DbActivityInstant dbActivityInstant;
        if (instantsSorted.isEmpty()) {
            dbActivityInstant = new DbActivityInstant();
            dbActivityInstant.setStartDatetime(date);
        } else {
            DbActivityInstant lastInstant = instantsSorted.get(instantsSorted.size() - 1);
            if (lastInstant.getEndDatetime() == null) {
                dbActivityInstant = lastInstant;
                if (lastInstant.getStartDatetime().compareTo(date) > 0)
                dbActivityInstant.setEndDatetime(date);
            } else {
                dbActivityInstant = new DbActivityInstant();
                dbActivityInstant.setStartDatetime(date);
            }
        }

        return ActivityInstantDto.from(activityInstantDAO.makePersistent(dbActivityInstant));
    }

    private void checkDate(List<DbActivityInstant> instantsSorted, Date date) throws ModuleException {
        Instant dateInstant = date.toInstant();
        for (DbActivityInstant dbActivityInstant : instantsSorted) {
            if (dbActivityInstant.getEndDatetime() == null) {
                continue;
            }
            Instant startDatetime = dbActivityInstant.getStartDatetime().toInstant();
            Instant endDatetime = dbActivityInstant.getEndDatetime().toInstant();
            if (!dateInstant.isBefore(startDatetime) && !dateInstant.isAfter(endDatetime)) {
                throw TimeManagementExceptionBuilder.buildTimeBelongsToAnotherActivityInstant(dbActivityInstant.getId());
            }
        }

    }

    private void setFields(DbActivity dbActivity,
                           ActivityBuilder builder) throws ModuleException {
        if (builder.isContainName()) {
            dbActivity.setName(builder.getName());
        }

        if (builder.isContainUserId()) {
            dbActivity.setUser(userDAO.checkExistenceAndGet(builder.getUserId()));
        }
    }

}
