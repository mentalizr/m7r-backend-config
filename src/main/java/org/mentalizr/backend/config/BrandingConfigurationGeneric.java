package org.mentalizr.backend.config;

public class BrandingConfigurationGeneric {

    private final String title;
    private final String logo;

    public BrandingConfigurationGeneric(String title, String logo) {
        this.title = title;
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public String getLogo() {
        return logo;
    }

}
