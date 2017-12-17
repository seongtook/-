package gimal;
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
	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String temp = "";

		temp = JOptionPane.showInputDialog("시작할까요?\n" + " (끝내려면 -1을 입력하세요.)");

		// 사용자가 취소버튼을 누르거나 -1을 입력하면 창을 닫는다.
		if (temp == null || temp.equals("-1")) {
			PM_frame.dispose();
		}

		//술래의 자동이동을 위한 변수
		//술래 이동의 기본 메카니즘 : 갈림길이나 막다른길이 나올때까지 무조건직진 갈림길이나 막다른 길이 나오면 진행할 방향을 랜덤하게 선택함
		int x = enemy.getX();// 술래의 x좌표값
		int y = enemy.getY();// 술래의 y좌표값

		int go = 0;
		int Up = 0,Down=0,Left=0,Right=0;
		
		while (PM_frame.isFocused()) {

			try {

				go = (int) (Math.random() * 4);

				// 충돌테스트를 위한 준비
				Point pt,ptPM;
				Rectangle rectBullet = new Rectangle();
				Rectangle rectBulletPM = new Rectangle();
				Rectangle rectBullet2 = new Rectangle();
				Rectangle rectBullet3 = new Rectangle();


				
				int g = 2;//술래의 단위 이동거리 반드시 50(길너비)의 인수
				int delay=10;
				int speed;
				boolean strait = true;// 갈림길 까지 한방향유지를 위한 변수(0,1)
				

				switch (go) {
				case 0://위로이동
					while (strait == true) {
						Thread.sleep(delay);
						speed = g;

						y -= speed;// 일단 위치를 옮긴 후 아래 충돌테스트에서 충돌이 발생하면 다시 제자리로 돌아옴
						enemy.setLocation(x, y);

						pt = enemy.getLocation();// 술래 좌표 얻어옴
						ptPM = pacman.getLocation();//팩맨좌표
						
						rectBullet = new Rectangle(pt.x, pt.y, 50, 50);// 술래사각형을만듬
						rectBulletPM = new Rectangle(ptPM.x, ptPM.y, 50, 50);// 팩맨사각형을만듬
						
						rectBullet2 = new Rectangle(pt.x - 50, pt.y+49, 50, 1);// 술래왼편사각형을만듬
						rectBullet3 = new Rectangle(pt.x + 50, pt.y+49, 50, 1);// 술래오른편사각형을만듬
						
						if (rectBullet.intersects(rectBulletPM))// 팩맨과 술래가 만나면~
							{
								failMassage();	
								break;
							}// End 팩맨과 술래가 만나면~
						
						for (int i = 0; i < Wall_garo; i++) {
							for (int j = 0; j < Wall_sero; j++) {
								Point ptTarget = wall[i][j].getLocation();// 벽면의좌표를얻어옴
								Rectangle rectTarget = new Rectangle(ptTarget.x, ptTarget.y, 50, 50);// 벽면사각형을만듬
								if (rectBullet.intersects(rectTarget))// 두개의 사각형이 겹치는지 검사 시작
								{
									if ((wall[i][j].getIcon()).equals(Icon_wall)) {
										speed = -g;// 충돌발생하면 바로 전위치로 돌아옴
										strait = false;
										break;// 한번이라도 충돌이 발생하면 더이상 포문을 반복하지 않음
									} else
										speed = 0;
								}// End 두개사각형 겹치는 검사 끝
								// 왼쪽 오른쪽에 길이 있는지 검사
								if ((rectBullet2.intersects(rectTarget))|| (rectBullet3.intersects(rectTarget))) {
									if (((wall[i][j].getIcon()).equals(Icon_coin))||((wall[i][j].getIcon()).equals(Icon_road))) {
										if(Up==0){
											strait = false;
											Up=50/g;
											continue;
										}
										Up--;
									}
								}//end 왼쪽 오른쪽에 길이 있는지 검사
							}// for(j)끝
							if (speed == -g)
								break;
						}// for(i)끝
						
						y -= speed;
					}// while문 끝
					
					break;

				case 1://아래로이동
					while (strait == true) {
						Thread.sleep(delay);
						speed = g;
						y += speed;
						enemy.setLocation(x, y);

						pt = enemy.getLocation();// 술래 좌표 얻어옴
						ptPM = pacman.getLocation();//팩맨좌표
						
						rectBullet = new Rectangle(pt.x, pt.y, 50, 50);// 술래사각형을만듬
						rectBulletPM = new Rectangle(ptPM.x, ptPM.y, 50, 50);// 팩맨사각형을만듬
						
						rectBullet2 = new Rectangle(pt.x - 50, pt.y, 50, 1);// 술래왼편사각형을만듬
						rectBullet3 = new Rectangle(pt.x + 50, pt.y, 50, 1);// 술래오른편사각형을만듬
				
						if (rectBullet.intersects(rectBulletPM))// 팩맨과 술래가 만나면~
						{
							failMassage();
							break;
						}// End 팩맨과 술래가 만나면~
						
						for (int i = 0; i < Wall_garo; i++) {
							for (int j = 0; j < Wall_sero; j++) {
								Point ptTarget = wall[i][j].getLocation();// 타겟의좌표를얻어옴
								Rectangle rectTarget = new Rectangle(ptTarget.x, ptTarget.y, 50, 50);// 타겟사각형을만듬

								if (rectBullet.intersects(rectTarget))// 두개의사각형이겹치는지검사 시작
								{
									if ((wall[i][j].getIcon()).equals(Icon_wall)) {
										speed = -g;// 충돌발생하면 바로 전위치로 돌아옴
										strait = false;
										break;// 한번이라도 충돌이 발생하면 더이상 포문을 반복하지 않음
									} else
										speed = 0;
								}// End 두개사각형 겹치는 검사 끝
									// 왼쪽에 길이 있는지 검사
								
								if ((rectBullet2.intersects(rectTarget))||(rectBullet3.intersects(rectTarget))) {
									if (((wall[i][j].getIcon()).equals(Icon_coin))||((wall[i][j].getIcon()).equals(Icon_road))) {
										if(Down==0){
											strait = false;
											Down=50/g;
											continue;
										}
										Down--;
									}
								}
							}// for(j)끝
							if (speed == -g)
								break;
						}// for(i)끝
						
						y += speed;
					}// while문 끝
					
					break;

				case 2://왼쪽으로 이동
					while (strait == true) {
						Thread.sleep(delay);
						speed = g;
						x -= speed;
						enemy.setLocation(x, y);
						pt = enemy.getLocation();// 술래 좌표 얻어옴
						ptPM = pacman.getLocation();//팩맨좌표
						rectBullet = new Rectangle(pt.x, pt.y, 50, 50);// 술래사각형을만듬
						rectBulletPM = new Rectangle(ptPM.x, ptPM.y, 50, 50);// 팩맨사각형을만듬

						rectBullet2 = new Rectangle(pt.x+49, pt.y - 50, 1, 50);// 술래위편사각형을만듬
						rectBullet3 = new Rectangle(pt.x+49, pt.y + 50, 1, 50);// 술래아래편사각형을만듬

						if (rectBullet.intersects(rectBulletPM))// 팩맨과 술래가 만나면~
						{
							failMassage();
							break;
						}// End 팩맨과 술래가 만나면~
						for (int i = 0; i < Wall_garo; i++) {
							for (int j = 0; j < Wall_sero; j++) {
								Point ptTarget = wall[i][j].getLocation();// 타겟의좌표를얻어옴
								Rectangle rectTarget = new Rectangle(
										ptTarget.x, ptTarget.y, 50, 50);// 타겟사각형을만듬
								if (rectBullet.intersects(rectTarget))// 두개의사각형이겹치는검사 시작
								{
									if ((wall[i][j].getIcon()).equals(Icon_wall)) {
										speed = -g;// 충돌발생하면 바로 전위치로 돌아옴
										strait = false;
										break;// 한번이라도 충돌이 발생하면 더이상 포문을 반복하지 않음
									} else
										speed = 0;
								}// End 두개사각형 겹치는 검사 끝
									// 위, 아래쪽에 길이나 코인이 있는지 검사
								if ((rectBullet2.intersects(rectTarget)) || (rectBullet3.intersects(rectTarget))) {
									if (((wall[i][j].getIcon()).equals(Icon_coin))||((wall[i][j].getIcon()).equals(Icon_road))) {
										if(Left==0){
											strait = false;// while문 종료
											Left=50/g;
											continue;
										}
										Left--;
									}
								}
							}// for(j)끝
							if (speed == -g)
								break;
						}// for(i)끝
						
						x -= speed;
					}// while문 끝
					break;
				case 3://오른쪽으로 이동
					while (strait == true) {
						Thread.sleep(delay);
						speed = g;
						x += speed;
						enemy.setLocation(x, y);

						pt = enemy.getLocation();// 술래 좌표 얻어옴
						ptPM = pacman.getLocation();//팩맨좌표
						
						rectBullet = new Rectangle(pt.x, pt.y, 50, 50);// 술래사각형을만듬
						rectBulletPM = new Rectangle(ptPM.x, ptPM.y, 50, 50);// 팩맨사각형을만듬
						
						rectBullet2 = new Rectangle(pt.x, pt.y - 50, 1, 50);// 술래위편사각형을만듬
						rectBullet3 = new Rectangle(pt.x, pt.y + 50, 1, 50);// 술래아래편사각형을만듬
					
						if (rectBullet.intersects(rectBulletPM))// 팩맨과 술래가 만나면~
						{
							failMassage();
							break;
						}// End 팩맨과 술래가 만나면~
						
						for (int i = 0; i < Wall_garo; i++) {
							for (int j = 0; j < Wall_sero; j++) {
								Point ptTarget = wall[i][j].getLocation();// 타겟의좌표를얻어옴
								Rectangle rectTarget = new Rectangle(
										ptTarget.x, ptTarget.y, 50, 50);// 타겟사각형을만듬

								if (rectBullet.intersects(rectTarget))// 두개의사각형이겹치는검사 시작
								{
									if ((wall[i][j].getIcon()).equals(Icon_wall)) {
										speed = -g;// 충돌발생하면 바로 전위치로 돌아옴
										strait = false;
										break;// 한번이라도 충돌이 발생하면 더이상 포문을 반복하지 않음
									} else
										speed = 0;
								}// End 두개사각형 겹치는 검사 끝
								// 위, 아래쪽에 길이나 코인이 있는지 검사
								
								if ((rectBullet2.intersects(rectTarget))|| (rectBullet3.intersects(rectTarget))) {
									if (((wall[i][j].getIcon()).equals(Icon_coin))||((wall[i][j].getIcon()).equals(Icon_road))) {
										if(Right==0){
											strait = false;// while문 종료
											Right=50/g-1;
											continue;
										}
										Right--;
									}
								}
							}// for(j)끝
							if (speed == -g)
								break;
						}// for(i)끝
						
						x += speed;
					}// 화일문끝
					
					break;

				}// 스위치문 끝

				enemy.setLocation(x, y);// 이동 후 최종위치
				
			} catch (Exception e) {
				return;
			}
		}// 화일종료
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		String temp = "";
		if (cnt_coin == 0) {
			temp = JOptionPane.showInputDialog("축하합니다~! 성공하셨습니다 ^^");
		}
		// 사용자가 취소버튼을 누르거나 -1을 입력하면 창을 닫는다.
		if (temp == null || temp.equals("-1")) {
			PM_frame.dispose();
		}

		
