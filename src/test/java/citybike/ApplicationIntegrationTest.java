package citybike;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Ignore
    @Test
    public void serviceAtRootShouldReturnSomeHTML() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(content().string(containsString("<th>Navnet på sykkelstasjonen</th>")));
    }

    @Ignore
    @Test
    public void serviceAtStationsShouldReturnSomeJSONWithData() throws Exception {
        this.mockMvc.perform(get("/stations")).andExpect(status().isOk())
                .andExpect(content().string(containsString("[{\"title\":\"")))
                .andExpect(content().string(containsString("\"numberOfLocksAvailable\"")));
    }
}
