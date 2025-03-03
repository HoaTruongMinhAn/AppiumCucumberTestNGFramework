package com.qa.runners;

import com.qa.utils.GlobalParams;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestCaseStarted;

public class DeviceInfoPlugin implements ConcurrentEventListener {
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
    }

    private void handleTestCaseStarted(TestCaseStarted event) {
        String deviceName = new GlobalParams().getDeviceName();
        // Log or modify report with device name
    }
}
