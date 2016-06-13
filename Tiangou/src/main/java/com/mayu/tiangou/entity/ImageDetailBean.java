package com.mayu.tiangou.entity;

import java.util.List;

/**
 * Created by mayu on 16/6/14,上午10:26.
 */
public class ImageDetailBean {

    /**
     * count : 2260
     * fcount : 0
     * galleryclass : 1
     * id : 18
     * img : /ext/150714/e76407c9a23da57a0f30690aa7917f3e.jpg
     * list : [{"gallery":18,"id":260,"src":"/ext/150714/e76407c9a23da57a0f30690aa7917f3e.jpg"},{"gallery":18,"id":261,"src":"/ext/150714/6ca012a9644e7bed72e2f7cfcbd92cb9.jpg"},{"gallery":18,"id":262,"src":"/ext/150714/62c84af13e36cbb13c902a58a8e8ce81.jpg"},{"gallery":18,"id":263,"src":"/ext/150714/7cf66ef7494e13d3bb09a16bb7170328.jpg"},{"gallery":18,"id":264,"src":"/ext/150714/5b1c5cb5419f46d48ae768d569d69b37.jpg"},{"gallery":18,"id":265,"src":"/ext/150714/98b9b436a060f67b3cbbab15e0bb5d51.jpg"}]
     * rcount : 0
     * size : 6
     * status : true
     * time : 1436878500000
     * title : MiStar苏小曼姿势性感诱人私房照
     * url : http://www.tngou.net/tnfs/show/18
     */

    private int count;
    private int fcount;
    private int galleryclass;
    private int id;
    private String img;
    private int rcount;
    private int size;
    private boolean status;
    private long time;
    private String title;
    private String url;
    /**
     * gallery : 18
     * id : 260
     * src : /ext/150714/e76407c9a23da57a0f30690aa7917f3e.jpg
     */

    private List<ListBean> list;

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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private int gallery;
        private int id;
        private String src;

        public int getGallery() {
            return gallery;
        }

        public void setGallery(int gallery) {
            this.gallery = gallery;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }
    }
}
