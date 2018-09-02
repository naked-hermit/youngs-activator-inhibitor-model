//*******************************************************
// Main.java
// created by Naked Hermit on 2018/08/29.
// © 2018 Naked Hermit.
//*******************************************************
/* Processingによる描画処理 */

import processing.core.*;

public class Main extends Grid2D {
	private int row = simulator.row; //列の数 (横のセル数)
	private int col = simulator.col; //行の数 (縦のセル数)
	
    public void settings() {
		setStrokeWeight(0);
		setCellSize(4,4); //セルの大きさを設定
		size(row, col); //ウィンドウサイズを指定
		setTerminate(10); //終了ステップ数を設定
		doesSave(true); //動画を保存
    }

    private State state[][] = new State[col][row];; //状態
	
	//描画処理
    public void controller() {
		frameRate(20); //描画レート
		//overlook(); //俯瞰		
		t = simulator.getStep(); //経過ステップ数を取得
		background(240);//単色背景

		//シミュレータからセルの状態を取得
		for(int i = 0; i < col; i++) {
			for(int j = 0; j < row; j++) {
				state[i][j] = simulator.cell[i][j].state;
			}
		}
		
		for(int i = 0; i < col; i++) {
			for(int j = 0; j < row; j++) {
				if(state[i][j] == State.DIF) {
					fill(32);
				} else {
					fill(240);
				}
				cell(i, j);
			}			
		}
		
		/*
		fill(40);
		showCountLabel(t); //カウントラベルを表示
		*/
    }
	
    public static void main(String args[]) {
		PApplet.main("Main"); //Mainクラスを呼び出してProcessingを起動
    }
}
