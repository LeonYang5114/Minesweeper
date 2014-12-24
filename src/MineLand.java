import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * This game is an imitation of the classic Minesweeper game on the Windows
 * platform.
 * 
 * @author Guohong Yang
 *
 */
public class MineLand extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1155115013778043961L;
	private Game game;
	private MineUnit[][] mines;
	private boolean[][] mineMap;
	private int row, col;
	private int defaultRow = 9, defaultCol = 9;
	private int numMines;
	public Image[] numberIcons = new Image[9];
	/* I drew all these images! */
	public Image undugIcon, flaggedIcon, mineIcon, explodedMineIcon,
			pressedIcon;
	private int numUndug;
	private boolean hasLose;
	private boolean hasGameStarted;
	private MineListener l = new MineListener();

	public MineLand(int r, int c, int nm, Game g) {

		try {
			for (int i = 0; i < numberIcons.length; i++) {
				numberIcons[i] = ImageIO.read(getClass().getResource(
						"images/" + i + ".png"));
			}

			undugIcon = ImageIO
					.read(getClass().getResource("images/undug.png"));
			flaggedIcon = ImageIO.read(getClass().getResource(
					"images/flagged.png"));
			mineIcon = ImageIO.read(getClass().getResource("images/mine.png"));
			explodedMineIcon = ImageIO.read(getClass().getResource(
					"images/explodedMine.png"));
			pressedIcon = ImageIO.read(getClass().getResource(
					"images/pressed.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		game = g;

		resetMineLand(r, c, nm);

	}

	/**
	 * Dig all the squares in the mine land that are not surround with any mine
	 * by recursion.
	 * 
	 * @param r
	 *            the row of the square to be dug
	 * @param c
	 *            the column of the square to be dug
	 */
	public void digAll(int r, int c) {
		if (mines[r][c].getStatus() != MineUnit.UNDUG
				&& mines[r][c].getStatus() != MineUnit.PRESSED)
			return;
		mines[r][c].dig();
		numUndug--;
		int minesAround = getMinesAround(r, c);
		if (minesAround == 0) {
			for (int i = r - 1; i <= r + 1; i++) {
				if (i < 0 || i >= row)
					continue;
				for (int j = c - 1; j <= c + 1; j++) {
					if (j < 0 || j >= col)
						continue;
					digAll(i, j);
				}
			}
		}
	}

	/**
	 * If a mine is dug, review all the squares.
	 */
	public void explode() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				mines[i][j].review();
			}
		}
		paintComponents(getGraphics());
		hasLose = true;
		game.lose();
	}

	/**
	 * set the squares around the mine unit unpressed
	 * 
	 * @param mu
	 *            the target mine unit
	 */
	public void unpressAround(MineUnit mu) {
		int r = mu.getRow();
		int c = mu.getCol();
		for (int i = r - 1; i <= r + 1; i++) {
			if (i < 0 || i >= row)
				continue;
			for (int j = c - 1; j <= c + 1; j++) {
				if (j < 0 || j >= col)
					continue;
				mines[i][j].unpress();
			}
		}
	}

	/**
	 * set the squares around the mine unit pressed
	 * 
	 * @param mu
	 *            the target mine unit
	 */
	public void pressAround(MineUnit mu) {
		int r = mu.getRow();
		int c = mu.getCol();
		for (int i = r - 1; i <= r + 1; i++) {
			if (i < 0 || i >= row)
				continue;
			for (int j = c - 1; j <= c + 1; j++) {
				if (j < 0 || j >= col)
					continue;
				mines[i][j].press();
			}
		}
	}

	/**
	 * the game is running if the game has started, and the game has not won or
	 * lose
	 * 
	 * @return
	 */
	public boolean isGameRunning() {
		return hasGameStarted && !hasLose && !(numUndug == numMines);
	}

	/**
	 * reset the game to a new initial configuration
	 * 
	 * @param r
	 *            the number of rows
	 * @param c
	 *            the number of columns
	 * @param nm
	 *            the number of mines
	 */
	public void resetMineLand(int r, int c, int nm) {
		hasGameStarted = false;
		hasLose = false;
		row = (r > 0 && r < 200) ? r : defaultRow;
		col = (c > 0 && c < 200) ? c : defaultCol;
		numMines = (nm > 0 && nm < row * col) ? nm : row * col / 8;
		numUndug = row * col;
		mineMap = new boolean[row][col];
		mines = new MineUnit[row][col];
		boolean[] temp = new boolean[row * col];
		for (int i = 0; i < numMines; i++)
			temp[i] = true;
		Random rnd = new Random();
		for (int i = temp.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			boolean t = temp[index];
			temp[index] = temp[i];
			temp[i] = t;
		}
		for (int i = 0; i < row * col; i++)
			mineMap[i / col][i % col] = temp[i];

		this.removeAll();
		this.setLayout(new GridLayout(row, col));
		this.setVisible(false);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				mines[i][j] = new MineUnit(this, getMinesAround(i, j), i, j);
				mines[i][j].addMouseListener(l);
				this.add(mines[i][j]);
			}
		}
		this.setVisible(true);
	}

	/**
	 * return the mines around a given square
	 * @param r the row of the square
	 * @param c the column of the square
	 * @return the number of mines around
	 */
	private int getMinesAround(int r, int c) {
		if (mineMap[r][c])
			return -1;
		int counter = 0;
		for (int i = r - 1; i <= r + 1; i++) {
			if (i < 0 || i >= row)
				continue;
			for (int j = c - 1; j <= c + 1; j++) {
				if (j < 0 || j >= col)
					continue;
				if (mineMap[i][j])
					counter++;
			}
		}
		return counter;
	}

	class MineListener implements MouseListener {

		private boolean isLeftDown, isRightDown;
		private MineUnit lastUnit;

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
			MineUnit source = (MineUnit) e.getSource();
			if (isLeftDown && isRightDown) {
				lastUnit = source;
				MineLand.this.pressAround(source);
			} else if (isLeftDown || isRightDown) {
				lastUnit = source;
				if (isLeftDown)
					source.press();
			}
		}

		public void mouseExited(MouseEvent e) {
			MineUnit source = (MineUnit) e.getSource();
			if (isLeftDown && isRightDown) {
				MineLand.this.unpressAround(source);
			} else if (isLeftDown && source.getStatus() == MineUnit.PRESSED)
				source.unpress();
		}

		public void mousePressed(MouseEvent e) {
			if (!hasGameStarted) {
				hasGameStarted = true;
				game.getTopPanel().startCounting();
			}
			MineUnit source = (MineUnit) e.getSource();
			if (e.getButton() == MouseEvent.BUTTON1) {
				source.press();
				isLeftDown = true;
				if (isRightDown)
					MineLand.this.pressAround(lastUnit);
				else
					lastUnit = source;
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				isRightDown = true;
				if (isLeftDown) {
					MineLand.this.pressAround(lastUnit);
					return;
				}
				lastUnit = source;
				game.getTopPanel().useFlag(source.flag());
			}
		}

		public void mouseReleased(MouseEvent e) {
			if (!isLeftDown && !isRightDown)
				return;
			if (isLeftDown && isRightDown) {
				int r = lastUnit.getRow();
				int c = lastUnit.getCol();
				if (lastUnit.getStatus() == MineUnit.DUG) {
					int numUnitFlagged = 0;
					for (int i = r - 1; i <= r + 1; i++) {
						if (i < 0 || i >= row)
							continue;
						for (int j = c - 1; j <= c + 1; j++) {
							if (j < 0 || j >= col)
								continue;
							if (mines[i][j].getStatus() == MineUnit.FLAGGED)
								numUnitFlagged++;
						}
					}
					if (numUnitFlagged == lastUnit.getMinesAround()) {
						for (int i = r - 1; i <= r + 1; i++) {
							if (i < 0 || i >= row)
								continue;
							for (int j = c - 1; j <= c + 1; j++) {
								if (j < 0 || j >= col)
									continue;
								digAll(i, j);
							}
						}
						if (numUndug == numMines && !hasLose)
							game.win();
					}
				}
				MineLand.this.unpressAround(lastUnit);
				isLeftDown = false;
				isRightDown = false;
				return;
			}
			if (lastUnit.getStatus() == MineUnit.PRESSED)
				lastUnit.unpress();
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (lastUnit.getStatus() == MineUnit.UNDUG) {
					int r = lastUnit.getRow();
					int c = lastUnit.getCol();
					MineLand.this.digAll(r, c);
					paintComponents(getGraphics());
					if (numUndug == numMines && !hasLose)
						game.win();
				}
				isLeftDown = false;
			} else if (e.getButton() == MouseEvent.BUTTON3)
				isRightDown = false;
		}
	}
}
