package util_handling;

import static org.junit.Assert.*;

import org.junit.Test;

import net.whg.we.utils.ObjectPool;
import net.whg.we.utils.logging.LogProperty;

public class ObjectPoolTest {
    
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
