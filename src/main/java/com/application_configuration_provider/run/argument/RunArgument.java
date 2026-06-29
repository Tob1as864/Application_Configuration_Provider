package main.java.com.application_configuration_provider.run.argument;

import main.java.com.application_configuration_provider.run.configuration.RunConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum RunArgument {
    DEFAULT_RUN_ARGUMENT,
    DEFAULT_RUN_STRING;

    private static final Map<RunArgument, RunConfiguration> defaultConfiguration = new HashMap();

    private static <T> void defaultRunArgument(RunArgument runArgument, Class<T> classType, T defaultValue, String description) {
        if (defaultConfiguration.containsKey(runArgument)) {
            throw new RuntimeException("This argument is already preconfigured.");
        } else {
            defaultConfiguration.put(runArgument, new RunConfiguration(runArgument, classType, defaultValue, description));
        }
    }

    public static Map<RunArgument, RunConfiguration> getDefaultRunArguments() {
        return defaultConfiguration;
    }

    public static Map<RunArgument, RunConfiguration> configureRunArguments(Map<RunArgument, RunConfiguration> configuredRunArguments) {
        Map<RunArgument, RunConfiguration> preConfiguredRunArguments = getDefaultRunArguments();

        for(RunArgument runArgument : preConfiguredRunArguments.keySet()) {
            if (!configuredRunArguments.containsKey(runArgument)) {
                configuredRunArguments.put(runArgument, preConfiguredRunArguments.get(runArgument));
            }
        }

        return configuredRunArguments;
    }

    public static Map<RunArgument, RunConfiguration> configureRunArguments(String configuredRunArguments) {
        String patternConfig = "-(\\w+)=((?:\\w|\\s(?!-))+)";
        Map<String, String> stringConfiguredRunArgumentsMap = new HashMap();
        Matcher matcher = match(configuredRunArguments, patternConfig);

        while(matcher.find()) {
            stringConfiguredRunArgumentsMap.put(matcher.group(1), matcher.group(2));
        }

        return configureRunArguments(convertStringToRunConfiguration(stringConfiguredRunArgumentsMap));
    }

    private static Map<RunArgument, RunConfiguration> convertStringToRunConfiguration(Map<String, String> stringConfiguredRunArgumentsMap) {
        Map<RunArgument, RunConfiguration> configuration = new HashMap();

        for(String stringRunArgument : stringConfiguredRunArgumentsMap.keySet()) {
            String stringRunConfiguration = (String)stringConfiguredRunArgumentsMap.get(stringRunArgument);

            RunArgument runArgument;
            try {
                runArgument = valueOf(stringRunArgument.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println("Could not get RunArgument value of " + stringRunArgument + e.getMessage());
                continue;
            }

            Map<RunArgument, RunConfiguration> preConfiguredRunArguments = getDefaultRunArguments();
            RunConfiguration runConfiguration = (RunConfiguration)preConfiguredRunArguments.get(runArgument);
            if (runConfiguration == null) {
                System.err.println("Could not get Configuration for RunArgument.");
            } else {
                Class<?> classType = runConfiguration.getClassType();
                if (String.class == classType) {
                    configuration.put(runArgument, runConfiguration.getNewRunConfiguration(stringRunConfiguration));
                } else if (Boolean.class == classType) {
                    configuration.put(runArgument, runConfiguration.getNewRunConfiguration(Boolean.valueOf(stringRunConfiguration)));
                } else if (Integer.class == classType) {
                    configuration.put(runArgument, runConfiguration.getNewRunConfiguration(Integer.valueOf(stringRunConfiguration)));
                } else if (Double.class == classType) {
                    configuration.put(runArgument, runConfiguration.getNewRunConfiguration(Double.valueOf(stringRunConfiguration)));
                }
            }
        }

        return configuration;
    }

    static {
        defaultRunArgument(DEFAULT_RUN_ARGUMENT, Boolean.class, false, "Ist ein Beispiel für das Anlegen eines RunArguments.");
        defaultRunArgument(DEFAULT_RUN_STRING, String.class, "Zielwert <5 und >1", "Ist ein Beispiel für das Anlegen eines RunArguments.");
    }

    public static Matcher match(String text, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        return m;
    }
}
