package cn.andylhl.crm.workbench.service.impl;

import cn.andylhl.crm.exception.ClueActivityRelationExecption;
import cn.andylhl.crm.exception.ClueExecption;
import cn.andylhl.crm.exception.ClueRemarkException;
import cn.andylhl.crm.utils.UUIDUtil;
import cn.andylhl.crm.vo.PaginationVO;
import cn.andylhl.crm.workbench.dao.ActivityDao;
import cn.andylhl.crm.workbench.dao.ClueActivityRelationDao;
import cn.andylhl.crm.workbench.dao.ClueDao;
import cn.andylhl.crm.workbench.dao.ClueRemarkDao;
import cn.andylhl.crm.workbench.domain.Activity;
import cn.andylhl.crm.workbench.domain.Clue;
import cn.andylhl.crm.workbench.domain.ClueActivityRelation;
import cn.andylhl.crm.workbench.domain.ClueRemark;
import cn.andylhl.crm.workbench.service.ClueService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * @Title: ClueServiceImpl
 * @Description: 线索业务实现类
 * @author: lhl
 * @date: 2020/10/22 14:53
 */
public class ClueServiceImpl implements ClueService {
    private ActivityDao activityDao;
    private ClueDao clueDao;
    private ClueRemarkDao clueRemarkDao;
    private ClueActivityRelationDao clueActivityRelationDao;

    public void setActivityDao(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    public void setClueDao(ClueDao clueDao) {
        this.clueDao = clueDao;
    }

    public void setClueRemarkDao(ClueRemarkDao clueRemarkDao) {
        this.clueRemarkDao = clueRemarkDao;
    }

    public void setClueActivityRelationDao(ClueActivityRelationDao clueActivityRelationDao) {
        this.clueActivityRelationDao = clueActivityRelationDao;
    }

    /**
     * 保存线索对象
     * @param clue
     */
    @Override
    public void save(Clue clue) throws ClueExecption {
        int count = 0;
        count = clueDao.save(clue);
        if (count != 1){
            throw new ClueExecption("保存线索对象异常");
        }
    }

    @Override
    public PaginationVO<Clue> pageList(Map<String, Object> conditionMap) {
        PaginationVO<Clue> paginationVO = new PaginationVO<>();
        int total = clueDao.getTotalByConditionMap(conditionMap);
        List<Clue> dataList = clueDao.getClueListByConditionMap(conditionMap);
        paginationVO.setTotal(total);
        paginationVO.setDataList(dataList);
        return paginationVO;
    }

    /**
     * 删除线索对象，及线索对象备注，及线索对象与市场活动之间关系
     * @param ids
     */
    @Override
    public void deleteByIds(String[] ids) throws ClueExecption {
        int remarkCount1 = clueRemarkDao.getClueRemarkSizeByIds(ids);
        int remarkCount2 = clueRemarkDao.deleteClueRemarkByIds(ids);
        if(remarkCount1 != remarkCount2){
            throw new ClueExecption("删除线索对象相关备注异常");
        }
        int carCount1 = clueActivityRelationDao.getCarSizeByIds(ids);
        int carCount2 = clueActivityRelationDao.deleteCarByyIds(ids);
        if (carCount1 != carCount2){
            throw new ClueExecption("删除线索相关市场活动异常");
        }
        int clueCount = clueDao.deleteByIds(ids);
        if (clueCount != ids.length){
            throw new ClueExecption("删除线索对象异常");
        }
    }

    /**
     * 根据id获取线索对象信息
     * @param id
     * @return
     */
    @Override
    public Clue getClueById(String id) {
        return clueDao.getClueById(id);
    }

    /**
     * 更新线索对象信息
     * @param clue
     */
    @Override
    public void update(Clue clue) throws ClueExecption {
        int count = clueDao.update(clue);
        if (count != 1){
            throw new ClueExecption("线索对象更新异常");
        }
    }

    /**
     * //根绝id获取线索对象信息(owner显示为真名)
     * @param id
     * @return
     */
    @Override
    public Clue getDetailById(String id) {
        return clueDao.getDetailById(id);
    }

    /**
     * 获取线索相关备注
     * @param id
     * @return
     */
    @Override
    public List<ClueRemark> getRemarkListById(String id) {
        return clueRemarkDao.getRemarkListById(id);
    }

    /**
     * 根据id删除执行备注
     * @param id
     */
    @Override
    public void deleteRemarkById(String id) throws ClueRemarkException {
        int count = clueRemarkDao.deleteRemarkById(id);
        if (count != 1){
            throw new ClueRemarkException("删除线索备注异常");
        }
    }

    /**
     * 保存线索备注
     * @param clueRemark
     */
    @Override
    public void saveRemark(ClueRemark clueRemark) throws ClueRemarkException {
        int count = clueRemarkDao.saveRemark(clueRemark);
        if (count != 1){
            throw new ClueRemarkException("线索备注保存异常");
        }
    }

    /**
     * 更新备注
     * @param clueRemark
     */
    @Override
    public void updateRemark(ClueRemark clueRemark) throws ClueRemarkException {
        int count = clueRemarkDao.updateRemark(clueRemark);
        if (count != 1){
            throw new ClueRemarkException("更新备注异常");
        }
    }

    /**
     * 根据线索id，查询所关联的市场活动
     * @param clueId
     * @return
     */
    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        return activityDao.getActivityListByClueId(clueId);
    }

    /**
     * 解除关联
     * @param id
     */
    @Override
    public void deleteCarById(String id) throws ClueActivityRelationExecption {
        int count = clueActivityRelationDao.deleteCarById(id);
        if(count != 1){
            throw new ClueActivityRelationExecption("解除关联异常");
        }
    }

    /**
     * 获取该线索未关联的市场活动(带参数模糊查询)
     * @param paraMap
     * @return
     */
    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> paraMap) {
        return activityDao.getActivityListByNameAndNotByClueId(paraMap);
    }

    /**
     * 关联市场活动
     * @param clueId
     * @param aids
     */
    @Override
    public void saveCar(String clueId, String[] aids) throws ClueActivityRelationExecption {
        //遍历aids执行关联
        int count = 0;
        for (String aid : aids){
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(clueId);
            car.setActivityId(aid);
            count += clueActivityRelationDao.saveCar(car);
        }
        if (count != aids.length){
            throw new ClueActivityRelationExecption("关联市场活动异常");
        }
    }
}
