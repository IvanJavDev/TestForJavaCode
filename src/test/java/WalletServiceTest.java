

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.example.walletservice.model.Transaction;
import org.example.walletservice.model.Wallet;
import org.example.walletservice.repository.TransactionRepository;
import org.example.walletservice.repository.WalletRepository;
import org.example.walletservice.service.WalletService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private WalletService walletService;

    private Wallet wallet;
    private UUID walletId;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        walletId = UUID.randomUUID();
        wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.valueOf(1000));
    }

    @Test
    public void testModifyWalletDeposit() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        walletService.modifyWallet(walletId, "DEPOSIT", BigDecimal.valueOf(500));

        assertEquals(BigDecimal.valueOf(1500), wallet.getBalance());
        verify(walletRepository).save(wallet);

        Transaction transaction = new Transaction();
        transaction.setWalletId(walletId);
        transaction.setOperationType("DEPOSIT");
        transaction.setAmount(BigDecimal.valueOf(500));
        transaction.setTimestamp(any(LocalDateTime.class));

        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    public void testModifyWalletWithdraw() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        walletService.modifyWallet(walletId, "WITHDRAW", BigDecimal.valueOf(500));

        assertEquals(BigDecimal.valueOf(500), wallet.getBalance());
        verify(walletRepository).save(wallet);

        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test(expected = RuntimeException.class)
    public void testModifyWalletWithdrawInsufficientFunds() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        walletService.modifyWallet(walletId, "WITHDRAW", BigDecimal.valueOf(1500));
    }

    @Test(expected = RuntimeException.class)
    public void testModifyWalletWalletNotFound() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        walletService.modifyWallet(walletId, "DEPOSIT", BigDecimal.valueOf(500));
    }

    @Test
    public void testGetBalance() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        BigDecimal balance = walletService.getBalance(walletId);

        assertEquals(BigDecimal.valueOf(1000), balance);
    }

    @Test(expected = RuntimeException.class)
    public void testGetBalanceWalletNotFound() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        walletService.getBalance(walletId);
    }
}
