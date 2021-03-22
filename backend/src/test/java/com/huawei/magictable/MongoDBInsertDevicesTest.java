/**
 * 测试MongoDB插入数据，10000个汽车，每个汽车有一条记录，包括车牌号、驾驶员、手机号、油耗
 */
package com.huawei.magictable;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MongoDBInsertDevicesTest {

    @Autowired
    MongoTemplate mongoTemplate;

    //生成随机手机号
    String getRandPhone() {
        String[] heads = {"134", "138", "139", "150", "151", "152", "157", "158", "159", "170", "189"};
        StringBuilder phone = new StringBuilder(heads[(int) (Math.random() * heads.length)]);
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        for (int i = 0; i < 8; i++) {
            phone.append(numbers[(int) (Math.random() * numbers.length)]);
        }
        return phone.toString();
    }

    // 生成车牌号
    String getRandPlate() {
        // 地区，用于生成车牌号
        String[] loc = {"川", "渝", "贵", "陕", "京", "沪", "粤", "津", "赣", "湘", "鄂"};
        // 字母，用于生成车牌号
        String[] chars = {"A", "B", "C", "D"};
        //数字，用于生成车牌号
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        StringBuilder plate = new StringBuilder();
        plate.append(loc[(int) (Math.random() * loc.length)]); //第一位地区简称
        plate.append(chars[(int) (Math.random() * chars.length)]); //第二位字母简称
        // 4位数字
        for (int i = 0; i < 4; i++) {
            plate.append(numbers[(int) (Math.random() * numbers.length)]);
        }
        return plate.toString();
    }

    // 生成名字
    String getRandName() {
        String[] familyNames = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈",
                "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许",
                "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏",
                "陶", "姜", "戚", "谢", "邹", "喻", "柏", "水", "窦", "章",
                "云", "苏", "潘", "葛", "奚", "范", "彭", "郎", "鲁", "韦",
                "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳",
                "酆", "鲍", "史", "唐", "费", "廉", "岑", "薛", "雷", "贺",
                "倪", "汤", "滕", "殷", "罗", "毕", "郝", "邬", "安", "常",
                "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余",
                "元", "卜", "顾", "孟", "平", "黄", "和", "穆", "萧", "尹"
        };
        String[] givenNames = {"子璇", "淼", "国栋", "夫子", "瑞堂", "甜", "敏", "尚", "国贤", "贺祥", "晨涛",
                "昊轩", "易轩", "益辰", "益帆", "益冉", "瑾春", "瑾昆", "春齐", "杨", "文昊",
                "东东", "雄霖", "浩晨", "熙涵", "溶溶", "冰枫", "欣欣", "宜豪", "欣慧", "建政",
                "美欣", "淑慧", "文轩", "文杰", "欣源", "忠林", "榕润", "欣汝", "慧嘉", "新建",
                "建林", "亦菲", "林", "冰洁", "佳欣", "涵涵", "禹辰", "淳美", "泽惠", "伟洋",
                "涵越", "润丽", "翔", "淑华", "晶莹", "凌晶", "苒溪", "雨涵", "嘉怡", "佳毅",
                "子辰", "佳琪", "紫轩", "瑞辰", "昕蕊", "萌", "明远", "欣宜", "泽远", "欣怡",
                "佳怡", "佳惠", "晨茜", "晨璐", "运昊", "汝鑫", "淑君", "晶滢", "润莎", "榕汕",
                "佳钰", "佳玉", "晓庆", "一鸣", "语晨", "添池", "添昊", "雨泽", "雅晗", "雅涵",
                "清妍", "诗悦", "嘉乐", "晨涵", "天赫", "玥傲", "佳昊", "天昊", "萌萌", "若萌",
                "泽民", "国强", "胜利", "小凡", "碧瑶", "书书", "京雨", "卫东", "小佳", "长江"
        };
        String name = familyNames[(int) (Math.random() * familyNames.length)];
        name += givenNames[(int) (Math.random() * givenNames.length)];
        return name;
    }

    /**
     * 生成年龄
     */
    int getAge() {
        return (int) (Math.random() * 100);
    }

    /**
     * 生成油耗
     *
     * @param base 油耗基准值
     */
    double getFuelConsume(int base, int range) {
        assert base > range;
        double[] trueRange = {base - range, base + range}; //假定偏离基准不超过range
        double fuel = Math.random() * (trueRange[1] - trueRange[0]);
        fuel += trueRange[0];
        return (int) (fuel * 100) * 1.0 / 100; //转成2位小数
    }


    List<Document> genData(int deviceNum) {
        List<Document> devDocs = new ArrayList<>();
        for (int i = 0; i < deviceNum; i++) {
            String phone = getRandPhone();
            String plate = getRandPlate();
            String driver = getRandName();
            int age = getAge();
            double fuel = getFuelConsume(1000, 600);
            Document doc = new Document();
            doc.append("plate", plate)
                    .append("fuel", fuel)
                    .append("driver", driver)
                    .append("phone", phone)
                    .append("age", age);
            devDocs.add(doc);
        }
        return devDocs;
    }

    @Test
    void addDevices() {
        MongoCollection<Document> device = mongoTemplate.getCollection("device");
        List<Document> docs = genData(9876);
        device.insertMany(docs);
        System.out.println(docs.size());
    }
}

