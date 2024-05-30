package org.grampus.core.demo.helloword;

import org.grampus.core.GCell;
import org.grampus.core.GService;
import org.grampus.core.GWorkflow;
import org.grampus.log.GLogger;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ReactiveWorkflow extends GWorkflow {
    public static String EVENT_1 = "EVENT_1";
    public static String EVENT_2 = "EVENT_2";
    public static String SOURCE_SERVICE = "SourceService";
    public static String TARGET_SERVICE = "TargetService";

    @Override
    public void buildWorkflow() {
        GService sourceService = service(SOURCE_SERVICE);
        sourceService.source(new GCell() {
            int seq = 0;
            @Override
            public void start() {
                //in start func, trigger a timer to generate messages
                getController()
                        .createTimer(() -> onEvent(EVENT_1, generateMsg()))
                        .schedule(2l, TimeUnit.SECONDS, 10);
            }

            private Integer generateMsg() {
                return seq++;
            }
        });

        sourceService
                .listen(EVENT_1)
                .filter((seq,meta)->{return (int)seq % 2 == 0;})
                .map((seq,meta)->{return (int)seq * 10;})
                .sink(new GCell(){
                    @Override
                    public void handle(Object payload, Map meta) {
                        onEvent(EVENT_2, payload);
                    }
                });

        sourceService.openEvent(EVENT_2);

        service(TARGET_SERVICE).cell(new GCell() {
            @Override
            public void handle(Object payload, Map meta) {
                GLogger.info("Received Msg: " + payload);
            }
        });

        chain(link(event(SOURCE_SERVICE,EVENT_2),TARGET_SERVICE));
    }

    public static void main(String[] args) {
        new ReactiveWorkflow().start();
    }
}
