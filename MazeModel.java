import java.io.*;

/**
 * 強化学習によりゴールまでの経路を学習するクラス
 */
public class MazeModel implements Runnable {
	
  /**
   * 強化学習によりゴールまでの経路を学習するオブジェクトを生成する
   * @param mazeFile 迷路データのファイル名
   */
  public MazeModel(String mazeFile)
  {
    // 迷路データを生成
    mazeData = new MazeData(mazeFile);
    // ロボットを生成
    robot = new Robot(mazeData.getSX(), mazeData.getSY());
  }

  /**
   * 実行用関数
   */
  public void run()
  {
    try {
    	int states = mazeData.getHeight() * mazeData.getWidth(); 
    	int actions = 4; //上下左右で行動の数が４である
    	double alpha = 1.0;
    	double gamma = 1.0;
    	QLearning q1 = new QLearning(states, actions, alpha, gamma);
    	int trials = 500;
    	int steps = 100;
    	for(int t = 0; t < trials; t++) {
    		robot.setX(mazeData.getSX());
    		robot.setY(mazeData.getSY());
    		for(int s = 0; s < steps; s++) {
    			/*ε-Greedy法により行動を選択*/
    			int state = 0; //現在の状態、0で初期化
    			
    			/*現在の状態を取得する*/
    			for(int i = 0; i < robot.getX(); i++) 
    				state++;
    			for(int j = 0; j < robot.getY(); j++)
    				state += mazeData.getWidth();
    			
    			double epsilon = 0.5;
    			
    			int action = q1.selectAction(state, epsilon);
    			
    			int x = robot.getX();
    			int y = robot.getY();
    			/*選択した行動を実行し、ロボットを移動させる
    			 * action = 0の時、左に
    			 * action = 1の時、右に
    			 * action = 2の時、上に
    			 * action = 3の時、下に
    			 */
    			
    			if(action == 0 && x > 0)
    				x--;
    			else if(action == 1 && x < mazeData.getWidth() - 1)
    				x++;
    			else if(action == 2 && y > 0)
    				y--;
    			else if(action == 3 && y < mazeData.getHeight() - 1)
    				y++;
    			
    			robot.setX(x);
    			robot.setY(y);
    			int x_ = robot.getX();
    			int y_ = robot.getY();
    			
    			/*新しいを取得する*/
    			int after = 0;
    			for(int i = 0; i < x_; i++)
    				after++;
    			for(int j = 0; j < y_; j++)
    				after += mazeData.getWidth();
    			/*報酬関数を定義する
    			 * 現在の状態が壁であれば -10
    			 * 現在の状態が通路やスタートでれば +10
    			 * 現在の状態がゴールであれば +100
    			 */
    			int[] reward = new int[states];
    			for(int i = 0; i < states; i++)
    				reward[i] = 0;
    			if(mazeData.get(x_, y_) == MazeData.BLOCK)
    				reward[after] = -50;
    			else if(mazeData.get(x_, y_) == MazeData.SPACE
    					|| mazeData.get(x_, y_) == MazeData.START)
    				reward[after] = 20;
    			else if(mazeData.get(x_, y_) == MazeData.GOAL)
    				reward[after] = 100;
    			
    			/*Q値を更新する*/
    			q1.update(state, action, after, reward[after]); 
    	        // 現在の状態を描画する
    	        mazeView.repaint();

    	        // 速すぎるので 500msec 寝る
    	        Thread.sleep(5);
    	        
    	        
    		}
    		
    	}
    	q1.toString(states);
    	
      /*
      // ここをがんばって作る
      
      // ゴール座標の取得
      int gx = mazeData.getGX();
      int gy = mazeData.getGY();
      
      while (true) {

        // ロボットの現在位置を取得
        int x = robot.getX();
        int y = robot.getY();
        
        // 下に行けるならば下に行く
        if (mazeData.get(x, y+1) != MazeData.BLOCK)
          y++;
        
        // ロボットの位置座標を更新
        robot.setX(x);
        robot.setY(y);
        
        // 現在の状態を描画する
        mazeView.repaint();

        // 速すぎるので 500msec 寝る
        Thread.sleep(500);

        // もしゴールに到達すれば終了
        if (x == gx && y == gy)
          break;

        // デバッグ用に現在位置を出力
        System.out.println("x = " + x + ", y = " + y);
      }*/
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }
  
  /**
   * 描画用のビューを登録
   */
  public void setView(MazeView view)
  {
    mazeView = view;
  }
  
  /**
   * ロボットオブジェクトを取得する
   * @return ロボットオブジェクト
   */
  public Robot getRobot()
  {
    return robot;
  }
  
  /**
   * 迷路データオブジェクトを取得する
   * @return 迷路データオブジェクト
   */
  public MazeData getMazeData()
  {
    return mazeData;
  }
  
  /** 迷路データ */
  private MazeData mazeData = null;
  /** ロボットデータ */
  private Robot robot = null;

  /** 描画用オブジェクト */
  private MazeView mazeView = null;
}
