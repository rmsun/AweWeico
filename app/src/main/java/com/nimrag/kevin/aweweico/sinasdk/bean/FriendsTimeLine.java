package com.nimrag.kevin.aweweico.sinasdk.bean;

import com.nimrag.kevin.aweweico.lib.IResult;
import com.nimrag.kevin.aweweico.lib.orm.annotation.PrimaryKey;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kevin on 2017/3/4.
 * 当前用户所关注用户的最新微博信息 (别名: statuses/home_timeline)
 */

public class FriendsTimeLine implements IResult{
    /**
     * statuses : [{"created_at":"Tue May 31 17:46:55 +0800 2011","id":11488058246,"text":"求关注。","source":"新浪微博","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","geo":null,"mid":"5612814510546515491","reposts_count":8,"comments_count":9,"annotations":[],"user":{"id":1404376560,"screen_name":"zaku","name":"zaku","province":"11","city":"5","location":"北京 朝阳区","description":"人生五十年，乃如梦如幻；有生斯有死，壮士复何憾。","url":"http://blog.sina.com.cn/zaku","profile_image_url":"http://tp1.sinaimg.cn/1404376560/50/0/1","domain":"zaku","gender":"m","followers_count":1204,"friends_count":447,"statuses_count":2908,"favourites_count":0,"created_at":"Fri Aug 28 00:00:00 +0800 2009","following":false,"allow_all_act_msg":false,"remark":"","geo_enabled":true,"verified":false,"allow_all_comment":true,"avatar_large":"http://tp1.sinaimg.cn/1404376560/180/0/1","verified_reason":"","follow_me":false,"online_status":0,"bi_followers_count":215}}]
     * ad : [{"id":3366614911586452,"mark":"AB21321XDFJJK"}]
     * total_number : 81655
     */

    private int total_number;
    /**
     * since_id max_id分别用于刷新和加载更多
     */
    private long since_id;
    private long max_id;
    private List<StatusesBean> statuses;
    private List<AdBean> ad;
    private boolean outOfDate = false;
    private boolean fromCache = false;

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public long getSince_id() {return since_id;}

    public void setSince_id(long since_id) { this.since_id = since_id; }

    public long getMax_id() { return max_id; }

    public void setMax_id(long max_id) { this.max_id = max_id; }

