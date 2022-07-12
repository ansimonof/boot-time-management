package org.myorg.module.timemanagement.controller;

import org.myorg.module.auth.access.context.UserAuthenticatedContext;
import org.myorg.module.auth.exception.AuthExceptionBuilder;
import org.myorg.module.core.access.AccessPermission;
import org.myorg.module.core.access.context.source.CoreUserSource;
import org.myorg.module.core.access.privilege.AccessOp;
import org.myorg.module.timemanagement.TimeManagementModuleConsts;
import org.myorg.module.timemanagement.database.domainobject.DbActivity;
import org.myorg.module.timemanagement.database.service.activity.ActivityBuilder;
import org.myorg.module.timemanagement.database.service.activity.ActivityDto;
import org.myorg.module.timemanagement.database.service.activity.ActivityService;
import org.myorg.module.timemanagement.database.service.activityinstant.ActivityInstantDto;
import org.myorg.module.timemanagement.exception.TimeManagementExceptionBuilder;
import org.myorg.module.timemanagement.privilege.ActivityPrivilege;
import org.myorg.modules.access.context.Context;
import org.myorg.modules.modules.exception.ModuleException;
import org.myorg.modules.modules.exception.ModuleExceptionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(TimeManagementModuleConsts.REST_CONTROLLER_PREFIX + "/activity")
public class ActivityController {

    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/list")
    @AccessPermission(
            context = UserAuthenticatedContext.class,
            privilege = ActivityPrivilege.class,
            ops = { AccessOp.READ }
    )
    public ResponseEntity<Set<ActivityDto>> list(
            final Context<?> context
    ) throws ModuleException {
        if (!(context.getSource() instanceof CoreUserSource)) {
            throw AuthExceptionBuilder.buildInvalidRequestSourceException(
                    context.getSource().getClass(), CoreUserSource.class);
        }
        CoreUserSource source = (CoreUserSource) context.getSource();

        Set<ActivityDto> activities = activityService.findAllByUserId(source.getId());
        return ResponseEntity.ok(activities);
    }

    @PostMapping("/")
    @AccessPermission(
            context = UserAuthenticatedContext.class,
            privilege = ActivityPrivilege.class,
            ops = { AccessOp.WRITE }
    )
    public ResponseEntity<ActivityDto> create(
            final Context<?> context,
            @RequestParam("name") final String name
    ) throws ModuleException {
        if (!(context.getSource() instanceof CoreUserSource)) {
            throw AuthExceptionBuilder.buildInvalidRequestSourceException(
                    context.getSource().getClass(), CoreUserSource.class);
        }
        CoreUserSource source = (CoreUserSource) context.getSource();
        ActivityDto activityDto = activityService.create(
                ActivityBuilder.builder().name(name).userId(source.getId()),
                context);
        return ResponseEntity.ok(activityDto);
    }

    @PostMapping("/set-time")
    @AccessPermission(
            context = UserAuthenticatedContext.class,
            privilege = ActivityPrivilege.class,
            ops = { AccessOp.WRITE }
    )
    public ResponseEntity<ActivityInstantDto> setTime(
            final Context<?> context,
            @RequestParam("activity-id") final long activityId,
            @RequestParam("time-ms") final long timeMs
    ) throws ModuleException {
        if (!(context.getSource() instanceof CoreUserSource)) {
            throw AuthExceptionBuilder.buildInvalidRequestSourceException(
                    context.getSource().getClass(), CoreUserSource.class);
        }
        CoreUserSource source = (CoreUserSource) context.getSource();
        checkActivityExistence(activityId);
        checkIfActivityBelongsToUser(activityId, source.getId());

        return ResponseEntity.ok(activityService.setTime(activityId, new Date(timeMs)));
    }

    @GetMapping("/instant-list")
    @AccessPermission(
            context = UserAuthenticatedContext.class,
            privilege = ActivityPrivilege.class,
            ops = { AccessOp.READ }
    )
    public ResponseEntity<List<ActivityInstantDto>> listInstants(
            final Context<?> context,
            @RequestParam("activity-id") final long activityId
    ) throws ModuleException {
        if (!(context.getSource() instanceof CoreUserSource)) {
            throw AuthExceptionBuilder.buildInvalidRequestSourceException(
                    context.getSource().getClass(), CoreUserSource.class);
        }
        CoreUserSource source = (CoreUserSource) context.getSource();
        checkActivityExistence(activityId);
        checkIfActivityBelongsToUser(activityId, source.getId());

        return ResponseEntity.ok(activityService.findAllInstants(activityId).stream()
                .sorted(Comparator.comparing(ActivityInstantDto::getStartDatetime))
                .collect(Collectors.toList()));
    }

    private void checkActivityExistence(long activityId) throws ModuleException {
        if (activityService.findById(activityId) == null) {
            throw ModuleExceptionBuilder.buildNotFoundDomainObjectException(DbActivity.class, activityId);
        }
    }

    private void checkIfActivityBelongsToUser(long activityId, long userId) throws ModuleException {
        boolean activityBelongsToUser = activityService.findAllByUserId(userId).stream()
                .anyMatch(activityDto -> activityDto.getId() == activityId);
        if (!activityBelongsToUser) {
            throw TimeManagementExceptionBuilder.buildActivityDoesNotBelongToUser(activityId, userId);
        }
    }
}
