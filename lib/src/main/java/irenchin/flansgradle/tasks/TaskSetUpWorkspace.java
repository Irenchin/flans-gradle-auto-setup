package irenchin.flansgradle.tasks;

import irenchin.flansgradle.Main;

public class TaskSetUpWorkspace extends Task {

	@Override
	public boolean execute() {
		updateStatus("\n\n\n");
		updateStatus("Setting up workspace...\n");
		if(runCommand("gradlew.bat setupDecompWorkspace --refresh-dependencies", Main.folder.toPath().toString())) {
			return runCommand("gradlew.bat eclipse", Main.folder.toPath().toString());	
		} else {
			return false;
		}
	}

}
