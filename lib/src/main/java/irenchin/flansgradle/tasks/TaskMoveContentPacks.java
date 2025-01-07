package irenchin.flansgradle.tasks;

import java.io.File;

import irenchin.flansgradle.Main;

public class TaskMoveContentPacks extends Task {

	@Override
	public boolean execute() {
		updateStatus("\n\n\n");
		if(new File(Main.folder.toPath()+"/run/Flan").exists()) {
			updateStatus("Placing content packs...\n");
			return moveContents(Main.folder.toPath()+"/run/Flan", Main.folder.toPath()+"/eclipse/Flan");		
		} else {
			updateStatus("/run/Flan folder not found, skipping content packs...");
			return true;
		}
	}

}
