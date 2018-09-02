//*******************************************************
// Simulator.java
// created by Naked Hermit on 2018/08/29.
// © 2018 Naked Hermit.
//*******************************************************
/* シミュレータのクラス */

public class Simulator extends AbstractSimulator {
	public int col = Cell.col; //行の数 (縦のセル数)
	public int row = Cell.row; //列の数 (横のセル数)

    public Cell[][] cell = new Cell[col][row]; //セル
	
    public Simulator() {
		//セルを生成
		for(int i = 0; i < col; i++) {
			//double w2 = -0.34 + i * 0.18 / col; //gradation
			double w2 = -1;
			for(int j = 0; j < row; j++) {
				cell[i][j] = new Cell(this, i, j);
				cell[i][j].	setParameters(1.0, w2, 2.3, 6.01);;
			}
		}
    }

	//処理の内容
    public void process() {
		//観測フェーズ
		for(int i = 0; i < col; i++) {
			for(int j = 0; j < row; j++) {
				cell[i][j].observe();
			}
		}
		
		//相互作用フェーズ
		for(int i = 0; i < col; i++) {
			for(int j = 0; j < row; j++) {
				cell[i][j].interact();
			}
		}

		//更新フェーズ
		for(int i = 0; i < col; i++) {
			for(int j = 0; j < row; j++) {
				cell[i][j].update();
			}
		}
	}
}
