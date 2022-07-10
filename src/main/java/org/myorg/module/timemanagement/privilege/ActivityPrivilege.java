package org.myorg.module.timemanagement.privilege;

import org.myorg.module.core.access.privilege.AbstractPrivilege;
import org.myorg.module.core.access.privilege.AccessOp;

public class ActivityPrivilege extends AbstractPrivilege {

    public static final ActivityPrivilege INSTANCE = new ActivityPrivilege();

    private ActivityPrivilege() {
        super(
                "timemanagement.activity",
                AccessOp.READ, AccessOp.WRITE, AccessOp.DELETE, AccessOp.EXECUTE
        );
    }
}
