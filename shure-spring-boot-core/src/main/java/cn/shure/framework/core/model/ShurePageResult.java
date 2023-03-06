package cn.shure.framework.core.model;

import lombok.Data;

import java.util.List;

/**
 * @Description ShurePageResult
 * @Author 下课菌
 * @Date 2023/3/5
 */
@Data
public final class ShurePageResult<T> {

    /**
     * 当前页
     */
    private long current;

    /**
     * 每页数量
     */
    private long size;

    /**
     * 总页数
     */
    private long total;

    /**
     * 是否有前一页
     */
    private boolean hasPreviousPage;

    /**
     * 是否有下一页
     */
    private boolean hasNextPage;

    /**
     * 分页结果
     */
    private List<T> records;

}
