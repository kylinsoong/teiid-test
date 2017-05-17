package io.fabric8.example;

public class SampleLogger implements io.fabric8.maven.docker.util.Logger {

    @Override
    public void debug(String format, Object... params) {
        System.out.printf(format, params);
    }

    @Override
    public void info(String format, Object... params) {
        System.out.printf(format, params);
    }

    @Override
    public void verbose(String format, Object... params) {
        System.out.printf(format, params);
    }

    @Override
    public void warn(String format, Object... params) {
        System.out.printf(format, params);
    }

    @Override
    public void error(String format, Object... params) {
        System.out.printf(format, params);
    }

    @Override
    public String errorMessage(String message) {
        return "ERROR";
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public void progressStart() {
        
    }

    @Override
    public void progressUpdate(String layerId, String status, String progressMessage) {
        
    }

    @Override
    public void progressFinished() {
        
    }

}
