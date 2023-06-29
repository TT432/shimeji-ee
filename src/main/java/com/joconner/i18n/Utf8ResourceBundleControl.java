package com.joconner.i18n;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Created by joconner on 1/11/17.
 */
public class Utf8ResourceBundleControl extends PackageableResourceControl {


    public Utf8ResourceBundleControl() {
    }

    public Utf8ResourceBundleControl(boolean isPackageBased) {
        super(isPackageBased);
    }

    public ResourceBundle newBundle(String baseName, Locale locale, String format,
                                    ClassLoader loader, boolean reload)
            throws IllegalAccessException, InstantiationException, IOException {
        String bundleName = toBundleName(baseName, locale);
        ResourceBundle bundle = null;
        if (format.equals("java.class")) {
            bundle = super.newBundle(baseName, locale, format, loader, reload);
        } else if (format.equals("java.properties")) {
            final String resourceName = bundleName.contains("://") ? null :
                    toResourceName(bundleName, "properties");
            if (resourceName == null) {
                return bundle;
            }

            InputStream stream = new FileInputStream("conf/" + resourceName);

            Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);

            try {
                bundle = new PropertyResourceBundle(reader);
            } finally {
                reader.close();
            }
        } else {
            throw new IllegalArgumentException("Unknown format: " + format);
        }
        return bundle;
    }
}
