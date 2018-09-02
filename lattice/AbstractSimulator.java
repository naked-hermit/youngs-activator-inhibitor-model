//*******************************************************
// Simulator.java
// created by Naked Hermit on 2018/05/25.
// © 2018 Naked Hermit.
//*******************************************************
/* シミュレータの抽象クラス */

import java.util.*;

public abstract class AbstractSimulator {
	private int t; //経過ステップ数
	
	protected Random rnd = new Random(); //乱数生成用インスタンス
	
    //コンストラクタ
    public AbstractSimulator() {
		t = 0; //ステップ数を初期化
    }
	
    ///シミュレータを1ステップ動かす
    public void run(){
		process(); //処理を実行
		t++; //ステップ数をカウント
    }
	
    abstract protected void process();	//シミュレータの処理内容: オーバーライド前提


	
	//現在のステップを求める
	public int getStep() {
		return t;
	}
}
