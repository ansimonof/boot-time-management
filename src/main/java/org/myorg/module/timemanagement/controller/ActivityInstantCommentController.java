package org.myorg.module.timemanagement.controller;

import org.myorg.module.auth.access.context.UserAuthenticatedContext;
import org.myorg.module.auth.exception.AuthExceptionBuilder;
import org.myorg.module.core.access.AccessPermission;
import org.myorg.module.core.access.context.source.CoreUserSource;
import org.myorg.module.core.access.privilege.AccessOp;
import org.myorg.module.timemanagement.TimeManagementModuleConsts;
import org.myorg.module.timemanagement.database.domainobject.DbActivity;
import org.myorg.module.timemanagement.database.domainobject.DbActivityInstant;
import org.myorg.module.timemanagement.database.domainobject.DbActivityInstantComment;
import org.myorg.module.timemanagement.database.service.activity.ActivityService;
import org.myorg.module.timemanagement.database.service.activityinstant.ActivityInstantDto;
import org.myorg.module.timemanagement.database.service.activityinstant.ActivityInstantService;
import org.myorg.module.timemanagement.database.service.activityinstantcomment.ActivityInstantCommentBuilder;
import org.myorg.module.timemanagement.database.service.activityinstantcomment.ActivityInstantCommentDto;
import org.myorg.module.timemanagement.database.service.activityinstantcomment.ActivityInstantCommentService;
import org.myorg.module.timemanagement.exception.TimeManagementExceptionBuilder;
import org.myorg.module.timemanagement.privilege.ActivityPrivilege;
import org.myorg.modules.access.context.Context;
import org.myorg.modules.modules.exception.ModuleException;
import org.myorg.modules.modules.exception.ModuleExceptionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(TimeManagementModuleConsts.REST_CONTROLLER_PREFIX + "/activity-instant/comment")
public class ActivityInstantCommentController {

    private final ActivityService activityService;
    private final ActivityInstantService activityInstantService;
    private final ActivityInstantCommentService commentService;

    @Autowired
    public ActivityInstantCommentController(ActivityService activityService,
                                            ActivityInstantService activityInstantService,
                                            ActivityInstantCommentService commentService) {
        this.activityService = activityService;
        this.activityInstantService = activityInstantService;
        this.commentService = commentService;
    }

    @PostMapping("/list")
    @AccessPermission(
            context = UserAuthenticatedContext.class,
            privilege = ActivityPrivilege.class,
            ops = { AccessOp.READ }
    )
    public ResponseEntity<List<ActivityInstantCommentDto>> list(
            final Context<?> context,
            @RequestParam("activity-instant-id") final long activityInstantId
    ) throws ModuleException {
        if (!(context.getSource() instanceof CoreUserSource)) {
            throw AuthExceptionBuilder.buildInvalidRequestSourceException(
                    context.getSource().getClass(), CoreUserSource.class);
        }
        CoreUserSource source = (CoreUserSource) context.getSource();

        ActivityInstantDto activityInstantDto = checkActivityInstantExistenceAndGet(activityInstantId);
        checkIfActivityBelongsToUser(activityInstantDto.getActivityId(), source.getId());

        List<ActivityInstantCommentDto> commentDtos =
                new ArrayList<>(commentService.findAllByActivityInstantId(activityInstantId));
        return ResponseEntity.ok(commentDtos);
    }

    @PostMapping("/create")
    @AccessPermission(
            context = UserAuthenticatedContext.class,
            privilege = ActivityPrivilege.class,
            ops = { AccessOp.WRITE }
    )
    public ResponseEntity<ActivityInstantCommentDto> create(
            final Context<?> context,
            @RequestParam("text") final String text,
            @RequestParam("activity-instant-id") final long activityInstantId
    ) throws ModuleException {
        if (!(context.getSource() instanceof CoreUserSource)) {
            throw AuthExceptionBuilder.buildInvalidRequestSourceException(
                    context.getSource().getClass(), CoreUserSource.class);
        }
        CoreUserSource source = (CoreUserSource) context.getSource();

        ActivityInstantDto activityInstantDto = checkActivityInstantExistenceAndGet(activityInstantId);
        checkActivityExistence(activityInstantDto.getActivityId());
        checkIfActivityBelongsToUser(activityInstantDto.getActivityId(), source.getId());

        ActivityInstantCommentDto commentDto = commentService.create(
                ActivityInstantCommentBuilder.builder().text(text).activityInstantId(activityInstantId),
                context);

        return ResponseEntity.ok(commentDto);
    }

    @DeleteMapping("/remove")
    @AccessPermission(
            context = UserAuthenticatedContext.class,
            privilege = ActivityPrivilege.class,
            ops = { AccessOp.DELETE }
    )
    public ResponseEntity<Long> remove(
            final Context<?> context,
            @RequestParam("comment-id") final long commentId
    ) throws ModuleException {
        if (!(context.getSource() instanceof CoreUserSource)) {
            throw AuthExceptionBuilder.buildInvalidRequestSourceException(
                    context.getSource().getClass(), CoreUserSource.class);
        }
        CoreUserSource source = (CoreUserSource) context.getSource();

        ActivityInstantCommentDto commentDto = checkCommentExistenceAndGet(commentId);
        ActivityInstantDto activityInstantDto = checkActivityInstantExistenceAndGet(commentDto.getActivityInstantId());
        checkActivityExistence(activityInstantDto.getActivityId());
        checkIfActivityBelongsToUser(activityInstantDto.getActivityId(), source.getId());

        commentService.remove(commentId);

        return ResponseEntity.ok(commentId);
    }

    private ActivityInstantCommentDto checkCommentExistenceAndGet(long commendId) throws ModuleException {
        ActivityInstantCommentDto commentDto = commentService.findById(commendId);
        if (commentDto == null) {
            throw ModuleExceptionBuilder.buildNotFoundDomainObjectException(DbActivityInstantComment.class, commendId);
        }

        return commentDto;
    }

    private ActivityInstantDto checkActivityInstantExistenceAndGet(long activityInstantId) throws ModuleException {
        ActivityInstantDto activityInstantDto = activityInstantService.findById(activityInstantId);
        if (activityInstantDto == null) {
            throw ModuleExceptionBuilder.buildNotFoundDomainObjectException(DbActivityInstant.class, activityInstantId);
        }

        return activityInstantDto;
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
