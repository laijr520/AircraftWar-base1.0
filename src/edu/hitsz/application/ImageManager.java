package edu.hitsz.application;


import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.enemy.BossEnemy;
import edu.hitsz.aircraft.enemy.EliteEnemy;
import edu.hitsz.aircraft.enemy.EliteProEnemy;
import edu.hitsz.aircraft.enemy.ElitePlusEnemy;
import edu.hitsz.aircraft.enemy.MobEnemy;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.gameConfig.DifficultyLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * 综合管理图片的加载，访问
 * 提供图片的静态访问方法
 * @author hitsz
 */
public class ImageManager {

    /**
     * 类名-图片 映射，存储各基类的图片 <br>
     * 可使用 CLASSNAME_IMAGE_MAP.get( obj.getClass().getName() ) 获得 obj 所属基类对应的图片
     */
    private static final Map<String, BufferedImage> CLASSNAME_IMAGE_MAP = new HashMap<>();
    //背景图片
    public static BufferedImage BACKGROUND_IMAGE;
    //英雄机图片
    public static BufferedImage HERO_IMAGE;
    //子弹图片
    public static BufferedImage HERO_BULLET_IMAGE;
    public static BufferedImage ENEMY_BULLET_IMAGE;
    //敌机图片
    public static BufferedImage MOB_ENEMY_IMAGE;
    public static BufferedImage ELITE_ENEMY_IMAGE;
    public static BufferedImage ELITE_PLUS_ENEMY_IMAGE;
    public static BufferedImage ELITE_PRO_ENEMY_IMAGE;
    public static BufferedImage BOSS_ENEMY_IMAGE;
    //道具图片
    public static BufferedImage PROP_BLOOD_IMAGE;
    public static BufferedImage PROP_BULLET_IMAGE;
    public static BufferedImage PROP_BOMB_IMAGE;
    public static BufferedImage PROP_BULLET_PLUS_IMAGE;

    static {
        try {

            BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg.jpg"));

            HERO_IMAGE = ImageIO.read(new FileInputStream("src/images/hero.png"));
            MOB_ENEMY_IMAGE = ImageIO.read(new FileInputStream("src/images/mob.png"));
            ELITE_ENEMY_IMAGE = ImageIO.read(new FileInputStream("src/images/elite.png"));
            ELITE_PLUS_ENEMY_IMAGE = ImageIO.read(new FileInputStream("src/images/ElitePro.png"));
            ELITE_PRO_ENEMY_IMAGE = ImageIO.read(new FileInputStream("src/images/ElitePlus.png"));
            BOSS_ENEMY_IMAGE = ImageIO.read(new FileInputStream("src/images/boss.png"));

            HERO_BULLET_IMAGE = ImageIO.read(new FileInputStream("src/images/bullet_hero.png"));
            ENEMY_BULLET_IMAGE = ImageIO.read(new FileInputStream("src/images/bullet_enemy.png"));

            PROP_BLOOD_IMAGE = ImageIO.read(new FileInputStream("src/images/prop_blood.png"));
            PROP_BULLET_IMAGE = ImageIO.read(new FileInputStream("src/images/prop_bullet.png"));
            PROP_BOMB_IMAGE = ImageIO.read(new FileInputStream("src/images/prop_bomb.png"));
            PROP_BULLET_PLUS_IMAGE = ImageIO.read(new FileInputStream("src/images/prop_bulletPlus.png"));
            
            CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), HERO_IMAGE);
            CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), MOB_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), ELITE_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EliteProEnemy.class.getName(), ELITE_PLUS_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(ElitePlusEnemy.class.getName(), ELITE_PRO_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BossEnemy.class.getName(), BOSS_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), HERO_BULLET_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ENEMY_BULLET_IMAGE);

            CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), MOB_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), ELITE_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EliteProEnemy.class.getName(), ELITE_PLUS_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(ElitePlusEnemy.class.getName(), ELITE_PRO_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BossEnemy.class.getName(), BOSS_ENEMY_IMAGE);

            CLASSNAME_IMAGE_MAP.put(edu.hitsz.prop.PropBlood.class.getName(), PROP_BLOOD_IMAGE);
            CLASSNAME_IMAGE_MAP.put(edu.hitsz.prop.PropBullet.class.getName(), PROP_BULLET_IMAGE);
            CLASSNAME_IMAGE_MAP.put(edu.hitsz.prop.PropBomb.class.getName(), PROP_BOMB_IMAGE);
            CLASSNAME_IMAGE_MAP.put(edu.hitsz.prop.PropBulletPlus.class.getName(), PROP_BULLET_PLUS_IMAGE);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static BufferedImage get(String className){
        return CLASSNAME_IMAGE_MAP.get(className);
    }

    public static BufferedImage get(Object obj){
        if (obj == null){
            return null;
        }
        return get(obj.getClass().getName());
    }

    private static final Map<DifficultyLevel, String> BG_PATH = new EnumMap<>(DifficultyLevel.class);
    private static final Map<DifficultyLevel, BufferedImage> BG_CACHE = new EnumMap<>(DifficultyLevel.class);
    static {
        BG_PATH.put(DifficultyLevel.NORMAL, "src/images/bg.jpg");
        BG_PATH.put(DifficultyLevel.HARD,   "src/images/bg2.jpg");
        BG_PATH.put(DifficultyLevel.EXPERT, "src/images/bg3.jpg");
    }

    public static BufferedImage loadBackground(DifficultyLevel level) {
        BufferedImage cached = BG_CACHE.get(level);
        if (cached != null) {
            return cached;
        }
        String path = BG_PATH.getOrDefault(level, "src/images/bg.jpg");
        try (FileInputStream in = new FileInputStream(path)) {
            BufferedImage img = ImageIO.read(in);
            BG_CACHE.put(level, img);
            return img;
        } catch (IOException e) {
            e.printStackTrace();
            return BACKGROUND_IMAGE;
        }
    }

    public static void applyBackground(DifficultyLevel level) {
        BufferedImage img = loadBackground(level);
        if (img != null) {
            BACKGROUND_IMAGE = img;
        }
    }
}
