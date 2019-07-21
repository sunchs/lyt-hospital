package com.sunchs.lyt.db.business.service.impl;

import com.sunchs.lyt.db.business.entity.Item;
import com.sunchs.lyt.db.business.mapper.ItemMapper;
import com.sunchs.lyt.db.business.service.IItemService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author king
 * @since 2019-07-21
 */
@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements IItemService {

}
