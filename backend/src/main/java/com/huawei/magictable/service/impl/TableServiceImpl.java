/***********************************************************
 * @Description : 
 * @author      : 梁山广(Liang Shan Guang)
 * @date        : 2021/3/22 00:04
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.magictable.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huawei.magictable.service.TableService;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TableServiceImpl implements TableService {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public JSONObject getPageData(String table, int pageNum, int pageSize, JSONObject filter) {
        JSONObject result = new JSONObject();
        List<JSONObject> docs = new ArrayList<>();
        // json必须转换为bson才能进行查询
        Bson filterBson = Document.parse(filter.getJSONObject("filter").toString());
        Bson sorterBson = Document.parse(filter.getJSONObject("sorter").toString());
        MongoCollection<Document> collection = mongoTemplate.getCollection(table);
        // 1.查询符合筛选条件的文档数目
        long cnt = collection.countDocuments(filterBson);
        // 2.查询符合条件的指定页的文档
        // 注意这里的find是可以传入各种查询条件的，查所有数据前端传入个{}就行，查询指定条件传例如
        FindIterable<Document> documents = collection
                /* 过滤 */
                .find(filterBson)
                /* 排序 */
                .sort(sorterBson)
                /* 结果分页 */
                .skip((pageNum - 1) * pageSize).limit(pageSize);
        for (Document document : documents) {
            document.append("id", document.getObjectId("_id").toString());
            docs.add((JSONObject) JSON.parse(document.toJson()));
        }
        // 3.组装返回数据
        result.put("table", table);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        result.put("totalCnt", cnt);
        result.put("pageData", docs);
        return result;
    }
}
