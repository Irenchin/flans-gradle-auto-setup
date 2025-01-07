package irenchin.flansgradle.tasks;

import irenchin.flansgradle.Main;

public class TaskDownloadForge extends Task {

	@Override
	public boolean execute() {
		updateStatus("\n\n\n");
		updateStatus("Downloading Forge MDK...\n");
		return downloadAndExtractZip("https://drive.usercontent.google.com/u/0/uc?id=1v_rtgHKBt3oy7YXeOvIFdifdWki2X6TI&export=download", Main.folder.toPath().toString());	
	}

}
