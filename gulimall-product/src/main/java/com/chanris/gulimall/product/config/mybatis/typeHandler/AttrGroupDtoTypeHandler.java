package com.chanris.gulimall.product.config.mybatis.typeHandler;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.chanris.gulimall.product.dto.AttrDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * @author chenyue7@foxmail.com
 * @date 8/2/2024
 * @description
 */
@Slf4j
public class AttrGroupDtoTypeHandler extends BaseTypeHandler<List<AttrDTO>> {

    /**
     * 设置PreparedStatement 中指定位置的参数值。它将Java对象(parameter)转换为适合于指定JDBC类型(jdbcType)的数据库参数。
     *
     * @param ps
     * @param i
     * @param parameter
     * @param jdbcType
     * @throws SQLException
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<AttrDTO> parameter, JdbcType jdbcType) throws SQLException {
        log.info("SQL参数：{}",  Arrays.toString(parameter.toArray()));
    }

    /**
     * 从ResultSet中获得指定列的值，并将其转换为Java对象。
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    @Override
    public List<AttrDTO> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        log.info("getNullableResult 参数：columnName: {}", columnName);
        String res = rs.getString(columnName);
        if(null != res) {
            List<AttrDTO> attrDTOS = JSONArray.parseArray(res, AttrDTO.class);
            return attrDTOS;
        }
        return null;
    }

    /**
     * 和上一个方法类似，但通过列的索引来获取值。
     * @param rs
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public List<AttrDTO> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        log.info("getNullableResult 参数：columnIndex: {}", columnIndex);

        return null;
    }

    /**
     * 用于从CallableStatement 中获取指定位置的值， 并将其转换为Java对象。
     * @param cs
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public List<AttrDTO> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return null;
    }
}
