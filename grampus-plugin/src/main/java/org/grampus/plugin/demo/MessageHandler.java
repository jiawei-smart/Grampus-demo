package org.grampus.plugin.demo;

import org.grampus.core.annotation.plugin.GPluginApi;
import org.grampus.core.plugin.GPromise;

/**
 * declared the interface that plugin support function
 * */
@GPluginApi(event = "TEST_API")
public interface MessageHandler {

    void onMessage(Integer seq, String message, GPromise<Boolean> promise);
    void onAdmin(String event);
}
