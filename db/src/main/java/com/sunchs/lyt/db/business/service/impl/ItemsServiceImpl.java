package com.sunchs.lyt.db.business.service.impl;

import com.sunchs.lyt.db.business.entity.Items;
import com.sunchs.lyt.db.business.mapper.ItemsMapper;
import com.sunchs.lyt.db.business.service.IItemsService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author king
 * @since 2019-05-29
 */
@Service
public class ItemsServiceImpl extends ServiceImpl<ItemsMapper, Items> implements IItemsService {

}
