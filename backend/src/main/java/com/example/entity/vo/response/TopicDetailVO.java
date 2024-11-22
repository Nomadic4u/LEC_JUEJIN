package com.example.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
public class TopicDetailVO {
    Integer id;
    String title;
    String content;
    Integer type;
    Date time;
    User user;
    Interact interact;
    Long commentCount;

    @Data
    @AllArgsConstructor
    public static class Interact {
        Boolean like;
        Boolean collect;
    }

    @Data
    public static class User {
        Integer uid;
        String username;
        String avatar;
        String desc;
        boolean gender;
        String qq;
        String wx;
        String phone;
        String email;
    }
}