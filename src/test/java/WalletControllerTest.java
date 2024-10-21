import org.example.walletservice.controller.WalletController;

import org.example.walletservice.service.WalletService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
public class WalletControllerTest {

    @InjectMocks
    private WalletController walletController;

    @Mock
    private WalletService walletService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
    }

    @Test
    public void testModifyWallet_Success() throws Exception {
        UUID walletId = UUID.randomUUID();
        String operationType = "deposit";
        BigDecimal amount = BigDecimal.valueOf(100);

        // Arrange
        doNothing().when(walletService).modifyWallet(walletId, operationType, amount);

        // Act & Assert
        mockMvc.perform(post("/api/v1/wallet?walletId=" + walletId + "&operationType=" + operationType + "&amount=" + amount)
                        .contentType("application/x-www-form-urlencoded"))
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));

        verify(walletService, times(1)).modifyWallet(walletId, operationType, amount);
    }

    @Test
    public void testModifyWallet_Failure() throws Exception {
        UUID walletId = UUID.randomUUID();
        String operationType = "deposit";
        BigDecimal amount = BigDecimal.valueOf(100);

        // Arrange
        doThrow(new RuntimeException("Error")).when(walletService).modifyWallet(walletId, operationType, amount);

        // Act & Assert
        mockMvc.perform(post("/api/v1/wallet?walletId=" + walletId + "&operationType=" + operationType + "&amount=" + amount)
                        .contentType("application/x-www-form-urlencoded"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error"));

        verify(walletService, times(1)).modifyWallet(walletId, operationType, amount);
    }

    @Test
    public void testGetBalance_Success() throws Exception {
        UUID walletId = UUID.randomUUID();
        BigDecimal balance = BigDecimal.valueOf(150);

        // Arrange
        when(walletService.getBalance(walletId)).thenReturn(balance);

        // Act & Assert
        mockMvc.perform(get("/api/v1/wallet/wallets/" + walletId))
                .andExpect(status().isOk())
                .andExpect(content().string(balance.toString()));

        verify(walletService, times(1)).getBalance(walletId);
    }

    @Test
    public void testGetBalance_NotFound() throws Exception {
        UUID walletId = UUID.randomUUID();

        // Arrange
        when(walletService.getBalance(walletId)).thenThrow(new RuntimeException("Wallet not found"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/wallet/wallets/" + walletId))
                .andExpect(status().isNotFound());

        verify(walletService, times(1)).getBalance(walletId);
    }
}