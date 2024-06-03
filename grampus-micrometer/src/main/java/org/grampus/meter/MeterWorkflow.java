package org.grampus.meter;

import org.grampus.core.GCell;
import org.grampus.core.GCellOptions;
import org.grampus.core.GWorkflow;
import org.grampus.core.executor.GTimer;
import org.grampus.log.GLogger;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MeterWorkflow {
    public static void main(String[] args) {
        GWorkflow workflow = new GWorkflow() {
            long startTime;
            GTimer timer;
            Integer messageSize = 10000000;
            Integer parallel = 32;

            public void buildWorkflow() {
                this.service("S1").cell(new GCell() {
                    public void start() {
                        AtomicInteger msgSeq = new AtomicInteger(1);
                        timer = this.getController().createTimer(() -> {
                            this.onEvent("E0", msgSeq.addAndGet(1));

                        }).scheduleMills(1L, messageSize);
                        startTime = this.now();
                    }
                }).openEvent("E0");
                GCellOptions cellOptions = new GCellOptions();
                cellOptions.setParallel(this.parallel);
                this.service("S2").cell(new GCell(cellOptions) {
                    Random random = new Random();
                    AtomicInteger receivedMsgCount = new AtomicInteger(0);
                    public void handle(Object payload, Map meta) {
                        try {
                            Thread.sleep(this.random.nextInt(50));
                            this.receivedMsgCount.addAndGet(1);
                            GLogger.info("**** process msg seq [{}],total msgCount [{}], total cost time [{}], avg [{}]", new Object[]{payload, this.receivedMsgCount.get(), this.now() - startTime, (this.now() - startTime) / (long)this.receivedMsgCount.get()});
                        } catch (InterruptedException exception) {
                            GLogger.error("Interrupted exception", exception);
                        }
                    }
                });
                this.chain("S1.E0->S2");
            }
        };
        workflow.start();
    }
}
