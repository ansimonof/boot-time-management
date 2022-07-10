package org.myorg.module.timemanagement.database.service.activityinstantcomment;

import org.myorg.modules.utils.DomainObjectBuilder;

public class ActivityInstantCommentBuilder extends DomainObjectBuilder {

    private BuilderField<String> text = new BuilderField<>();
    private BuilderField<Long> activityInstantId = new BuilderField<>();

    public static ActivityInstantCommentBuilder builder() {
        return new ActivityInstantCommentBuilder();
    }

    public ActivityInstantCommentBuilder text(String text) {
        this.text.setValue(text);
        return this;
    }

    public ActivityInstantCommentBuilder activityInstantId(Long activityInstantId) {
        this.activityInstantId.setValue(activityInstantId);
        return this;
    }

    //-----------

    public String getText() {
        return text.getValue();
    }

    public Long getActivityInstantId() {
        return activityInstantId.getValue();
    }

    //-----------

    public boolean isContainText() {
        return text.isContain();
    }

    public boolean isContainActivityInstantId() {
        return activityInstantId.isContain();
    }
}
