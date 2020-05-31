package com.hdm.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdm.domain.cargo.ContractProduct;
import com.hdm.domain.cargo.ContractProductExample;
import com.hdm.domain.cargo.Factory;
import com.hdm.domain.cargo.FactoryExample;
import com.hdm.service.cargo.ContractProductService;
import com.hdm.service.cargo.FactoryService;
import com.hdm.web.controller.BaseController;
import com.hdm.web.utils.FileUploadUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController {

    @Reference
    private ContractProductService contractProductService;
    @Reference
    private FactoryService factoryService;

    /**
     * 货物列表显示
     * 分析：
     * 1. 查询生产厂家
     * 2. 根据购销合同id，查询该购销合同下所有货物
     * 3. 存储购销合同id
     * 4. 返回
     */
    @RequestMapping("/list")
    public String list(
            String contractId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "5") int pageSize) {
        //1. 查询生产厂家
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria factoryExampleCriteria = factoryExample.createCriteria();
        factoryExampleCriteria.andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList", factoryList);

        //2. 根据购销合同id，查询该购销合同下所有货物
        ContractProductExample cpExample = new ContractProductExample();
        ContractProductExample.Criteria cpExampleCriteria = cpExample.createCriteria();
        cpExampleCriteria.andContractIdEqualTo(contractId);
        PageInfo<ContractProduct> pageInfo =
                contractProductService.findByPage(cpExample, pageNum, pageSize);
        request.setAttribute("pageInfo", pageInfo);

        //3.  存储购销合同id
        request.setAttribute("contractId", contractId);
        //4.  返回        
        return "cargo/product/product-list";
    }

    /**
     * 2. 添加或者修改
     */
    @Autowired
    private FileUploadUtil fileUploadUtil;

    @RequestMapping("/edit")
    public String edit(ContractProduct contractProduct, MultipartFile productPhoto) throws Exception {
        // 设置购销合同货物所属企业信息
        contractProduct.setCompanyId(getLoginCompanyId());
        contractProduct.setCompanyName(getLoginCompanyName());

        if (StringUtils.isEmpty(contractProduct.getId())) {
            // 上传图片到七牛云
            if (productPhoto != null) {
                String img = "http://" + fileUploadUtil.upload(productPhoto);
                contractProduct.setProductImage(img);
            }
            // 添加
            contractProductService.save(contractProduct);
        } else {
            // 修改
            contractProductService.update(contractProduct);
        }
        // 进入货物列表：传入购销合同id
        return "redirect:/cargo/contractProduct/list.do?contractId=" + contractProduct.getContractId();
    }

    /**
     * 进入到修改界面
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //货物信息
        ContractProduct contractProduct = contractProductService.findById(id);
        request.setAttribute("contractProduct", contractProduct);

        //查询所有生产厂家
        FactoryExample example2 = new FactoryExample();
        FactoryExample.Criteria criteria2 = example2.createCriteria();
        criteria2.andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(example2);
        request.setAttribute("factoryList", factoryList);
        return "cargo/product/product-update";
    }

    /**
     * 删除购销合同
     */
    @RequestMapping("/delete")
    public String delete(String id, String contractId) {
        contractProductService.delete(id);
        return "redirect:/cargo/contractProduct/list.do?contractId=" + contractId;
    }

    /**
     * 文件上传
     * MultipartFile ： springmvc提供的文件对象
     */
    @RequestMapping("/import")
    public String importExcel(MultipartFile file, String contractId) throws Exception {
        //1.根据上传的excel文件创建工作簿
        Workbook wb = new XSSFWorkbook(file.getInputStream());
        //2.获取第一个sheet
        Sheet sheet = wb.getSheetAt(0);
        //3.循环获取每一个行对象

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
            Row row = sheet.getRow(i);
            //4.循环获取每一个单元格
            Object objs[] = new Object[10];
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                //5.获取单元格中的每一个数据
                if (cell != null) {
                    objs[j] = getCellValue(cell);
                }
            }
            //6.构造货物对象
            ContractProduct cp = new ContractProduct(objs, getLoginCompanyId(), getLoginCompanyName());
            //设置购销合同id
            cp.setContractId(contractId);

            // 调用service保存货物
            contractProductService.save(cp);
        }

        return "redirect:/cargo/contractProduct/list.do?contractId=" + contractId;
    }

    public Object getCellValue(Cell cell) {
        /**
         * 获取单元格的类型
         */
        CellType type = cell.getCellType();

        Object result = null;

        switch (type) {
            case STRING: {
                result = cell.getStringCellValue();//获取string类型数据
                break;
            }
            case NUMERIC: {
                /**
                 * 判断
                 */
                if (DateUtil.isCellDateFormatted(cell)) {  //日期格式
                    result = cell.getDateCellValue();
                } else {
                    //double类型
                    result = cell.getNumericCellValue(); //数字类型
                }
                break;
            }
            case BOOLEAN: {
                result = cell.getBooleanCellValue();//获取boolean类型数据
                break;
            }
            default: {
                break;
            }
        }

        return result;
    }
}