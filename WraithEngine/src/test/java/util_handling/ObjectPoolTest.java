package util_handling;

import static org.junit.Assert.*;

import org.junit.Test;
import net.whg.frameworks.util.ObjectPool;
import net.whg.we.utils.logging.LogProperty;

public class ObjectPoolTest {
    /**
     * Test that putting and getting from the pool work properly.
     */
    @Test
    public void putAndGet() {

        ObjectPool<LogProperty> pool = new ObjectPool<LogProperty>() {
            @Override
            protected LogProperty build() {
                return new LogProperty();
            }
        };

        LogProperty lp1 = new LogProperty();
        LogProperty lp2 = new LogProperty();



        pool.put(lp1);
        pool.put(lp2);
        assertEquals(lp1,pool.get());
    }

    /**
     * Test that getting from an empty pool returns a newly built object.
     */
    @Test
    public void getFromEmptyPool() {

        ObjectPool<LogProperty> pool = new ObjectPool<LogProperty>() {
            @Override
            protected LogProperty build() {
                LogProperty result = new LogProperty();
                return result.setMessage("default message");
            }
        };

        assertEquals(pool.get().getMessage(),"default message");
    }

}
