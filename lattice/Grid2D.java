//*******************************************************
// Grid2D.java
// created by Naked Hermit on 2018/06/04.
// © 2018 Naked Hermit.
//*******************************************************
/* PAppletのラッパークラス */

import java.util.*;
import java.text.*;
import java.io.*;
import processing.core.*;

public abstract class Grid2D extends PApplet {
	protected static Simulator simulator; //シミュレータのインスタンス
	
	private int cellWidth = 1; //セルの横幅
	private int cellHeight = 1; //セルの縦幅
	//セルのサイズを設定
	public void setCellSize(int cellWidth, int cellHeight) {
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
	}

	//ウィンドウサイズを設定
	public void size(int row, int col) {
		super.size(row * cellWidth, col * cellHeight);
	}
	
	private int windowSize = 600; //ウィンドウサイズ
	private int terminate = -1; //終了ステップ数．-1 のとき無限ループ
	public int t; //経過ステップ数
	
    //コンストラクタ
    public Grid2D() {
		windowSize = 600; //ウィンドウサイズ
		simulator = new Simulator(); //シミュレータを生成
    }
	
	public abstract void controller(); //描画処理: オーバーライド前提

	private int strokeWeight = 0; //枠線の太さ
	public void setStrokeWeight(int strokeWeight) {
		if(strokeWeight < 0) {
			System.exit(1);
		} 
		this.strokeWeight = strokeWeight;
	}
	
	private void stroke() {
		if (strokeWeight == 0) {
			noStroke();
		} else {
			strokeWeight(strokeWeight);
		}
	}	
	
	public void setup() {
		stroke(); //枠線の太さを設定
	}
	
	public void draw(){
		simulator.run(); //系を動かす
		
		controller(); //描画処理: オーバーライド前提
		
		if(doesSave) {
			snap(); //スクリーンショットを保存
		}
		
		//終了条件
		if(t == terminate) {
			if(doesSave) {
				saveMovie(); //動画を保存
			}
			noLoop(); //描画停止
		}
	}

	/********** start: cellの描画 **********/
	public void cell(int col, int row) {
		int verX = row * cellWidth; //左上の座標x
		int verY = col * cellHeight; //左上の座標y
		int width = cellWidth; //幅
		int height = cellHeight; //高さ
		rect(verX, verY, width, height);
	}
	/********** start: cellの描画 **********/
	
	/********** start: 終了条件の設定 **********/
	protected void setTerminate(int terminate) {
		this.terminate = terminate;
	}
	/********** end: 終了条件の設定 **********/
	
	/********** start: 日付の設定 **********/
	private String getDate() {
		Date date = new Date();  //Dateオブジェクトを生成
		SimpleDateFormat sdf = new SimpleDateFormat("YYMMdd'_'HHmmss"); //SimpleDateFormatオブジェクトを生成
		return sdf.format(date);
	}

	protected String date = getDate(); //シミュレータ実行時点の日時
	/********** end: 日付の設定 **********/
	
	/********** start: カウントラベルの表示処理 **********/	
	protected void showCountLabel(int t) {
		textSize(24); //文字サイズを指定
		String countLabel = "t = " + Integer.toString(t);
		text(countLabel, (float)0.04* windowSize, (float)0.064 * windowSize);
	}
	/********** end: カウントラベルの表示処理 **********/
	
	/********** start: 画像・動画の保存処理 **********/
	private boolean doesSave = false; //動画を保存するか決めるフラグ
	protected String dirPass = "frames/" + date + "/"; //スナップ保存先ディレクトリ
	
	//動画を保存するか否か
	protected void doesSave(boolean doesSave) {
		this.doesSave = doesSave;
	}

	//スクリーンショットを保存
	protected void snap() {
		saveFrame("frames/" + date + "/" + "######.png");//スクリーンショットを保存
	}

	//動画を自動生成
	protected void saveMovie() {
		try {
			Runtime rt = Runtime.getRuntime();
			String command = "ffmpeg -r 30 -start_number 0 -i " + dirPass + "%6d.png " + "-vframes " + 2 * (terminate + 1) + " -crf 1  -vcodec libx264 -pix_fmt yuv420p -r 60 " + dirPass + "/../" + date + ".mp4";
			rt.exec(command); //コマンド実行
			System.exit(0); //プログラム終了
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	/********** end: 画像・動画の保存処理 **********/
}
