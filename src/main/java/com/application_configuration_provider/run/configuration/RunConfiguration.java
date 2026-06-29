package main.java.com.application_configuration_provider.run.configuration;


import main.java.com.application_configuration_provider.run.argument.RunArgument;

public class RunConfiguration<T> {
    RunArgument runArgument;
    Class<T> classType;
    T configValue;
    String description;

    public RunConfiguration(RunArgument runArgument, Class<T> classType, T configValue, String description) {
        this.runArgument = runArgument;
        this.classType = classType;
        this.configValue = configValue;
        this.description = description;
    }

    public T getConfiguration() {
        return this.configValue;
    }

    public RunArgument getRunArgument() {
        return this.runArgument;
    }

    public T getConfigValue() {
        return this.configValue;
    }

    public String getDescription() {
        return this.description;
    }

    public void setConfiguration(T value) {
        this.configValue = value;
    }

    public Class<T> getClassType() {
        return this.classType;
    }

    public boolean getConfigValueBool() {
        if (this.configValue instanceof Boolean) {
            return (Boolean)this.configValue;
        } else {
            throw new RuntimeException("The configuration value is from type " + this.classType + " not Boolean.");
        }
    }

    public String getConfigValueString() {
        if (this.configValue instanceof String) {
            return (String)this.configValue;
        } else {
            throw new RuntimeException("The configuration value is from type " + this.classType + " not String.");
        }
    }

    public int getConfigValueInteger() {
        if (this.configValue instanceof Integer) {
            return (Integer)this.configValue;
        } else {
            throw new RuntimeException("The configuration value is from type " + this.classType + " not Integer.");
        }
    }

    public double getConfigValueDouble() {
        if (this.configValue instanceof Double) {
            return (Double)this.configValue;
        } else {
            throw new RuntimeException("The configuration value is from type " + this.classType + " not Double.");
        }
    }

    public String toString() {
        return "RunConfiguration: " + this.getRunArgument().name() + " | " + this.getConfigValue().toString() + " | " + this.getDescription();
    }

    public RunConfiguration getNewRunConfiguration(T configValue) {
        return new RunConfiguration(this.runArgument, this.classType, configValue, this.description);
    }
}
