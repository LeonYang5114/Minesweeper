import java.awt.BorderLayout;

import javax.swing.JFrame;


public class Game extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2566325521038748850L;
	private MineLand mineLand;
	private TopPanel topPanel;
	private GameMenu gameMenu;
	public static final int EASY = 0, MEDIUM = 1, HARD = 2, CUSTOM = -1;
	private int width = 9, height = 9, numberMines = 10;
	
	public Game() {
		mineLand = new MineLand(height, width, numberMines, this);
		topPanel = new TopPanel(this);
		gameMenu = new GameMenu(this);
		this.setJMenuBar(gameMenu);
		this.setLayout(new BorderLayout());
		this.add(topPanel, BorderLayout.NORTH);
		this.add(mineLand, BorderLayout.SOUTH);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pack();
		this.setTitle("Mine Sweeper");
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public int getNumberMines() {
		return numberMines;
	}
	
	public void lose() {
		System.out.println("Game Over");
		topPanel.changeFace();
	}
	
	public void win() {
		System.out.println("Congratulations!");
		gameMenu.updateScore(topPanel.getSecondsPassed());
	}
	
	public void restart(int difficulty) {
		switch(difficulty) {
		case EASY:
			restart(9, 9, 10);
			break;
		case MEDIUM:
			restart(16, 16, 40);
			break;
		case HARD:
			restart(30, 16, 99);
			break;
		default:
			restart();
			break;
		}
	}
	
	public void restart(int w, int h, int nm) {
		width = w;
		height = h;
		numberMines = nm;
		restart();
	}
	
	public void restart() {
		topPanel.restart();
		mineLand.resetMineLand(height, width, numberMines);
		pack();
	}
	
	public void exit() {
		dispose();
	}
	
	public MineLand getMineLand() {
		return mineLand;
	}
	
	public TopPanel getTopPanel() {
		return topPanel;
	}
	
	public static void main(String[] args) {
		new Game();
	}
}
