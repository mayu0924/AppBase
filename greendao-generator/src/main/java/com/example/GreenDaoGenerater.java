package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerater {
    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(1, "com.appheader.db");
        addArticle(schema);
        new DaoGenerator().generateAll(schema, "../AppBase/app/src/main/java-gen");

    }
    /**
     * 数据库对应表实体
     * @param schema
     */
    private static void addArticle(Schema schema) {
        Entity note = schema.addEntity("GreenArticle");
        note.addIdProperty();
        note.addStringProperty("title").notNull();
        note.addStringProperty("href");
        note.addStringProperty("date");
        note.addStringProperty("content");
        note.addStringProperty("color");
        note.addIntProperty("position");
    }
}
