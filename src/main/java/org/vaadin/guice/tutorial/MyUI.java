package org.vaadin.guice.tutorial;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import org.ilay.Authorization;
import org.ilay.api.Authorizer;
import org.vaadin.guice.tutorial.components.*;
import org.vaadin.guice.tutorial.evaluators.GridItemAuthorizer;
import org.vaadin.guice.tutorial.evaluators.ItemIdAuthorizer;
import org.vaadin.guice.tutorial.evaluators.RolePermissionAuthorizer;
import org.vaadin.guice.tutorial.grid.GridItem;
import org.vaadin.guice.tutorial.grid.RestrictedGrid;
import org.vaadin.guice.tutorial.views.AdminView;
import org.vaadin.guice.tutorial.views.DefaultView;
import org.vaadin.guice.tutorial.views.ErrorView;
import org.vaadin.guice.tutorial.views.ItemsView;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.HashSet;
import java.util.Set;

public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        DefaultNavigationButton defaultNavigationButton = new DefaultNavigationButton();

        AdminNavigationButton adminNavigationButton = new AdminNavigationButton();

        UserSelector userSelector = new UserSelector();

        NavigationBar navigationBar = new NavigationBar(defaultNavigationButton, adminNavigationButton, userSelector);

        Header header = new Header(navigationBar);

        ViewContainer viewContainer = new ViewContainer();

        RootLayout rootLayout = new RootLayout(header, viewContainer);

        RestrictedGrid restrictedGrid = new RestrictedGrid();

        Navigator navigator = new Navigator(this, viewContainer);

        navigator.addView("", new DefaultView(restrictedGrid));
        AdminView adminView = new AdminView();
        navigator.addView("adminView", adminView);
        navigator.addView("error", new ErrorView());
        navigator.addView("items", new ItemsView());

        setNavigator(navigator);

        setContent(rootLayout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {

        @Override
        protected void servletInitialized() throws ServletException {
            super.servletInitialized();

            ItemIdAuthorizer itemIdAuthorizer = new ItemIdAuthorizer();
            RolePermissionAuthorizer rolePermissionAuthorizer = new RolePermissionAuthorizer();
            GridItemAuthorizer gridItemAuthorizer = new GridItemAuthorizer(rolePermissionAuthorizer);

            Set<Authorizer> authorizers = new HashSet<>();
            authorizers.add(itemIdAuthorizer);
            authorizers.add(rolePermissionAuthorizer);
            authorizers.add(gridItemAuthorizer);

            Authorization.start(authorizers);
        }
    }
}
