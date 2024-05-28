import org.grampus.GAssert;
import org.grampus.core.GCell;
import org.grampus.core.GConstant;
import org.grampus.core.demo.helloword.Workflow;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class WorkflowTest {

    @Test
    public void test(){
        Workflow workflow = new Workflow(){
            @Override
            public void buildWorkflow() {
                super.buildWorkflow();

                //replace the sink cell to get the source message and assert it.
                service(TARGET_SERVICE).cell(GConstant.DEFAULT_EVENT,0,new GCell<String>(){
                    @Override
                    public void handle(String payload, Map meta) {
                        GAssert.assertEquals("Hello world, Grampus !", payload);
                    }
                });
            }
        };

        // the test(count) indicates how many asserts were expected to execute,
        // default value is one, and it need to declare the value when here assert more than one.
        workflow.test();
    }
}
