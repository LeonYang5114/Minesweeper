import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;

public class MineUnit extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6459656187637373785L;
	private int status;
	private int row, col;
	private int minesAround;
	private MineLand mineLand;
	private Image[] icons = new Image[4];
	public static final int UNDUG = 0, DUG = 1, PRESSED = 2, FLAGGED = 3;

	public MineUnit(MineLand ml, int ma, int r, int c) {
		mineLand = ml;
		minesAround = ma;
		icons[UNDUG] = mineLand.undugIcon;
		icons[DUG] = (ma != -1) ? mineLand.numberIcons[ma] : mineLand.mineIcon;
		icons[PRESSED] = mineLand.pressedIcon;
		icons[FLAGGED] = mineLand.flaggedIcon;
		status = 0;
		row = r;
		col = c;
		setPreferredSize(new Dimension(20, 20));
	}

	public int getStatus() {
		return status;
	}

	public int getMinesAround() {
		return minesAround;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public void dig() {
		if (status == UNDUG || status == PRESSED) {
			status = DUG;
			if (minesAround == -1) {
				icons[DUG] = mineLand.explodedMineIcon;
				mineLand.explode();
			}
			repaint();
		}
	}

	public void press() {
		if (status == UNDUG) {
			status = PRESSED;
			repaint();
		}
	}

	public void unpress() {
		if (status == PRESSED) {
			status = UNDUG;
			repaint();
		}
	}

	public int flag() {
		if (status == FLAGGED) {
			status = UNDUG;
			repaint();
			return 1;
		}
		if (status == UNDUG) {
			status = FLAGGED;
			repaint();
			return -1;
		}
		return 0;
	}

	public void review() {
		if (status == UNDUG) {
			status = DUG;
		}
	}

	public void paintComponent(Graphics g) {
		g.drawImage(icons[status], 0, 0, this);
	}
}