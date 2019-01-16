package util;
import java.io.File;
import net.whg.we.main.Plugin;
import net.whg.we.resources.SimpleFileDatabase;

public class CommonMock
{
	public static SimpleFileDatabase getSimpleFileDatabase()
	{
		File workingDir = new File(System.getProperty("user.dir"));
		return new SimpleFileDatabase(workingDir);
	}

	public static Plugin getTestPlugin()
	{
		return new Plugin()
		{

			@Override
			public String getPluginName()
			{
				return "TestPlugin";
			}

			@Override
			public void initPlugin()
			{
			}

			@Override
			public void enablePlugin()
			{
			}

			@Override
			public int getPriority()
			{
				return 0;
			}
		};
	}
}
