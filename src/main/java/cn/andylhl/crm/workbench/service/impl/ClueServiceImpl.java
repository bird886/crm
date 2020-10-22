package cn.andylhl.crm.workbench.service.impl;

import cn.andylhl.crm.exception.ClueExecption;
import cn.andylhl.crm.vo.PaginationVO;
import cn.andylhl.crm.workbench.dao.ActivityDao;
import cn.andylhl.crm.workbench.dao.ClueActivityRelationDao;
import cn.andylhl.crm.workbench.dao.ClueDao;
import cn.andylhl.crm.workbench.dao.ClueRemarkDao;
import cn.andylhl.crm.workbench.domain.Clue;
import cn.andylhl.crm.workbench.domain.ClueRemark;
import cn.andylhl.crm.workbench.service.ClueService;

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
    public void deleteByIds(String[] ids) {
        int remarkCount1 = clueRemarkDao.getClueRemarkSizeByIds(ids);
        int remarkCount2 = clueRemarkDao.deleteClueRemarkByIds(ids);
    }
}
