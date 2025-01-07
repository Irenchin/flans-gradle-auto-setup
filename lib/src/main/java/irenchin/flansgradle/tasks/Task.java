package irenchin.flansgradle.tasks;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import irenchin.flansgradle.Main;

public abstract class Task {

	public abstract boolean execute();

	public static boolean downloadAndExtractZip(String fileURL, String saveDir) {
		try {
			File tempZipFile = new File(saveDir + "/temp.zip");
			try (InputStream inputStream = new URL(fileURL).openStream();
					BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tempZipFile))) {
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
					updateStatus(".");
				}
			}
			try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(tempZipFile))) {
				ZipEntry entry;
				while ((entry = zipInputStream.getNextEntry()) != null) {
					File outputFile = new File(saveDir + "/" + entry.getName());
					if (entry.isDirectory()) {
						outputFile.mkdirs();
					} else {
						try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {
							byte[] buffer = new byte[4096];
							int bytesRead;
							while ((bytesRead = zipInputStream.read(buffer)) != -1) {
								bos.write(buffer, 0, bytesRead);
								updateStatus(".");
							}
						}
					}
					zipInputStream.closeEntry();
				}
			}
			tempZipFile.delete();
			return true;
		} catch (Exception e) {
			updateStatus(e);
			return false;
		}
	}

	public static boolean moveContentsToParentAndDelete(String childFolderPath) {
	    try {
	        File childFolder = new File(childFolderPath);
	        if (!childFolder.isDirectory()) {
	            throw new IllegalArgumentException("Provided file is not a directory: " + childFolder);
	        }
	        File parentFolder = childFolder.getParentFile();
	        if (parentFolder == null) {
	            throw new IllegalStateException("Child folder has no parent: " + childFolder);
	        }
	        moveFilesRecursive(childFolder, parentFolder);
//	        if (!childFolder.delete()) {
//	            throw new IOException("Failed to delete the empty folder: " + childFolder);
//	        }
	        return true;
	    } catch (Exception e) {
	        updateStatus(e);
	        return false;
	    }
	}

	private static void moveFilesRecursive(File source, File destination) throws IOException {
	    File[] files = source.listFiles();
	    if (files != null) {
	        for (File file : files) {
	        	updateStatus(".");
	            File destFile = new File(destination, file.getName());
	            if (file.isDirectory()) {
	                if (!destFile.exists() && !destFile.mkdirs()) {
	                    throw new IOException("Failed to create directory: " + destFile);
	                }
	                moveFilesRecursive(file, destFile);
	            } else {
	                if (destFile.exists() && !destFile.delete()) {
	                    throw new IOException("Failed to delete existing file: " + destFile);
	                }
	                if (!file.renameTo(destFile)) {
	                    throw new IOException("Failed to move file: " + file + " to " + destFile);
	                }
	            }
	        }
	    }
	}

	public static boolean replaceLine(String filePath, String targetLine, String replacementLine) {
		try {
			List<String> lines = Files.readAllLines(Paths.get(filePath));
			for (int i = 0; i < lines.size(); i++) {
				if (lines.get(i).trim().startsWith(targetLine.trim())) {
					lines.set(i, replacementLine);
					updateStatus(".");
				}
			}
			Files.write(Paths.get(filePath), lines);
			return true;
		} catch (Exception e) {
			updateStatus(e);
			return false;
		}
	}

	public static boolean runCommand(String command, String workingDir) {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder();
			if (workingDir != null && !workingDir.isEmpty()) {
				processBuilder.directory(new File(workingDir));
			}
			processBuilder.command("cmd.exe", "/c", command);
			Process process = processBuilder.start();
			StringBuilder output = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					output.append(line).append(System.lineSeparator());
					updateStatus(line+"\n");
				}
			}
			int exitCode = process.waitFor();
			if (exitCode != 0) {
				throw new RuntimeException("Command failed with exit code: " + exitCode);
			}
			return true;
		} catch (Exception e) {
			updateStatus(e);
			return false;
		}
	}
	
	public static boolean moveContents(String sourceFolderPath, String targetFolderPath) {
	    try {
	        File sourceFolder = new File(sourceFolderPath);
	        File targetFolder = new File(targetFolderPath);
	        if (!sourceFolder.isDirectory()) {
	            throw new IllegalArgumentException("Source is not a directory: " + sourceFolderPath);
	        }
	        if (!targetFolder.exists() && !targetFolder.mkdirs()) {
	            throw new IOException("Failed to create target folder: " + targetFolderPath);
	        }
	        File[] files = sourceFolder.listFiles();
	        if (files != null) {
	            for (File file : files) {
	            	updateStatus(".");
	                File destination = new File(targetFolder, file.getName());
	                if (file.isDirectory()) {
	                    if (!moveContents(file.getPath(), destination.getPath())) {
	                        throw new IOException("Failed to move directory: " + file.getPath());
	                    }
	                } else {
	                    if (destination.exists() && !destination.delete()) {
	                        throw new IOException("Failed to delete existing file: " + destination);
	                    }
	                    if (!file.renameTo(destination)) {
	                        throw new IOException("Failed to move file: " + file + " to " + destination);
	                    }
	                }
	            }
	        }
	        if (!sourceFolder.delete()) {
	            throw new IOException("Failed to delete the empty source folder: " + sourceFolderPath);
	        }
	        return true;
	    } catch (Exception e) {
	        updateStatus(e);
	        return false;
	    }
	}

	public static void updateStatus(String status) {
		Main.textArea.append(status);
		Main.textArea.setCaretPosition(Main.textArea.getDocument().getLength());
	}

	public static void updateStatus(Throwable throwable) {
		StringWriter stringWriter = new StringWriter();
		try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
			throwable.printStackTrace(printWriter);
			Main.textArea.append(stringWriter.toString()+"\n");
			Main.textArea.setCaretPosition(Main.textArea.getDocument().getLength());
		}
	}

}
