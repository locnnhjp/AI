import java.util.ArrayList;
import java.util.Random;

/**
 * Ｑ学習を行うクラス
 */
public class QLearning {

	/**
	 * Ｑ学習を行うオブジェクトを生成する
	 * @param states  状態数
	 * @param actions 行動数
	 * @param alpha   学習率（0.0〜1.0）
	 * @param gamma   割引率（0.0〜1.0）
	 */
	public QLearning(int states, int actions, double alpha, double gamma)
	{
		this.qTable = new double[states][actions];
		this.alpha = alpha;
		this.gamma = gamma;
		
		//Q値テーブルの値全て０で初期化する
		for(int i = 0; i < states; i++) {
			for(int j = 0; j < actions; j++) {
				qTable[i][j] = 0;
			}
		}
	}

	/**
	 * epsilon-Greedy 法により行動を選択する
	 * @param state   現在の状態
	 * @param epsilon ランダムに行動を選択する確率（0.0〜1.0）
	 * @return 選択された行動番号
	 */
	public int selectAction(int state, double epsilon)
	{

		double e = random.nextDouble(); //０から１までの値を生成する
		if(e <= epsilon) //もしランダムの値がepsilonより小か等しければ
			return random.nextInt(4); //ランラムに行動番号を返す
		return selectAction(state);	//そうでない場合Greedy法によって行動を選択する
	}

	/**
	 * Greedy 法により行動を選択する
	 * @param state   現在の状態
	 * @return 選択された行動番号
	 */
	public int selectAction(int state)
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		//int action_num = 0; //行動の番号を格納変数
		double max = -1.0; //Q値テーブルでのstate状態における最大値を格納変数
		
		//Q値の値の最大値が複数個ある場合、ランダム的に行動番号を選択
		for(int i = 0; i < 4; i++) { 
				if(qTable[state][i] >= max){
					if(qTable[state][i] > max) {
					max = qTable[state][i];
					list.clear();
					}
				}
				list.add(i);
		}
		int size = list.size();
		return list.get(random.nextInt(size));
	}

	/**
	 * Ｑ値を更新する
	 * @param before 状態
	 * @param action 行動
	 * @param after  遷移後の状態
	 * @param reward 報酬
	 */
	public void update(int before, int action, int after, double reward)
	{
		double max = -1.0;
		for(int i = 0; i < 4; i++) {
			if(qTable[after][i] >= max) {
				max = qTable[after][i];
			}
		}
		qTable[before][action] = qTable[before][action] 
				+ alpha * (reward + gamma*max - qTable[before][action]);
		
	}
	/*
	 * Q値テーブルを出力する
	 */
	public void toString(int states) {
		for(int i = 0; i < states; i++) {
			for(int j = 0; j < 4; j++)
				System.out.print(i + " " + qTable[i][j] + ", ");
			System.out.println();
		}
	}

	//フィールド
	private double qTable[][] = null;
	private double alpha = 0;
	private double gamma = 0;	
	private Random random = new Random();
}
