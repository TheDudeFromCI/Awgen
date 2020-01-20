package unit;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import net.whg.awgen.lib.util.Version;

public class VersionTest
{
    @Test
    public void loadBuildNumber()
    {
        Version v = Version.get();
        v.printInfo();

        assertTrue(v.getBuildNumber()
                    .matches("dev_build|build_[0-9]+"));
    }
}
