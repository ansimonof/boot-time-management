package org.myorg.module.timemanagement.database.service.activityinstant;

import org.myorg.modules.utils.DomainObjectBuilder;

import java.util.Date;

public class ActivityInstantBuilder extends DomainObjectBuilder {

    private BuilderField<Date> startDatetime = new BuilderField<>();
    private BuilderField<Date> endDatetime = new BuilderField<>();
    private BuilderField<Long> activityId = new BuilderField<>();

    public static ActivityInstantBuilder builder() {
        return new ActivityInstantBuilder();
    }

    public ActivityInstantBuilder startDatetime(Date startDatetime) {
        this.startDatetime.setValue(startDatetime);
        return this;
    }

    public ActivityInstantBuilder endDatetime(Date endDatetime) {
        this.endDatetime.setValue(endDatetime);
        return this;
    }

    public ActivityInstantBuilder activityId(Long activityId) {
        this.activityId.setValue(activityId);
        return this;
    }

    //--------------------------

    public Date getStartDatetime() {
        return startDatetime.getValue();
    }

    public Date getEndDatetime() {
        return endDatetime.getValue();
    }

    public Long getActivityId() {
        return activityId.getValue();
    }

    //---------------------------

    public boolean isContainStartDatetime() {
        return startDatetime.isContain();
    }

    public boolean isContainEndDatetime() {
        return endDatetime.isContain();
    }

    public boolean isContainActivityId() {
        return activityId.isContain();
    }

}
