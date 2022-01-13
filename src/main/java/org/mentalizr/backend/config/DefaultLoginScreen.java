package org.mentalizr.backend.config;

public class DefaultLoginScreen {

    public static final String LOGIN = "login";
    public static final String ACCESS_KEY = "accessKey";

    private final String defaultLoginScreen;

    public DefaultLoginScreen(String defaultLoginScreen) {
        if (!defaultLoginScreen.equals(LOGIN) && !defaultLoginScreen.equals(ACCESS_KEY))
            throw new IllegalArgumentException(DefaultLoginScreen.class.getSimpleName()
                    + " must be initialized with either [" + LOGIN + "] or [" + ACCESS_KEY + "].");
        this.defaultLoginScreen = defaultLoginScreen;
    }

    public boolean isLogin() {
        return this.defaultLoginScreen.equals(LOGIN);
    }

    public boolean isAccessKey() {
        return this.defaultLoginScreen.equals(ACCESS_KEY);
    }

}
