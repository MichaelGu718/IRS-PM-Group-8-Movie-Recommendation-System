package com.nusiss.movie.listener;

import com.nusiss.movie.annotation.AutoInc;
import com.nusiss.movie.model.entity.Incr;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.util.Objects;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.Listener
 *
 * Created by tangyi on 2022-10-27 02:16
 * @author zjh
 * https://blog.csdn.net/qq_36989302/article/details/98944708
 *
 * modified by tangyi
 */
@Component
public class SaveMongoEventListener extends AbstractMongoEventListener<Object> {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public SaveMongoEventListener(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    /**
     * Listener for save event, increase ID by reflection
     * Now MongoDB data is about to operate the entity class converted to the Document object,
     * So at this time, the document object should be replaced with ID
     * @param event
     */
    @Override
    public void onBeforeSave(BeforeSaveEvent<Object> event) {
        //得到操作的实体类对象
        Object source = event.getSource();
        //spring-mongo-data与MongoDB交互的document对象
        Document document = event.getDocument();
        if (Objects.nonNull(source)) {
            //利用反射进行相关操作
            ReflectionUtils.doWithFields(source.getClass(), field -> {
                //使操作的成员可访问
                ReflectionUtils.makeAccessible(field);
                //该字段是否使用自增注解且是Number类型
                if (field.isAnnotationPresent(AutoInc.class) && field.get(source) instanceof Number) {
                    String collectionName = source.getClass().getSimpleName().substring(0, 1).toLowerCase()
                            + source.getClass().getSimpleName().substring(1);
                    //判断document不能为空
                    Assert.notNull(document,"event.document must not be null");
                    //only get and put IncrId when the document is new
                    if (document.get("_id") == null) {
                        //获取自增主键
                        Long incrId = getIncrId(collectionName);
                        //对ID进行替换
                        document.put("userId", incrId);
                        field.set(source,incrId);
                    }
                }
            });
        }
        super.onBeforeSave(event);
    }

    /**
     * return the next self-increment id
     * @param collectionName collection name
     * @return next self-increment id
     */
    private Long getIncrId(String collectionName) {
        Query query = new Query(Criteria.where("collectionName").is(collectionName));
        Update update = new Update();
        update.inc("incrId");
        FindAndModifyOptions options = FindAndModifyOptions.options();
        options.upsert(true);
        options.returnNew(true);
        Incr andModify = mongoTemplate.findAndModify(query, update, options, Incr.class);
        Assert.notNull(andModify,"id self-increment error");
        return andModify.getIncrId();
    }
}
