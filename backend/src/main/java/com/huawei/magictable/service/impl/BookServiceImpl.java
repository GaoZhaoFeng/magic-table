package com.huawei.magictable.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huawei.magictable.dao.BookInfoDao;
import com.huawei.magictable.model.BookInfo;
import com.huawei.magictable.service.BookService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    @Autowired
    BookInfoDao bookInfoDao;

    @Override
    public JSONObject insertBookByURl(String url) {
        JSONObject result = new JSONObject();
        BookInfo bookInfo = new BookInfo();
        Document parse = null;
        try {
            parse = Jsoup.parse(new URL(url), 2000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bookInfo =this.spiderBook(parse,bookInfo);
        //url
        bookInfo.setUrl(url);
        bookInfoDao.save(bookInfo);
        result.put("bookInfo",bookInfo);
        System.out.println("书名"+bookInfo.getBookName()+" 链接: " + url);
        return result;
}

    @SneakyThrows
    @Override
    public JSONObject insertBooksByURl(String url) {
        JSONObject result = new JSONObject();
        List<JSONObject> books = new ArrayList<>();
        Document doc = null;
        try {
            URL detailUrl = new URL(url);
            doc = Jsoup.parse(detailUrl, 30000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements select = doc.select("a[name=itemlist-picture]");
        for (Element element : select) {
            BookInfo bookInfo = new BookInfo();
            String detail = element.attr("href");
            Document parse = Jsoup.parse(new URL(detail), 50000);
            bookInfo =  this.spiderBook(parse,bookInfo);
            bookInfo.setUrl(url);
            bookInfoDao.save(bookInfo);
            books.add((JSONObject) JSONObject.toJSON(bookInfo));
        }
        result.put("count",books.size());
        result.put("books",books);
        result.put("url",url);
        return result;
    }

    @Override
    public JSONObject queryAllBooks() {
        JSONObject result = new JSONObject();
        List<BookInfo> all = bookInfoDao.findAll();
        result.put("count",all.size());
        result.put("books",all);
        return result;
    }

    public BookInfo spiderBook(Document parse,BookInfo bookInfo){
        //书名
        String bookName = parse.select(".name_info").get(0).select("h1").get(0).attr("title");
        bookInfo.setBookName(bookName);
        //价格
        Elements e1 = parse.select("#original-price");
        String price = e1.html();
        int n = price.lastIndexOf(">");
        price = price.substring(n + 1);
        bookInfo.setPrice(Double.valueOf(price));
        Elements select1 = parse.select(".messbox_info");
        Elements elements = select1.get(0).select(".t1");

        //作者
        List<String> author = elements.get(0).select("a").stream().map(x -> x.html()).collect(Collectors.toList());
        bookInfo.setAuthor(StringUtils.join(author, ","));

        //出版社
        List<String> publisher = elements.get(1).select("a").stream().map(x -> x.html()).collect(Collectors.toList());
        bookInfo.setPublisher(StringUtils.join(publisher, ","));

        //出版时间
        String time = elements.get(2).html();
        bookInfo.setBook_time(time.substring(5, time.indexOf("&")));

        //ISBN
        String ISBN = parse.select("#detail_describe").get(0).child(0).child(4).html();
        bookInfo.setIsbn(ISBN.substring(11));

        //分类
        List<String> typeList = parse.select("#detail-category-path").get(0)
                .child(1).select("a").stream().map(x -> x.html()).collect(Collectors.toList());
        bookInfo.setType(StringUtils.join(typeList, ","));
        return bookInfo;
    }
}