//*******************************************************
// AbstractCell.java
// created by Naked Hermit on 2018/06/04.
// © 2018 Naked Hermit.
//*******************************************************
/* セルの抽象クラス */

import java.util.*;

public class AbstractCell{
	public Simulator simulator; //属するシミュレータ
	protected int idI; //行番号
	protected int idJ; //列番号

	private int col; //行の数 (縦のセル数)
	private int row; //列の数 (横のセル数)
	
	private Edge edge = Edge.NORMAL; //境界のタイプ
	
    protected static Random rnd = new Random(); //乱数生成用インスタンス
	
    //コンストラクタ
    public AbstractCell(Simulator simulator, int idI, int idJ) {
		this.simulator = simulator;
		if(idI < 0 || idJ < 0) {
			System.exit(1); //idが負のとき異常終了
		}
		this.idI = idI;
		this.idJ = idJ;
    }

	//格子全体の大きさを設定
	protected void size(int col, int row) {
		this.col = col;
		this.row = row;
	}
	
	//N近傍を返す
	private <T extends AbstractCell> ArrayList<T> getNNeighbors(T[][] cell, int[] dr) {
		ArrayList<T> neighbors = new ArrayList<T>(); //近傍のセル集合
		
		int gain = dr.length / 5; //添字の進み
		int num = dr.length - gain; //近傍数
		
		for(int r = 0; r < num; r++) {
			int i = idI + dr[r]; //行の添字
			int j = idJ + dr[r + gain]; //列の添字

			if(i < 0 || i > col || j < 0 || j > row) { //境界の処理
				switch(edge) {
				case NORMAL:
					continue;
				case WALLED:
					break;
				case TOROIDAL:
					break;
				default:
					System.exit(1); //異常終了
				}
			}
			neighbors.add(cell[i][j]);
		}
		return neighbors;
	}

	private int dr4[] = {0, 1, 0, -1, 0}; //4近傍探索用
	//ノイマン近傍を返す
	public <T extends AbstractCell> ArrayList<T> get4Neighbors(T[][] cell) {
		return getNNeighbors(cell, dr4);
	}
	
	private int dr8[] = {0, 1, 1, 1, 0, -1, -1, -1, 0, 1}; //8近傍探索用
	//ムーア近傍を返す
	public <T extends AbstractCell> ArrayList<T> get8Neighbors(T[][] cell) {
		return getNNeighbors(cell, dr8);
	}
}
