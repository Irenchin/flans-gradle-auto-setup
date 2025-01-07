package irenchin.flansgradle.tasks;

import irenchin.flansgradle.Main;

public class TaskDownloadFlans extends Task {

	@Override
	public boolean execute() {
		updateStatus("\n\n\n");
		updateStatus("Downloading Flans Mod src...\n");
		return downloadAndExtractZip("https://github.com/"+Main.user.getText().trim()+"/"+Main.repo.getText().trim()+"/archive/"+Main.branch.getText().trim()+".zip", Main.folder.toPath().toString());		
	}

}
