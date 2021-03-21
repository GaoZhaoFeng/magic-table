/***********************************************************
 * @Description : 获取用户自定义表的数据
 * @author      : 梁山广(Liang Shan Guang)
 * @date        : 2021/3/21 21:25
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.magictable.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "Table APIs")
@RequestMapping("/table")
public class TableController {
    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * 根据查询条件得到分页查询结果
     * @param table 表名
     * @param pageNum 第几页，从1开始
     * @param pageSize 每页多少条
     * @param filter 查询语法，参考https://www.runoob.com/mongodb/mongodb-query.html
     *               举例如下：查询40~70岁，且姓金或鲍的人
     * {
     *     "age": {
     *         "$gt": 40,
     *         "$lt": 70
     *     },
     *     "$or": [
     *         {
     *             "driver": {
     *                 "$regex": "金"
     *             }
     *         },
     *         {
     *             "driver": {
     *                 "$regex": "鲍"
     *             }
     *         }
     *     ]
     * }
     * @return 符合条件的记录数
     */
    @PostMapping("/page_data")
    @ApiOperation("获取用户信息")
    List<JSONObject> getPageData(@RequestParam String table, @RequestParam int pageNum,
                                 @RequestParam int pageSize, @RequestBody JSONObject filter) {
        List<JSONObject> docs = new ArrayList<>();
        // json必须转换为bson才能进行查询
        Bson filterBson = Document.parse(filter.toString());
        MongoCollection<Document> collection = mongoTemplate.getCollection(table);
        // 注意这里的find是可以传入各种查询条件的，查所有数据前端传入个{}就行，查询指定条件传例如
        FindIterable<Document> documents = collection.find(filterBson).skip((pageNum - 1) * pageSize).limit(pageSize);
        for (Document document : documents) {
            docs.add((JSONObject) JSON.parse(document.toJson()));
        }
        return docs;
    }
}
