package irenchin.flansgradle.tasks;

import irenchin.flansgradle.Main;

public class TaskConfigureGradle extends Task {

	@Override
	public boolean execute() {
		updateStatus("\n\n\n");
		updateStatus("Configuring Gradle...\n");
		if(replaceLine(Main.folder+"/build.gradle",
				"classpath 'net.minecraftforge.gradle:ForgeGradle:1.2-SNAPSHOT'",
				"classpath ('com.anatawa12.forge:ForgeGradle:1.2-1.0.+') { changing = true }")) {
			return replaceLine(Main.folder+"/gradle/wrapper/gradle-wrapper.properties",
					"distributionUrl",
					"distributionUrl=https\\://services.gradle.org/distributions/gradle-4.4.1-bin.zip");	
		} else {
			return false;
		}
	}

}
