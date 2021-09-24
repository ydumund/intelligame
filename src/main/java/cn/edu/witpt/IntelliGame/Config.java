package cn.edu.witpt.IntelliGame;

/**
 * @author nIck_
 */
public final class Config {
    private Config(){}

    /*
    * 窗口大小
    * */

    public static final int WIDTH = (int) (Math.pow(2,7)*7);
    public static final int HEIGHT = (int) (Math.pow(2,7)*7);

    /*
    * 秒显示级别信息
    * */

    public static final double PLAYER_MOVE_SPEED = 300;
    public static final double MONSTER_MOVE_SPEED = 20;

    public static final int START_LIVES = 3;
    public static final int START_HOUSE_LIVES = 5;

    /*
    * 攻击速度
    * */

    public static final double PLAYER_ATTACK_SPEED = 2.0;

    public static final class Asset {
        public static final String FXML_MAIN_UI = "main.fxml";
    }

    public static final double INVINCIBILITY_TIME = 1.0;
    public static final int SCORE_MONSTER_KILL = 7;

}
