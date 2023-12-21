package com.webnongsan.greenshop.api.admin;

import com.webnongsan.greenshop.model.dto.CategoryDTO;
import com.webnongsan.greenshop.model.dto.ProductDTO;
import com.webnongsan.greenshop.model.response.PageLayOut;
import com.webnongsan.greenshop.model.response.PaginateResponse;
import com.webnongsan.greenshop.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController(value = "Product")
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductAPI {
    private final ProductServiceImpl productService;

    @GetMapping("/products")
    public PageLayOut getProductOfPage(@RequestParam int currentPage, @RequestParam int limit,
                                        @RequestParam(value="key", required = false) String key){
        PageRequest pageRequest = PageRequest.of(currentPage-1,limit,
                Sort.by("id").ascending()
        );
        Page<ProductDTO> productDTOPage = productService.findAllProductOfPage(pageRequest);
        int totalPages = productDTOPage.getTotalPages();
        PaginateResponse paginateResponse = new PaginateResponse();
        List<ProductDTO> productDTOList = new ArrayList<>();
        if(key.isEmpty()){
            paginateResponse.setPage(currentPage);
            paginateResponse.setTotalPage(totalPages);
            productDTOList = productDTOPage.getContent();
        }else {
            Page<ProductDTO> productDTOS = productService.findProductOfName(key,pageRequest);
            int totalPage = productDTOS.getTotalPages();
            paginateResponse.setTotalPage(totalPage);
            paginateResponse.setPage(currentPage);
            productDTOList = productDTOS.getContent();
        }
        PageLayOut pageLayOut = new PageLayOut();
        String tbody = "";
        for (ProductDTO productDTO : productDTOList) {
            Locale localeVN = new Locale("vi", "VN");
            NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
            String price = currencyVN.format(productDTO.getPrice());
            tbody+="<tr>\n" +
                    "<!-- <td>"+productDTO.getId()+"</td> -->\n" +
                    "<td>\n" +
                    "<img src='/loadImage?imageName="+productDTO.getProductImage()+"' width='80px' alt='product'>\n" +
                    "</td>\n" +
                    "<td>"+productDTO.getProductName()+"</td>\n" +
                    "<td>"+productDTO.getCategory().getCategoryName()+"</td>\n" +
                    "<td>"+price+"</td>\n" +
                    "<td>"+productDTO.getDiscount()+"%</td>\n" +
                    "<td>"+productDTO.getQuantity()+"</td>\n" +
                    "<td>"+productDTO.getEnteredDate()+"</td>\n" +
                    "<td>"+productDTO.getDescription()+"</td>\n" +
                    "<td>\n" +
                    "<div class='form-button-action'>\n" +
                    "<a href='/admin/editProduct/"+productDTO.getId()+"' type='button' data-toggle='tooltip' title='' class='btn btn-link btn-primary btn-lg' data-original-title='Chỉnh sửa'>\n" +
                    "<i class='fa fa-edit'></i>\n" +
                    "</a>\n" +
                    "<button data-id='"+productDTO.getId()+"' data-name='"+productDTO.getProductName()+"' onclick='showConfigModalDialog(this.getAttribute(\"data-id\"), this.getAttribute(\"data-name\"))' type='button' data-toggle='tooltip' title='' class='btn btn-link btn-danger' data-original-title='Xóa'>\n" +
                    "<i class='fa fa-times'></i>\n" +
                    "</button>\n" +
                    "</div>\n" +
                    "</td>\n" +
                    "</tr>";
        }
        String pagination ="";
        for (int i = 1; i <= paginateResponse.getTotalPage(); i++) {
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

    @GetMapping(value = "/searchProducts")
    public PageLayOut getCategoryOfName(@RequestParam int limit , @RequestParam String key) {
        PageRequest pageRequest = PageRequest.of(1-1,limit,
                Sort.by("id").ascending()
        );
        Page<ProductDTO> productDTOPage = productService.findProductOfName(key,pageRequest);
        int totalPage = productDTOPage.getTotalPages();
        PaginateResponse paginateResponse = new PaginateResponse();
        paginateResponse.setTotalPage(totalPage);
        paginateResponse.setPage(1);
        List<ProductDTO> productDTOList = productDTOPage.getContent();
        PageLayOut pageLayOut = new PageLayOut();
        String tbody = "";
        for (ProductDTO productDTO : productDTOList) {
            Locale localeVN = new Locale("vi", "VN");
            NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
            String price = currencyVN.format(productDTO.getPrice());
            tbody+="<tr>\n" +
                    "<!-- <td>"+productDTO.getId()+"</td> -->\n" +
                    "<td>\n" +
                    "<img src='/loadImage?imageName="+productDTO.getProductImage()+"' width='80px' alt='product'>\n" +
                    "</td>\n" +
                    "<td>"+productDTO.getProductName()+"</td>\n" +
                    "<td>"+productDTO.getCategory().getCategoryName()+"</td>\n" +
                    "<td>"+price+"</td>\n" +
                    "<td>"+productDTO.getDiscount()+"%</td>\n" +
                    "<td>"+productDTO.getQuantity()+"</td>\n" +
                    "<td>"+productDTO.getEnteredDate()+"</td>\n" +
                    "<td>"+productDTO.getDescription()+"</td>\n" +
                    "<td>\n" +
                    "<div class='form-button-action'>\n" +
                    "<a href='/admin/editProduct/"+productDTO.getId()+"' type='button' data-toggle='tooltip' title='' class='btn btn-link btn-primary btn-lg' data-original-title='Chỉnh sửa'>\n" +
                    "<i class='fa fa-edit'></i>\n" +
                    "</a>\n" +
                    "<button data-id='"+productDTO.getId()+"' data-name='"+productDTO.getProductName()+"' onclick='showConfigModalDialog(this.getAttribute(\"data-id\"), this.getAttribute(\"data-name\"))' type='button' data-toggle='tooltip' title='' class='btn btn-link btn-danger' data-original-title='Xóa'>\n" +
                    "<i class='fa fa-times'></i>\n" +
                    "</button>\n" +
                    "</div>\n" +
                    "</td>\n" +
                    "</tr>";
        }
        String pagination ="";
        for (int i = 1; i <= paginateResponse.getTotalPage(); i++) {
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
