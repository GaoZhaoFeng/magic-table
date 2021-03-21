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
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;
import sun.tools.jconsole.JConsole;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "Table APIs")
@RequestMapping("/table")
public class TableController {
    @Autowired
    MongoTemplate mongoTemplate;

    @PostMapping("/page_data")
    @ApiOperation("获取用户信息")
    List<JSONObject> getPageData(@RequestParam String table, @RequestParam int pageNum,
                                 @RequestParam int pageSize, @RequestBody JSONObject filter) {
        List<JSONObject> docs = new ArrayList<>();
        Bson filterBson = Document.parse(filter.toString());
        MongoCollection<Document> collection = mongoTemplate.getCollection(table);
        // Todo：注意这里的find是可以传入各种查询条件的，这里没有查询条件，所以传入为空，查所有数据。后面前端筛选这里肯定用地到
        FindIterable<Document> documents = collection.find(filterBson).skip((pageNum - 1) * pageSize).limit(pageSize);
        for (Document document : documents) {
            docs.add((JSONObject) JSON.parse(document.toJson()));
        }
        return docs;
    }
}
