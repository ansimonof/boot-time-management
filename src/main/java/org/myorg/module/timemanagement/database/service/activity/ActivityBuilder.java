package org.myorg.module.timemanagement.database.service.activity;

import org.myorg.modules.utils.DomainObjectBuilder;

public class ActivityBuilder extends DomainObjectBuilder {

    private BuilderField<String> name = new BuilderField<>();
    private BuilderField<Long> userId = new BuilderField<>();

    public static ActivityBuilder builder() {
        return new ActivityBuilder();
    }

    public ActivityBuilder name(String name) {
        this.name.setValue(name);
        return this;
    }

    public ActivityBuilder userId(Long userId) {
        this.userId.setValue(userId);
        return this;
    }

    //---------------

    public String getName() {
        return name.getValue();
    }

    public Long getUserId() {
        return userId.getValue();
    }

    //----------------

    public boolean isContainName() {
        return name.isContain();
    }

    public boolean isContainUserId() {
        return userId.isContain();
    }
}
