package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.Interact;
import com.example.entity.vo.request.AddCommentVO;
import com.example.entity.vo.request.TopicCreateVO;
import com.example.entity.vo.request.TopicUpdateVO;
import com.example.entity.vo.response.*;
import com.example.service.TopicService;
import com.example.utils.Const;
import com.example.utils.ControllerUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 与掘金主题的接口
 */
@Validated
@RestController
@RequestMapping("/api/forum")
public class ForumController {


    @Resource
    TopicService topicService;

    @Resource
    ControllerUtils utils;


    /**
     * 获取帖子类型介绍
     *
     * @return 类型实体类
     */
    @GetMapping("/types")
    public RestBean<List<TopicTypeVO>> listTypes() {
        return RestBean.success(topicService
                .listType()
                .stream()
                .map(type -> type.asViewObject(TopicTypeVO.class))
                .toList());
    }

    /**
     * 发布新的帖子
     *
     * @param vo 帖子实体类
     * @param id 用户id
     * @return 是否成功
     */
    @PostMapping("/create-topic")
    public RestBean<Void> createTopic(@Valid @RequestBody TopicCreateVO vo,
                                      @RequestAttribute(Const.ATTR_USER_ID) int id) {
        return utils.messageHandle(() -> topicService.createTopic(id, vo));
    }

    /**
     * 帖子展示
     *
     * @param page 页码
     * @param type 帖子类型
     * @return 帖子实体类
     */
    @GetMapping("/list-topic")
    public RestBean<List<TopicPreviewVO>> listTopic(@RequestAttribute(Const.ATTR_USER_ID) int id,
                                                    @RequestParam @Min(0) int page,
                                                    @RequestParam @Min(0) int type) {
        return RestBean.success(topicService.listTopicByPage(id, page + 1, type));
    }

    /**
     * 获取置顶帖子列表
     *
     * @return 帖子实体类
     */
    @GetMapping("/top-topic")
    public RestBean<List<TopicTopVO>> topTopic() {
        return RestBean.success(topicService.topTopics());
    }

    /**
     * 帖子详细内容
     *
     * @param tid 帖子id
     * @return 帖子实体类
     */
    @GetMapping("/topic")
    public RestBean<TopicDetailVO> topic(@RequestParam @Min(0) int tid,
                                         @RequestAttribute(Const.ATTR_USER_ID) int id) {
        return RestBean.success(topicService.getTopic(tid, id));
    }

    /**
     * 用户对帖子交互(点赞/收藏)
     *
     * @param tid   帖子ID
     * @param type  交互类型
     * @param state 交互状态
     * @param id    用户id
     * @return 帖子详细实体类
     */
    @GetMapping("/interact")
    public RestBean<TopicDetailVO> topic(@RequestParam @Min(0) int tid,
                                         @RequestParam @Pattern(regexp = "(like|collect)") String type,
                                         @RequestParam boolean state,
                                         @RequestAttribute(Const.ATTR_USER_ID) int id) {
        topicService.interact(new Interact(tid, id, new Date(), type), state);
        return RestBean.success();
    }


    /**
     * 获取用户收藏的全部帖子
     *
     * @param id 用户ID
     * @return 帖子列表
     */
    @GetMapping("/collects")
    public RestBean<List<TopicPreviewVO>> collects(@RequestAttribute(Const.ATTR_USER_ID) int id) {
        return RestBean.success(topicService.listCollectTopic(id));
    }

    /**
     * 更新帖子
     *
     * @param vo 更新帖子实体类
     * @param id 用户id
     * @return 是否成功
     */
    @PostMapping("/update-topic")
    public RestBean<Void> updateTopic(@Valid @RequestBody TopicUpdateVO vo,
                                      @RequestAttribute(Const.ATTR_USER_ID) int id) {
        topicService.updateTopic(id, vo);
        return RestBean.success();
    }

    /**
     * 添加评论
     * @param vo 添加评论实体类
     * @param id 用户id
     * @return 是否成功
     */
    @PostMapping("/add-comment")
    public RestBean<String> addComment(@Valid @RequestBody AddCommentVO vo,
                                       @RequestAttribute(Const.ATTR_USER_ID) int id) {
        return utils.messageHandle(() -> topicService.createComment(vo, id));
    }

    /**
     * 获取对应帖子的评论
     * @param tid 帖子ID
     * @param page 分页
     * @return 评论集合
     */
    @GetMapping("/comments")
    public RestBean<List<CommentVO>> comments(@RequestParam @Min(0) int tid,
                                              @RequestParam @Min(0) int page) {
        return RestBean.success(topicService.comments(tid, page + 1));
    }

    /**
     * 删除对应评论
     * @param id  评论id
     * @param uid 用户id
     * @return 是否成功
     */
    @DeleteMapping("/delete-comment")
    public RestBean<Void> deleteComment(@RequestParam @Min(0) int id,
                                        @RequestAttribute(Const.ATTR_USER_ID) int uid) {
        topicService.deleteComment(id, uid);
        return RestBean.success();
    }

}
