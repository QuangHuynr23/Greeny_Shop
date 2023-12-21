package com.webnongsan.greenshop.api.admin;

import com.webnongsan.greenshop.model.dto.CategoryDTO;
import com.webnongsan.greenshop.model.response.PageLayOut;
import com.webnongsan.greenshop.model.response.PaginateResponse;
import com.webnongsan.greenshop.service.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController(value = "Category")
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryAPI {
    private final CategoryServiceImpl categoryService;

    @GetMapping("/categories")
    public PageLayOut getCategoryOfPage(@RequestParam int currentPage, @RequestParam int limit,
                                        @RequestParam(value="key", required = false) String key){
        PageRequest pageRequest = PageRequest.of(currentPage-1,limit,
                Sort.by("id").ascending()
        );
        Page<CategoryDTO> categoryDTOPage = categoryService.findAllCategoryOfPage(pageRequest);
        int totalPages = categoryDTOPage.getTotalPages();
        PaginateResponse paginateResponse = new PaginateResponse();
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        if(key.isEmpty()){
            paginateResponse.setPage(currentPage);
            paginateResponse.setTotalPage(totalPages);
            categoryDTOList = categoryDTOPage.getContent();
        }else {
            Page<CategoryDTO> categoryDTOS = categoryService.findCategoryOfPageByName(key,pageRequest);
            int totalPage = categoryDTOS.getTotalPages();
            paginateResponse.setTotalPage(totalPage);
            paginateResponse.setPage(currentPage);
            categoryDTOList = categoryDTOS.getContent();
        }
        PageLayOut pageLayOut = new PageLayOut();
        String tbody = "";
        for(CategoryDTO categoryDTO : categoryDTOList){
            tbody+="<tr>\n" +
                    "<td>"+categoryDTO.getId()+"</td>\n" +
                    "<td>"+categoryDTO.getCategoryName()+"</td>\r\n" +
                    "<td>"+categoryDTO.getStatus()+"</td>\r\n" +
                    "<td>\n" +
                    "<div class='form-button-action'>\n" +
                    "<a href='/admin/editCategory/"+categoryDTO.getId()+"' type='button' data-toggle='tooltip' title='' class='btn btn-link btn-primary btn-lg' data-original-title='Chỉnh sửa'>\n" +
                    "<i class='fa fa-edit'></i>\n" +
                    "</a>\n" +
                    "<button data-id='"+categoryDTO.getId()+"' data-name='"+categoryDTO.getCategoryName()+"' onclick='showConfigModalDialog(this.getAttribute(\"data-id\"), this.getAttribute(\"data-name\"))' type='button' data-toggle='tooltip' title='' class='btn btn-link btn-danger' data-original-title='Xóa'>\n" +
                    "<i class='fa fa-times'></i>\n" +
                    "</button>\n" +
                    "</div>\n" +
                    "</td>\n" +
                    "</tr>";
        }
        String pagination = "";
        for (int i = 1; i <=paginateResponse.getTotalPage(); i++) {
            if (i==currentPage) {
                pagination +="<li class='paginate_button page-item active'>\n" +
                        "	     <a aria-controls='add-row' data-dt-idx='"+i+"' tabindex='0' class='page-link'>"+i+"</a>\n" +
                        "	  </li>";
            }else {
                pagination +="<li class='paginate_button page-item'>\n" +
                        "	     <a aria-controls='add-row' data-dt-idx='"+i+"' tabindex='0' class='page-link'>"+i+"</a>\n" +
                        "	  </li>";
            }
        }
        pageLayOut.setBody(tbody);
        pageLayOut.setPagination(pagination);
        return pageLayOut;
    }

    @GetMapping(value = "/searchCategories")
    public PageLayOut getCategoryOfName(@RequestParam int limit , @RequestParam String key) {
        PageRequest pageRequest = PageRequest.of(1-1,limit,
                Sort.by("id").ascending()
        );
        Page<CategoryDTO> categoryDTOPage = categoryService.findCategoryOfPageByName(key,pageRequest);
        int totalPage = categoryDTOPage.getTotalPages();
        PaginateResponse paginateResponse = new PaginateResponse();
        paginateResponse.setTotalPage(totalPage);
        paginateResponse.setPage(1);
        List<CategoryDTO> categoryDTOS = categoryDTOPage.getContent();
        PageLayOut pageLayOut = new PageLayOut();
        String tbody = "";
        for(CategoryDTO categoryDTO : categoryDTOS){
            tbody+="<tr>\n" +
                    "<td>"+categoryDTO.getId()+"</td>\n" +
                    "<td>"+categoryDTO.getCategoryName()+"</td>\r\n" +
                    "<td>"+categoryDTO.getStatus()+"</td>\r\n" +
                    "<td>\n" +
                    "<div class='form-button-action'>\n" +
                    "<a href='/admin/editCategory/"+categoryDTO.getId()+"' type='button' data-toggle='tooltip' title='' class='btn btn-link btn-primary btn-lg' data-original-title='Chỉnh sửa'>\n" +
                    "<i class='fa fa-edit'></i>\n" +
                    "</a>\n" +
                    "<button data-id='"+categoryDTO.getId()+"' data-name='"+categoryDTO.getCategoryName()+"' onclick='showConfigModalDialog(this.getAttribute(\"data-id\"), this.getAttribute(\"data-name\"))' type='button' data-toggle='tooltip' title='' class='btn btn-link btn-danger' data-original-title='Xóa'>\n" +
                    "<i class='fa fa-times'></i>\n" +
                    "</button>\n" +
                    "</div>\n" +
                    "</td>\n" +
                    "</tr>";
        }
        String pagination = "";
        for (int i = 1; i <=paginateResponse.getTotalPage(); i++) {
            if (i==1) {
                pagination +="<li class='paginate_button page-item active'>\n" +
                        "	     <a aria-controls='add-row' data-dt-idx='"+i+"' tabindex='0' class='page-link'>"+i+"</a>\n" +
                        "	  </li>";
            }else {
                pagination +="<li class='paginate_button page-item'>\n" +
                        "	     <a aria-controls='add-row' data-dt-idx='"+i+"' tabindex='0' class='page-link'>"+i+"</a>\n" +
                        "	  </li>";
            }
        }
        pageLayOut.setBody(tbody);
        pageLayOut.setPagination(pagination);
        return pageLayOut;
    }
}
