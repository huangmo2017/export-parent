package com.hdm.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdm.domain.cargo.Contract;
import com.hdm.domain.cargo.ContractExample;
import com.hdm.service.cargo.ContractService;
import com.hdm.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/cargo/contract")
public class ContractController extends BaseController {

    // 注入dubbo的服务对象
    // import com.alibaba.dubbo.config.annotation.Reference;
    @Reference
    private ContractService contractService;

    /**
     * 1. 分页查询
     *
     * @param pageNum  当前页，默认1
     * @param pageSize 页大小，默认5
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "5") int pageSize) {
        // 构造条件对象
        ContractExample example = new ContractExample();
        // 根据创建时间降序查询
        example.setOrderByClause("create_time desc");
        // 构造条件 - 企业id
        example.createCriteria().andCompanyIdEqualTo(getLoginCompanyId());

        // 分页查询
        PageInfo<Contract> pageInfo =
                contractService.findByPage(example, pageNum, pageSize);
        // 返回
        ModelAndView mv = new ModelAndView();
        mv.addObject("pageInfo", pageInfo);
        mv.setViewName("cargo/contract/contract-list");
        return mv;
    }

    /**
     * 2. 进入添加页面
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        return "cargo/contract/contract-add";
    }

    /**
     * 3. 添加或者修改
     */
    @RequestMapping("/edit")
    public String edit(Contract contract) {
        // 设置购销合同所属企业信息
        contract.setCompanyId(getLoginCompanyId());
        contract.setCompanyName(getLoginCompanyName());

        if (StringUtils.isEmpty(contract.getId())) {
            // 添加
            contractService.save(contract);
        } else {
            // 修改
            contractService.update(contract);
        }
        return "redirect:/cargo/contract/list.do";
    }

    /**
     * 4. 进入修改页面
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        Contract contract = contractService.findById(id);
        request.setAttribute("contract", contract);
        return "cargo/contract/contract-update";
    }

    /**
     * 5. 删除
     */
    @RequestMapping("/delete")
    public String delete(String id) {
        contractService.delete(id);
        return "redirect:/cargo/contract/list.do";
    }

    /**
     * 查看购销合同
     */
    @RequestMapping("/toView")
    public String toView(String id) {
        Contract contract = contractService.findById(id);
        request.setAttribute("contract", contract);
        return "cargo/contract/contract-view";
    }

    /**
     * 修改购销合同状态 - 提交 1
     */
    @RequestMapping("/submit")
    public String submit(String id) {
        Contract contract = new Contract();
        contract.setId(id);
        // 提交，设置状态为1，表示已上报
        contract.setState(1);
        // 修改
        contractService.update(contract);
        return "redirect:/cargo/contract/list.do";
    }

    /**
     * 修改购销合同状态 - 取消 0
     */
    @RequestMapping("/cancel")
    public String cancel(String id) {
        Contract contract = new Contract();
        contract.setId(id);
        // 提交，设置状态为1，表示已上报
        contract.setState(0);
        // 修改
        contractService.update(contract);
        return "redirect:/cargo/contract/list.do";
    }
}