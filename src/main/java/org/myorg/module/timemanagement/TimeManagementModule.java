package org.myorg.module.timemanagement;

import org.myorg.module.core.CoreModule;
import org.myorg.module.core.access.privilege.AccessOp;
import org.myorg.module.core.access.privilege.AccessOpCollection;
import org.myorg.module.core.application.Application;
import org.myorg.module.core.database.service.accessrole.AccessRoleDto;
import org.myorg.module.core.database.service.accessrole.PrivilegeDto;
import org.myorg.module.core.database.service.user.UserService;
import org.myorg.module.timemanagement.privilege.ActivityPrivilege;
import org.myorg.modules.modules.BootModule;
import org.myorg.modules.modules.Module;
import org.myorg.modules.modules.exception.ModuleException;
import org.myorg.modules.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Component
@BootModule(
        uuid = TimeManagementModuleConsts.UUID,
        dependencies = { CoreModule.class }
)
public class TimeManagementModule extends Module implements Application {

    private UserService userService;

    @Autowired
    public TimeManagementModule(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onStart() throws ModuleException {

    }

    @Override
    public void onDestroy() throws ModuleException {

    }

    @Override
    public String getUuid() {
        return TimeManagementModuleConsts.UUID;
    }

    @Override
    public boolean isUserHasAccess(long userId) throws ModuleException {
        String key = ActivityPrivilege.INSTANCE.getKey();

        Set<AccessRoleDto> accessRoles = userService.findAllAccessRoles(userId, ContextUtils.createSystemContext());
        for (AccessRoleDto accessRole : accessRoles) {
            for (PrivilegeDto privilege : accessRole.getPrivileges()) {
                if (Objects.equals(privilege.getKey(), key)
                        && new AccessOpCollection(privilege.getOps()).contains(AccessOp.READ)) {
                    return true;
                }
            }
        }
        return false;
    }
}
