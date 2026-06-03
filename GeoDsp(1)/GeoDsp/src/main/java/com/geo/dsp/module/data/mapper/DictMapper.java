package com.geo.dsp.module.data.mapper;

import com.geo.dsp.module.data.entity.Dict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DictMapper {

    // 新增（类型/数据 都用这个）
    int insert(Dict dict);

    // 修改（类型/数据 通用)
    int updateByUuid(Dict dict);

    // 查询所有字典类型（dict_name 不为空）
    List<Dict> listDictType();

    // 根据字典编码查询字典数据
    List<Dict> listDictDataByCode(@Param("dictCode") String dictCode);

    // 逻辑删除
    int deleteByUuid(@Param("uuid") String uuid);
}