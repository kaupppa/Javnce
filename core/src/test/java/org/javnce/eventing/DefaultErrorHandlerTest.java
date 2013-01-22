package org.javnce.eventing;

import org.junit.Test;

public class DefaultErrorHandlerTest {

    @Test
    public void testFatalError() {

        DefaultErrorHandler handler = new DefaultErrorHandler();
        // No catch here
        handler.fatalError(null, null);
        handler.fatalError(handler, null);
        handler.fatalError(handler, new Throwable());
    }
}
