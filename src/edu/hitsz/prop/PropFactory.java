package edu.hitsz.prop;

/**
 * 道具工厂类，实现简单工厂模式
 * @author laijr
 */
public class PropFactory {

    /**
     * 根据道具类型创建道具实例
     * @param type 道具类型
     * @param locationX X坐标
     * @param locationY Y坐标
     * @param speedX X速度
     * @param speedY Y速度
     * @return 道具实例
     */
    public static BaseProp createProp(PropTypeEnum type, int locationX, int locationY, int speedX, int speedY) {
        switch (type) {
            case BLOOD:
                return new PropBlood(locationX, locationY, speedX, speedY);
            case BULLET:
                return new PropBullet(locationX, locationY, speedX, speedY);
            case BOMB:
                return new PropBomb(locationX, locationY, speedX, speedY);
            case BULLET_PLUS:
                return new PropBombPlus(locationX, locationY, speedX, speedY);
            default:
                throw new IllegalArgumentException("Unknown prop type: " + type);
        }
    }
}