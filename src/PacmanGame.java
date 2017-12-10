
import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

class PacmanGame extends JFrame implements KeyListener, Runnable,
		ComponentListener {
	private static final long serialVersionUID = 1L;

	int Wall_garo = 17;// 블럭전체 가로
	int Wall_sero = 15;// 블럭전체 세로
	int cnt_coin = 110;// dot개수

	// 아이콘지정
	final ImageIcon Icon_wall = new ImageIcon("wall.png");
	final ImageIcon Icon_road = new ImageIcon("road.png");
	final ImageIcon Icon_coin = new ImageIcon("s_dot.png");
	final ImageIcon Icon_candy = new ImageIcon("candy.png");
	final ImageIcon Icon_enemy = new ImageIcon("enemy.png");
	final ImageIcon pacmanRt = new ImageIcon("pacmanRt.png");
	final ImageIcon pacmanLt = new ImageIcon("pacmanLt.png");
	final ImageIcon pacmanUp = new ImageIcon("pacmanUp.png");
	final ImageIcon pacmanDn = new ImageIcon("pacmanDn.png");

	JFrame PM_frame = new JFrame();
	Container PM_contain = null;

	JLabel pacman = new JLabel();// 팩맨개체생성
	JLabel enemy = new JLabel();// 술래개체생성
	JLabel wall[][] = new JLabel[Wall_garo][Wall_sero];// 벽면개체생성

	public PacmanGame() {// 팩맨게임의 생성자

		PM_contain = PM_frame.getContentPane();
		PM_contain.setBackground(Color.gray);
		PM_frame.setLayout(null);
		PM_frame.setBounds(380, 100, 855, 778);
		PM_frame.setTitle("PACMAN Ver1.0");
		PM_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		PM_frame.setVisible(true);
		PM_frame.setResizable(false);

		PM_frame.addKeyListener(this);
		PM_frame.addComponentListener(this);

		init(); 
		Thread th = new Thread(this);
		th.start();

	}

	public void init() {
		
		// 술래 속성정의
		enemy.setBounds(400, 350, 50, 50);
		enemy.setIcon(Icon_enemy);
		PM_contain.add(enemy);
		
		// 팩맨 속성정의
		pacman.setBounds(400, 580, 50, 50);// 기본위치와 크기(x,y x크기,y크기)
		pacman.setIcon(pacmanRt);
		PM_contain.add(pacman);

		// 벽생성.일단 모든 면적을 벽으로 만든다
		for (int i = 0; i < Wall_garo; i++) {
			for (int j = 0; j < Wall_sero; j++) {

				wall[i][j] = new JLabel();
				wall[i][j].setBounds(i * 50, j * 50, 50, 50);
				PM_contain.add(wall[i][j]);
				wall[i][j].setIcon(Icon_wall);
			}
		}

		// Coin아이콘을 배치
		for (int i = 1; i < 16; i++) {
			wall[i][1].setIcon(Icon_coin);
		}
		for (int i = 3; i < 14; i++) {
			wall[i][3].setIcon(Icon_coin);
			wall[i][7].setIcon(Icon_coin);
			wall[i][9].setIcon(Icon_coin);
		}
		for (int i = 1; i < 14; i++) {
			wall[1][i].setIcon(Icon_coin);
			wall[15][i].setIcon(Icon_coin);
		}
		for (int i = 4; i < 7; i++) {
			wall[3][i].setIcon(Icon_coin);
			wall[5][i].setIcon(Icon_coin);
			wall[11][i].setIcon(Icon_coin);
			wall[13][i].setIcon(Icon_coin);
		}
		for (int i = 10; i < 14; i++) {
			wall[3][i].setIcon(Icon_coin);
			wall[13][i].setIcon(Icon_coin);
		}
		for (int i = 5; i < 12; i++) {
			wall[i][13].setIcon(Icon_coin);
		}
		for (int i = 4; i < 6; i++) {
			wall[i][11].setIcon(Icon_coin);
			wall[i + 7][11].setIcon(Icon_coin);
		}
		wall[8][2].setIcon(Icon_coin);
		wall[2][5].setIcon(Icon_coin);
		wall[14][5].setIcon(Icon_coin);
		wall[5][12].setIcon(Icon_coin);
		wall[11][12].setIcon(Icon_coin);
		wall[2][13].setIcon(Icon_coin);
		wall[14][13].setIcon(Icon_coin);

		//안전영역		
		for (int i = 7; i < 10; i++) {
			wall[i][5].setIcon(Icon_candy);
			wall[i][6].setIcon(Icon_candy);
			wall[i][11].setIcon(Icon_candy);
			wall[i][12].setIcon(Icon_candy);
		}
		// dot아이콘 배치 끝
	}
}