    public List<StatusesBean> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<StatusesBean> statuses) {
        this.statuses = statuses;
    }

    public List<AdBean> getAd() {
        return ad;
    }

    public void setAd(List<AdBean> ad) {
        this.ad = ad;
    }

    public static class StatusesBean implements Serializable, IResult{
        private static final long serialVersionUID = 6106704567725953159L;
        /**
         * created_at : Tue May 31 17:46:55 +0800 2011
         * id : 11488058246
         * text : 求关注。
         * source : 新浪微博
         * favorited : false
         * truncated : false
         * in_reply_to_status_id :
         * in_reply_to_user_id :
         * in_reply_to_screen_name :
         * geo : null
         * mid : 5612814510546515491
         * reposts_count : 8
         * comments_count : 9
         * annotations : []
         * user : {"id":1404376560,"screen_name":"zaku","name":"zaku","province":"11","city":"5","location":"北京 朝阳区","description":"人生五十年，乃如梦如幻；有生斯有死，壮士复何憾。","url":"http://blog.sina.com.cn/zaku","profile_image_url":"http://tp1.sinaimg.cn/1404376560/50/0/1","domain":"zaku","gender":"m","followers_count":1204,"friends_count":447,"statuses_count":2908,"favourites_count":0,"created_at":"Fri Aug 28 00:00:00 +0800 2009","following":false,"allow_all_act_msg":false,"remark":"","geo_enabled":true,"verified":false,"allow_all_comment":true,"avatar_large":"http://tp1.sinaimg.cn/1404376560/180/0/1","verified_reason":"","follow_me":false,"online_status":0,"bi_followers_count":215}
         */

        /**
         * 创建时间
         */
        private String created_at;
        /**
         * 微博id
         */
        @PrimaryKey(column = "id")
        private long id;
        /**
         * 微博内容
         */
        private String text;
        /**
         * 微博来源
         */
        private String source;
        /**
         * 是否已收藏
         */
        private boolean favorited;
        /**
         * 是否被截断
         */
        private boolean truncated;
        private String in_reply_to_status_id;
        private String in_reply_to_user_id;
        private String in_reply_to_screen_name;
        private Object geo;
        private String mid;
        /**
         * 转发数量
         */
        private int reposts_count;
        /**
         * 评论数量
         */
        private int comments_count;
        /**
         * 表态数量
         */
        private int attitudes_count;
        /**
         * 缩略图
         */
        private String thumbnail_pic;

        /**
         * 中型图片
         */
        private String bmiddle_pic;

        /**
         * 原始图片
         */
        private String original_pic;

        /**
         * 图片配图，多图时，返回多图链接
         */
        private List<picUrlsBean> pic_urls;

        private UserBean user;

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public boolean isFavorited() {
            return favorited;
        }

        public void setFavorited(boolean favorited) {
            this.favorited = favorited;
        }

        public boolean isTruncated() {
            return truncated;
        }

        public void setTruncated(boolean truncated) {
            this.truncated = truncated;
        }

        public String getIn_reply_to_status_id() {
            return in_reply_to_status_id;
        }

        public void setIn_reply_to_status_id(String in_reply_to_status_id) {
            this.in_reply_to_status_id = in_reply_to_status_id;
        }

        public String getIn_reply_to_user_id() {
            return in_reply_to_user_id;
        }

        public void setIn_reply_to_user_id(String in_reply_to_user_id) {
            this.in_reply_to_user_id = in_reply_to_user_id;
        }

        public String getIn_reply_to_screen_name() {
            return in_reply_to_screen_name;
        }

        public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
            this.in_reply_to_screen_name = in_reply_to_screen_name;
        }

        public Object getGeo() {
            return geo;
        }

        public void setGeo(Object geo) {
            this.geo = geo;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public int getReposts_count() {
            return reposts_count;
        }

        public void setReposts_count(int reposts_count) {
            this.reposts_count = reposts_count;
        }

        public int getComments_count() {
            return comments_count;
        }

        public void setComments_count(int comments_count) {
            this.comments_count = comments_count;
        }

        public int getAttitudes_count() { return attitudes_count; }

        public void setAttitudes_count(int attitudes_count) { this.attitudes_count = attitudes_count; }

        public String getThumbnail_pic() {return thumbnail_pic;}

        public void setThumbnail_pic(String thumbnail_pic) {this.thumbnail_pic = thumbnail_pic;}

        public String getBmiddle_pic() {return bmiddle_pic;}

        public void setBmiddle_pic(String bmiddle_pic) {this.bmiddle_pic = bmiddle_pic;}

        public String getOriginal_pic() {return original_pic;}

        public void setOriginal_pic(String original_pic) {this.original_pic = original_pic;}

        public List<picUrlsBean> getPic_urls() {
            return pic_urls;
        }

        public void setPic_urls(List<picUrlsBean> pic_urls) {this.pic_urls = pic_urls;}

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * id : 1404376560
             * screen_name : zaku
             * name : zaku
             * province : 11
             * city : 5
             * location : 北京 朝阳区
             * description : 人生五十年，乃如梦如幻；有生斯有死，壮士复何憾。
             * url : http://blog.sina.com.cn/zaku
             * profile_image_url : http://tp1.sinaimg.cn/1404376560/50/0/1
             * domain : zaku
             * gender : m
             * followers_count : 1204
             * friends_count : 447
             * statuses_count : 2908
             * favourites_count : 0
             * created_at : Fri Aug 28 00:00:00 +0800 2009
             * following : false
             * allow_all_act_msg : false
             * remark :
             * geo_enabled : true
             * verified : false
             * allow_all_comment : true
             * avatar_large : http://tp1.sinaimg.cn/1404376560/180/0/1
             * verified_reason :
             * follow_me : false
             * online_status : 0
             * bi_followers_count : 215
             */

            private long id;
            private String screen_name;
            private String name;
            private String province;
            private String city;
            private String location;
            private String description;
            private String url;
            private String profile_image_url;
            private String domain;
            private String gender;
            private int followers_count;
            private int friends_count;
            private int statuses_count;
            private int favourites_count;
            private String created_at;
            private boolean following;
            private boolean allow_all_act_msg;
            private String remark;
            private boolean geo_enabled;
            private boolean verified;
            private boolean allow_all_comment;
            private String avatar_large;
            private String verified_reason;
            private boolean follow_me;
            private int online_status;
            private int bi_followers_count;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getScreen_name() {
                return screen_name;
            }

            public void setScreen_name(String screen_name) {
                this.screen_name = screen_name;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getProfile_image_url() {
                return profile_image_url;
            }

            public void setProfile_image_url(String profile_image_url) {
                this.profile_image_url = profile_image_url;
            }

            public String getDomain() {
                return domain;
            }

            public void setDomain(String domain) {
                this.domain = domain;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public int getFollowers_count() {
                return followers_count;
            }

            public void setFollowers_count(int followers_count) {
                this.followers_count = followers_count;
            }

            public int getFriends_count() {
                return friends_count;
            }

            public void setFriends_count(int friends_count) {
                this.friends_count = friends_count;
            }

            public int getStatuses_count() {
                return statuses_count;
            }

            public void setStatuses_count(int statuses_count) {
                this.statuses_count = statuses_count;
            }

            public int getFavourites_count() {
                return favourites_count;
            }

            public void setFavourites_count(int favourites_count) {
                this.favourites_count = favourites_count;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public boolean isFollowing() {
                return following;
            }

            public void setFollowing(boolean following) {
                this.following = following;
            }

            public boolean isAllow_all_act_msg() {
                return allow_all_act_msg;
            }

            public void setAllow_all_act_msg(boolean allow_all_act_msg) {
                this.allow_all_act_msg = allow_all_act_msg;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public boolean isGeo_enabled() {
                return geo_enabled;
            }

            public void setGeo_enabled(boolean geo_enabled) {
                this.geo_enabled = geo_enabled;
            }

            public boolean isVerified() {
                return verified;
            }

            public void setVerified(boolean verified) {
                this.verified = verified;
            }

            public boolean isAllow_all_comment() {
                return allow_all_comment;
            }

            public void setAllow_all_comment(boolean allow_all_comment) {
                this.allow_all_comment = allow_all_comment;
            }

            public String getAvatar_large() {
                return avatar_large;
            }

            public void setAvatar_large(String avatar_large) {
                this.avatar_large = avatar_large;
            }

            public String getVerified_reason() {
                return verified_reason;
            }

            public void setVerified_reason(String verified_reason) {
                this.verified_reason = verified_reason;
            }

            public boolean isFollow_me() {
                return follow_me;
            }

            public void setFollow_me(boolean follow_me) {
                this.follow_me = follow_me;
            }

            public int getOnline_status() {
                return online_status;
            }

            public void setOnline_status(int online_status) {
                this.online_status = online_status;
            }

            public int getBi_followers_count() {
                return bi_followers_count;
            }

            public void setBi_followers_count(int bi_followers_count) {
                this.bi_followers_count = bi_followers_count;
            }
        }

        @Override
        public boolean outOfDate() {
            return false;
        }

        @Override
        public boolean fromCache() {
            return false;
        }
    }

    public static class AdBean {
        /**
         * id : 3366614911586452
         * mark : AB21321XDFJJK
         */

        private long id;
        private String mark;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }
    }

    public static class picUrlsBean {
        private String thumbnail_pic;

        public String getThumbnail_pic() {return thumbnail_pic;}
        public void setThumbnail_pic(String thumbnail_pic) {this.thumbnail_pic = thumbnail_pic;}
    }

    @Override
    public boolean outOfDate() {
        return outOfDate;
    }

    @Override
    public boolean fromCache() {
        return fromCache;
    }

    public void setOutOfDate(boolean v) { this.outOfDate = v; }
    public void setFromCache(boolean v) { this.fromCache = v; }
}
