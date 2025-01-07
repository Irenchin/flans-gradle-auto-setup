package irenchin.flansgradle;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import irenchin.flansgradle.tasks.Task;
import irenchin.flansgradle.tasks.TaskConfigureGradle;
import irenchin.flansgradle.tasks.TaskDownloadFlans;
import irenchin.flansgradle.tasks.TaskDownloadForge;
import irenchin.flansgradle.tasks.TaskMoveContentPacks;
import irenchin.flansgradle.tasks.TaskSetUpWorkspace;
import irenchin.flansgradle.tasks.TaskUnpackFlans;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Main {

	public JFrame frmFlansGradleSet;
	public static JTextArea textArea;
	public static JLabel lblNewLabel;
	public static JButton btnRun;
	public static File folder = null;
	public static boolean runTasks = false;
	public static ConcurrentLinkedQueue<Task> tasks = new ConcurrentLinkedQueue<Task>();
	public static JTextField user;
	public static JTextField repo;
	public static JTextField branch;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmFlansGradleSet.setVisible(true);
					Thread t = new Thread() {
						@Override
						public void run() {
							super.run();
							long l = System.currentTimeMillis();
							while(true) {
								if(System.currentTimeMillis()-l>1000) {
									if(runTasks) {
										Task task = tasks.poll();
										if(task!=null) {
											System.out.println(task);
											boolean result = task.execute();
											if(!result) {
												runTasks = false;
											} else {
												if(tasks.peek()==null) {
													Task.updateStatus("\n\n\n");
													Task.updateStatus("Complete!!!");
												}
											}
										}
									} else {
										if(folder!=null&&folder.isDirectory()&&folder.listFiles()!=null&&folder.listFiles().length==0) {
											btnRun.setEnabled(true);
										}
									}
									l = System.currentTimeMillis();
								}
							}
						}
					};
					t.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFlansGradleSet = new JFrame();
		frmFlansGradleSet.setTitle("Flans Gradle Set Up");
		frmFlansGradleSet.setMinimumSize(new Dimension(800, 600));
		frmFlansGradleSet.setBounds(100, 100, 800, 600);
		frmFlansGradleSet.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFlansGradleSet.setLocationRelativeTo(null);
		frmFlansGradleSet.getContentPane().setLayout(null);

		JButton btnNewButton = new JButton("Choose Folder");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(folder);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int ret = chooser.showOpenDialog(frmFlansGradleSet);
				if(ret == JFileChooser.APPROVE_OPTION) {
					folderChanged(chooser.getSelectedFile());
				}
			}
		});
		btnNewButton.setBounds(10, 10, 766, 44);
		frmFlansGradleSet.getContentPane().add(btnNewButton);

		lblNewLabel = new JLabel("No folder chosen.");
		lblNewLabel.setBounds(10, 64, 766, 13);
		frmFlansGradleSet.getContentPane().add(lblNewLabel);

		btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnRun.setEnabled(false);
				tasks.clear();
				tasks.add(new TaskDownloadForge());
				tasks.add(new TaskDownloadFlans());
				tasks.add(new TaskUnpackFlans());
				tasks.add(new TaskConfigureGradle());
				tasks.add(new TaskSetUpWorkspace());
				tasks.add(new TaskMoveContentPacks());
				runTasks = true;
			}
		});
		btnRun.setEnabled(false);
		btnRun.setBounds(10, 215, 766, 44);
		frmFlansGradleSet.getContentPane().add(btnRun);

		JLabel lblChooseAnEmpty = new JLabel("Choose an empty folder to run.");
		lblChooseAnEmpty.setBounds(10, 87, 766, 13);
		frmFlansGradleSet.getContentPane().add(lblChooseAnEmpty);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 269, 766, 284);
		frmFlansGradleSet.getContentPane().add(scrollPane);
		
				textArea = new JTextArea();
				scrollPane.setViewportView(textArea);
				textArea.setWrapStyleWord(true);
				textArea.setLineWrap(true);
				textArea.setEditable(false);
				
				user = new JTextField();
				user.setText("FlansMods");
				user.setBounds(10, 176, 138, 19);
				frmFlansGradleSet.getContentPane().add(user);
				user.setColumns(10);
				
				JLabel lblFlansModGithub = new JLabel("Flans Mod Github Repo ( Don't touch unless you want a different repo! )");
				lblFlansModGithub.setBounds(10, 130, 766, 13);
				frmFlansGradleSet.getContentPane().add(lblFlansModGithub);
				
				JLabel lblNewLabel_1 = new JLabel("Github User");
				lblNewLabel_1.setBounds(10, 153, 138, 13);
				frmFlansGradleSet.getContentPane().add(lblNewLabel_1);
				
				JLabel lblNewLabel_1_1 = new JLabel("Repository");
				lblNewLabel_1_1.setBounds(158, 153, 138, 13);
				frmFlansGradleSet.getContentPane().add(lblNewLabel_1_1);
				
				repo = new JTextField();
				repo.setText("FlansMod");
				repo.setColumns(10);
				repo.setBounds(158, 176, 138, 19);
				frmFlansGradleSet.getContentPane().add(repo);
				
				JLabel lblNewLabel_1_1_1 = new JLabel("Branch");
				lblNewLabel_1_1_1.setBounds(306, 153, 138, 13);
				frmFlansGradleSet.getContentPane().add(lblNewLabel_1_1_1);
				
				branch = new JTextField();
				branch.setText("1.7.10");
				branch.setColumns(10);
				branch.setBounds(306, 176, 138, 19);
				frmFlansGradleSet.getContentPane().add(branch);
				
				JLabel lblThisUiCurrently = new JLabel("This UI currently works only with 1.7.10.");
				lblThisUiCurrently.setBounds(10, 107, 766, 13);
				frmFlansGradleSet.getContentPane().add(lblThisUiCurrently);
	}

	public static void folderChanged(File f) {
		folder = f;
		lblNewLabel.setText(f.toPath()+"");
		btnRun.setEnabled(false);
		if(f.exists()&&f.isDirectory()) {
			File[] l = f.listFiles();
			System.out.println(l.length);
			System.out.println(btnRun.isEnabled());
			if(l!=null&&l.length==0) {
				btnRun.setEnabled(true);
			}
		}
	}
}
