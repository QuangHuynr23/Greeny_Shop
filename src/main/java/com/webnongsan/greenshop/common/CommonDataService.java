package com.webnongsan.greenshop.common;

import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.model.response.CategoryResponse;
import com.webnongsan.greenshop.repository.Entities.CartItemEntity;
import com.webnongsan.greenshop.repository.Entities.OrderEntity;
import com.webnongsan.greenshop.repository.FavoriteRepository;
import com.webnongsan.greenshop.service.FavoriteServiceImpl;
import com.webnongsan.greenshop.service.ProductServiceImpl;
import com.webnongsan.greenshop.service.ShoppingCartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CommonDataService {
	private final ProductServiceImpl productService;
 	private final FavoriteServiceImpl favoriteService;
	private final ShoppingCartServiceImpl shoppingCartService;
	@Autowired
	public JavaMailSender emailSender;
	@Autowired
	TemplateEngine templateEngine;

 	public void commonData(Model model, UserDTO userDTO){
 		listCategoryByProductName(model);
 		Integer totalSave =0;
		// get count yêu thích
 		if(userDTO.getId()!= null){
 			totalSave = favoriteService.selectCountFavoriteSave(userDTO.getId());
		}
 		Integer totalCartItems = shoppingCartService.getCountCart();
		Collection<CartItemEntity> cartItems = shoppingCartService.getCartItems();
 		model.addAttribute("totalSave", totalSave);
		model.addAttribute("totalCartItems", totalCartItems);
		model.addAttribute("cartItems", cartItems);

	}

	// Lấy đếm sản phẩm theo danh mục
	public void listCategoryByProductName(Model model) {
		List<CategoryResponse> countProductByCategory = productService.findListCategoryByProduct();
		model.addAttribute("countProductByCategory", countProductByCategory);
	}

	//sendEmail by order success
	public void sendSimpleEmail(String email, String subject, String contentEmail, Collection<CartItemEntity> cartItems,
								double totalPrice, OrderEntity orderFinal) throws MessagingException {
		Locale locale = LocaleContextHolder.getLocale();

		// Prepare the evaluation context
		Context ctx = new Context(locale);
		ctx.setVariable("cartItems", cartItems);
		ctx.setVariable("totalPrice", totalPrice);
		ctx.setVariable("orderFinal", orderFinal);
		// Prepare message using a Spring helper
		MimeMessage mimeMessage = emailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
		mimeMessageHelper.setSubject(subject);
		mimeMessageHelper.setTo(email);
		// Create the HTML body
		String htmlContent = "";
		htmlContent = templateEngine.process("mail/email_en.html", ctx);
		mimeMessageHelper.setText(htmlContent, true);

		// Send Message!
		emailSender.send(mimeMessage);

	}
}
