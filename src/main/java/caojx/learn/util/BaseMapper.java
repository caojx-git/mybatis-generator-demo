package caojx.learn.util;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 通用Mapper，注意，该接口不能被Mybatis扫描到，否则会出错
 *
 * @author halley.qiu
 * @version $Id: VoPlugin.java,v 1.0 2019-04-17 18:41 halley.qiu
 * @date 2019/04/17 18:41
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
    //TODO
    //FIXME
}
