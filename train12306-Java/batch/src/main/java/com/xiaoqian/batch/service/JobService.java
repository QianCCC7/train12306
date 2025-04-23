package com.xiaoqian.batch.service;

import com.xiaoqian.batch.domain.dto.JobDTO;
import com.xiaoqian.batch.domain.vo.JobVo;
import com.xiaoqian.common.domain.ResponseResult;

import java.util.List;

public interface JobService {
    ResponseResult<Void> add(JobDTO jobDTO);

    ResponseResult<Void> pause(JobDTO jobDTO);

    ResponseResult<Void> resume(JobDTO jobDTO);

    ResponseResult<Void> reschedule(JobDTO jobDTO);

    ResponseResult<Void> delete(JobDTO jobDTO);

    ResponseResult<List<JobVo>> query();

    ResponseResult<Void> run(JobDTO jobDTO);
}
