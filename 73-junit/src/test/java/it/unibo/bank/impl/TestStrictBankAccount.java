package it.unibo.bank.impl;

import it.unibo.bank.api.AccountHolder;
import it.unibo.bank.api.BankAccount;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test class for the {@link StrictBankAccount} class.
 */
class TestStrictBankAccount {

    private static final double AMOUNT = 100.0;
    private static final double DELTA = 0.001;
    private static final int TRANSACTIONS_NUMBER = 10;
    private static final int ACCEPTABLE_MESSAGE_LENGTH = 10;
    // Create a new AccountHolder and a StrictBankAccount for it each time tests are executed.
    private AccountHolder mRossi;
    private BankAccount strictBankAccount;

    /**
     * Prepare the tests.
     */
    @BeforeEach
    void setUp() {
        this.mRossi = new AccountHolder("Mario", "Rossi", 1);
        this.strictBankAccount = new StrictBankAccount(mRossi, 0.0);
    }

    /**
     * Test the initial state of the StrictBankAccount.
     */
    @Test
    void testInitialization() {
        assertEquals(0.0, strictBankAccount.getBalance());
        assertEquals(mRossi, strictBankAccount.getAccountHolder());
        assertEquals(0, strictBankAccount.getTransactionsCount());
    }

    /**
     * Perform a deposit of 100€, compute the management fees, and check that the balance is correctly reduced.
     */
    @Test
    void testManagementFees() {
        double feesApplied = 0.0;
        int i;
        int j = 0;
        while (j < TRANSACTIONS_NUMBER) {
            i = 0;
            assertEquals(i, strictBankAccount.getTransactionsCount());
            assertEquals(j * AMOUNT - feesApplied, strictBankAccount.getBalance(), DELTA);
            strictBankAccount.deposit(mRossi.getUserID(), AMOUNT);
            i++;
            j++;
            assertEquals(j * AMOUNT - feesApplied, strictBankAccount.getBalance(), DELTA);
            assertEquals(i, strictBankAccount.getTransactionsCount());
            strictBankAccount.chargeManagementFees(mRossi.getUserID());
            feesApplied += StrictBankAccount.TRANSACTION_FEE * i + StrictBankAccount.MANAGEMENT_FEE;
            assertEquals(j * AMOUNT - feesApplied, strictBankAccount.getBalance(), DELTA);
        }
    }

    /**
     * Test that withdrawing a negative amount causes a failure.
     */
    @Test
    void testNegativeWithdraw() {
        try {
            strictBankAccount.withdraw(mRossi.getUserID(), -TRANSACTIONS_NUMBER);
            fail("It's not possible withdrawing a negative quantity");
        } catch (final IllegalArgumentException e) {
            assertEquals(0, strictBankAccount.getBalance());
            assertEquals(0, strictBankAccount.getTransactionsCount());
            assertNotNull(e.getMessage()); // Non-null message
            assertFalse(e.getMessage().isBlank()); // Not a blank or empty message
            assertTrue(e.getMessage().length() >= ACCEPTABLE_MESSAGE_LENGTH); // A message with a decent length
        }
    }

    /**
     * Test that withdrawing more money than it is in the account is not allowed.
     */
    @Test
    void testWithdrawingTooMuch() {
        strictBankAccount.deposit(mRossi.getUserID(), AMOUNT);
        assertEquals(AMOUNT, strictBankAccount.getBalance());
        assertEquals(1, strictBankAccount.getTransactionsCount());
        final int numOfTransactions = strictBankAccount.getTransactionsCount();
        try {
            strictBankAccount.withdraw(mRossi.getUserID(), AMOUNT * 2);
            fail("It's not possible withdraw more money than you have in your balance");
        } catch (final IllegalArgumentException e) {
            assertEquals(AMOUNT, strictBankAccount.getBalance());
            assertEquals(numOfTransactions, strictBankAccount.getTransactionsCount()); // transactions remains the same
            assertNotNull(e.getMessage()); // Non-null message
            assertFalse(e.getMessage().isBlank()); // Not a blank or empty message
            assertTrue(e.getMessage().length() >= ACCEPTABLE_MESSAGE_LENGTH); // A message with a decent length
        }
    }
}
