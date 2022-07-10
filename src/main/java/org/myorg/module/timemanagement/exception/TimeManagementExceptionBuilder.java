package org.myorg.module.timemanagement.exception;

import org.myorg.modules.modules.exception.ModuleException;

import java.util.HashMap;

public class TimeManagementExceptionBuilder {

    public static ModuleException buildTimeBelongsToAnotherActivityInstant(long activityInstantId) {
        return new ModuleException("time_belongs_to_another_activity_instant", new HashMap<String, Object>() {{
            put("activity_instant_id", activityInstantId);
        }});
    }

    public static ModuleException buildActivityDoesNotBelongToUser(long activityId, long userId) {
        return new ModuleException("activity_does_not_belong_to_user", new HashMap<String, Object>() {{
            put("activity_id", activityId);
            put("user_id", userId);
        }});
    }
}
