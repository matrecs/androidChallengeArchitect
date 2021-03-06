package com.whynot.checkOtrade.web.controller.signup;

import com.whynot.checkOtrade.web.support.web.MessageHelper;
import com.whynot.checkOtrade.web.dao.AccountRepository;
import com.whynot.checkOtrade.web.service.UserService;
import com.whynot.checkOtrade.web.domain.Account;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Calendar;
import org.hsqldb.types.Types;

@Controller
public class SignupController {

    private static final String SIGNUP_VIEW_NAME = "signup/signup";

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "signup")
	public String signup(Model model) {
		model.addAttribute(new SignupForm());
        return SIGNUP_VIEW_NAME;
	}
	
	@RequestMapping(value = "signup", method = RequestMethod.POST)
	public String signup(@Valid @ModelAttribute SignupForm signupForm, Errors errors, RedirectAttributes ra) {
		if (errors.hasErrors()) {
			return SIGNUP_VIEW_NAME;
		}
		Account account = accountRepository.create(signupForm.createAccount());
		userService.signin(account);
        MessageHelper.addSuccessAttribute(ra, "signup.success");
		return "redirect:/";
	}
	
	public static String getTime(){
		return Calendar.getInstance().getTime().toLocaleString();
	}
}
