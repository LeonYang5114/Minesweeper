import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TopPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5670159513332111670L;
	private Game game;
	private int secondsPassed;
	private int flagsLeft;
	private JLabel secondBoard, flagBoard;
	private JButton restart;
	private ImageIcon smilingFace, sadFace;

	public TopPanel(Game g) {
		game = g;
		flagsLeft = game.getNumberMines();
		smilingFace = new ImageIcon(getClass().getResource(
				"images/smilingFace.png"));
		sadFace = new ImageIcon(getClass().getResource("images/sadFace.png"));
		restart = new JButton(smilingFace);
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.restart();
			}

		});
		restart.setPreferredSize(new Dimension(smilingFace.getIconWidth(),
				smilingFace.getIconHeight()));
		secondBoard = new JLabel("0", SwingConstants.RIGHT);
		secondBoard.setPreferredSize(new Dimension(40, 35));
		flagBoard = new JLabel(flagsLeft + "");
		flagBoard.setPreferredSize(new Dimension(40, 35));
		setPreferredSize(new Dimension(game.getMineLand().getWidth(), 45));
		this.setBackground(new Color(192, 192, 192));
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(secondBoard);
		this.add(new Box.Filler(new Dimension(10, 45), new Dimension(10, 45),
				new Dimension(Short.MAX_VALUE, 45)));
		this.add(restart);
		this.add(new Box.Filler(new Dimension(10, 45), new Dimension(10, 45),
				new Dimension(Short.MAX_VALUE, 45)));
		this.add(flagBoard);
	}

	public void changeFace() {
		restart.setIcon(sadFace);
		restart.repaint();
	}
	
	public void useFlag(int flagIndex) {
		flagsLeft += flagIndex;
		flagBoard.setText(flagsLeft + "");
	}

	public void startCounting() {
		(new Thread() {
			public void run() {
				long startTime = System.currentTimeMillis();
				long now = System.currentTimeMillis();
				while (game.getMineLand().isGameRunning()) {
					try {
						yield();
						sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					now = System.currentTimeMillis();
					int temp = (int) ((now - startTime) / 1000);
					if(temp != secondsPassed) {
						secondsPassed = temp;
						secondBoard.setText(secondsPassed + "");
					}
				}
			}
		}).start();
	}

	public void restart() {
		restart.setIcon(smilingFace);
		secondBoard.setText("0");
		flagsLeft = game.getNumberMines();
		flagBoard.setText(flagsLeft + "");
	}

	public int getSecondsPassed() {
		return secondsPassed;
	}
}
