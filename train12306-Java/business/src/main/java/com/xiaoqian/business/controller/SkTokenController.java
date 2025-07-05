package com.xiaoqian.business.controller;


import com.xiaoqian.business.domain.dto.SkTokenDTO;
import com.xiaoqian.business.domain.query.SkTokenQueryDTO;
import com.xiaoqian.business.domain.vo.SkTokenVo;
import com.xiaoqian.business.service.ISkTokenService;
import com.xiaoqian.common.domain.ResponseResult;
import com.xiaoqian.common.query.PageVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 秒杀令牌 前端控制器
 * </p>
 *
 * @author xiaoqian
 * @since 2025-06-02
 */
@RestController
@RequestMapping("/admin/sk-token")
@RequiredArgsConstructor
public class SkTokenController {
    private final ISkTokenService skTokenService;

    @PostMapping("/saveSkToken")
    public ResponseResult<Void> saveSkToken(@Valid @RequestBody SkTokenDTO skTokenDTO) {
        return skTokenService.saveSkToken(skTokenDTO);
    }

    @GetMapping("/listSkTokenPage")
    public ResponseResult<PageVo<SkTokenVo>> listSkTokenPage(SkTokenQueryDTO queryDTO) {
        return skTokenService.listSkTokenPage(queryDTO);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseResult<Void> deleteById(@PathVariable("id") Long id) {
        return skTokenService.deleteById(id);
    }
}
