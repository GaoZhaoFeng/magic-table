package com.huawei.magictable.dao;

import com.huawei.magictable.model.BookInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zf100
 */
public interface BookInfoDao extends JpaRepository<BookInfo,String>{
}