//팩맨 이동
		
		int x = pacman.getX();// move의 x좌표값
		int y = pacman.getY();// move의 y좌표값

		// 충돌테스트를 위한 준비
		
		Point pt = pacman.getLocation();
		Rectangle rectBullet = new Rectangle();

		int g = 10;
		int speed = g;

		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			pacman.setIcon(pacmanUp);
			y -= speed;// 일단 위치를 옮긴 후 아래 충돌테스트에서 충돌이 발생하면 다시 제자리로 돌아옴
			pacman.setLocation(x, y);

			pt = pacman.getLocation();// 팩맨의 좌표 얻어옴
			rectBullet = new Rectangle(pt.x, pt.y, 50, 50);// 팩맨 사각형을 만듬

			for (int i = 0; i < Wall_garo; i++) {
				for (int j = 0; j < Wall_sero; j++) {
					Point ptTarget = wall[i][j].getLocation();// 타겟의 좌표를 얻어옴
					Rectangle rectTarget = new Rectangle(ptTarget.x,ptTarget.y, 50, 50);// 타겟 사각형을 만듬
					if (rectBullet.intersects(rectTarget))// 두개의 사각형이 겹치는 검사 시작
					{
						if ((wall[i][j].getIcon()).equals(Icon_wall)) {
							speed = -g;// 충돌발생하면 바로 전위치로 돌아옴
							break;// 한번이라도 충돌이 발생하면 더이상 포문을 반복하지 않음
						} else if ((wall[i][j].getIcon()).equals(Icon_coin)) {//이동할 위치에 코인이 있다면,,
							speed = 0; // 현재 위치를 유지하고
							wall[i][j].setIcon(Icon_road); // 코인을 먹었으므로 코인 대신 길로 만든다.
							cnt_coin--; //코인전체 개수 1개 감소
						} else {
							speed = 0;
						}
					}// End 두개사각형 겹치는 검사 끝
				}// for(j)끝
				if (speed == -g)
					break;
			}// for(i)끝
			y -= speed;

			break;
		case KeyEvent.VK_DOWN:
			pacman.setIcon(pacmanDn);
			y += speed;
			pacman.setLocation(x, y);

			pt = pacman.getLocation();// 팩맨의 좌표 얻어옴
			rectBullet = new Rectangle(pt.x, pt.y, 50, 50);// 팩맨 사각형을 만듬
			for (int i = 0; i < Wall_garo; i++) {
				for (int j = 0; j < Wall_sero; j++) {
					Point ptTarget = wall[i][j].getLocation();// 타겟의 좌표를 얻어옴
					Rectangle rectTarget = new Rectangle(ptTarget.x,ptTarget.y, 50, 50);// 타겟 사각형을 만듬
					if (rectBullet.intersects(rectTarget))// 두개의 사각형이 겹치는 검사 시작
					{
						if ((wall[i][j].getIcon()).equals(Icon_wall)) {
							speed = -g;// 충돌발생하면 바로 전위치로 돌아옴
							break;
						} else if ((wall[i][j].getIcon()).equals(Icon_coin)) {
							speed = 0; // 충돌 발생하지 않으면 그자리에
							wall[i][j].setIcon(Icon_road);
							cnt_coin--;
						} else {
							speed = 0;
						}
					}// End 두개사각형 겹치는 검사 끝
				}// for(j)끝
				if (speed == -g)
					break;
			}// for(i)끝
			y += speed;
			break;

		case KeyEvent.VK_LEFT:
			pacman.setIcon(pacmanLt);
			x -= speed;
			pacman.setLocation(x, y);

			pt = pacman.getLocation();// 팩맨의 좌표 얻어옴
			rectBullet = new Rectangle(pt.x, pt.y, 50, 50);// 팩맨 사각형을 만듬
			for (int i = 0; i < Wall_garo; i++) {
				for (int j = 0; j < Wall_sero; j++) {
					Point ptTarget = wall[i][j].getLocation();// 타겟의 좌표를 얻어옴
					Rectangle rectTarget = new Rectangle(ptTarget.x,ptTarget.y, 50, 50);// 타겟 사각형을 만듬
					if (rectBullet.intersects(rectTarget))// 두개의 사각형이 겹치는 검사 시작
					{
						if ((wall[i][j].getIcon()).equals(Icon_wall)) {
							speed = -g;// 충돌발생하면 바로 전위치로 돌아옴
							break;
						} else if ((wall[i][j].getIcon()).equals(Icon_coin)) {
							speed = 0; // 충돌 발생하지 않으면 그자리에
							wall[i][j].setIcon(Icon_road);
							cnt_coin--;
						} else {
							speed = 0;
						}
					}// End 두개사각형 겹치는 검사 끝
				}// for(j)끝
				if (speed == -g)
					break;
			}// for(i)끝
			x -= speed;
			break;
		case KeyEvent.VK_RIGHT:
			pacman.setIcon(pacmanRt);
			x += speed;
			pacman.setLocation(x, y);

			pt = pacman.getLocation();// 팩맨의 좌표 얻어옴
			rectBullet = new Rectangle(pt.x, pt.y, 50, 50);// 팩맨 사각형을 만듬
			for (int i = 0; i < Wall_garo; i++) {
				for (int j = 0; j < Wall_sero; j++) {
					Point ptTarget = wall[i][j].getLocation();// 타겟의 좌표를 얻어옴
					Rectangle rectTarget = new Rectangle(ptTarget.x,ptTarget.y, 50, 50);// 타겟 사각형을 만듬
					if (rectBullet.intersects(rectTarget))// 두개의 사각형이 겹치는 검사 시작
					{
						if ((wall[i][j].getIcon()).equals(Icon_wall)) {
							speed = -g;// 충돌발생하면 바로 전위치로 돌아옴
							break;
						} else if ((wall[i][j].getIcon()).equals(Icon_coin)) {
							speed = 0; // 충돌 발생하지 않으면 그자리에
							wall[i][j].setIcon(Icon_road);
							cnt_coin--;
						} else {
							speed = 0;
						}
					}// End 두개사각형 겹치는 검사 끝
				}// for(j)끝
				if (speed == -g)
					break;
			}// for(i)끝
			x += speed;
			break;

		}

		pacman.setLocation(x, y);// 방향키 사용 후 최종위치

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	
	public void failMassage(){
		JOptionPane.showMessageDialog(PM_frame, "아쉽네요 실패하셨습니다~!!!");
		PM_frame.dispose();
	}

}