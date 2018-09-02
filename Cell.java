//*******************************************************
// Cell.java
// created by Naked Hermit on 2018/08/29.
// © 2018 Naked Hermit.
//*******************************************************
/* セルのオブジェクト */

import java.util.*;

public class Cell extends AbstractCell {
	public static int col = 80; //行の数 (縦のセル数)
	public static int row = 80; //列の数 (横のセル数)

	private double wActivator; // 活性化物質濃度
	private double wInhibitor; // 阻害物質濃度． ←斑点  -0.34 -0.28 -0.24 -0.20 縞→
	private double rActivator; // 活性化半径
	private double rInhibitor; // 阻害半径
	private double sqRAct; // 活性化半径
	private double sqRInh; // 阻害半径

	public State state; //DIF(分化 : differentiated) or  IND (未分化 : indifferentiated)
	private State nextState;
	
    //コンストラクタ
    public Cell(Simulator simulator, int idI, int idJ) {
		super(simulator, idI, idJ); //コンストラクタを継承
		size(col, row); //格子全体の大きさを設定
		init(); //初期化
    }
	
	//初期化
	public void init() {
		state = rnd.nextBoolean() ? State.DIF : State.IND; //ランダムな状態
		setParameters(1.0, -0.24, 2.3, 6.01);
	}

	public void setParameters(double wActivator, double wInhibitor, double rActivator, double rInhibitor) {
		this.wActivator = wActivator;
		this.wInhibitor = wInhibitor;
		this.rActivator = rActivator;
		this.rInhibitor = rInhibitor;
		this.sqRAct = rActivator * rActivator;
		this.sqRInh = rInhibitor * rInhibitor;
	}
	
	//観測フェーズ
	public void observe() {
	
	}
	
	//状態更新フェーズ
	public void interact() {
		nextState = rule(); //次の状態を取得
	}
	
	//状態更新フェーズ
	public void update() {		
		state = nextState;
	}

	//ルールに基づいて次の状態を返す
	public State rule() {
		double w = wActivator * getActivators(simulator.cell) + wInhibitor * getInhibitors(simulator.cell);

		State nextState = state;
		
		if(w > 0) {
			nextState = State.DIF;
		}
		
		if(w < 0) {
			nextState = State.IND;
		}

		return nextState;
	}
	
	//wActivator を取得
	public double getWActivator() {
		return wActivator;
	}

	//wInhibitor を取得
	public double getWInhibitor() {
		return wInhibitor;
	}
	
	//rActivator を取得
	public double getRActivator() {
		return rActivator;
	}

	//rInhibitor を取得
	public double getRInhibitor() {
		return rInhibitor;
	}

	private int[] morphogenNum(Cell[][] cell) {		
		int[] num = new int[2]; //活性化の数，阻害の数
		num[0] = 0;
		num[1] = 0;
		
		int rowMin = idI - (int)rInhibitor;
		int rowMax = idI + (int)rInhibitor;
		int colMin = idI - (int)rInhibitor;
		int colMax = idJ + (int)rInhibitor;
		
		for(int i = colMin; i < colMax; i++) {
			for(int j = rowMin; j < rowMax; j++) {
				double dCol = idI - i;
				double dRow = idJ - j;
				double d = dCol * dCol + dRow * dRow; //ユークリッド距離
				if(d < sqRAct && d != 0 && cell[i][j].state == State.DIF) {
					num[0]++;
					num[1]++;
				}
			}
		}
		return num;
	}

	
	//活性化半径内の分化細胞の集合を取得
	private int getActivators(Cell[][] cell) {		
		int numAct = 0;
		
		for(int i = 0; i < col; i++) {
			for(int j = 0; j < row; j++) {
				double dCol = idI - i;
				double dRow = idJ - j;
				double d = dCol * dCol + dRow * dRow; //ユークリッド距離
				if(d < sqRAct && d != 0 && cell[i][j].state == State.DIF) {
					numAct++;
				}
			}
		}
		return numAct;
	}

	//阻害半径内かつ活性化半径外の分化細胞の集合を取得
	private int getInhibitors(Cell[][] cell) {
		int numInf = 0;
		for(int i = 0; i < col; i++) {
			for(int j = 0; j < row; j++) {
				double dCol = idI - i;
				double dRow = idJ - j;
				double d = dCol * dCol + dRow * dRow; //ユークリッド距離
				if(d < sqRInh && d >= sqRAct && d != 0 && cell[i][j].state == State.DIF) {
					numInf++;
				}
			}
		}
		return numInf;
	}
	
}
