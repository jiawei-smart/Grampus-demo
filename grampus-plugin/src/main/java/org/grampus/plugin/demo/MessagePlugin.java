package org.grampus.plugin.demo;

import org.grampus.core.annotation.plugin.GPlugin;
import org.grampus.core.plugin.GAsyncResult;
import org.grampus.core.plugin.GPluginCell;
import org.grampus.core.plugin.GPromise;
import org.grampus.log.GLogger;

@GPlugin(event = "TEST_API") // declare this cell as a plugin cell, then it will be load auto
/** enrich plugin by implement relevant interface */
public class MessagePlugin extends GPluginCell implements MessageHandler {

    @Override
    public void onMessage(Integer seq, String message, GPromise promise) {
        GLogger.info("onMessage [{}], seq [{}]", message, seq);
        getController().submitTask(() -> promise.handle(new GAsyncResult().result("received: " + seq)));
    }

    @Override
    public void onAdmin(String event) {
        GLogger.info("admin event [{}]", event);
    }
}
