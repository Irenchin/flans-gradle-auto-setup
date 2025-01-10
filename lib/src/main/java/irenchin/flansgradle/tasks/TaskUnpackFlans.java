package irenchin.flansgradle.tasks;

import irenchin.flansgradle.Main;

public class TaskUnpackFlans extends Task {

	@Override
	public boolean execute() {
		updateStatus("\n\n\n");
		updateStatus("Unpacking Flans Mod...\n");
		return moveContentsToParentAndDelete(Main.folder.toPath()+"/"+Main.repo.getText()+"-"+Main.branch.getText());		
	}

}
