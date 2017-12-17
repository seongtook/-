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

	int Wall_garo = 17;// ����ü ����
	int Wall_sero = 15;// ����ü ����
	int cnt_coin = 110;// dot����

	// ����������
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

	JLabel pacman = new JLabel();// �Ѹǰ�ü����
	JLabel enemy = new JLabel();// ������ü����
	JLabel wall[][] = new JLabel[Wall_garo][Wall_sero];// ���鰳ü����

	public PacmanGame() {// �Ѹǰ����� ������

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
		
		// ���� �Ӽ�����
		enemy.setBounds(400, 350, 50, 50);
		enemy.setIcon(Icon_enemy);
		PM_contain.add(enemy);
		
		// �Ѹ� �Ӽ�����
		pacman.setBounds(400, 580, 50, 50);// �⺻��ġ�� ũ��(x,y xũ��,yũ��)
		pacman.setIcon(pacmanRt);
		PM_contain.add(pacman);

		// ������.�ϴ� ��� ������ ������ �����
		for (int i = 0; i < Wall_garo; i++) {
			for (int j = 0; j < Wall_sero; j++) {

				wall[i][j] = new JLabel();
				wall[i][j].setBounds(i * 50, j * 50, 50, 50);
				PM_contain.add(wall[i][j]);
				wall[i][j].setIcon(Icon_wall);
			}
		}

		// Coin�������� ��ġ
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

		//��������		
		for (int i = 7; i < 10; i++) {
			wall[i][5].setIcon(Icon_candy);
			wall[i][6].setIcon(Icon_candy);
			wall[i][11].setIcon(Icon_candy);
			wall[i][12].setIcon(Icon_candy);
		}
		// dot������ ��ġ ��
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

		temp = JOptionPane.showInputDialog("�����ұ��?\n" + " (�������� -1�� �Է��ϼ���.)");

		// ����ڰ� ��ҹ�ư�� �����ų� -1�� �Է��ϸ� â�� �ݴ´�.
		if (temp == null || temp.equals("-1")) {
			PM_frame.dispose();
		}

		//������ �ڵ��̵��� ���� ����
		//���� �̵��� �⺻ ��ī���� : �������̳� ���ٸ����� ���ö����� ���������� �������̳� ���ٸ� ���� ������ ������ ������ �����ϰ� ������
		int x = enemy.getX();// ������ x��ǥ��
		int y = enemy.getY();// ������ y��ǥ��

		int go = 0;
		int Up = 0,Down=0,Left=0,Right=0;
		
		while (PM_frame.isFocused()) {

			try {

				go = (int) (Math.random() * 4);

				// �浹�׽�Ʈ�� ���� �غ�
				Point pt,ptPM;
				Rectangle rectBullet = new Rectangle();
				Rectangle rectBulletPM = new Rectangle();
				Rectangle rectBullet2 = new Rectangle();
				Rectangle rectBullet3 = new Rectangle();


				
				int g = 2;//������ ���� �̵��Ÿ� �ݵ�� 50(��ʺ�)�� �μ�
				int delay=10;
				int speed;
				boolean strait = true;// ������ ���� �ѹ��������� ���� ����(0,1)
				

				switch (go) {
				case 0://�����̵�
					while (strait == true) {
						Thread.sleep(delay);
						speed = g;

						y -= speed;// �ϴ� ��ġ�� �ű� �� �Ʒ� �浹�׽�Ʈ���� �浹�� �߻��ϸ� �ٽ� ���ڸ��� ���ƿ�
						enemy.setLocation(x, y);

						pt = enemy.getLocation();// ���� ��ǥ ����
						ptPM = pacman.getLocation();//�Ѹ���ǥ
						
						rectBullet = new Rectangle(pt.x, pt.y, 50, 50);// �����簢��������
						rectBulletPM = new Rectangle(ptPM.x, ptPM.y, 50, 50);// �Ѹǻ簢��������
						
						rectBullet2 = new Rectangle(pt.x - 50, pt.y+49, 50, 1);// ��������簢��������
						rectBullet3 = new Rectangle(pt.x + 50, pt.y+49, 50, 1);// ����������簢��������
						
						if (rectBullet.intersects(rectBulletPM))// �Ѹǰ� ������ ������~
							{
								failMassage();	
								break;
							}// End �Ѹǰ� ������ ������~
						
						for (int i = 0; i < Wall_garo; i++) {
							for (int j = 0; j < Wall_sero; j++) {
								Point ptTarget = wall[i][j].getLocation();// ��������ǥ������
								Rectangle rectTarget = new Rectangle(ptTarget.x, ptTarget.y, 50, 50);// ����簢��������
								if (rectBullet.intersects(rectTarget))// �ΰ��� �簢���� ��ġ���� �˻� ����
								{
									if ((wall[i][j].getIcon()).equals(Icon_wall)) {
										speed = -g;// �浹�߻��ϸ� �ٷ� ����ġ�� ���ƿ�
										strait = false;
										break;// �ѹ��̶� �浹�� �߻��ϸ� ���̻� ������ �ݺ����� ����
									} else
										speed = 0;
								}// End �ΰ��簢�� ��ġ�� �˻� ��
								// ���� �����ʿ� ���� �ִ��� �˻�
								if ((rectBullet2.intersects(rectTarget))|| (rectBullet3.intersects(rectTarget))) {
									if (((wall[i][j].getIcon()).equals(Icon_coin))||((wall[i][j].getIcon()).equals(Icon_road))) {
										if(Up==0){
											strait = false;
											Up=50/g;
											continue;
										}
										Up--;
									}
								}//end ���� �����ʿ� ���� �ִ��� �˻�
							}// for(j)��
							if (speed == -g)
								break;
						}// for(i)��
						
						y -= speed;
					}// while�� ��
					
					break;

				case 1://�Ʒ����̵�
					while (strait == true) {
						Thread.sleep(delay);
						speed = g;
						y += speed;
						enemy.setLocation(x, y);

						pt = enemy.getLocation();// ���� ��ǥ ����
						ptPM = pacman.getLocation();//�Ѹ���ǥ
						
						rectBullet = new Rectangle(pt.x, pt.y, 50, 50);// �����簢��������
						rectBulletPM = new Rectangle(ptPM.x, ptPM.y, 50, 50);// �Ѹǻ簢��������
						
						rectBullet2 = new Rectangle(pt.x - 50, pt.y, 50, 1);// ��������簢��������
						rectBullet3 = new Rectangle(pt.x + 50, pt.y, 50, 1);// ����������簢��������
				
						if (rectBullet.intersects(rectBulletPM))// �Ѹǰ� ������ ������~
						{
							failMassage();
							break;
						}// End �Ѹǰ� ������ ������~
						
						for (int i = 0; i < Wall_garo; i++) {
							for (int j = 0; j < Wall_sero; j++) {
								Point ptTarget = wall[i][j].getLocation();// Ÿ������ǥ������
								Rectangle rectTarget = new Rectangle(ptTarget.x, ptTarget.y, 50, 50);// Ÿ�ٻ簢��������

								if (rectBullet.intersects(rectTarget))// �ΰ��ǻ簢���̰�ġ�����˻� ����
								{
									if ((wall[i][j].getIcon()).equals(Icon_wall)) {
										speed = -g;// �浹�߻��ϸ� �ٷ� ����ġ�� ���ƿ�
										strait = false;
										break;// �ѹ��̶� �浹�� �߻��ϸ� ���̻� ������ �ݺ����� ����
									} else
										speed = 0;
								}// End �ΰ��簢�� ��ġ�� �˻� ��
									// ���ʿ� ���� �ִ��� �˻�
								
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
							}// for(j)��
							if (speed == -g)
								break;
						}// for(i)��
						
						y += speed;
					}// while�� ��
					
					break;

				case 2://�������� �̵�
					while (strait == true) {
						Thread.sleep(delay);
						speed = g;
						x -= speed;
						enemy.setLocation(x, y);
						pt = enemy.getLocation();// ���� ��ǥ ����
						ptPM = pacman.getLocation();//�Ѹ���ǥ
						rectBullet = new Rectangle(pt.x, pt.y, 50, 50);// �����簢��������
						rectBulletPM = new Rectangle(ptPM.x, ptPM.y, 50, 50);// �Ѹǻ簢��������

						rectBullet2 = new Rectangle(pt.x+49, pt.y - 50, 1, 50);// ��������簢��������
						rectBullet3 = new Rectangle(pt.x+49, pt.y + 50, 1, 50);// �����Ʒ���簢��������

						if (rectBullet.intersects(rectBulletPM))// �Ѹǰ� ������ ������~
						{
							failMassage();
							break;
						}// End �Ѹǰ� ������ ������~
						for (int i = 0; i < Wall_garo; i++) {
							for (int j = 0; j < Wall_sero; j++) {
								Point ptTarget = wall[i][j].getLocation();// Ÿ������ǥ������
								Rectangle rectTarget = new Rectangle(
										ptTarget.x, ptTarget.y, 50, 50);// Ÿ�ٻ簢��������
								if (rectBullet.intersects(rectTarget))// �ΰ��ǻ簢���̰�ġ�°˻� ����
								{
									if ((wall[i][j].getIcon()).equals(Icon_wall)) {
										speed = -g;// �浹�߻��ϸ� �ٷ� ����ġ�� ���ƿ�
										strait = false;
										break;// �ѹ��̶� �浹�� �߻��ϸ� ���̻� ������ �ݺ����� ����
									} else
										speed = 0;
								}// End �ΰ��簢�� ��ġ�� �˻� ��
									// ��, �Ʒ��ʿ� ���̳� ������ �ִ��� �˻�
								if ((rectBullet2.intersects(rectTarget)) || (rectBullet3.intersects(rectTarget))) {
									if (((wall[i][j].getIcon()).equals(Icon_coin))||((wall[i][j].getIcon()).equals(Icon_road))) {
										if(Left==0){
											strait = false;// while�� ����
											Left=50/g;
											continue;
										}
										Left--;
									}
								}
							}// for(j)��
							if (speed == -g)
								break;
						}// for(i)��
						
						x -= speed;
					}// while�� ��
					break;
				case 3://���������� �̵�
					while (strait == true) {
						Thread.sleep(delay);
						speed = g;
						x += speed;
						enemy.setLocation(x, y);

						pt = enemy.getLocation();// ���� ��ǥ ����
						ptPM = pacman.getLocation();//�Ѹ���ǥ
						
						rectBullet = new Rectangle(pt.x, pt.y, 50, 50);// �����簢��������
						rectBulletPM = new Rectangle(ptPM.x, ptPM.y, 50, 50);// �Ѹǻ簢��������
						
						rectBullet2 = new Rectangle(pt.x, pt.y - 50, 1, 50);// ��������簢��������
						rectBullet3 = new Rectangle(pt.x, pt.y + 50, 1, 50);// �����Ʒ���簢��������
					
						if (rectBullet.intersects(rectBulletPM))// �Ѹǰ� ������ ������~
						{
							failMassage();
							break;
						}// End �Ѹǰ� ������ ������~
						
						for (int i = 0; i < Wall_garo; i++) {
							for (int j = 0; j < Wall_sero; j++) {
								Point ptTarget = wall[i][j].getLocation();// Ÿ������ǥ������
								Rectangle rectTarget = new Rectangle(
										ptTarget.x, ptTarget.y, 50, 50);// Ÿ�ٻ簢��������

								if (rectBullet.intersects(rectTarget))// �ΰ��ǻ簢���̰�ġ�°˻� ����
								{
									if ((wall[i][j].getIcon()).equals(Icon_wall)) {
										speed = -g;// �浹�߻��ϸ� �ٷ� ����ġ�� ���ƿ�
										strait = false;
										break;// �ѹ��̶� �浹�� �߻��ϸ� ���̻� ������ �ݺ����� ����
									} else
										speed = 0;
								}// End �ΰ��簢�� ��ġ�� �˻� ��
								// ��, �Ʒ��ʿ� ���̳� ������ �ִ��� �˻�
								
								if ((rectBullet2.intersects(rectTarget))|| (rectBullet3.intersects(rectTarget))) {
									if (((wall[i][j].getIcon()).equals(Icon_coin))||((wall[i][j].getIcon()).equals(Icon_road))) {
										if(Right==0){
											strait = false;// while�� ����
											Right=50/g-1;
											continue;
										}
										Right--;
									}
								}
							}// for(j)��
							if (speed == -g)
								break;
						}// for(i)��
						
						x += speed;
					}// ȭ�Ϲ���
					
					break;

				}// ����ġ�� ��

				enemy.setLocation(x, y);// �̵� �� ������ġ
				
			} catch (Exception e) {
				return;
			}
		}// ȭ������
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		String temp = "";
		if (cnt_coin == 0) {
			temp = JOptionPane.showInputDialog("�����մϴ�~! �����ϼ̽��ϴ� ^^");
		}
		// ����ڰ� ��ҹ�ư�� �����ų� -1�� �Է��ϸ� â�� �ݴ´�.
		if (temp == null || temp.equals("-1")) {
			PM_frame.dispose();
		}

		
//�Ѹ� �̵�
		
		int x = pacman.getX();// move�� x��ǥ��
		int y = pacman.getY();// move�� y��ǥ��

		// �浹�׽�Ʈ�� ���� �غ�
		
		Point pt = pacman.getLocation();
		Rectangle rectBullet = new Rectangle();

		int g = 10;
		int speed = g;

		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			pacman.setIcon(pacmanUp);
			y -= speed;// �ϴ� ��ġ�� �ű� �� �Ʒ� �浹�׽�Ʈ���� �浹�� �߻��ϸ� �ٽ� ���ڸ��� ���ƿ�
			pacman.setLocation(x, y);

			pt = pacman.getLocation();// �Ѹ��� ��ǥ ����
			rectBullet = new Rectangle(pt.x, pt.y, 50, 50);// �Ѹ� �簢���� ����

			for (int i = 0; i < Wall_garo; i++) {
				for (int j = 0; j < Wall_sero; j++) {
					Point ptTarget = wall[i][j].getLocation();// Ÿ���� ��ǥ�� ����
					Rectangle rectTarget = new Rectangle(ptTarget.x,ptTarget.y, 50, 50);// Ÿ�� �簢���� ����
					if (rectBullet.intersects(rectTarget))// �ΰ��� �簢���� ��ġ�� �˻� ����
					{
						if ((wall[i][j].getIcon()).equals(Icon_wall)) {
							speed = -g;// �浹�߻��ϸ� �ٷ� ����ġ�� ���ƿ�
							break;// �ѹ��̶� �浹�� �߻��ϸ� ���̻� ������ �ݺ����� ����
						} else if ((wall[i][j].getIcon()).equals(Icon_coin)) {//�̵��� ��ġ�� ������ �ִٸ�,,
							speed = 0; // ���� ��ġ�� �����ϰ�
							wall[i][j].setIcon(Icon_road); // ������ �Ծ����Ƿ� ���� ��� ��� �����.
							cnt_coin--; //������ü ���� 1�� ����
						} else {
							speed = 0;
						}
					}// End �ΰ��簢�� ��ġ�� �˻� ��
				}// for(j)��
				if (speed == -g)
					break;
			}// for(i)��
			y -= speed;

			break;
		case KeyEvent.VK_DOWN:
			pacman.setIcon(pacmanDn);
			y += speed;
			pacman.setLocation(x, y);

			pt = pacman.getLocation();// �Ѹ��� ��ǥ ����
			rectBullet = new Rectangle(pt.x, pt.y, 50, 50);// �Ѹ� �簢���� ����
			for (int i = 0; i < Wall_garo; i++) {
				for (int j = 0; j < Wall_sero; j++) {
					Point ptTarget = wall[i][j].getLocation();// Ÿ���� ��ǥ�� ����
					Rectangle rectTarget = new Rectangle(ptTarget.x,ptTarget.y, 50, 50);// Ÿ�� �簢���� ����
					if (rectBullet.intersects(rectTarget))// �ΰ��� �簢���� ��ġ�� �˻� ����
					{
						if ((wall[i][j].getIcon()).equals(Icon_wall)) {
							speed = -g;// �浹�߻��ϸ� �ٷ� ����ġ�� ���ƿ�
							break;
						} else if ((wall[i][j].getIcon()).equals(Icon_coin)) {
							speed = 0; // �浹 �߻����� ������ ���ڸ���
							wall[i][j].setIcon(Icon_road);
							cnt_coin--;
						} else {
							speed = 0;
						}
					}// End �ΰ��簢�� ��ġ�� �˻� ��
				}// for(j)��
				if (speed == -g)
					break;
			}// for(i)��
			y += speed;
			break;

		case KeyEvent.VK_LEFT:
			pacman.setIcon(pacmanLt);
			x -= speed;
			pacman.setLocation(x, y);

			pt = pacman.getLocation();// �Ѹ��� ��ǥ ����
			rectBullet = new Rectangle(pt.x, pt.y, 50, 50);// �Ѹ� �簢���� ����
			for (int i = 0; i < Wall_garo; i++) {
				for (int j = 0; j < Wall_sero; j++) {
					Point ptTarget = wall[i][j].getLocation();// Ÿ���� ��ǥ�� ����
					Rectangle rectTarget = new Rectangle(ptTarget.x,ptTarget.y, 50, 50);// Ÿ�� �簢���� ����
					if (rectBullet.intersects(rectTarget))// �ΰ��� �簢���� ��ġ�� �˻� ����
					{
						if ((wall[i][j].getIcon()).equals(Icon_wall)) {
							speed = -g;// �浹�߻��ϸ� �ٷ� ����ġ�� ���ƿ�
							break;
						} else if ((wall[i][j].getIcon()).equals(Icon_coin)) {
							speed = 0; // �浹 �߻����� ������ ���ڸ���
							wall[i][j].setIcon(Icon_road);
							cnt_coin--;
						} else {
							speed = 0;
						}
					}// End �ΰ��簢�� ��ġ�� �˻� ��
				}// for(j)��
				if (speed == -g)
					break;
			}// for(i)��
			x -= speed;
			break;
		case KeyEvent.VK_RIGHT:
			pacman.setIcon(pacmanRt);
			x += speed;
			pacman.setLocation(x, y);

			pt = pacman.getLocation();// �Ѹ��� ��ǥ ����
			rectBullet = new Rectangle(pt.x, pt.y, 50, 50);// �Ѹ� �簢���� ����
			for (int i = 0; i < Wall_garo; i++) {
				for (int j = 0; j < Wall_sero; j++) {
					Point ptTarget = wall[i][j].getLocation();// Ÿ���� ��ǥ�� ����
					Rectangle rectTarget = new Rectangle(ptTarget.x,ptTarget.y, 50, 50);// Ÿ�� �簢���� ����
					if (rectBullet.intersects(rectTarget))// �ΰ��� �簢���� ��ġ�� �˻� ����
					{
						if ((wall[i][j].getIcon()).equals(Icon_wall)) {
							speed = -g;// �浹�߻��ϸ� �ٷ� ����ġ�� ���ƿ�
							break;
						} else if ((wall[i][j].getIcon()).equals(Icon_coin)) {
							speed = 0; // �浹 �߻����� ������ ���ڸ���
							wall[i][j].setIcon(Icon_road);
							cnt_coin--;
						} else {
							speed = 0;
						}
					}// End �ΰ��簢�� ��ġ�� �˻� ��
				}// for(j)��
				if (speed == -g)
					break;
			}// for(i)��
			x += speed;
			break;

		}

		pacman.setLocation(x, y);// ����Ű ��� �� ������ġ

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
		JOptionPane.showMessageDialog(PM_frame, "�ƽ��׿� �����ϼ̽��ϴ�~!!!");
		PM_frame.dispose();
	}

}