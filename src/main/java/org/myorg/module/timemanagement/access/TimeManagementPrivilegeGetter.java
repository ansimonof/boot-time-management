package org.myorg.module.timemanagement.access;

import org.myorg.module.core.access.privilege.AbstractPrivilege;
import org.myorg.module.core.access.privilege.getter.ModulePrivilegeGetter;
import org.myorg.module.timemanagement.privilege.ActivityPrivilege;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TimeManagementPrivilegeGetter extends ModulePrivilegeGetter {

    @Override
    public List<? extends AbstractPrivilege> getPrivileges() {
        return new ArrayList<AbstractPrivilege>() {{
            add(ActivityPrivilege.INSTANCE);
        }};
    }
}
