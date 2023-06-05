package core.basesyntax.strategy.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.db.Storage;
import core.basesyntax.model.FruitTransaction;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PurchaseImplTest {
    private PurchaseImpl purchase;
    private FruitTransaction fruitTransaction;

    @BeforeEach
    void setUp() {
        purchase = new PurchaseImpl();
        fruitTransaction = new FruitTransaction(FruitTransaction.Operation.PURCHASE, "banana", 15);
    }

    @Test
    public void purchaseHandler_NegativeQuantity_notOk() {
        Storage storage = new Storage();
        storage.put(fruitTransaction.getFruit(), 3);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> purchase.calculateFruitOperation(fruitTransaction));
    }

    @Test
    public void doCalculation_keyDoesNotExist_notOk() {
        assertThrows(RuntimeException.class,
                () -> purchase.calculateFruitOperation(fruitTransaction));
    }

    @Test
    public void doCalculation_validOperation_ok() {
        Storage storage = new Storage();
        storage.put(fruitTransaction.getFruit(), 50);
        purchase.calculateFruitOperation(fruitTransaction);
        Map<String, Integer> expected = Map.of("banana", 35);
        Map<String, Integer> actual = Storage.fruitStorage;
        assertEquals(expected, actual);
    }

    @AfterEach
    public void afterEach() {
        Storage.fruitStorage.clear();
    }
}