package com.webnongsan.greenshop.api.admin;

import com.webnongsan.greenshop.Enum.StatisticalType;
import com.webnongsan.greenshop.model.response.PageLayOut;
import com.webnongsan.greenshop.model.response.PaginateResponse;
import com.webnongsan.greenshop.model.response.StatisticalOrderDetail;
import com.webnongsan.greenshop.service.OrderDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController(value ="StatisticalOfProduct")
@RequestMapping("/api")
@RequiredArgsConstructor
public class StatisticalOfProductAPI {
    private final OrderDetailServiceImpl orderDetailService;

    @GetMapping(value ="/reportProduct")
    public PageLayOut getReportOfProduct (@RequestParam int currentPage, @RequestParam int limit, @RequestParam(value="key", required = false) String key) {
        PageRequest pageRequest = PageRequest.of(currentPage-1,limit,
                Sort.by("id").ascending()
        );
        Page<Object[]> productDTOPage = orderDetailService.statisticalWhereProducts(pageRequest);
        int totalPages = productDTOPage.getTotalPages();
        PaginateResponse paginateResponse = new PaginateResponse();
        List<StatisticalOrderDetail> statisticalOrderDetails = new ArrayList<>();
        if(key.isEmpty()){
            paginateResponse.setPage(currentPage);
            paginateResponse.setTotalPage(totalPages);
            statisticalOrderDetails = orderDetailService.mapToStatisticalOrderDetailList(productDTOPage, StatisticalType.Other.getValue());
        }else {
            Page<Object[]> productDTOS = orderDetailService.statisticalProductOfKey(key,pageRequest);
            int totalPage = productDTOS.getTotalPages();
            paginateResponse.setTotalPage(totalPage);
            paginateResponse.setPage(currentPage);
            statisticalOrderDetails = orderDetailService.mapToStatisticalOrderDetailList(productDTOS,StatisticalType.Other.getValue());
        }
        PageLayOut pageLayOut = new PageLayOut();
        String tbody = "";
        for (StatisticalOrderDetail detailOfProduct : statisticalOrderDetails) {
            Locale localeVN = new Locale("vi", "VN");
            NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
            String sumPrice = currencyVN.format(detailOfProduct.getSumPrice());
            String averagePrice = currencyVN.format(detailOfProduct.getAveragePrice());
            String minPrice = currencyVN.format(detailOfProduct.getMinimumPrice());
            String maxPrice = currencyVN.format(detailOfProduct.getMaximumPrice());
            tbody +="<tr>\n" +
                    "<td>"+detailOfProduct.getName()+"</td>\n" +
                    "<td>"+detailOfProduct.getQuantity()+"</td>\n" +
                    "<td>"+sumPrice+"</td>\n" +
                    "<td>"+averagePrice+"</td>\n" +
                    "<td>"+minPrice+"</td>\n" +
                    "<td>"+maxPrice+"</td>\n" +
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

    @GetMapping(value = "/searchReportProduct")
    public PageLayOut getCategoryOfName(@RequestParam int limit , @RequestParam String key) {
        PageRequest pageRequest = PageRequest.of(1 - 1, limit,
                Sort.by("id").ascending()
        );
        Page<Object[]> productDTOS = orderDetailService.statisticalProductOfKey(key,pageRequest);
        int totalPage = productDTOS.getTotalPages();
        PaginateResponse paginateResponse = new PaginateResponse();
        paginateResponse.setTotalPage(totalPage);
        paginateResponse.setPage(1);
        List<StatisticalOrderDetail> statisticalOrderDetails = orderDetailService.mapToStatisticalOrderDetailList(productDTOS,StatisticalType.Other.getValue());
        PageLayOut pageLayOut = new PageLayOut();
        String tbody = "";
        for (StatisticalOrderDetail detailOfProduct : statisticalOrderDetails) {
            int i = 1;
            Locale localeVN = new Locale("vi", "VN");
            NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
            String sumPrice = currencyVN.format(detailOfProduct.getSumPrice());
            String averagePrice = currencyVN.format(detailOfProduct.getAveragePrice());
            String minPrice = currencyVN.format(detailOfProduct.getMinimumPrice());
            String maxPrice = currencyVN.format(detailOfProduct.getMaximumPrice());
            tbody +="<tr>\n" +
                    "<td>"+detailOfProduct.getName()+"</td>\n" +
                    "<td>"+detailOfProduct.getQuantity()+"</td>\n" +
                    "<td>"+sumPrice+"</td>\n" +
                    "<td>"+averagePrice+"</td>\n" +
                    "<td>"+minPrice+"</td>\n" +
                    "<td>"+maxPrice+"</td>\n" +
                    "</tr>";
            i++;
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
