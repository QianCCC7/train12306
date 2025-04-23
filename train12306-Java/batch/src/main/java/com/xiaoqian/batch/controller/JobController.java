package com.xiaoqian.batch.controller;

import com.xiaoqian.batch.domain.dto.JobDTO;
import com.xiaoqian.batch.domain.vo.JobVo;
import com.xiaoqian.batch.service.JobService;
import com.xiaoqian.common.domain.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/job")
public class JobController {
    private final JobService jobService;

    @PostMapping("/add")
    public ResponseResult<Void> add(@RequestBody JobDTO jobDTO) {
        return jobService.add(jobDTO);
    }

    @PostMapping("/pause")
    public ResponseResult<Void> pause(@RequestBody JobDTO jobDTO) {
        return jobService.pause(jobDTO);
    }

    // 重启任务
    @PostMapping("/resume")
    public ResponseResult<Void> resume(@RequestBody JobDTO jobDTO) {
        return jobService.resume(jobDTO);
    }

    // 更新任务
    @PostMapping("/reschedule")
    public ResponseResult<Void> reschedule(@RequestBody JobDTO jobDTO) {
        return jobService.reschedule(jobDTO);
    }

    @PostMapping("/delete")
    public ResponseResult<Void> delete(@RequestBody JobDTO jobDTO) {
        return jobService.delete(jobDTO);
    }

    @GetMapping("/query")
    public ResponseResult<List<JobVo>> query() {
        return jobService.query();
    }
    @PostMapping("/run")
    public ResponseResult<Void> run(@RequestBody JobDTO jobDTO) {
        return jobService.run(jobDTO);
    }

}
