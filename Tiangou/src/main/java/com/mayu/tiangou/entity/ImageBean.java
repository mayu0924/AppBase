package com.mayu.tiangou.entity;

import java.util.List;

/**
 * Created by mayu on 16/6/12,下午5:12.
 */
public class ImageBean {

    /**
     * status : true
     * total : 54
     * tngou : [{"count":1288,"fcount":0,"galleryclass":2,"id":684,"img":"/ext/160310/93a453490dcda4a4294ace7a8e159fb1.jpg","rcount":0,"size":8,"time":1457572889000,"title":"美女户外清纯靓丽写真"},{"count":1636,"fcount":0,"galleryclass":2,"id":667,"img":"/ext/160301/38c8feeaf15859a543f3a882218c3a14.jpg","rcount":0,"size":28,"time":1456798717000,"title":"日本美女歌手白石麻衣清纯私房照"}]
     */

    private boolean status;
    private int total;
    /**
     * count : 1288
     * fcount : 0
     * galleryclass : 2
     * id : 684
     * img : /ext/160310/93a453490dcda4a4294ace7a8e159fb1.jpg
     * rcount : 0
     * size : 8
     * time : 1457572889000
     * title : 美女户外清纯靓丽写真
     */

    private List<TngouBean> tngou;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<TngouBean> getTngou() {
        return tngou;
    }

    public void setTngou(List<TngouBean> tngou) {
        this.tngou = tngou;
    }

    public static class TngouBean {
        private int count;
        private int fcount;
        private int galleryclass;
        private int id;
        private String img;
        private int rcount;
        private int size;
        private long time;
        private String title;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getFcount() {
            return fcount;
        }

        public void setFcount(int fcount) {
            this.fcount = fcount;
        }

        public int getGalleryclass() {
            return galleryclass;
        }

        public void setGalleryclass(int galleryclass) {
            this.galleryclass = galleryclass;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getRcount() {
            return rcount;
        }

        public void setRcount(int rcount) {
            this.rcount = rcount;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
