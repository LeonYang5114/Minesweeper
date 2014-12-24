import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GameMenu extends JMenuBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5489106248480118166L;
	private Game game;
	private JMenu gameMenu;
	private JMenu helpMenu;
	private JMenuItem newGame;
	private JMenuItem statistic;
	private JMenuItem easy;
	private JMenuItem medium;
	private JMenuItem hard;
	private JMenuItem custom;
	private JMenuItem exit;
	private JMenuItem help;
	private JMenuItem credits;
	private int currDifficulty = Game.EASY;
	private int[] scoreBoard = new int[] { 999, 999, 999 };
	private String[] scoreBoardNames = new String[] { "", "", "" };

	public GameMenu(Game g) {
		game = g;
		gameMenu = new JMenu("Game");
		helpMenu = new JMenu("Help");
		add(gameMenu);
		add(helpMenu);
		ActionListener l = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object source = e.getSource();
				if (source.equals(newGame)) {
					game.restart();
				} else if (source.equals(statistic)) {
					JTextArea jta = new JTextArea("Easy:\t"
							+ scoreBoard[Game.EASY] + "\t"
							+ scoreBoardNames[Game.EASY] + "\t\nMedium:\t"
							+ scoreBoard[Game.MEDIUM] + "\t"
							+ scoreBoardNames[Game.MEDIUM] + "\t\nHard:\t"
							+ scoreBoard[Game.HARD] + "\t"
							+ scoreBoardNames[Game.HARD] + "\t");
					jta.setEditable(false);
					jta.setHighlighter(null);
					JOptionPane.showMessageDialog(game, jta, "High Scores",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (source.equals(easy)) {
					game.restart(Game.EASY);
					currDifficulty = Game.EASY;
				} else if (source.equals(medium)) {
					game.restart(Game.MEDIUM);
					currDifficulty = Game.MEDIUM;
				} else if (source.equals(hard)) {
					game.restart(Game.HARD);
					currDifficulty = Game.HARD;
				} else if (source.equals(custom)) {
					JTextField w = new JTextField(5);
					JTextField h = new JTextField(5);
					JTextField nm = new JTextField(5);
					JPanel p = new JPanel();
					p.add(new JLabel("Width: "));
					p.add(w);
					p.add(Box.createHorizontalStrut(15));
					p.add(new JLabel("Height: "));
					p.add(h);
					p.add(Box.createHorizontalStrut(15));
					p.add(new JLabel("Number of Mines: "));
					p.add(nm);
					if (JOptionPane.showConfirmDialog(game, p,
							"Please Enter width, height and number of mines",
							JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
						int width, height, numberMines;
						try {
							width = Integer.parseInt(w.getText());
							height = Integer.parseInt(h.getText());
							numberMines = Integer.parseInt(nm.getText());
						} catch (NumberFormatException ect) {
							JOptionPane
									.showMessageDialog(game, "Invalid Input");
							return;
						}
						width = Math.min(Math.max(9, width), 30);
						height = Math.min(Math.max(9, height), 24);
						numberMines = Math.min(Math.max(10, numberMines),
								(width - 1) * (height - 1));
						game.restart(width, height, numberMines);
						currDifficulty = Game.CUSTOM;
					}
				} else if (source.equals(exit)) {
					game.exit();
				} else if (source.equals(help)) {
					JOptionPane.showMessageDialog(game,
							"Google \"how to play minesweeper\" :D", "Help",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (source.equals(credits)) {
					JOptionPane.showMessageDialog(game, "by Leon Yang" + "\n"
							+ "leonyang1994@gmail.com", "Credits",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		};
		newGame = new JMenuItem("New Game");
		newGame.addActionListener(l);
		statistic = new JMenuItem("Statistic");
		statistic.addActionListener(l);
		easy = new JMenuItem("easy");
		easy.addActionListener(l);
		medium = new JMenuItem("medium");
		medium.addActionListener(l);
		hard = new JMenuItem("hard");
		hard.addActionListener(l);
		custom = new JMenuItem("custom");
		custom.addActionListener(l);
		exit = new JMenuItem("Exit");
		exit.addActionListener(l);
		help = new JMenuItem("Help");
		help.addActionListener(l);
		credits = new JMenuItem("Credits");
		credits.addActionListener(l);
		gameMenu.add(newGame);
		gameMenu.add(statistic);
		gameMenu.addSeparator();
		gameMenu.add(easy);
		gameMenu.add(medium);
		gameMenu.add(hard);
		gameMenu.add(custom);
		gameMenu.addSeparator();
		gameMenu.add(exit);
		helpMenu.add(help);
		helpMenu.add(credits);
	}

	public void updateScore(int seconds) {
		if (currDifficulty != -1 && seconds < scoreBoard[currDifficulty]) {
			scoreBoard[currDifficulty] = seconds;
			scoreBoardNames[currDifficulty] = (String) JOptionPane
					.showInputDialog(game, "Your Name: ", "New Record!",
							JOptionPane.INFORMATION_MESSAGE, null, null, "");
		}
	}
}
