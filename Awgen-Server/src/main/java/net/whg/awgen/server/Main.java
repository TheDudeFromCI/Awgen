package net.whg.awgen.server;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.whg.awgen.lib.gameloop.GameLoopAdapter;
import net.whg.awgen.lib.util.GameLoopPhysicsYield;
import net.whg.awgen.server.commands.CommandModule;
import net.whg.awgen.server.core.GameState;

public class Main
{
	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args)
	{
		logSystemInfo();

		GameState game = new GameState();
		TerminalInput input = new TerminalInput();

		input.loadModule(CommandModule.build(game));

		game.getGameLoop().getEvents().addListener(new GameLoopPhysicsYield());
		game.getGameLoop().getEvents().addListener(new GameLoopAdapter()
		{
			@Override
			public void onPhysics()
			{
				input.pushCommands();
			}
		});

		game.getGameLoop().run();
	}

	private static void logSystemInfo()
	{
		String version;

		try
		{
			MavenXpp3Reader reader = new MavenXpp3Reader();
			Model model;
			if (new File("pom.xml").exists())
				model = reader.read(new FileReader("pom.xml"));
			else
				model = reader.read(new InputStreamReader(Main.class.getResourceAsStream(
						"/META-INF/maven/de.scrum-master.stackoverflow/aspectj-introduce-method/pom.xml")));

			version = model.getVersion();
		}
		catch (Exception e)
		{
			logger.error("Failed to load project properties!", e);
			version = "unknown";
		}

		logger.info("Launching Awgen Server.");
		logger.debug("Awgen Version: {}", version);
		logger.debug("OS: {}, {}", System.getProperty("os.name"), System.getProperty("os.arch"));
		logger.debug("Java: {}, {}", System.getProperty("java.version"), System.getProperty("java.vendor"));
		logger.debug("User: {}", System.getProperty("user.name"));
		logger.debug("Directory: {}", System.getProperty("user.dir"));
	}
}
