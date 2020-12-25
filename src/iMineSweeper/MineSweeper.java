package iMineSweeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class MineSweeper implements ActionListener, MouseListener {
	//游戏信息
	int ROW ;	//行
	int COL;	//列
	int unopened = COL*ROW;
	int[][] data;	//每个格子雷的信息
	JButton[][] btns;	//一个按钮代表一个块
	int MINECNT;
	
	int MIENCODE = -1;	//-1代表有雷
	int flagCnt = 0;
	int opened = 0;
	int secs = 0;	//时间
	int[] startXY = {-1, -1};	//第一个打开按钮的坐标
	
	//窗体元素
	JFrame frame = new JFrame();	//窗体

	ImageIcon failIcon = new ImageIcon("fail.png");
	ImageIcon winIcon = new ImageIcon("win.png");
	ImageIcon winFlagIcon = new ImageIcon("win_flag.png");
	ImageIcon bombIcon = new ImageIcon("bomb.png");
	ImageIcon guessIcon = new ImageIcon("uess.png");

	JButton bannerBtn = new JButton("开始");
	
	
	JLabel label1 = new JLabel("待开：");
	JLabel label2 = new JLabel("已开："+opened);
	JLabel label3 = new JLabel("标雷："+flagCnt);
	JLabel label4 = new JLabel("用时："+secs+"s");
	
	JLabel label5 = new JLabel("行");
	JLabel label6 = new JLabel("列");
	JLabel label7 = new JLabel("雷");
	
	JTextField text1 = new JTextField("");
	JTextField text2 = new JTextField("");
	JTextField text3 = new JTextField("");
	
	Timer timer = new Timer(1000, this);	//1000ms触发一次，并找this.perforem
	
	public MineSweeper(int row, int col, int minecnt){
		ROW = row;
		COL = col;
		MINECNT = minecnt;
		data = new int[ROW][COL];	//每个格子雷的信息
		btns = new JButton[ROW][COL];	//一个按钮代表一个块
		unopened = COL*ROW;
		
		label1.setText("待开："+unopened);
		label2.setText("已开："+opened);
		label3.setText("标雷："+flagCnt);
		label4.setText("用时："+secs+"s");
		
		text1.setText(ROW+"");
		text2.setText(COL+"");
		text3.setText(MINECNT+"");

		frame.setSize(COL*45,60+ROW*45);	//窗体大小
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//点关闭退出
		frame.setLayout(new BorderLayout());	//布局
		
		setHeader();
		setButtons();
		
		timer.start();
		frame.setVisible(true);
	}	
	
	private void addMine() {
		//加雷
		Random rand = new Random();
		for (int i = 0; i < MINECNT;) {
			int r = rand.nextInt(ROW);	//产生0-20整数
			int c = rand.nextInt(COL);	//产生0-20整数
			
			if(data[r][c] != MIENCODE) {
				//第一个开的格子周围不能有雷,直接跳过
				if(r==startXY[0]-1 && c>=startXY[1]-1 && c<=startXY[1]+1) continue;
				if(r==startXY[0]   && c>=startXY[1]-1 && c<=startXY[1]+1) continue;
				if(r==startXY[0]+1 && c>=startXY[1]-1 && c<=startXY[1]+1) continue;
				
				data[r][c] = MIENCODE;
				i++;
			}
		}
		
		//计算周围雷数量
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				if(data[i][j]==MIENCODE) continue;
				int tempCnt = 0;
				if(i>0  && j>0  && data[i-1][j-1]==MIENCODE) tempCnt++;
				if(i>0  && 		   data[i-1][j  ]==MIENCODE) tempCnt++;
				if(i>0  && j<COL-1 && data[i-1][j+1]==MIENCODE) tempCnt++;
				if( 	   j>0  && data[i  ][j-1]==MIENCODE) tempCnt++;
				if(		   j<COL-1 && data[i  ][j+1]==MIENCODE) tempCnt++;
				if(i<ROW-1 && j>0  && data[i+1][j-1]==MIENCODE) tempCnt++;
				if(i<ROW-1  		&& data[i+1][j  ]==MIENCODE) tempCnt++;
				if(i<ROW-1 && j<COL-1 && data[i+1][j+1]==MIENCODE) tempCnt++;
				data[i][j] = tempCnt;
			}
		}
	}
	
	//添加按钮
	private void setButtons(){
		Container con = new Container();
		con.setLayout(new GridLayout(ROW,COL));
		
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				JButton btn = new JButton(guessIcon);
				btn.setBackground(new Color(244,183,113));
				btn.setOpaque(true);
				btn.addMouseListener(this);
				con.add(btn);
				btns[i][j] = btn;
			}
		}
		
		frame.add(con, BorderLayout.CENTER);
	}
	
	//设置菜单栏
	private void setHeader() {
		JPanel panel = new JPanel(new GridBagLayout());
		
		frame.add(panel, BorderLayout.NORTH);
		
		label1.setOpaque(true);
		label1.setBackground(Color.white);
		label1.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
		label2.setOpaque(true);
		label2.setBackground(Color.white);
		label2.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
		label3.setOpaque(true);
		label3.setBackground(Color.white);
		label3.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
		label4.setOpaque(true);
		label4.setBackground(Color.white);
		label4.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
		GridBagConstraints c2 = new GridBagConstraints(0,0,2,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
		panel.add(label1, c2);
		GridBagConstraints c3 = new GridBagConstraints(2,0,2,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
		panel.add(label2, c3);
		GridBagConstraints c4 = new GridBagConstraints(4,0,2,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
		panel.add(label3, c4);
		GridBagConstraints c5 = new GridBagConstraints(6,0,2,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
		panel.add(label4, c5);	
		
		label5.setOpaque(true);
		label5.setBackground(Color.white);
		label5.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
		label6.setOpaque(true);
		label6.setBackground(Color.white);
		label6.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
		label7.setOpaque(true);
		label7.setBackground(Color.white);
		label7.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
		text1.setOpaque(true);
		text1.setBackground(Color.white);
		text1.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
		text2.setOpaque(true);
		text2.setBackground(Color.white);
		text2.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
		text3.setOpaque(true);
		text3.setBackground(Color.white);
		text3.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

		GridBagConstraints c9 =  new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
		panel.add(label5, c9);
		GridBagConstraints c6 =  new GridBagConstraints(1,1,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
		panel.add(text1, c6);
		GridBagConstraints c10 = new GridBagConstraints(2,1,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
		panel.add(label6, c10);
		GridBagConstraints c7 =  new GridBagConstraints(3,1,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
		panel.add(text2, c7);
		GridBagConstraints c11 = new GridBagConstraints(4,1,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
		panel.add(label7, c11);
		GridBagConstraints c8 =  new GridBagConstraints(5,1,1,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
		panel.add(text3, c8);

		GridBagConstraints c1 = new GridBagConstraints(6,1,2,1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0);
		panel.add(bannerBtn, c1);
		bannerBtn.addActionListener(this);
		
		
	}
	
	public static void main(String[] args) {
		new MineSweeper(8,8,15);
	}


	//事件发生时调用
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof Timer) {
			secs++;
			label4.setText("用时："+secs+"s");
			return;
		}
		JButton btn = (JButton)e.getSource();
		if(btn.equals(bannerBtn)) {
			restart();
			return;
		}
	}

	//鼠标点击按钮
	@Override
	public void mousePressed(MouseEvent e) {
		JButton btn = (JButton)e.getSource();
		for(int i = 0;i<ROW;i++){
			for(int j = 0;j<COL;j++){
				//获取按键坐标
				if(btn.equals(btns[i][j])){
					if(e.getButton() == MouseEvent.BUTTON3) {
						setFlag(i, j, btn.getText());
					}
					if(e.getButton() == MouseEvent.BUTTON1 && btn.isEnabled()) {
						if(opened==0) {
							startXY[0] = i;
							startXY[1] = j;
							addMine();
							openCell(i, j);
							checkWin();
						}else{
							if(data[i][j]==MIENCODE) {
								lose();
							}else {
								openCell(i, j);
								checkWin();
							}
						}
					}					
				}
			}
		}
	}
	
	//标雷
	private void setFlag(int i, int j, String text) {
		JButton btn = btns[i][j];
		if(text=="x") {
			btn.setIcon(guessIcon);
			btn.setBackground(new Color(244,183,113));
			btn.setEnabled(true);
			btn.setText("");
			flagCnt--;
		}else if(btn.isEnabled()){
			btn.setIcon(null);
			btn.setBackground(new Color(255,200,200));
			btn.setEnabled(false);
			btn.setText("x");
			flagCnt++;
		}
		label3.setText("标雷："+flagCnt);
	}
	
	private void restart() {
		int tempROW =  Integer.parseInt(text1.getText());
		int tempCOL =  Integer.parseInt(text2.getText());
		int tempMine = Integer.parseInt(text3.getText());
		
		if( tempROW<4 || tempCOL<4 ) {
			JOptionPane.showMessageDialog(frame, "行/列数必须大于3","行列设置错误！",JOptionPane.PLAIN_MESSAGE);
			return;
		}
		if(tempROW*tempCOL <= tempMine) {
			JOptionPane.showMessageDialog(frame, "地雷数必须小于总格数","雷叔设置错误！",JOptionPane.PLAIN_MESSAGE);
			return;
		}
		
		ROW = tempROW;
		COL = tempCOL;
		MINECNT = tempMine;
		
		unopened = COL*ROW;
		opened = 0;
		secs=0;	//时间
		flagCnt = 0;
		startXY[0] = -1;
		startXY[1] = -1;

		label1.setText("待开："+unopened);
		label2.setText("已开："+opened);
		label3.setText("标雷："+flagCnt);
		label4.setText("用时："+secs+"s");
		
		System.out.println(COL+"&"+COL+"&"+MINECNT);
		frame.dispose();
		new MineSweeper(ROW, COL, MINECNT);
	}
	
	private void checkWin() {
		//判断胜利
		if(unopened==MINECNT) {
			timer.stop();
			for (int i = 0; i < ROW; i++) {
				for (int j = 0; j < COL; j++) {
					if(btns[i][j].isEnabled()) btns[i][j].setIcon(winFlagIcon);
				}
			}
			JOptionPane.showMessageDialog(frame, "你的用时"+secs+"s","Win！",JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	private void lose() {
		timer.stop();
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				JButton btn = btns[i][j];
				if(btn.isEnabled()) {
					if(data[i][j]==MIENCODE) {
						btn.setEnabled(false);
						btn.setIcon(bombIcon);
						btn.setDisabledIcon(bombIcon);
					}else {
						btn.setEnabled(false);
						btn.setIcon(null);
						btn.setText(data[i][j]+"");
					}
				}
				//标错雷的格子
				if(data[i][j]!=MIENCODE && btn.getText()=="x") {
					btn.setBackground(new Color(255,0,0));
				}
			}
		}
		JOptionPane.showMessageDialog(frame, "Ooops, you lose.","Game Over",JOptionPane.PLAIN_MESSAGE);
	}
	
	private void openCell(int i, int j) {
		JButton btn = btns[i][j];
		if(!btn.isEnabled()) return;
		
		btn.setIcon(null);
		btn.setEnabled(false);
		btn.setBackground(Color.GREEN);
		btn.setText(data[i][j]+"");
		
		updateCnt();
		
		//是否为空格子且周围没有雷
		if(data[i][j] == 0) {			
			if(i>0  && j>0    ) openCell(i-1,j-1);
			if(i>0     		  ) openCell(i-1,j);
			if(i>0  && j<COL-1) openCell(i-1,j+1);
			if(j>0    ) openCell(i,j-1);
			if(j<COL-1) openCell(i,j+1);
			if(i<ROW-1 && j>0 ) openCell(i+1,j-1);
			if(i<ROW-1  		) openCell(i+1,j);
			if(i<ROW-1 && j<COL-1) openCell(i+1,j+1);
		}
	}
	
	private void updateCnt() {
		opened++;
		unopened--;
		label1.setText("待开："+unopened);
		label2.setText("已开："+opened);
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
