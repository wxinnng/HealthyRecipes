package com.healthyrecipes.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.healthyrecipes.common.constant.MessageConstant;
import com.healthyrecipes.common.utils.RedisUtil;
import com.healthyrecipes.exception.BusinessException;
import com.healthyrecipes.mapper.GroupMapper;
import com.healthyrecipes.pojo.entity.Group;
import com.healthyrecipes.pojo.entity.Member;
import com.healthyrecipes.pojo.query.GroupQuery;
import com.healthyrecipes.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * @Author:86198
 * @DATE:2024/3/18 18:09
 * @DESCRIPTION: GroupService 的具体实现
 * @VERSION:1.0
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupMapper groupMapper;

    @Resource
    private RedisUtil redisUtil;

    @Transactional
    @Override
    public void createGroup(Group group) {

        /*1.查看用户是否符合创建群聊的条件*/
        GroupQuery groupQuery = new GroupQuery();
        groupQuery.setOwnerId(groupQuery.getOwnerId());
        List<Group> groupList = groupMapper.getGroupList(groupQuery);

        if(groupList != null && groupList.size() > 0)
            throw new BusinessException("您已经创建过组队了！");

        /*2.生成一个不重复的随机7位邀请码*/
        group.setCodeInfo(getARandomCode(MessageConstant.INVITATION_CODE_LEN));

        /*3.封装其他信息*/
        group.setCurNum(1); //当前人数
        group.setCreateTime(LocalDateTime.now());  //创建时间

        /*4.将信息放到数据库中*/
        groupMapper.createAGroup(group);

        /*5.member 数据添加*/
        groupMapper.insertAMember(group.getOwnerId(),group.getId(),1);

        /*6.添加redis中的数据*/
        redisUtil.setbitmap(MessageConstant.REDIS_CLOCK_IN_KEY + group.getId() +":"+ group.getOwnerId(),0,true);
    }

    @Override
    public Page<Group> getList(GroupQuery query) {
        if( query.getPageNum() != null && query.getPageSize() != null)
            PageHelper.startPage(query.getPageNum(), query.getPageSize());
        return groupMapper.getGroupList(query);
    }

    @Transactional
    @Override
    public void delete(Integer userid, Integer groupId) {
        /*1.判断是不是群主*/
        Member member = new Member();
        member.setGroupId(groupId);
        member.setUserId(userid);
        List<Member> memberList = groupMapper.getMemberList(member);

        if(memberList == null || memberList.size() == 0)
            throw new BusinessException("用户不在该群中！");

        Member targetMember = memberList.get(0);
        if (targetMember.getIsOwner() == 0){
            //不是群主
            /*2. 清理Redis中的内容*/
            redisUtil.del(MessageConstant.REDIS_CLOCK_IN_KEY +groupId +":"+ userid);
            /*3. 清理MySQL中的内容*/
            groupMapper.deleteMember(member);
            /*4. 更新Group信息*/
            groupMapper.subCurNum(groupId);
            /*5. 更新Member中的信息*/
            groupMapper.deleteAMember(groupId,userid);

        }else{
            //群主
            /*2. 清理redis中的内容*/
            List<Integer> ids = groupMapper.getMemberIds(groupId);
            for(Integer id:ids){
                redisUtil.del(MessageConstant.REDIS_CLOCK_IN_KEY + groupId + ":" + id);
            }

            /*3.清理MySql中的内容*/
            member.setUserId(null);
            groupMapper.deleteMember(member);
            /*4. 删除group中的信息*/
            groupMapper.deleteAGroup(groupId);
            /*5.删除groupId下所有的member*/
            groupMapper.deleteAllMemberByGroupId(groupId);
        }
    }



    @Transactional
    @Override
    public void joinInGroup(Integer userid, String code) {

        GroupQuery query = new GroupQuery();
        query.setCodeInfo(code);
        Page<Group> list = groupMapper.getGroupList(query);

        /*1.判断group是否存在*/
        if(list == null || list.size() == 0)
            throw new BusinessException("不存在该小组！");

        Group targetGroup = list.get(0);   //目标小组

        /*2. 判断是否可以进入队伍*/
        if(Objects.equals(targetGroup.getCurNum(), list.get(0).getGroupSize()))
            throw new BusinessException("该小组人数已满！");

        /*4.判断用户是否已经在该组中*/
        Member memberQuery = new Member();
        memberQuery.setGroupId(targetGroup.getId());
        memberQuery.setUserId(userid);
        List<Member> memberList = groupMapper.getMemberList(memberQuery);
        if(memberList != null && memberList.size() > 0)
            throw new BusinessException("您已经在本组中了~");

        /*3. 可以进入小组,增加curNum*/
        query.setCurNum(targetGroup.getCurNum() + 1);
        groupMapper.updateGroup(query);

        /*4. 插入Member信息*/
        groupMapper.insertAMember(userid,targetGroup.getId(),0);

        /*5.Redis中添加对应信息*/
        redisUtil.setbitmap(MessageConstant.REDIS_CLOCK_IN_KEY + targetGroup.getId()+ ":" + userid,0,true);

    }

    /**
     * @description: 随机获得7位邀请码，不和数据库中的重复
     * @param: []
     * @return: java.lang.String
     */
    private String getARandomCode(int len){

        String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1230948576";
        int strLen = str.length();
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        GroupQuery groupQuery = new GroupQuery();

        do{
            builder.setLength(0); //清除

            for(int i = 0 ;i < len; i++){
                int number = random.nextInt(strLen);
                builder.append(str.charAt(number));
            }
            groupQuery.setCodeInfo(builder.toString());
        }while(groupMapper.getGroupList(groupQuery).size() > 0);         //查询数据库中是否重复
        return builder.toString();
    }
}
