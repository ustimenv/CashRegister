package shop.cashregister;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.JsonPath;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import shop.cashregister.control.CheckOutController;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CheckOutController.class)
public class CheckoutControllerTest{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CheckOutController checkoutController;

    @Test
    public void testBeginTransaction() throws Exception {
        MvcResult response = mockMvc.perform(post("/checkout/begin"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

//        JsonPath.read(response.getResponse().getContentAsString(), "");
    }
}
