package org.vaadin.guice.tutorial.evaluators;

import com.vaadin.server.VaadinSession;

import org.ilay.api.Authorizer;
import org.vaadin.guice.tutorial.CurrentUserRole;

public class RolePermissionAuthorizer implements Authorizer<CurrentUserRole> {

    @Override
    public boolean isGranted(CurrentUserRole permission) {

        CurrentUserRole currentUserRole = VaadinSession.getCurrent().getAttribute(CurrentUserRole.class);

        if (currentUserRole == null) {
            currentUserRole = CurrentUserRole.USER;
            VaadinSession.getCurrent().setAttribute(CurrentUserRole.class, currentUserRole);
        }

        return currentUserRole.ordinal() >= permission.ordinal();
    }

    @Override
    public Class<CurrentUserRole> getPermissionClass() {
        return CurrentUserRole.class;
    }
}

