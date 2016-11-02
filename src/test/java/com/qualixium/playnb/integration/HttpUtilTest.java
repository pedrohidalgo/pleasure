package com.qualixium.playnb.integration;

import com.qualixium.playnb.util.HttpUtil;
import java.io.IOException;
import java.util.Optional;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class HttpUtilTest {

    @Test
    public void testExecuteGetRequest() throws IOException {
        String targetURL = "http://google.com";

        Optional<String> result = HttpUtil.executeGetRequest(targetURL);

        assertTrue(result.isPresent());
    }

}
